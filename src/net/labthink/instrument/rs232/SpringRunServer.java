package net.labthink.instrument.rs232;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.labthink.instrument.device.cofp01.Cofp01Tester01;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.serial.SerialAddress;
import org.apache.mina.transport.serial.SerialAddress.DataBits;
import org.apache.mina.transport.serial.SerialAddress.FlowControl;
import org.apache.mina.transport.serial.SerialAddress.Parity;
import org.apache.mina.transport.serial.SerialAddress.StopBits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import ch.qos.logback.core.joran.spi.JoranException;

public class SpringRunServer implements InitializingBean {
    final Logger log = LoggerFactory.getLogger(SpringRunServer.class);

//    private int port;

//    // 定义acceptor
//    @Autowired
//    private NioSocketAcceptor acceptor;
    
    private String comport;
    
    
    // 定义232接口
    @Autowired
    private RS232Connector rs232connector;
    
    @Autowired
    private LoggingFilter loggingFilter;

    @Autowired
    private ProtocolCodecFilter protocolCodecFilter;
    
    @Autowired 
    private IoHandlerAdapter handler;

    
    
    public void afterPropertiesSet() throws Exception {
	doPreSet();
	
	rs232connector.startListen();
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//	acceptor.getFilterChain().addLast("logger", loggingFilter);
//	cfg.getFilterChain().addLast("codec", protocolFilter);
//	acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new HttL1ProtocolCodecFactory()));
//	acceptor.setHandler(handler);   
//	acceptor.bind(new InetSocketAddress(port));  
	System.out.println("************************************");
	System.out.println("********SpringRs232Operator*********");
	System.out.println("*******" + sdf.format(new Date()) + "***");
	System.out.println("************************************");
	
	//TODO 启动新线程，用来完成任务。
	new Thread(new Cofp01Tester01(rs232connector)).start();
	
//	IoSession session = rs232connector.getReceiverSession();
//	Httl1Message pkt = new Httl1Message();
//	byte bt[] = {1,123,93,23,4,-90,43,0,1};
//	pkt.setContent(bt);
//	WriteFuture wf = session.write(pkt);// 发送消息
//	wf.awaitUninterruptibly();
//	bt = new byte[]{2,4,93,23,4,-90,43,0,1};
//	pkt.setContent(bt);
//	wf = session.write(pkt);// 发送消息
//	bt = new byte[]{3,6,93,23,4,-90,43,0,1};
//	pkt.setContent(bt);
//	wf.awaitUninterruptibly();
//	wf = session.write(pkt);// 发送消息
//	bt = new byte[]{4,9,93,23,4,-90,43,0,1};
//	pkt.setContent(bt);
//	pkt.setHeader((short) 0xcccc);
//	System.out.println(pkt.getHeader());
//	wf = session.write(pkt);// 发送消息
//	wf.awaitUninterruptibly();
//	pkt.setHeader((short) 0xAAAA);
//	wf = session.write(pkt);// 发送消息
////	wf = session.write("test");// 发送消息
//	wf.awaitUninterruptibly();
//	rs232connector.endListen();
	
    }

    private void doPreSet() throws JoranException, IOException {

	SerialAddress portAddress = new SerialAddress(comport, 9600,
		DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
		FlowControl.NONE);
	rs232connector = RS232Connector.getInstance(handler,portAddress);
	rs232connector.addFilter("logger", loggingFilter);
	rs232connector.addFilter("codec", protocolCodecFilter); // 设置编码过滤器
	
//	IoBuffer.setUseDirectBuffer(false);
//	IoBuffer.setAllocator(new SimpleBufferAllocator());
//	acceptor.setReuseAddress(true);
    }

    public String getComport() {
        return comport;
    }

    public void setComport(String comport) {
        this.comport = comport;
    }



 

//    public int getPort() {
//        return port;
//    }
//
//    public void setPort(int port) {
//        this.port = port;
//    }

}
