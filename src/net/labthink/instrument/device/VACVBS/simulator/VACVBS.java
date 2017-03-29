package net.labthink.instrument.device.VACVBS.simulator;

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
public class VACVBS implements Serializable {

    //TODO 需要修改
    private static final long serialVersionUID = -685345324534534510L;
    private int currentState = 0;
    private String[] stateString = {"待机", "上腔吹洗"};
    private transient long startTimeSpot = 0;
    /** 机器是否上电 */
    private boolean devicepower = false;
    /** 是否正在试验 */
    private boolean testing = false;
    /** 阀门状态 */
    private byte[] valves = new byte[2];
    /** 标定系数 */
    private byte[] factor = new byte[2];
    /** 温度零点 */
    private byte[] tempzero = new byte[2];
    /** 温度终点 */
    private byte[] tempend = new byte[2];
    /** 湿度零点 */
    private byte[] humidityzero = new byte[2];
    /** 湿度终点 */
    private byte[] humidityend = new byte[2];
    /** 高压零点 */
    private byte[] highpressurezero = new byte[2];
    /** 高压终点 */
    private byte[] highpressureend = new byte[2];
    /** 低压零点 */
    private byte[] lowpressurezero = new byte[2];
    /** 低压终点 */
    private byte[] lowpressureend = new byte[2];
    /** 设定压力 */
    private byte[] pressuresetpoint = new byte[2];
    /** 设定压力修正值 */
    private byte[] pressuresetpointfix = new byte[2];
    /** 抽空持续时间，毫秒 */
    private byte[] pumpouttime = new byte[2];
    /** 控制幅值，	单位：kPa（X2X3设定值，高位在前，两位小数） */
    private byte[] controlamplitude = new byte[2];
    /** 控制周期，单位：秒（X2X3设定值） */
    private byte[] controlperiod = new byte[2];
    /** 指示灯 */
    private byte leds = 0;

    /**
     * 获取 机器是否上电 的值
     * @return the devicepower
     */
    public boolean getDevicepower() {
        return devicepower;
    }

    /**
     * 设定 机器是否上电 的值
     * @param devicepower, the devicepower to set
     */
    public void setDevicepower(boolean devicepower) {
        this.devicepower = devicepower;
    }

    /**
     * 判断 机器是否上电 的值
     * @return the devicepower
     */
    public boolean isDevicepower() {
        return devicepower;
    }

    /**
     * 获取 是否正在试验 的值
     * @return the testing
     */
    public boolean getTesting() {
        return testing;
    }

    /**
     * 设定 是否正在试验 的值
     * @param testing, the testing to set
     */
    public void setTesting(boolean testing) {
        this.testing = testing;
    }

    /**
     * 判断 是否正在试验 的值
     * @return the testing
     */
    public boolean isTesting() {
        return testing;
    }

    /**
     * 获取具体的阀门的状态
     *
     */

    public boolean isValveN(int i) {
        byte ab = (byte) (1 << i);
        return (valves[1]&ab)==ab;
    }


    /**
     * 获取 阀门状态 的值
     * @return the valves
     */
    public byte[] getValves() {
        return valves;
    }

    /**
     * 设定 阀门状态 的值
     * @param valves, the valves to set
     */
    public void setValves(byte[] valves) {
        this.valves = valves;
    }

    /**
     * 获取 阀门状态 的int值
     * @return the valves
     */
    public int getValvesInt() {
        return BytePlus.bytes2int(valves);
    }

    /**
     * 使用int值设定 阀门状态
     * @param valves, the valves to set
     */
    public void setValvesInt(int valves) {
        this.valves = BytePlus.getPartBytes(BytePlus.int2bytes(valves), 2, 4 - 2);
    }

    /**
     * 获取 标定系数 的值
     * @return the factor
     */
    public byte[] getFactor() {
        return factor;
    }

    /**
     * 设定 标定系数 的值
     * @param factor, the factor to set
     */
    public void setFactor(byte[] factor) {
        this.factor = factor;
    }

    /**
     * 获取 标定系数 的int值
     * @return the factor
     */
    public int getFactorInt() {
        return BytePlus.bytes2int(factor);
    }

