/*******************************************************************************
 * * Copyright (c) 2014, UIA * All rights reserved. * Redistribution and use in source and binary forms, with or without * modification, are permitted provided that the following conditions are met: * * * Redistributions of source code must retain
 * the above copyright * notice, this list of conditions and the following disclaimer. * * Redistributions in binary form must reproduce the above copyright * notice, this list of conditions and the following disclaimer in the * documentation and/or
 * other materials provided with the distribution. * * Neither the name of the {company name} nor the * names of its contributors may be used to endorse or promote products * derived from this software without specific prior written permission. * *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS "AS IS" AND ANY * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE * DISCLAIMED. IN NO
 * EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; * LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS * SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package uia.comm.my;

import org.apache.log4j.Logger;

import uia.comm.MessageCallIn;
import uia.comm.MessageCallOut;
import uia.comm.SocketDataController;

public class MyServerRequest implements MessageCallIn<SocketDataController>, MessageCallOut {

    public static Logger logger = Logger.getLogger(MyServerRequest.class);

    @Override
    public String getCmdName() {
        return "ABC";
    }

    @Override
    public String getTxId() {
        return "1";
    }

    @Override
    public void execute(byte[] request, SocketDataController controller) {
        try {
            logger.info(controller.getName() + "> callin: " + new String(request));
            controller.send(new byte[] { (byte) 0x8a, 0x44, 0x45, 0x46, 0x31, 0x32, (byte) 0xa8 }, 1);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void execute(byte[] reply) {
        logger.info("server reply: " + new String(reply));
    }

    @Override
    public void timeout() {
        logger.info("svr request timeout");
    }

}
