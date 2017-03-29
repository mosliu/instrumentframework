package net.labthink.instrument.device.intelligent.industrialpc.simulator;

//~--- non-JDK imports --------------------------------------------------------

import net.labthink.utils.BytePlus;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.BitSet;
import java.util.Random;

/**
 * 模拟仪器
 * @author Moses
 */
public class industrialpc implements Serializable {

	//TODO 需要修改
    private static final long serialVersionUID = -685345324534534510L;

    private int currentState = 0;

    private String[] stateString =  {"",""};

    private transient  long   startTimeSpot        = 0;

	/** 是否重发 */
	private boolean resend = false;
	/** 是否校验错 */
	private boolean checkerror = false;
	/** 内容 */
	private byte[] content = new byte[2];
	/** XOR校验位 */
	private boolean xor = false;


	/**
	 * 获取 是否重发 的值
     * @return the resend
     */
    public boolean getResend() {
		return resend;
    }

    /**
	 * 设定 是否重发 的值
     * @param resend, the resend to set
     */
    public void setResend(boolean resend) {
		this.resend = resend;
    }
	/**
	 * 判断 是否重发 的值
     * @return the resend
     */
    public boolean isResend() {
		return resend;
    }


	/**
	 * 获取 是否校验错 的值
     * @return the checkerror
     */
    public boolean getCheckerror() {
		return checkerror;
    }

    /**
	 * 设定 是否校验错 的值
     * @param checkerror, the checkerror to set
     */
    public void setCheckerror(boolean checkerror) {
		this.checkerror = checkerror;
    }
	/**
	 * 判断 是否校验错 的值
     * @return the checkerror
     */
    public boolean isCheckerror() {
		return checkerror;
    }


	/**
	 * 获取 内容 的值
     * @return the content
     */
    public byte[] getContent() {
		return content;
    }

    /**
	 * 设定 内容 的值
     * @param content, the content to set
     */
    public void setContent(byte[] content) {
		this.content = content;
    }
	/**
	 * 获取 内容 的int值
     * @return the content
     */
    public int getContentInt() {
		return BytePlus.bytes2int(content);
    }

    /**
	 * 使用int值设定 内容
     * @param content, the content to set
     */
    public void setContentInt(int content) {
		this.content = BytePlus.getPartBytes(BytePlus.int2bytes(content),  4-2,2);
    }


	/**
	 * 获取 XOR校验位 的值
     * @return the xor
     */
    public boolean getXor() {
		return xor;
    }

    /**
	 * 设定 XOR校验位 的值
     * @param xor, the xor to set
     */
    public void setXor(boolean xor) {
		this.xor = xor;
    }


    private Random rand = new Random(System.currentTimeMillis());

    /**
     * 构造函数
     *
     */
    public industrialpc() {}

	//TODO 是否需要预设一些值
    public void PreSetSimulatorParameters(int balancetime,int muliplier) {
	/*
        cell1.setBalancetime(balancetime);
        cell1.setMultiplier(muliplier);
        cell1.setComputeFlag(1);
//        cell1.setComputeFlag(0);
        cell1.setPressureInt(rand.nextInt(13000));
        cell2.setBalancetime(balancetime);
        cell2.setMultiplier(muliplier);
        cell2.setComputeFlag(2);
//        cell2.setComputeFlag(0);
        cell2.setPressureInt(rand.nextInt(13000));
        cell3.setBalancetime(balancetime);
        cell3.setMultiplier(muliplier);
        cell3.setComputeFlag(3);
//        cell3.setComputeFlag(0);
        cell3.setPressureInt(rand.nextInt(13000));
	*/
    }

    public static void main(String[] args) {

    }

}