    /**
     * 使用int值设定 标定系数
     * @param factor, the factor to set
     */
    public void setFactorInt(int factor) {
        this.factor = BytePlus.getPartBytes(BytePlus.int2bytes(factor), 2, 4 - 2);
    }

    /**
     * 获取 温度零点 的值
     * @return the tempzero
     */
    public byte[] getTempzero() {
        return tempzero;
    }

    /**
     * 设定 温度零点 的值
     * @param tempzero, the tempzero to set
     */
    public void setTempzero(byte[] tempzero) {
        this.tempzero = tempzero;
    }

    /**
     * 获取 温度零点 的int值
     * @return the tempzero
     */
    public int getTempzeroInt() {
        return BytePlus.bytes2int(tempzero);
    }

    /**
     * 使用int值设定 温度零点
     * @param tempzero, the tempzero to set
     */
    public void setTempzeroInt(int tempzero) {
        this.tempzero = BytePlus.getPartBytes(BytePlus.int2bytes(tempzero), 2, 4 - 2);
    }

    /**
     * 获取 温度终点 的值
     * @return the tempend
     */
    public byte[] getTempend() {
        return tempend;
    }

    /**
     * 设定 温度终点 的值
     * @param tempend, the tempend to set
     */
    public void setTempend(byte[] tempend) {
        this.tempend = tempend;
    }

    /**
     * 获取 温度终点 的int值
     * @return the tempend
     */
    public int getTempendInt() {
        return BytePlus.bytes2int(tempend);
    }

    /**
     * 使用int值设定 温度终点
     * @param tempend, the tempend to set
     */
    public void setTempendInt(int tempend) {
        this.tempend = BytePlus.getPartBytes(BytePlus.int2bytes(tempend), 2, 4 - 2);
    }

    /**
     * 获取 湿度零点 的值
     * @return the humidityzero
     */
    public byte[] getHumidityzero() {
        return humidityzero;
    }

    /**
     * 设定 湿度零点 的值
     * @param humidityzero, the humidityzero to set
     */
    public void setHumidityzero(byte[] humidityzero) {
        this.humidityzero = humidityzero;
    }

    /**
     * 获取 湿度零点 的int值
     * @return the humidityzero
     */
    public int getHumidityzeroInt() {
        return BytePlus.bytes2int(humidityzero);
    }

    /**
     * 使用int值设定 湿度零点
     * @param humidityzero, the humidityzero to set
     */
    public void setHumidityzeroInt(int humidityzero) {
        this.humidityzero = BytePlus.getPartBytes(BytePlus.int2bytes(humidityzero), 2, 4 - 2);
    }

    /**
     * 获取 湿度终点 的值
     * @return the humidityend
     */
    public byte[] getHumidityend() {
        return humidityend;
    }

    /**
     * 设定 湿度终点 的值
     * @param humidityend, the humidityend to set
     */
    public void setHumidityend(byte[] humidityend) {
        this.humidityend = humidityend;
    }

    /**
     * 获取 湿度终点 的int值
     * @return the humidityend
     */
    public int getHumidityendInt() {
        return BytePlus.bytes2int(humidityend);
    }

    /**
     * 使用int值设定 湿度终点
     * @param humidityend, the humidityend to set
     */
    public void setHumidityendInt(int humidityend) {
        this.humidityend = BytePlus.getPartBytes(BytePlus.int2bytes(humidityend), 2, 4 - 2);
    }

    /**
     * 获取 高压零点 的值
     * @return the highpressurezero
     */
    public byte[] getHighpressurezero() {
        return highpressurezero;
    }

    /**
     * 设定 高压零点 的值
     * @param highpressurezero, the highpressurezero to set
     */
    public void setHighpressurezero(byte[] highpressurezero) {
        this.highpressurezero = highpressurezero;
    }

    /**
     * 获取 高压零点 的int值
     * @return the highpressurezero
     */
    public int getHighpressurezeroInt() {
        return BytePlus.bytes2int(highpressurezero);
    }

    /**
     * 使用int值设定 高压零点
     * @param highpressurezero, the highpressurezero to set
     */
    public void setHighpressurezeroInt(int highpressurezero) {
        this.highpressurezero = BytePlus.getPartBytes(BytePlus.int2bytes(highpressurezero), 2, 4 - 2);
    }

