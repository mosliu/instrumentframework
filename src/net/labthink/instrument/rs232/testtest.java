package net.labthink.instrument.rs232;

import net.labthink.instrument.device.httl1.codec.HttL1ProtocolCodecFactory;
import net.labthink.instrument.device.httl1.handler.Httl1handler;
import net.labthink.instrument.device.httl1.message.Httl1Message;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.serial.SerialAddress;
import org.apache.mina.transport.serial.SerialAddress.DataBits;
import org.apache.mina.transport.serial.SerialAddress.FlowControl;
import org.apache.mina.transport.serial.SerialAddress.Parity;
import org.apache.mina.transport.serial.SerialAddress.StopBits;

public class testtest {

    /**
     * @param args
     */
    public static void main(String[] args) {
	SerialAddress portAddress = new SerialAddress("COM2", 9600,
		DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
		FlowControl.NONE);
	IoHandlerAdapter handler = new Httl1handler();
	RS232Connector receiver = RS232Connector.getInstance(handler,
		portAddress);
	receiver.addFilter("logger", new LoggingFilter());
	receiver.addFilter("codec", new ProtocolCodecFilter(
		new HttL1ProtocolCodecFactory())); // 设置编码过滤器
	// receiver.addFilter("codec", new ProtocolCodecFilter(
	// new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
	receiver.startListen();
	IoSession session = receiver.getReceiverSession();
	Httl1Message pkt = new Httl1Message();
	byte bt[] = {1,123,93,23,4,-90,43,0,1};
	pkt.setContent(bt);
	WriteFuture wf = session.write(pkt);// 发送消息
	wf.awaitUninterruptibly();
	bt = new byte[]{2,4,93,23,4,-90,43,0,1};
	pkt.setContent(bt);
	wf = session.write(pkt);// 发送消息
	bt = new byte[]{3,6,93,23,4,-90,43,0,1};
	pkt.setContent(bt);
	wf.awaitUninterruptibly();
	wf = session.write(pkt);// 发送消息
	bt = new byte[]{4,9,93,23,4,-90,43,0,1};
	pkt.setContent(bt);
	pkt.setHeader((short) 0xcccc);
	System.out.println(pkt.getHeader());
	wf = session.write(pkt);// 发送消息
	wf.awaitUninterruptibly();
	pkt.setHeader((short) 0xAAAA);
	wf = session.write(pkt);// 发送消息
//	wf = session.write("test");// 发送消息
	wf.awaitUninterruptibly();
	receiver.endListen();
    }

}
