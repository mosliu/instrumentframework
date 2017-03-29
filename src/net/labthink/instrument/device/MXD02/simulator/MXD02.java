package net.labthink.instrument.device.MXD02.simulator;

//~--- non-JDK imports --------------------------------------------------------
import net.labthink.utils.BytePlus;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.BitSet;
import java.util.Random;
import net.labthink.utils.Randomer;

/**
 * 模拟仪器
 * @author Moses
 */
public class MXD02 implements Serializable {

    //TODO 需要修改
    private static final long serialVersionUID = -38537457634510L;
    private Randomer rand = new Randomer();
    private int currentState = 0;
    private String[] stateString = {"", ""};
    private transient long startTimeSpot = 0;
    /** 是否重发 */
    private boolean resend = false;
    /** 是否校验错 */
    private boolean checkerror = false;
    /** 内容 */
    private byte[] content = new byte[8];
    /** SM校验位 */
    private byte checksum = 0;
    /** 实验类型 */
    private byte testtype = 0;
    /** 实验件数 */
    private int testtimes = 0;
    /** 波动范围 */
    private int range = 0;
    /** 滑块质量 */
    private int sledmess = 200;

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
        this.content = BytePlus.getPartBytes(BytePlus.int2bytes(content), 4 - 8, 8);
    }

    /**
     * 获取 SM校验位 的值
     * @return the checksum
     */
    public byte getChecksum() {
        return checksum;
    }

    /**
     * 设定 SM校验位 的值
     * @param checksum, the checksum to set
     */
    public void setChecksum(byte checksum) {
        this.checksum = checksum;
    }

    /**
     * 获取 实验类型 的值
     * @return the testtype
     */
    public byte getTesttype() {
        return testtype;
    }

    /**
     * 设定 实验类型 的值
     * @param testtype, the testtype to set
     */
    public void setTesttype(byte testtype) {
        this.testtype = testtype;
    }

    /**
     * 获取 实验件数 的值
     * @return the testtimes
     */
    public int getTesttimes() {
        return testtimes;
    }

    /**
     * 设定 实验件数 的值
     * @param testtimes, the testtimes to set
     */
    public void setTesttimes(int testtimes) {
        this.testtimes = testtimes;
    }

    /**
     * 获取 滑块质量 的值
     * @return the sledmess
     */
    public int getSledmess() {
        return sledmess;
    }

    /**
     * 设定 滑块质量 的值
     * @param sledmess, the sledmess to set
     */
    public void setSledmess(int sledmess) {
        this.sledmess = sledmess;
    }

    /**
     * @return the range
     */
    public int getRange() {
        return range;
    }

    /**
     * @param range the range to set
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * 构造函数
     *
     */
    public MXD02() {
    }

    //TODO 是否需要预设一些值
    public void PreSetSimulatorParameters(int balancetime, int muliplier) {
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

    public byte[] getPrePKT() {
        content[0] = 0;//标志位：参数
        content[1] = 1;//ASTM
        //设置件数
        byte[] tempbytes = BytePlus.int2bytes(testtimes);
        System.arraycopy(tempbytes, 2, content, 2, 2);
        content[4] = 5;//静摩擦时间
        //滑块质量
        tempbytes = BytePlus.int2bytes(sledmess);
        System.arraycopy(tempbytes, 2, content, 5, 2);

        content[7] = 0;

        return content;
    }

    public byte[] getResultPKT() {
        content[0] = 2;//标志位：结果数据
        int staticfriction = rand.nextInt(300, 400);
        int kineticfriction = rand.nextInt(250, 350);
        //设置静摩擦系数
        byte[] tempbytes = BytePlus.int2bytes(staticfriction);
        System.arraycopy(tempbytes, 2, content, 1, 2);
        //设置动摩擦系数
        tempbytes = BytePlus.int2bytes(kineticfriction);
        System.arraycopy(tempbytes, 2, content, 3, 2);

        //无用位
        content[5] = 0;
        content[6] = 0;
        //校验位
        content[7] = 0;

        return content;
    }

    public byte[] getStatPKT() {
        content[0] = 8;//统计试验结果
        int staticfriction = rand.nextInt(300, 400);
        int kineticfriction = rand.nextInt(250, 350);
        //设置静摩擦系数
        byte[] tempbytes = BytePlus.int2bytes(staticfriction);
        System.arraycopy(tempbytes, 2, content, 1, 2);
        //设置动摩擦系数
        tempbytes = BytePlus.int2bytes(kineticfriction);
        System.arraycopy(tempbytes, 2, content, 3, 2);

        //无用位
        content[5] = 0;
        content[6] = 0;
        //校验位
        content[7] = 0;
        return content;
    }

    public byte[] getSDPKT() {
        content[0] = 3;//统计试验结果
        int staticfriction = rand.nextInt(300, 400);
        int kineticfriction = rand.nextInt(250, 350);
        //设置静摩擦系数
        byte[] tempbytes = BytePlus.int2bytes(staticfriction);
        System.arraycopy(tempbytes, 2, content, 1, 2);
        //设置动摩擦系数
        tempbytes = BytePlus.int2bytes(kineticfriction);
        System.arraycopy(tempbytes, 2, content, 3, 2);

        //无用位
        content[5] = 0;
        content[6] = 0;
        //校验位
        content[7] = 0;
        return content;
    }

    public byte[][] getDetailsPKTs(int currentnum) {
        byte[][] rtn = new byte[30][8];
        for (int i = 0; i < rtn.length; i++) {
            content[0] = 1;//实时数据
            int friction = rand.nextInt(300, 350);
//            System.out.println(friction);
            //设置当前件数
            byte[] tempbytes = BytePlus.int2bytes(currentnum+1);
            System.arraycopy(tempbytes, 2, content, 1, 2);
            //设置摩擦力
            tempbytes = BytePlus.int2bytes(friction);
            System.arraycopy(tempbytes, 2, content, 3, 2);

            //试验时间
            content[5] = (byte)(i/12);
            //无用位
            content[6] = 0;
            //校验位
            content[7] = 0;
            System.arraycopy(content, 0, rtn[i], 0, 8);
//            rtn[i] = content;
        }

        return rtn;
    }

    public static void main(String[] args) {
        MXD02 device = new MXD02();
        byte[][] k = device.getDetailsPKTs(11);
        for (int i = 0; i < k.length; i++) {
            System.out.println(BytePlus.bytesToHexString(k[i]));
        }
            System.out.println(BytePlus.bytesToHexString(device.getResultPKT()));

        
    }
}
