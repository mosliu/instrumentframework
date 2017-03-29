package net.labthink.instrument.device.cofp01;

import net.labthink.instrument.device.cofp01.codec.Cofp01ProtocolCodecFactory;
import net.labthink.instrument.device.cofp01.message.Cofp01Message;
import net.labthink.instrument.device.httl1.handler.Httl1handler;
import net.labthink.instrument.rs232.RS232Connector;

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

public class Cofp01Tester01 implements Runnable {

    RS232Connector rs232connector;

    public Cofp01Tester01(RS232Connector rs232connector) {
	this.rs232connector = rs232connector;
    }

    @Override
    public void run() {
	try {
	    sendSample();
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    public void sendSample() throws InterruptedException {
	IoSession session = rs232connector.getReceiverSession();
	int count = 13;
	// 速度真实数值为变量/10.
	// 次数为原值
	Cofp01Message pkt = new Cofp01Message((short) 30, (short) count);
	WriteFuture wf = session.write(pkt);// 发送消息
	wf.awaitUninterruptibly();
	// 次数为原始值
	// 摩擦系数（包括最大最小平均）为1000倍
	// 角度100倍

	Counter nc = new Counter(12.28);
	SendOnePacket(session, pkt, nc, 300);

	for (int i = 0; i < count - 1; i++) {
	    nc.put(Math.random() * 100);
	    SendOnePacket(session, pkt, nc, 300);
	}
	
    }

    private void SendOnePacket(IoSession session, Cofp01Message pkt,
	    Counter nc, long sleeptime) throws InterruptedException {
	WriteFuture wf;
	byte bt[] = pkt.packDataPacket(nc.getnum(), nc.getlastcof(), nc
		.getlastdegree(), nc.getmax(), nc.getmin(), nc.getavg());
	pkt.setContent(bt);
	wf = session.write(pkt);// 发送消息
	wf.awaitUninterruptibly();
	if (sleeptime != 0) {
	    Thread.sleep(sleeptime);
	}
    }

    public class Counter {
	short count = 0;
	public double sum = 0;
	public double max = 0;
	public double min = 0;
	public double last = 0;
	public double lastfot = 0;

	public Counter(double d) {
	    last = d;
	    lastfot = Math.tan(Math.toRadians(last));
	    sum = lastfot;
	    max = lastfot;
	    min = lastfot;
	    count++;

	}

	public void put(double newer) {
	    count++;
	    last = newer;
	    lastfot = Math.tan(Math.toRadians(last));
	    if (lastfot < min)
		min = lastfot;
	    if (lastfot > max)
		max = lastfot;
	    sum += lastfot;
	}

	public short getmax() {
	    return (short) (max * 1000);
	}

	public short getmin() {
	    return (short) (min * 1000);
	}

	public short getavg() {
	    return (short) (sum / count * 1000);
	}

	public short getnum() {
	    return count;
	}

	public short getlastcof() {
	    return (short) (lastfot * 1000);
	}

	public short getlastdegree() {
	    return (short) (last * 100);
	}
    }

    public static void main(String[] args) {

	SerialAddress portAddress = new SerialAddress("COM1", 9600,
		DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
		FlowControl.NONE);
	IoHandlerAdapter handler = new Httl1handler();
	RS232Connector receiver = RS232Connector.getInstance(handler,
		portAddress);
	receiver.addFilter("logger", new LoggingFilter());
	receiver.addFilter("codec", new ProtocolCodecFilter(
		new Cofp01ProtocolCodecFactory())); // 设置编码过滤器
	// receiver.addFilter("codec", new ProtocolCodecFilter(
	// new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
	receiver.startListen();
	Cofp01Tester01 test = new Cofp01Tester01(receiver);
	new Thread(test).start();

    }

}
