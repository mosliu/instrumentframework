package net.labthink.instrument.device.OX2231;

import net.labthink.instrument.device.OX2231.simulator.OX2231DataProducer;
import net.labthink.instrument.device.OX2231.simulator.OX2231;
import net.labthink.instrument.device.OX2231.codec.OX2231ProtocolCodecFactory;
import net.labthink.instrument.device.OX2231.message.OX2231Message;
import net.labthink.instrument.device.httl1.handler.Httl1handler;
import net.labthink.instrument.rs232.RS232Connector;
import net.labthink.instrument.rs232.SpringRunServer;
import net.labthink.utils.FilePlus;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OX2231Tester01 implements Runnable {

	final Logger log = LoggerFactory.getLogger(SpringRunServer.class);
	RS232Connector rs232connector;

	public OX2231Tester01(RS232Connector rs232connector) {
		this.rs232connector = rs232connector;
	}

	@Override
	public void run() {
		try {
			sendSample();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendSample() throws InterruptedException, JSONException {
		IoSession session = rs232connector.getReceiverSession();
		int defaultsleeptime = 1000;

		OX2231Message pkt = new OX2231Message();
		// 从文件中读取配置
		String conf = FilePlus.ReadTextFileToString("bin/231.json");
		// 随机生成
		String conf_rand = OX2231DataProducer.allCurve();

		System.out.println(conf);
		
		// 开始一次试验：
		OX2231 device = new OX2231();
		// 预置实验参数//温湿度×10
		device.preSetParams(230, 700, 800, 100);

		// 重置
		byte[] tempbytes = device.getReset();
		pkt.setContent(tempbytes);
		SendPackets(session, pkt, defaultsleeptime, 2);
		// 发送件数
		tempbytes = device.getSampleCount();
		pkt.setContent(tempbytes);
		SendPackets(session, pkt, defaultsleeptime, 2);
		// 发送开始信号 1薄膜，2容器
		tempbytes = device.getStart(2);
                device.setKind(3);
		pkt.setContent(tempbytes);
		SendPackets(session, pkt, defaultsleeptime, 2);

		// 发送数据(多次)
//		for (int i = 0; i < 100; i++) {
//			tempbytes = device.fetchCurrentValue(1, 40, 1, 5);
//			pkt.setContent(tempbytes);
//			SendPackets(session, pkt, defaultsleeptime, 2);
//		}

		JSONArray ja = new JSONArray(conf);
		int counter = 0;
		for (int jacount = 0; jacount < ja.length(); jacount++) {

			// 发送实验数据
			JSONArray ja_temp = ja.getJSONArray(jacount);
			for (int j = 0; j < ja_temp.getInt(0); j++) {
				tempbytes = device.fetchCurrentValue(ja_temp.getInt(1), ja_temp
						.getInt(2), ja_temp.getInt(3), ja_temp.getInt(4));
				
				System.out.println(++counter+":OTR" + device.getORT() + "\tFLUX"
						+ device.getFlux());
//				log.warn("OTR" + device.getORT() + "\tFLUX"
//						+ device.getFlux());
				pkt.setContent(tempbytes);
				SendOnePacket(session, pkt, defaultsleeptime);

			}

		}

		tempbytes = device.getTestEnd();
		pkt.setContent(tempbytes);
		SendPackets(session, pkt, defaultsleeptime, 2);
		
		System.out.println("实验结束");
		System.exit(0);

	}

	
	public void sendSample2(){
		
	}
	
	private void SendOnePacket(IoSession session, OX2231Message pkt,
			long sleeptime) throws InterruptedException {
		SendPackets(session, pkt, sleeptime, 1);
	}

	private void SendPackets(IoSession session, OX2231Message pkt,
			long sleeptime, int times) throws InterruptedException {
		WriteFuture wf;
		for (int i = 0; i < times; i++) {
			wf = session.write(pkt);// 发送消息
			wf.awaitUninterruptibly();
			if (sleeptime != 0) {
				Thread.sleep(sleeptime);
			}
		}

	}

	public static void main(String[] args) {

		SerialAddress portAddress = new SerialAddress("COM2", 9600,
				DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
				FlowControl.NONE);
		IoHandlerAdapter handler = new Httl1handler();
		RS232Connector receiver = RS232Connector.getInstance(handler,
				portAddress);
		receiver.addFilter("logger", new LoggingFilter());
		receiver.addFilter("codec", new ProtocolCodecFilter(
				new OX2231ProtocolCodecFactory())); // 设置编码过滤器
		// receiver.addFilter("codec", new ProtocolCodecFilter(
		// new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
		receiver.startListen();
		OX2231Tester01 test = new OX2231Tester01(receiver);
		new Thread(test).start();

	}

}
