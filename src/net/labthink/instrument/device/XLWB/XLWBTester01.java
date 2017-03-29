package net.labthink.instrument.device.XLWB;

import net.labthink.instrument.device.XLWB.codec.XLWBProtocolCodecFactory;
import net.labthink.instrument.device.XLWB.message.XLWBMessage;
import net.labthink.instrument.device.XLWB.simulator.XLWB;
import net.labthink.instrument.device.XLWB.simulator.XLWBtestDataProducer;
import net.labthink.instrument.device.httl1.handler.Httl1handler;
import net.labthink.instrument.rs232.RS232Connector;
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
import org.json.JSONObject;

public class XLWBTester01 implements Runnable {

	RS232Connector rs232connector;

	public XLWBTester01(RS232Connector rs232connector) {
		this.rs232connector = rs232connector;
	}

	@Override
	public void run() {
		try {
			sendSample();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendSample() throws InterruptedException, JSONException {
		IoSession session = rs232connector.getReceiverSession();
		int count = 10;
		int defaultsleeptime = 30;

		XLWBMessage pkt = new XLWBMessage();

		// 从文件中读取配置
		String conf = FilePlus.ReadTextFileToString("bin/xlwb.json");
		// TODO 随机生成待完成
		 String conf_rand = XLWBtestDataProducer.allCurve();
		 System.out.println(conf_rand);

		JSONArray ja = new JSONArray(conf_rand);
		// JSONArray ja = new JSONArray(conf);
		XLWB device = new XLWB(0);
		for (int jacount = 0; jacount < ja.length(); jacount++) {
			JSONObject jo_conf = ja.getJSONObject(jacount);
			// 抗拉试验
			int kind = jo_conf.getInt("kind");
			// int kind = 0;

			// 设置配置信息
			device.preSettestParams(jo_conf.getInt("kind"), jo_conf
					.getInt("maxstr"), jo_conf.getInt("speed"), jo_conf
					.getInt("num"));

			byte[] tempbytes = device.getTestType();
			pkt.setContent(tempbytes);
			SendOnePacket(session, pkt, defaultsleeptime);

			tempbytes = device.getTestStrength_MaxValue();
			pkt.setContent(tempbytes);
			SendOnePacket(session, pkt, defaultsleeptime);

			tempbytes = device.getTestSpeed();
			pkt.setContent(tempbytes);
			SendOnePacket(session, pkt, defaultsleeptime);

			tempbytes = device.getTestCountValue();
			pkt.setContent(tempbytes);
			SendOnePacket(session, pkt, defaultsleeptime);

			tempbytes = device.getTestBegin();
			pkt.setContent(tempbytes);
			SendOnePacket(session, pkt, defaultsleeptime);

			// 发送实验数据

			JSONArray ja_conf = jo_conf.getJSONArray("DataLists");
			for (int i = 0; i < ja_conf.length(); i++) {
				JSONArray ja_temp = ja_conf.getJSONArray(i);
				for (int j = 0; j < ja_temp.getInt(0); j++) {
					tempbytes = device.setCurrent_Strength(ja_temp.getInt(1),
							ja_temp.getInt(2));
					System.out.println(device.getCurrent_Strength());
					pkt.setContent(tempbytes);
					SendOnePacket(session, pkt, defaultsleeptime);
				}
			}

			tempbytes = device.getTestEnd();
			pkt.setContent(tempbytes);
			SendOnePacket(session, pkt, defaultsleeptime);

			tempbytes = device.getMax_StrengthBs();
			pkt.setContent(tempbytes);
			SendOnePacket(session, pkt, defaultsleeptime);
			System.out.println("当前最大值为：" + device.getMax_Strength());

			if (device.isEnd()) {
				tempbytes = device.getTJMax();
				pkt.setContent(tempbytes);
				SendOnePacket(session, pkt, defaultsleeptime);

				tempbytes = device.getTJmin();
				pkt.setContent(tempbytes);
				SendOnePacket(session, pkt, defaultsleeptime);

				tempbytes = device.getTJAvg();
				pkt.setContent(tempbytes);
				SendOnePacket(session, pkt, defaultsleeptime);

			}
			

		}
		// JSONObject jo_conf = new JSONObject(conf);
		// jo_conf.getInt("range");
		//终止程序
		System.exit(0);
	}

	private void SendOnePacket(IoSession session, XLWBMessage pkt,
			long sleeptime) throws InterruptedException {
		SendPackets(session, pkt, sleeptime, 1);
	}

	private void SendPackets(IoSession session, XLWBMessage pkt,
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

		SerialAddress portAddress = new SerialAddress("COM1", 9600,
				DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
				FlowControl.NONE);
		IoHandlerAdapter handler = new Httl1handler();
		RS232Connector receiver = RS232Connector.getInstance(handler,
				portAddress);
		receiver.addFilter("logger", new LoggingFilter());
		receiver.addFilter("codec", new ProtocolCodecFilter(
				new XLWBProtocolCodecFactory())); // 设置编码过滤器
		// receiver.addFilter("codec", new ProtocolCodecFilter(
		// new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
		receiver.startListen();
		XLWBTester01 test = new XLWBTester01(receiver);
		new Thread(test).start();

	}

}
