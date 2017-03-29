package net.labthink.instrument.device.httl1.handler;

import net.labthink.instrument.device.httl1.message.Httl1Message;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class Httl1handler extends IoHandlerAdapter {

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
	    throws Exception {
	
	cause.printStackTrace();
	
	super.exceptionCaught(session, cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message)
	    throws Exception {
	System.out.println(message);
	
	Httl1Message pkt = new Httl1Message();
	byte bt[] = {1,123,93,23,4,-90,43,0,1};
	pkt.setContent(bt);
	
	WriteFuture wf = session.write(pkt);
	wf.awaitUninterruptibly();
	if(message.equals("test")){
	    session.write("close");
	}
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
	System.out.println("session closed");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
	    throws Exception {
	System.out.println("session idled:"  + status.toString());
    }

    
}
