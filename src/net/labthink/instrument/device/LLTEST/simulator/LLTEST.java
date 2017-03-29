package net.labthink.instrument.device.LLTEST.simulator;

//~--- non-JDK imports --------------------------------------------------------
import java.io.Serializable;

import net.labthink.instrument.device.LLTEST.message.LLTESTMessage;

/**
 * 模拟仪器，链路层测试。
 * 
 * @author Moses
 */
public class LLTEST implements Serializable {

    // private static int count= 0;
    // TODO 需要修改
    private static final long serialVersionUID = -585735756373510L;
    /** 是否重发 */
    private boolean resend = false;
    /** 是否校验错 */
    private boolean checkerror = false;
    /** 是否目标失败 */
    private boolean desterror = false;
    /** 是否网络繁忙 */
    private boolean netbusy = false;
    /** 是否传输失败 */
    private boolean sendfail = false;
    /** 传输地址 */
    private short destaddr = 0;
    private byte acknum = 0;
    /** 内容 */
    private LLTESTMessage packet = new LLTESTMessage(new byte[2]);
    /** XOR校验位 */
    private byte xor = 0;

    private void buildpacket() {
        if (resend) {
            packet.getContent()[0] = (byte) 0xff;
            if (checkerror) {
                packet.getContent()[1] = (byte) 0x1;

            } else if (sendfail) {
                packet.getContent()[1] = (byte) 0x2;
            } else if (desterror) {
                packet.getContent()[1] = (byte) 0x7;
            } else if (netbusy) {
                packet.getContent()[1] = (byte) 0x9;
            }
        } else {
            packet.getContent()[0] = (byte) 0x0;
            packet.getContent()[1] = (byte) 0x0;
        }

        packet.setHeader((byte) (0xE0 | (acknum & (byte) 0x0f)));
        packet.setAddress(destaddr);
        
        packet.rebuildpacket();
    }

    /**
     * 获取 是否重发 的值
     *
     * @return the resend
     */
    public boolean getResend() {
        return resend;
    }

    /**
     * 设定 是否重发 的值
     *
     * @param resend
     *            , the resend to set
     */
    public void setResend(boolean resend) {
        this.resend = resend;
    }

    /**
     * 判断 是否重发 的值
     *
     * @return the resend
     */
    public boolean isResend() {
        return resend;
    }

    /**
     * 获取 是否校验错 的值
     *
     * @return the checkerror
     */
    public boolean getCheckerror() {
        return checkerror;
    }

    /**
     * 设定 是否校验错 的值
     *
     * @param checkerror
     *            , the checkerror to set
     */
    public void setCheckerror(boolean checkerror) {
        this.checkerror = checkerror;
    }

    /**
     * 判断 是否校验错 的值
     *
     * @return the checkerror
     */
    public boolean isCheckerror() {
        return checkerror;
    }

    /**
     * 获取 XOR校验位 的值
     *
     * @return the xor
     */
    public byte getXor() {
        return xor;
    }

    /**
     * 设定 XOR校验位 的值
     *
     * @param xor
     *            , the xor to set
     */
    public void setXor(byte xor) {
        this.xor = xor;
    }

    /**
     * 构造函数
     *
     */
    public LLTEST() {
    }

    // TODO 是否需要预设一些值
    public void PreSetSimulatorParameters(int balancetime, int muliplier) {
        /*
         * cell1.setBalancetime(balancetime); cell1.setMultiplier(muliplier);
         * cell1.setComputeFlag(1); // cell1.setComputeFlag(0);
         * cell1.setPressureInt(rand.nextInt(13000));
         * cell2.setBalancetime(balancetime); cell2.setMultiplier(muliplier);
         * cell2.setComputeFlag(2); // cell2.setComputeFlag(0);
         * cell2.setPressureInt(rand.nextInt(13000));
         * cell3.setBalancetime(balancetime); cell3.setMultiplier(muliplier);
         * cell3.setComputeFlag(3); // cell3.setComputeFlag(0);
         * cell3.setPressureInt(rand.nextInt(13000));
         */
    }

    public static void main(String[] args) {
    }

    public boolean isSendfail() {
        return sendfail;
    }

    public void setSendfail(boolean sendfail) {
        this.sendfail = sendfail;
    }

    public short getDestaddr() {
        return destaddr;
    }

    public void setDestaddr(short destaddr) {
        this.destaddr = destaddr;
    }

    public boolean isDesterror() {
        return desterror;
    }

    public void setDesterror(boolean desterror) {
        this.desterror = desterror;
    }

    public boolean isNetbusy() {
        return netbusy;
    }

    public void setNetbusy(boolean netbusy) {
        this.netbusy = netbusy;
    }

    public byte getAcknum() {
        return acknum;
    }

    public void setAcknum(byte acknum) {
        this.acknum = acknum;
    }

    public LLTESTMessage getPacket() {
        buildpacket();
        return packet;
    }

    public void setPacket(LLTESTMessage packet) {
        this.packet = packet;
    }

}
