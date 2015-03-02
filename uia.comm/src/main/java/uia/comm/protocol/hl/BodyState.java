/*******************************************************************************
 * * Copyright (c) 2015, UIA
 * * All rights reserved.
 * * Redistribution and use in source and binary forms, with or without
 * * modification, are permitted provided that the following conditions are met:
 * *
 * *     * Redistributions of source code must retain the above copyright
 * *       notice, this list of conditions and the following disclaimer.
 * *     * Redistributions in binary form must reproduce the above copyright
 * *       notice, this list of conditions and the following disclaimer in the
 * *       documentation and/or other materials provided with the distribution.
 * *     * Neither the name of the {company name} nor the
 * *       names of its contributors may be used to endorse or promote products
 * *       derived from this software without specific prior written permission.
 * *
 * * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS "AS IS" AND ANY
 * * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 * * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package uia.comm.protocol.hl;

import uia.comm.protocol.ProtocolEventArgs;

public class BodyState<T> implements HLState<T> {

    private int len;

    private int headIdx;

    public BodyState() {
        this.len = -1;
        this.headIdx = 0;
    }

    @Override
    public void accept(HLProtocolMonitor<T> monitor, byte one) {
        if (one == monitor.protocol.head[this.headIdx]) {
            this.headIdx++;
        }
        else {
            this.headIdx = 0;
        }

        if (monitor.protocol.strict && this.headIdx > 0 && this.headIdx == monitor.protocol.head.length) {
            this.headIdx = 0;
            monitor.addOne(one);
            monitor.cancelPacking(ProtocolEventArgs.ErrorCode.ERR_HEAD_REPEAT);
            for (byte b : monitor.protocol.head) {
                monitor.addOne(b);
            }
        }
        else {
            monitor.addOne(one);
            if (monitor.getDataLength() == monitor.protocol.getLenFieldEndIdx()) {
                this.len = monitor.readLenFromLeField();
            }

            if (monitor.getDataLength() > monitor.protocol.getLenFieldEndIdx() && this.len < 0) {
                monitor.cancelPacking(ProtocolEventArgs.ErrorCode.ERR_BODY_LENGTH);
                monitor.setState(new IdleState<T>());
                return;
            }

            if (this.len >= 0 && (monitor.protocol.lenStartOffset + this.len + monitor.protocol.lenEndOffset) == monitor.getDataLength()) {
                monitor.finishPacking();
                monitor.setState(new IdleState<T>());
            }
        }
    }
}
