package net.labthink.instrument.device.CommandSender.simulator;

//~--- non-JDK imports --------------------------------------------------------
import net.labthink.utils.BytePlus;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.labthink.utils.Randomer;

/**
 * 模拟CommandSender仪器,未使用
 * @author Moses
 */
public class CommandSender implements Serializable {

    private static final long serialVersionUID = -67657689273576250L;
    private int currentState = 0;
    private String[] stateString = {"待机", "试验"};

    private Randomer r = null;

    private List<Integer> li = new ArrayList<Integer>();

    private transient long startTimeSpot = 0;

    private int count = 0;

    /** 机器是否上电 */
    private boolean devicepower = false;
    /** 是否正在试验 */
    private boolean testing = false;
    /** 当前值 */
    private byte[] currentvalue = new byte[3];
    /** 平均值 */
    private byte[] averagevalue = new byte[3];
    /** 标准差 */
    private byte[] deltavalue = new byte[2];
    /** 最大值 */
    private byte[] maxvalue = new byte[3];
    /** 最小值 */
    private byte[] minvalue = new byte[3];

    public void init(long max,long min){
        r = new Randomer(max,min);
        this.count = 0;
//        this.count= count;
    }

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
     * 获取 当前值 的值
     * @return the currentvalue
     */
    public byte[] getCurrentvalue() {
        productvalue();
        return currentvalue;
    }

    public byte[] getValue() {
        productvalue();
        return currentvalue;
    }

    /**
     * 设定 当前值 的值
     * @param currentvalue, the currentvalue to set
     */
    public void setCurrentvalue(byte[] currentvalue) {
        this.currentvalue = currentvalue;
    }

    /**
     * 获取 当前值 的int值
     * @return the currentvalue
     */
    public int getCurrentvalueInt() {
        return BytePlus.bytes2int(currentvalue);
    }

    /**
     * 获取 当前值 的int值
     * @return the currentvalue
     */
    public int getValueInt() {
        productvalue();
        return BytePlus.bytes2int(currentvalue);
    }

    /**
     * 使用int值设定 当前值
     * @param currentvalue, the currentvalue to set
     */
    public void setCurrentvalueInt(int currentvalue) {
        this.currentvalue = BytePlus.getPartBytes(BytePlus.int2bytes(currentvalue), 1, 3);
    }

    /**
     * 获取 平均值 的值
     * @return the averagevalue
     */
    public byte[] getAveragevalue() {
        return averagevalue;
    }

    /**
     * 设定 平均值 的值
     * @param averagevalue, the averagevalue to set
     */
    public void setAveragevalue(byte[] averagevalue) {
        this.averagevalue = averagevalue;
    }

    /**
     * 获取 平均值 的int值
     * @return the averagevalue
     */
    public int getAveragevalueInt() {
        return BytePlus.bytes2int(averagevalue);
    }

    /**
     * 使用int值设定 平均值
     * @param averagevalue, the averagevalue to set
     */
    public void setAveragevalueInt(int averagevalue) {
        this.averagevalue = BytePlus.getPartBytes(BytePlus.int2bytes(averagevalue), 1, 3);
    }

    /**
     * 获取 标准差 的值
     * @return the deltavalue
     */
    public byte[] getDeltavalue() {
        return deltavalue;
    }

    /**
     * 设定 标准差 的值
     * @param deltavalue, the deltavalue to set
     */
    public void setDeltavalue(byte[] deltavalue) {
        this.deltavalue = deltavalue;
    }

    /**
     * 获取 标准差 的int值
     * @return the deltavalue
     */
    public int getDeltavalueInt() {
        return BytePlus.bytes2int(deltavalue);
    }

    /**
     * 使用int值设定 标准差
     * @param deltavalue, the deltavalue to set
     */
    public void setDeltavalueInt(int deltavalue) {
        this.deltavalue = BytePlus.getPartBytes(BytePlus.int2bytes(deltavalue), 2, 4 - 2);
    }

    /**
     * 获取 最大值 的值
     * @return the maxvalue
     */
    public byte[] getMaxvalue() {
        return maxvalue;
    }

    /**
     * 设定 最大值 的值
     * @param maxvalue, the maxvalue to set
     */
    public void setMaxvalue(byte[] maxvalue) {
        this.maxvalue = maxvalue;
    }

    /**
     * 获取 最大值 的int值
     * @return the maxvalue
     */
    public int getMaxvalueInt() {
        return BytePlus.bytes2int(maxvalue);
    }

    /**
     * 使用int值设定 最大值
     * @param maxvalue, the maxvalue to set
     */
    public void setMaxvalueInt(int maxvalue) {
        this.maxvalue = BytePlus.getPartBytes(BytePlus.int2bytes(maxvalue), 1, 3);
    }

    /**
     * 获取 最小值 的值
     * @return the minvalue
     */
    public byte[] getMinvalue() {
        return minvalue;
    }

    /**
     * 设定 最小值 的值
     * @param minvalue, the minvalue to set
     */
    public void setMinvalue(byte[] minvalue) {
        this.minvalue = minvalue;
    }

    /**
     * 获取 最小值 的int值
     * @return the minvalue
     */
    public int getMinvalueInt() {
        return BytePlus.bytes2int(minvalue);
    }

    /**
     * 使用int值设定 最小值
     * @param minvalue, the minvalue to set
     */
    public void setMinvalueInt(int minvalue) {
        this.minvalue = BytePlus.getPartBytes(BytePlus.int2bytes(minvalue), 1, 3);
    }
    private Random rand = new Random(System.currentTimeMillis());

    /**
     * 构造函数
     *
     */
    public CommandSender() {
    }

    

    public void productvalue(){
        int cv = r.nextInt(0xffffff);
        if(cv>getMaxvalueInt()){
            setMaxvalueInt(cv);
        }
        if(cv<getMinvalueInt()){
            setMinvalueInt(cv);
        }
        if(count >0){
            setAveragevalueInt((getAveragevalueInt()*count+cv)/(count+1));
        }
        setCurrentvalueInt(cv);
        li.add(cv);
        count++;
        
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
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }
}
