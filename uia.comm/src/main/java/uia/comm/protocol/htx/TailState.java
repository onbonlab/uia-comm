/*******************************************************************************
 * * Copyright (c) 2015, UIA
 * * All rights reserved.
 * * Redistribution and use in source and binary forms, with or without
 * * modification, are permitted provided that the following conditions are met:
 * *
 * * * Redistributions of source code must retain the above copyright
 * * notice, this list of conditions and the following disclaimer.
 * * * Redistributions in binary form must reproduce the above copyright
 * * notice, this list of conditions and the following disclaimer in the
 * * documentation and/or other materials provided with the distribution.
 * * * Neither the name of the {company name} nor the
 * * names of its contributors may be used to endorse or promote products
 * * derived from this software without specific prior written permission.
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
package uia.comm.protocol.htx;

import uia.comm.protocol.ProtocolEventArgs;

public class TailState<C> implements HTxState<C> {

    @Override
    public void accept(HTxProtocolMonitor<C> monitor, byte one) {
        if (one == monitor.protocol.tail) {
            monitor.addOne(one);
            monitor.finsihPacking();
            monitor.reset();
            monitor.setState(new IdleState<C>());
        }
        else {
            monitor.cancelPacking(ProtocolEventArgs.ErrorCode.ERR_TAIL);
            monitor.reset();
            if (one == monitor.protocol.head) {
                monitor.setState(new HeadState<C>());
                monitor.read(one);
            }
            else {
                monitor.setState(new IdleState<C>());
            }
        }
    }
}