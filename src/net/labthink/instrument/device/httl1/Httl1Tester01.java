package net.labthink.instrument.device.httl1;

//~--- non-JDK imports --------------------------------------------------------
import net.labthink.instrument.device.httl1.codec.HttL1ProtocolCodecFactory;
import net.labthink.instrument.device.httl1.handler.Httl1handler;
import net.labthink.instrument.device.httl1.message.Httl1Message;
import net.labthink.instrument.device.httl1.simulator.Httl1;
import net.labthink.instrument.device.httl1.simulator.Httl1testDataProducer;
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
import org.json.JSONObject;

public class Httl1Tester01 implements Runnable {

    static int testtype = 1;    // 用于预设置的实验类型
    private static String comport = "COM1";
    private boolean stopflag = false;
    RS232Connector rs232connector;

    public Httl1Tester01(RS232Connector rs232connector) {
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
        int count = 1000;
        int defaultsleeptime = 130;// old:78
        Httl1Message pkt = new Httl1Message();

        // 从文件中读取配置
//      String conf = FilePlus.ReadTextFileToString("bin/conf.json");
        // 随机生成
//        Httl1testDataProducer.setKind(1);
        String conf_rand = Httl1testDataProducer.allCurve();

//      System.out.println(conf_rand);
        JSONArray ja = new JSONArray(conf_rand);

//      JSONArray ja = new JSONArray(conf);
        for (int jacount = 0; jacount < ja.length(); jacount++) {
            if(stopflag) return;//如果设为中止状态的话,中止执行。
            JSONObject jo_conf = ja.getJSONObject(jacount);
            int kind = jo_conf.getInt("kind");

//          int kind = 1;//实验类型

            // 发送配置信息
            Httl1 device = new Httl1(kind);

            device.preSettestParams(jo_conf.getInt("range"), jo_conf.getInt("num"), jo_conf.getInt("speed"), jo_conf //          .getInt("num"), 3, jo_conf//速度固定为3
                    .getInt("width"));

            byte[] tempbytes = device.getTestParams();

            pkt.setContent(tempbytes);
            SendOnePacket(session, pkt, defaultsleeptime);

//          SendOnePacket(session, pkt, defaultsleeptime);// 发送两遍

            if (kind == 3) {
                device.preSetHAParams(jo_conf.getInt("Move_Temp"), jo_conf.getInt("Still_Temp"),
                        jo_conf.getInt("Preasure"), jo_conf.getInt("Time"), jo_conf.getInt("Stoptime"));
                tempbytes = device.getHAParams();
                pkt.setContent(tempbytes);
                SendOnePacket(session, pkt, defaultsleeptime);

//              SendOnePacket(session, pkt, defaultsleeptime);// 发送两遍
                tempbytes = device.getHATime();
                pkt.setContent(tempbytes);
                SendOnePacket(session, pkt, defaultsleeptime);

//              SendOnePacket(session, pkt, defaultsleeptime);// 发送两遍
            }

            // 发送实验数据
            JSONArray ja_conf = jo_conf.getJSONArray("DataLists");

            for (int i = 0; i < ja_conf.length(); i++) {
                if(stopflag) return;//如果设为中止状态的话,中止执行。
                JSONArray ja_temp = ja_conf.getJSONArray(i);

                for (int j = 0; j < ja_temp.getInt(0); j++) {
                    tempbytes = device.setCurrent_Strength(ja_temp.getInt(1), ja_temp.getInt(2));
                    pkt.setContent(tempbytes);
                    SendOnePacket(session, pkt, defaultsleeptime);
                }
            }

            tempbytes = device.getTestEnd();
            pkt.setContent(tempbytes);
            SendOnePacket(session, pkt, defaultsleeptime);
            SendOnePacket(session, pkt, defaultsleeptime);
            SendOnePacket(session, pkt, defaultsleeptime);

            // 发送统计结果。
            if (kind == 1) {
                tempbytes = device.getMax_StrengthBs();
                pkt.setContent(tempbytes);
                SendOnePacket(session, pkt, defaultsleeptime);

//              SendOnePacket(session, pkt, defaultsleeptime);
                tempbytes[1] = 5;
                pkt.setContent(tempbytes);
                SendOnePacket(session, pkt, defaultsleeptime);
            } else if (kind == 2) {
                tempbytes = device.getBL_ResultBs();
                pkt.setContent(tempbytes);
                SendOnePacket(session, pkt, defaultsleeptime);

//              SendOnePacket(session, pkt, defaultsleeptime);
                tempbytes[1] = 5;
                pkt.setContent(tempbytes);
                SendOnePacket(session, pkt, defaultsleeptime);
            } else if (kind == 3) {
                tempbytes = device.getMax_StrengthBs();
                pkt.setContent(tempbytes);
                SendOnePacket(session, pkt, defaultsleeptime);

//              SendOnePacket(session, pkt, defaultsleeptime);
                tempbytes[1] = 5;
                pkt.setContent(tempbytes);
                SendOnePacket(session, pkt, defaultsleeptime);
            }

            System.out.println("当前最大值为：" + device.getMax_Strength());
        }

        // JSONObject jo_conf = new JSONObject(conf);
        // jo_conf.getInt("range");
    }

    private void SendOnePacket(IoSession session, Httl1Message pkt, long sleeptime) throws InterruptedException {
        WriteFuture wf;

        wf = session.write(pkt);    // 发送消息
        wf.awaitUninterruptibly();

        if (sleeptime != 0) {
            Thread.sleep(sleeptime);
        }
    }

    public static void main(String[] args) {
        SerialAddress portAddress = new SerialAddress("COM1", 9600, DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
                FlowControl.NONE);
        IoHandlerAdapter handler = new Httl1handler();
        RS232Connector receiver = RS232Connector.getInstance(handler, portAddress);

        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(new HttL1ProtocolCodecFactory()));    // 设置编码过滤器

        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        receiver.startListen();

        Httl1Tester01 test = new Httl1Tester01(receiver);

        new Thread(test).start();
    }

    public void UIStart() {
        SerialAddress portAddress = new SerialAddress(comport, 9600, DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
                FlowControl.NONE);
        IoHandlerAdapter handler = new Httl1handler();
        RS232Connector receiver = RS232Connector.getInstance(handler, portAddress);

        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(new HttL1ProtocolCodecFactory()));    // 设置编码过滤器

        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        receiver.startListen();

        Httl1Tester01 test = new Httl1Tester01(receiver);
        Httl1testDataProducer.setKind(testtype);
        new Thread(test).start();
    }

    // getters and setters
    public static void setTesttype(int testtype) {
        Httl1Tester01.testtype = testtype;
    }

    public static int getTesttype() {
        return testtype;
    }

    public Httl1Tester01() {
    }

    /**
     * @return the comport
     */
    public static String getComport() {
        return comport;
    }

    /**
     * @param aComport the comport to set
     */
    public static void setComport(String aComport) {
        comport = aComport;
    }

    public synchronized void shutDownThread() {
        stopflag = true;
    }

    public synchronized boolean isShutDown() {
        return stopflag;
    }

    public class Counter {

        short count = 0;
        public double last = 0;
        public double lastfot = 0;
        public double max = 0;
        public double min = 0;
        public double sum = 0;

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

            if (lastfot < min) {
                min = lastfot;
            }

            if (lastfot > max) {
                max = lastfot;
            }

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
}
