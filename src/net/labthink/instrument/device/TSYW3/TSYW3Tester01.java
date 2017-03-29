package net.labthink.instrument.device.TSYW3;

import java.util.Date;
import net.labthink.instrument.device.TSYW3.codec.TSYW3ProtocolCodecFactory;
import net.labthink.instrument.device.TSYW3.message.TSYW3Message;
import net.labthink.instrument.device.TSYW3.simulator.TSYW3;
import net.labthink.instrument.device.TSYW3.simulator.TSYW3DataProducer;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TSYW3Tester01 implements Runnable {

    final Logger log = LoggerFactory.getLogger(TSYW3Tester01.class);
    RS232Connector rs232connector;

    public TSYW3Tester01(RS232Connector rs232connector) {
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
        int defaultsleeptime = 250;

        TSYW3Message pkt = new TSYW3Message();
        // 从文件中读取配置
//		String conf = FilePlus.ReadTextFileToString("bin/conf.json");
        // 随机生成
        String conf_rand = TSYW3DataProducer.allCurve();

//		System.out.println(conf);

        // 开始一次试验：
        TSYW3 device = new TSYW3();
        //wvtr*1000,ppm*10,flux*100
        device.preSetParams(10000, -5000, 0);
        long timestart = System.currentTimeMillis();
        // 重置
        byte[] tempbytes = device.getReset();
        pkt.setContent(tempbytes);
        SendPackets(session, pkt, defaultsleeptime, 2);


        // 发送数据(多次)
//		for (int i = 0; i < 100; i++) {
//			tempbytes = device.fetchCurrentValue(1, 40, 1, 5);
//			pkt.setContent(tempbytes);
//			SendPackets(session, pkt, defaultsleeptime, 2);
//		}

        JSONArray ja = new JSONArray(conf_rand);
        int counter = 0;
        for (int jacount = 0; jacount < ja.length(); jacount++) {

            // 发送实验数据
            JSONArray ja_temp = ja.getJSONArray(jacount);
            for (int j = 0; j < ja_temp.getInt(0); j++) {
                tempbytes = device.fetchCurrentValue(ja_temp.getInt(1), ja_temp.getInt(2), ja_temp.getInt(3), ja_temp.getInt(4), ja_temp.getInt(5), ja_temp.getInt(6));

                System.out.println(++counter + ":WVTR" + device.getWvtr() + "\tPpm" + device.getPpm() + "\tFLUX"
                        + device.getFlux());
//				log.warn("OTR" + device.getORT() + "\tFLUX"
//						+ device.getFlux());
                pkt.setContent(tempbytes);
                SendOnePacket(session, pkt, defaultsleeptime);

            }

        }
        //多发几遍
        for (int i = 0; i < 30; i++) {



            conf_rand = TSYW3DataProducer.allCurve();
            ja = new JSONArray(conf_rand);

            for (int jacount = 0; jacount < ja.length(); jacount++) {

                // 发送实验数据
                JSONArray ja_temp = ja.getJSONArray(jacount);
                for (int j = 0; j < ja_temp.getInt(0); j++) {
                    tempbytes = device.fetchCurrentValue(ja_temp.getInt(1), ja_temp.getInt(2), ja_temp.getInt(3), ja_temp.getInt(4), ja_temp.getInt(5), ja_temp.getInt(6));

//                    System.out.println(++counter + ":WVTR" + device.getWvtr() + "\tPpm" + device.getPpm() + "\tFLUX"
//                            + device.getFlux());
//				log.warn("OTR" + device.getORT() + "\tFLUX"
//						+ device.getFlux());
                    pkt.setContent(tempbytes);
                    SendOnePacket(session, pkt, defaultsleeptime);

                }

            }
            System.out.print("当前运行时间是：");
            System.out.println(System.currentTimeMillis()-timestart);

        }

        tempbytes = device.getTestEnd();
        pkt.setContent(tempbytes);
        SendPackets(session, pkt, defaultsleeptime, 2);

        System.out.println("实验结束");

//		tempbytes = device.getBlowClean();
//		pkt.setContent(tempbytes);
//		SendPackets(session, pkt, defaultsleeptime, 2);
//		System.out.println("吹洗");		
        System.exit(0);

    }

    public void sendSample2() {
    }

    private void SendOnePacket(IoSession session, TSYW3Message pkt,
            long sleeptime) throws InterruptedException {
        SendPackets(session, pkt, sleeptime, 1);
    }

    private void SendPackets(IoSession session, TSYW3Message pkt,
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
    public static void main2(String[] args) {
        System.out.println(System.currentTimeMillis());
        Date a = new Date();
        a.setTime(1283216040000L+140760000);
        System.out.println(a);
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
                new TSYW3ProtocolCodecFactory())); // 设置编码过滤器
        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        receiver.startListen();
        TSYW3Tester01 test = new TSYW3Tester01(receiver);
        new Thread(test).start();

    }
}
