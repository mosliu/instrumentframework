package net.labthink.instrument.device.w3030.simulator;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.Random;
import net.labthink.utils.BytePlus;

/**
 * Class description
 *
 *
 * @version        1.0.0.2 ..., 10/07/02
 * @author         Moses
 */
public class W3030Device implements Serializable {
    private static long serialVersionUID = 1895845748234223474L;
    Random rand = new Random(System.currentTimeMillis());
    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }

    /**
     * 温控参数
     */
    private byte[][] PIDs = new byte[4][3];

    //环境湿度
    private int ambientHumidity = 0;
    //环境温度（通道2）
    private int ambientTemperature = 0;
    //天平量程X15=00H，200g量程，X15=01H，400g量程
    private byte balanceRange = 0;
    //标定系数1，两位小数
    private int calibrationfactor1 = 0;
    //标定系数2，两位小数
    private int calibrationfactor2 = 0;
    //标定系数3，两位小数
    private int calibrationfactor3 = 0;
    //标定系数4，两位小数
    private int calibrationfactor4 = 0;
    //透湿杯重量 (去皮后的重量)
    private int cupweight = 0;
    //透湿杯重量标志X14=01H，有透湿杯重量，X14=02H无透湿杯重量 
    private byte cupweightflag = 0;
    //去皮时间 一位小数，单位：分钟
    private byte peelofftime = 0;
    //是否开机
    private boolean poweron = false;
    //停风扇提前量 一位小数，单位：分钟
    private byte stopfantime = 0;
    //测试间隔 整数。单位：分钟
    private int testGap = 0;
    //试验湿度 高位在前，一位小数
    private int testHumidity = 0;
    //试验温度 高位在前，一位小数
    private int testTemperature = 0;
    //正在测试
    private boolean testing = false;

    public CupWeightProducer cw = new CupWeightProducer();

    public W3030Device() {
         balanceRange = 1;//400g天平
         testGap = 1;//间隔默认1
         peelofftime = 10;//去皮时间默认1分钟
         stopfantime = 20;//停风扇提前默认2分钟
         calibrationfactor1 = 54;//默认0.54标定系数
         setPoweron(true);
         testTemperature = 380;
         testHumidity = 900;
         ambientTemperature=280;
         ambientHumidity = 450;
         cupweightflag = 1;
    }



    /**
     * @return the PIDs
     */
    public byte[][] getPIDs() {
        return PIDs;
    }

    /**
     * @param PIDs the PIDs to set
     */
    public void setPIDs(byte[][] PIDs) {
        this.PIDs = PIDs;
    }

    /**
     * @return the ambientHumidity
     */
    public int getAmbientHumidity() {
        return ambientHumidity;
    }

    /**
     * @return the ambientHumidity
     */
    public byte[] getAmbientHumidityBS() {
        return CopyIntByLength(ambientHumidity+(rand.nextInt(21)-10),2);
    }


    /**
     * @param ambientHumidity the ambientHumidity to set
     */
    public void setAmbientHumidity(int ambientHumidity) {
        this.ambientHumidity = ambientHumidity;
    }

    /**
     * @return the ambientTemperature
     */
    public int getAmbientTemperature() {
        return ambientTemperature;
    }

    /**
     * @return the ambientTemperature
     */
    public byte[] getAmbientTemperatureBS() {
        return CopyIntByLength(ambientTemperature+(rand.nextInt(21)-10),2);
    }


    /**
     * @param ambientTemperature the ambientTemperature to set
     */
    public void setAmbientTemperature(int ambientTemperature) {
        this.ambientTemperature = ambientTemperature;
    }

    /**
     * @return the balanceRange
     */
    public byte getBalanceRange() {
        return balanceRange;
    }

    /**
     * @param balanceRange the balanceRange to set
     */
    public void setBalanceRange(byte balanceRange) {
        this.balanceRange = balanceRange;
    }

    /**
     * @return the calibrationfactor1
     */
    public int getCalibrationfactor1() {
        return calibrationfactor1;
    }

    /**
     * @return the calibrationfactor1
     */
    public byte[] getCalibrationfactor1BS() {
        return CopyIntByLength(calibrationfactor1,2);
    }

    /**
     * @param calibrationfactor1 the calibrationfactor1 to set
     */
    public void setCalibrationfactor1(int calibrationfactor1) {
        this.calibrationfactor1 = calibrationfactor1;
    }

    /**
     * @return the calibrationfactor2
     */
    public int getCalibrationfactor2() {
        return calibrationfactor2;
    }

    /**
     * @return the calibrationfactor2
     */
    public byte[] getCalibrationfactor2BS() {
        return CopyIntByLength(calibrationfactor2,2);
    }

    /**
     * @param calibrationfactor2 the calibrationfactor2 to set
     */
    public void setCalibrationfactor2(int calibrationfactor2) {
        this.calibrationfactor2 = calibrationfactor2;
    }

    /**
     * @return the calibrationfactor3
     */
    public int getCalibrationfactor3() {
        return calibrationfactor3;
    }

    /**
     * @return the calibrationfactor3
     */
    public byte[] getCalibrationfactor3BS() {
        return CopyIntByLength(calibrationfactor3,2);
    }


    /**
     * @param calibrationfactor3 the calibrationfactor3 to set
     */
    public void setCalibrationfactor3(int calibrationfactor3) {
        this.calibrationfactor3 = calibrationfactor3;
    }

    /**
     * @return the calibrationfactor4
     */
    public int getCalibrationfactor4() {
        return calibrationfactor4;
    }

    /**
     * @return the calibrationfactor4
     */
    public byte[] getCalibrationfactor4BS() {
        return CopyIntByLength(calibrationfactor4,2);
    }


    /**
     * @param calibrationfactor4 the calibrationfactor4 to set
     */
    public void setCalibrationfactor4(int calibrationfactor4) {
        this.calibrationfactor4 = calibrationfactor4;
    }

    /**
     * @return the cupweight
     */
    public int getCupweight() {
        cupweight = cw.getWeightRandom(testGap);
//        cupweight = cw.getWeightList(testGap);

        return cupweight;
    }

    /**
     * @return the cupweight
     */
    public byte[] getCupweightBS() {
        cupweight = cw.getWeightList(testGap);
//        cupweight = cw.getWeightRandom(testGap);
        return CopyIntByLength(cupweight,3);
    }

    /**
     * @param cupweight the cupweight to set
     */
    public void setCupweight(int cupweight) {
        this.cupweight = cupweight;
    }

    /**
     * @return the cupweightflag
     */
    public byte getCupweightflag() {
        
//        return cupweightflag;
        return cw.getWeightFlag(testGap);
    }

    /**
     * @param cupweightflag the cupweightflag to set
     */
    public void setCupweightflag(byte cupweightflag) {
        this.cupweightflag = cupweightflag;
    }

    /**
     * @return the peelofftime
     */
    public byte getPeelofftime() {
        return peelofftime;
    }

    /**
     * @param peelofftime the peelofftime to set
     */
    public void setPeelofftime(byte peelofftime) {
        this.peelofftime = peelofftime;
    }

    /**
     * @return the poweron
     */
    public boolean isPoweron() {
        return poweron;
    }

    /**
     * @param poweron the poweron to set
     */
    public void setPoweron(boolean poweron) {
        this.poweron = poweron;
    }

    /**
     * @return the stopfantime
     */
    public byte getStopfantime() {
        return stopfantime;
    }

    /**
     * @param stopfantime the stopfantime to set
     */
    public void setStopfantime(byte stopfantime) {
        this.stopfantime = stopfantime;
    }

    /**
     * @return the testGap
     */
    public int getTestGap() {
        return testGap;
    }

    /**
     * @return the testGap
     */
    public byte[] getTestGapBS() {
        return CopyIntByLength(testGap,2);
    }


    /**
     * @param testGap the testGap to set
     */
    public void setTestGap(int testGap) {
        this.testGap = testGap;
    }

    /**
     * @return the testHumidity
     */
    public int getTestHumidity() {
        return testHumidity;
    }


    /**
     * @return the testHumidity
     */
    public byte[] getTestHumidityBS() {
        return CopyIntByLength(testHumidity+(rand.nextInt(21)-10),2);
    }

    /**
     * @param testHumidity the testHumidity to set
     */
    public void setTestHumidity(int testHumidity) {
        this.testHumidity = testHumidity;
    }

    /**
     * @return the testTemperature
     */
    public int getTestTemperature() {
        return testTemperature;
    }

    /**
     * @return the testTemperature
     */
    public byte[] getTestTemperatureBS() {
//        return CopyIntByLength(testTemperature+(rand.nextInt(21)-10),2);
        return CopyIntByLength(testTemperature+(rand.nextInt(21)-10),2);
    }

    /**
     * @param testTemperature the testTemperature to set
     */
    public void setTestTemperature(int testTemperature) {
        this.testTemperature = testTemperature;
    }

    /**
     * @return the testing
     */
    public boolean isTesting() {
        return testing;
    }

    /**
     * @param testing the testing to set
     */
    public void setTesting(boolean testing) {
        this.testing = testing;
    }

    public static byte[] CopyByLength(byte[] src,int length){
        byte[] bs = new byte[length];
        System.arraycopy(src, src.length-length, bs, 0, length);
        return bs;
    }

    public static byte[] CopyIntByLength(int src,int length){
        byte[] bs = new byte[length];
        System.arraycopy(BytePlus.int2bytes(src), 4-length, bs, 0, length);
        return bs;
    }
}
