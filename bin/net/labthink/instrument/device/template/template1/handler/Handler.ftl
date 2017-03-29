<#macro buildNode node>
	switch(${node.switchsrc}[${node.switchpos}]) {
		<#list node.switchcase as prtc>
		case 0x${prtc.func}:
			/*${prtc.describe}*/
			System.out.println("${prtc.describe}");
			<#if prtc.action?? && prtc.action?size gt 0 >
				<#if prtc.action.setvalue?? && prtc.action.setvalue?size gt 0 >
					<#list prtc.action.setvalue as sv>
						//${sv.describe!}
						${protocal.setdest}[${sv.id}] = 	${sv.value};
					</#list>
				</#if>
				<#if prtc.action.copyvalue?? && prtc.action.copyvalue?size gt 0 >
					<#list prtc.action.copyvalue as cv>
						//${cv.describe!}
						System.arraycopy(${protocal.setsrc}, ${cv.infrom}, ${protocal.setdest}, ${cv.outfrom}, ${cv.length});
					</#list>
				</#if>
				<#if prtc.action.setdevice?? && prtc.action.setdevice?size gt 0 >
					<#list prtc.action.setdevice as sd>
						<#list device.vars as x>
							<#if x.name = sd.aim>
								<#switch x.type>
									<#case "byte[]">
										//设定 ${x.describe!}
										tempbytes = ${x.default};
										<#assign bits = x.default?substring(x.default?index_of("[")+1,x.default?index_of("]")) >
										System.arraycopy(${node.switchsrc}, ${sd.startposition}, tempbytes, 0, ${bits});
										getDevice().set${x.name?cap_first}(tempbytes);
										<#break>
									<#case "byte">
										//设定 ${x.describe!}
										getDevice().set${x.name?cap_first}(${node.switchsrc}[${sd.startposition}]);
										<#break>
									<#case "boolean">
										getDevice().set${x.name?cap_first}(${sd.value});
										<#break>
									<#default>
								</#switch>
							</#if>
						</#list>
					</#list>
				</#if>
				<#if prtc.action.getdevice?? && prtc.action.getdevice?size gt 0 >
					<#list prtc.action.getdevice as gd>
						<#list device.vars as x>
							<#if x.name = gd.aim>

								<#switch x.type>
									<#case "byte[]">
										//设定 ${x.describe!}
										tempbytes = getDevice().get${x.name?cap_first}();
										<#assign bits = x.default?substring(x.default?index_of("[")+1,x.default?index_of("]")) >
										System.arraycopy(tempbytes, 0, ${node.setdest}, ${gd.startposition} , ${bits});
										<#break>
									<#case "medium">
									 This will be processed if it is medium
										<#break>
									<#case "boolean">
										<#break>
									<#default>
								</#switch>
							</#if>
						</#list>
					</#list>
				</#if>
				<#if prtc.action.clearposition?? >
					//从第${prtc.action.clearposition}位清空返回包，防止错误数据
					BytePlus.fillcontent(${protocal.setdest}, ${prtc.action.clearposition});
				</#if>
			</#if>
			<#if prtc.protocal?? >
				//具体解析该命令
				<@buildNode node=prtc.protocal/>
			</#if>
			break;
		</#list>
		default:
            System.out.println("错误，不能解析的指令");
	}
 <#--if action?? && action?size gt 0>
        <#list action as t>
            //to build tree node
            <@buildNode action=t.action parent=t/>
        </#list>
    </#if-->
</#macro>
package net.labthink.instrument.device.${devicename}.handler;

//~--- non-JDK imports --------------------------------------------------------
import net.labthink.instrument.device.${devicename}.message.${devicename}InMessage;
import net.labthink.instrument.device.${devicename}.message.${devicename}OutMessage;
import net.labthink.instrument.device.${devicename}.simulator.${devicename};
import net.labthink.utils.BytePlus;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ${devicename}'s 信号处理
 *
 * @version        ${version}, ${date}
 * @author         ${author?cap_first}
 */
public class ${devicename}Handler extends IoHandlerAdapter {
    public boolean allerror = false;
    int errorcount = 0;
    private static final String STORE_FILE = "${devicename}save.dat";

	boolean startsender = false;
    long starttime = 0;
	SenderTask st = null;
    Timer timer = null;
    /** 每次发送后等待时间。 */
    private int waittime = 100;

    /* 仪器 */
    private ${devicename} device = new ${devicename}();

    /**
     * 构造函数
     *
     */
    public ${devicename}Handler() {
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(session, cause);
    }