    /**
     * 获取 高压终点 的值
     * @return the highpressureend
     */
    public byte[] getHighpressureend() {
        return highpressureend;
    }

    /**
     * 设定 高压终点 的值
     * @param highpressureend, the highpressureend to set
     */
    public void setHighpressureend(byte[] highpressureend) {
        this.highpressureend = highpressureend;
    }

    /**
     * 获取 高压终点 的int值
     * @return the highpressureend
     */
    public int getHighpressureendInt() {
        return BytePlus.bytes2int(highpressureend);
    }

    /**
     * 使用int值设定 高压终点
     * @param highpressureend, the highpressureend to set
     */
    public void setHighpressureendInt(int highpressureend) {
        this.highpressureend = BytePlus.getPartBytes(BytePlus.int2bytes(highpressureend), 2, 4 - 2);
    }

    /**
     * 获取 低压零点 的值
     * @return the lowpressurezero
     */
    public byte[] getLowpressurezero() {
        return lowpressurezero;
    }

    /**
     * 设定 低压零点 的值
     * @param lowpressurezero, the lowpressurezero to set
     */
    public void setLowpressurezero(byte[] lowpressurezero) {
        this.lowpressurezero = lowpressurezero;
    }

    /**
     * 获取 低压零点 的int值
     * @return the lowpressurezero
     */
    public int getLowpressurezeroInt() {
        return BytePlus.bytes2int(lowpressurezero);
    }

    /**
     * 使用int值设定 低压零点
     * @param lowpressurezero, the lowpressurezero to set
     */
    public void setLowpressurezeroInt(int lowpressurezero) {
        this.lowpressurezero = BytePlus.getPartBytes(BytePlus.int2bytes(lowpressurezero), 2, 4 - 2);
    }

    /**
     * 获取 低压终点 的值
     * @return the lowpressureend
     */
    public byte[] getLowpressureend() {
        return lowpressureend;
    }

    /**
     * 设定 低压终点 的值
     * @param lowpressureend, the lowpressureend to set
     */
    public void setLowpressureend(byte[] lowpressureend) {
        this.lowpressureend = lowpressureend;
    }

    /**
     * 获取 低压终点 的int值
     * @return the lowpressureend
     */
    public int getLowpressureendInt() {
        return BytePlus.bytes2int(lowpressureend);
    }

    /**
     * 使用int值设定 低压终点
     * @param lowpressureend, the lowpressureend to set
     */
    public void setLowpressureendInt(int lowpressureend) {
        this.lowpressureend = BytePlus.getPartBytes(BytePlus.int2bytes(lowpressureend), 2, 4 - 2);
    }

    /**
     * 获取 设定压力 的值
     * @return the pressuresetpoint
     */
    public byte[] getPressuresetpoint() {
        return pressuresetpoint;
    }

    /**
     * 设定 设定压力 的值
     * @param pressuresetpoint, the pressuresetpoint to set
     */
    public void setPressuresetpoint(byte[] pressuresetpoint) {
        this.pressuresetpoint = pressuresetpoint;
    }

    /**
     * 获取 设定压力 的int值
     * @return the pressuresetpoint
     */
    public int getPressuresetpointInt() {
        return BytePlus.bytes2int(pressuresetpoint);
    }

    /**
     * 使用int值设定 设定压力
     * @param pressuresetpoint, the pressuresetpoint to set
     */
    public void setPressuresetpointInt(int pressuresetpoint) {
        this.pressuresetpoint = BytePlus.getPartBytes(BytePlus.int2bytes(pressuresetpoint), 2, 4 - 2);
    }

    /**
     * 获取 设定压力修正值 的值
     * @return the pressuresetpointfix
     */
    public byte[] getPressuresetpointfix() {
        return pressuresetpointfix;
    }

    /**
     * 设定 设定压力修正值 的值
     * @param pressuresetpointfix, the pressuresetpointfix to set
     */
    public void setPressuresetpointfix(byte[] pressuresetpointfix) {
        this.pressuresetpointfix = pressuresetpointfix;
    }

    /**
     * 获取 设定压力修正值 的int值
     * @return the pressuresetpointfix
     */
    public int getPressuresetpointfixInt() {
        return BytePlus.bytes2int(pressuresetpointfix);
    }

