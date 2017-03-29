package net.labthink.instrument.device.W3060;

import net.labthink.instrument.device.OX2230W3330.codec.OX2230W3330ProtocolCodecFactory;
import net.labthink.instrument.device.OX2230W3330.handler.OX2230W3330Handler;
import net.labthink.instrument.device.OX2230W3330.message.OX2230W3330Message;
import net.labthink.instrument.device.cofp01.message.Cofp01Message;
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

public class W3060Tester01 implements Runnable {

	RS232Connector rs232connector;

	public W3060Tester01(RS232Connector rs232connector) {
		this.rs232connector = rs232connector;
	}

	@Override
	public void run() {
		try {
			sendSample1();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendSample1() throws InterruptedException {

	}

	public void sendSample() throws InterruptedException {
		IoSession session = rs232connector.getReceiverSession();
		int count = 7;
		// 速度真实数值为变量/10.
		// 次数为原值
		OX2230W3330Message pkt = new OX2230W3330Message();
		WriteFuture wf = session.write(pkt);// 发送消息
		wf.awaitUninterruptibly();
		// 次数为原始值
		// 摩擦系数（包括最大最2小平均）为1000倍
		// 角度100倍

		Counter nc = new Counter(12.28);
		// SendOnePacket(session, pkt, nc, 100);

		for (int i = 0; i < count - 1; i++) {
			nc.put(Math.random() * 100);
			// SendOnePacket(session, pkt, nc, 1000);
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
		IoHandlerAdapter handler = new OX2230W3330Handler();
		RS232Connector receiver = RS232Connector.getInstance(handler,
				portAddress);
		receiver.addFilter("logger", new LoggingFilter());
		receiver.addFilter("codec", new ProtocolCodecFilter(
				new OX2230W3330ProtocolCodecFactory())); // 设置编码过滤器
		// receiver.addFilter("codec", new ProtocolCodecFilter(
		// new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
		receiver.startListen();
		W3060Tester01 test = new W3060Tester01(receiver);
		new Thread(test).start();

	}

}