    public static void main(String[] args) {

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        ${devicename}InMessage msg = (${devicename}InMessage) message;
        byte[] incontent = msg.getContent();
        ${devicename}OutMessage outmsg = new ${devicename}OutMessage();
        byte[] outmsgcontent = outmsg.getContent();
        byte tempbyte;
        byte tempbyte2;
        byte[] tempbytes;


		<@buildNode node=protocal/>

		outmsg.setContent(outmsgcontent);


        <#if protocal.errorpkt?? >
		//byte[]  errorpkt1 = {(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0B,(byte)0x96,(byte)0x05,(byte)0xA8,(byte)0x06,(byte)0x59,(byte)0xAE};
		byte[]  errorpkt = {${protocal.errorpkt}};
		if(allerror){
            System.out.println("输出错误帧");
             outmsg.setContent(errorpkt);
        }
		//errorcount ++;
//        if(false&&errorcount%50==0){
//            System.out.println("输出错误的一帧");
//            outmsg.setContent(errorpkt1);
//            WriteFuture wf = session.write(outmsg);
//            wf.awaitUninterruptibly();
//        }
		</#if>




        WriteFuture wf = session.write(outmsg);
        wf.awaitUninterruptibly();

//        wf = session.write(outmsg);
//        wf.awaitUninterruptibly();
//      this.wait(getWaittime());
    }

//  byte[] k = BytePlus.int2bytes(r.nextInt(0x77777777));
//
//              // 传感器数据
//              System.arraycopy(k, 0, outmsgcontent, 6, 4);
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("session closed");
        saveData();
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("session idled:" + status.toString());
    }

    /**
     * @return the waittime
     */
    public int getWaittime() {
        return waittime;
    }

    /**
     * @param waittime the waittime to set
     */
    public void setWaittime(int waittime) {
        this.waittime = waittime;
    }

    /**
     * @return the device
     */
    public ${devicename} getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(${devicename} device) {
        this.device = device;
    }

    /**
     * 保存VirtualDevice机器数据
     */
    public void saveData() {
        try {
            ObjectOutputStream out = null;

            out = new ObjectOutputStream(new FileOutputStream(STORE_FILE));
            out.writeObject(device);
        } catch (IOException ex) {
            Logger.getLogger(${devicename}Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadData() {
        try {
            File f = new File(STORE_FILE);

            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream in = new ObjectInputStream(fis);
                device = (${devicename}) in.readObject();
//                device.setCellpresure_high(waittime);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(${devicename}Handler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(${devicename}Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

	//TODO 调用逻辑
	private void startsendertimer(IoSession session) {
        //开始试验
        startsender = true;
        if (timer == null) {
            timer = new Timer();
        } else {
            timer.cancel();
            timer = new Timer();
        }
        starttime = System.currentTimeMillis();
        if (st == null) {
            st = new SenderTask(session);
        } else {
            st.cancel();
            st = new SenderTask(session);
        }
        timer.scheduleAtFixedRate(st, 0, 1 * 1000); //subsequent rate
    }

	class SenderTask extends TimerTask {

        IoSession session;

        private SenderTask() {
        }

        SenderTask(IoSession _session) {
            session = _session;
        }

        public void run() {
            if (startsender == false) {
                this.cancel();
            } else {
                ${devicename}OutMessage pkt = new ${devicename}OutMessage();
                byte[] outmsgcontent = pkt.getContent();
				//TODO 进行数据包组装
                outmsgcontent[0] = 1;
                int temp, humidity, highpressure;
                temp = 380;
                System.arraycopy(BytePlus.int2bytes(temp), 2, outmsgcontent, 1, 2);
                humidity = 800;
                System.arraycopy(BytePlus.int2bytes(humidity), 2, outmsgcontent, 3, 2);
                highpressure = 1010;
                System.arraycopy(BytePlus.int2bytes(highpressure), 2, outmsgcontent, 5, 2);
                System.arraycopy(BytePlus.int2bytes(produceData(System.currentTimeMillis() - starttime)), 2, outmsgcontent, 7, 2);
                pkt.setContent(outmsgcontent);
                WriteFuture wf = session.write(pkt);// 发送消息
                System.out.println("send one data packet");
                wf.awaitUninterruptibly();
            }

        }

        public int produceData(long time) {
            time = time / 500;
			//TODO data生成逻辑
            double rtn = Math.abs(2e-10 * Math.pow(time, 4) - 1e-06 * Math.pow(time, 3) + 0.00278 * Math.pow(time, 2) - 0.34281 * time - 0.59106);

            return Math.round((float) rtn);
        }
    }

//    public static void main(String[] args) {
//        byte a = (byte) 0x80;
//        int b = a < 0 ? a + 256 : a;
//        System.out.println(b);
//    }
}