    /**
     * 使用int值设定 设定压力修正值
     * @param pressuresetpointfix, the pressuresetpointfix to set
     */
    public void setPressuresetpointfixInt(int pressuresetpointfix) {
        this.pressuresetpointfix = BytePlus.getPartBytes(BytePlus.int2bytes(pressuresetpointfix), 2, 4 - 2);
    }

    /**
     * 获取 抽空持续时间，毫秒 的值
     * @return the pumpouttime
     */
    public byte[] getPumpouttime() {
        return pumpouttime;
    }

    /**
     * 设定 抽空持续时间，毫秒 的值
     * @param pumpouttime, the pumpouttime to set
     */
    public void setPumpouttime(byte[] pumpouttime) {
        this.pumpouttime = pumpouttime;
    }

    /**
     * 获取 抽空持续时间，毫秒 的int值
     * @return the pumpouttime
     */
    public int getPumpouttimeInt() {
        return BytePlus.bytes2int(pumpouttime);
    }

    /**
     * 使用int值设定 抽空持续时间，毫秒
     * @param pumpouttime, the pumpouttime to set
     */
    public void setPumpouttimeInt(int pumpouttime) {
        this.pumpouttime = BytePlus.getPartBytes(BytePlus.int2bytes(pumpouttime), 2, 4 - 2);
    }

    /**
     * 获取 控制幅值，	单位：kPa（X2X3设定值，高位在前，两位小数） 的值
     * @return the controlamplitude
     */
    public byte[] getControlamplitude() {
        return controlamplitude;
    }

    /**
     * 设定 控制幅值，	单位：kPa（X2X3设定值，高位在前，两位小数） 的值
     * @param controlamplitude, the controlamplitude to set
     */
    public void setControlamplitude(byte[] controlamplitude) {
        this.controlamplitude = controlamplitude;
    }

    /**
     * 获取 控制幅值，	单位：kPa（X2X3设定值，高位在前，两位小数） 的int值
     * @return the controlamplitude
     */
    public int getControlamplitudeInt() {
        return BytePlus.bytes2int(controlamplitude);
    }

    /**
     * 使用int值设定 控制幅值，	单位：kPa（X2X3设定值，高位在前，两位小数）
     * @param controlamplitude, the controlamplitude to set
     */
    public void setControlamplitudeInt(int controlamplitude) {
        this.controlamplitude = BytePlus.getPartBytes(BytePlus.int2bytes(controlamplitude), 2, 4 - 2);
    }

    /**
     * 获取 控制周期，单位：秒（X2X3设定值） 的值
     * @return the controlperiod
     */
    public byte[] getControlperiod() {
        return controlperiod;
    }

    /**
     * 设定 控制周期，单位：秒（X2X3设定值） 的值
     * @param controlperiod, the controlperiod to set
     */
    public void setControlperiod(byte[] controlperiod) {
        this.controlperiod = controlperiod;
    }

    /**
     * 获取 控制周期，单位：秒（X2X3设定值） 的int值
     * @return the controlperiod
     */
    public int getControlperiodInt() {
        return BytePlus.bytes2int(controlperiod);
    }

    /**
     * 使用int值设定 控制周期，单位：秒（X2X3设定值）
     * @param controlperiod, the controlperiod to set
     */
    public void setControlperiodInt(int controlperiod) {
        this.controlperiod = BytePlus.getPartBytes(BytePlus.int2bytes(controlperiod), 2, 4 - 2);
    }

    /**
     * 获取 指示灯 的值
     * @return the leds
     */
    public byte getLeds() {
        return leds;
    }

    /**
     * 设定 指示灯 的值
     * @param leds, the leds to set
     */
    public void setLeds(byte leds) {
        this.leds = leds;
    }
    private Random rand = new Random(System.currentTimeMillis());

    /**
     * 构造函数
     *
     */
    public VACVBS() {
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

     public static void main(String[] args) {
         byte[] aa = {00,17};
         VACVBS device = new VACVBS();
         device.setValves(aa);
         for (int i = 0; i < 5 ; i++) {
             System.out.println(device.isValveN(i));
         }
         System.out.println(1<<0);
    }
}
