package net.labthink.instrument.device.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.labthink.instrument.device.CommandSender.codec.CommandSenderProtocolCodecFactory;
import net.labthink.instrument.device.CommandSender.handler.CommandSenderHandler;
import net.labthink.instrument.device.CommandSender.message.CommandSenderOutMessage;
import net.labthink.instrument.device.CommandSender.simulator.CommandSender;
import net.labthink.instrument.rs232.RS232Connector;
import net.labthink.utils.BytePlus;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.serial.SerialAddress;
import org.apache.mina.transport.serial.SerialAddress.DataBits;
import org.apache.mina.transport.serial.SerialAddress.FlowControl;
import org.apache.mina.transport.serial.SerialAddress.Parity;
import org.apache.mina.transport.serial.SerialAddress.StopBits;

public class CommandSenderTester01 implements Runnable {

    RS232Connector rs232connector;
    private static final String STORE_FILE = "CommandSendersave.dat";

    public CommandSenderTester01(RS232Connector rs232connector) {
        this.rs232connector = rs232connector;
    }
    public String content = "";
    public long delay = 100;

    public static List<String> parsecontent(String content) {
        String[] strlist = content.split("\r|\n");
        List<String> rtnl = new ArrayList<String>();
        List<String> l = Arrays.asList(strlist);
        for (Iterator<String> it = l.iterator(); it.hasNext();) {
            String string = it.next();
            string = string.replaceAll("[^\\sa-fA-F0-9]", "").replaceAll("[\\s]+", " ").replaceAll("\\s([\\da-fA-F]{1})\\s", " 0$1 ").trim();
            if (string.length() > 1) {
                rtnl.add(string.toUpperCase());
            }
        }
//        for (Iterator<String> it = rtnl.iterator(); it.hasNext();) {
//            String string = it.next();
//            System.out.println(string);
//        }
        return rtnl;
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

    public void sendSample1() throws InterruptedException {
    }

    public void sendSample() throws InterruptedException {
        IoSession session = rs232connector.getReceiverSession();
        // SendOnePacket(session, pkt, nc, 1000);
        CommandSenderOutMessage pkt = new CommandSenderOutMessage();
        WriteFuture wf;
//        byte bt[] = pkt.getContent();
//        bt[0] = 0;
        List<String> l = parsecontent(content);
        if (l.size() > 0) {
            for (Iterator<String> it = l.iterator(); it.hasNext();) {
                String string = it.next();
                pkt.setContent(BytePlus.hexStringToBytes(string));
                wf = session.write(pkt);// 发送消息
                wf.awaitUninterruptibly();
                Thread.sleep(delay);

            }
        } else {
            return;
        }





    }

    public static void main(String[] args) {
        String a = "AA AA 00 00 06 00 02 01 00 01 00 01 00 01 00 0c BB BB\r\nAA AA 01 00 01 01 00 01 00 01 00 01 00 01 00 07 BB BB\r\nAA AA 01 00 02 01 00 01 00 01 00 01 00 01 00 08 BB BB\r\nAA AA 01 00 03 01 00 01 00 01 00 01 00 01 00 09 BB BB\r\nAA AA 01 00 04 01 00 01 00 01 00 01 00 01 00 0a BB BB\r\nAA AA 01 00 05 01 00 01 00 01 00 01 00 01 00 0b BB BB\r\nAA AA 01 00 06 01 00 01 00 01 00 01 00 01 00 0c BB BB";
        List<String> l = parsecontent(a);
      for (Iterator<String> it = l.iterator(); it.hasNext();) {
            String string = it.next();
            System.out.println(string);
            System.out.println(BytePlus.byteArray2String(BytePlus.hexStringToBytes(string)));
        }

    }

    public static void main2(String[] args) {

        SerialAddress portAddress = new SerialAddress("COM1", 9600,
                DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
                FlowControl.NONE);
        CommandSenderHandler handler = new CommandSenderHandler(new CommandSender());
        handler.loadData();
        RS232Connector receiver = RS232Connector.getInstance(handler,
                portAddress);
        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(
                new CommandSenderProtocolCodecFactory())); // 设置编码过滤器
        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        receiver.startListen();
        CommandSenderTester01 test = new CommandSenderTester01(receiver);
        new Thread(test).start();

    }
}
