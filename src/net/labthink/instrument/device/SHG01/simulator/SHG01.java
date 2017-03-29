
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package net.labthink.instrument.device.SHG01.simulator;

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
public class SHG01 implements Serializable {

    private static final long serialVersionUID = -685345324534534510L;

    private int currentState = 0;

    private String[] stateString = {"待机","上腔吹洗","下腔抽真空","上下腔抽真空","关闭试验下腔","上腔充气","上腔压力到，停止充气,下腔压力清理，开始显示压力变化","复位"};

    private transient  long   startTimeSpot        = 0;

    /** 温控参数 */
    private byte[][] PIDs = new byte[4][3];

    /** 环境湿度零点 */
    private byte[] ambientHumidityZeroSetPoint = new byte[2];

    /** 环境湿度终点 */
    private byte[] ambienthumidityEndSetPoint = new byte[2];

    /*
     * 实验腔*3
     */
    private SHG01Cell[] cells = new SHG01Cell[3];
    private SHG01Cell   cell3 = new SHG01Cell(this);
    private SHG01Cell   cell2 = new SHG01Cell(this);
    private SHG01Cell   cell1 = new SHG01Cell(this);

    /** 电流终点 */
    private byte[] currentEndSetPoint = new byte[2];

    /** 电流零点 */
    private byte[] currentZeroSetPoint = new byte[2];

    /** 日期 */
    private byte[] datebyte = new byte[3];

    /** 高压终点 */
    private byte[] pressuEndSetPoint = new byte[2];

    /** 高压零点 */
    private byte[] pressuZeroSetPoint = new byte[2];

    /** 温度设定值 */
    private byte[] temperatureSetPoint = new byte[2];

    /** 时间 */
    private byte[] timebyte = new byte[3];

    /*
     * 阀,123为五路阀，4-10为平常的阀。
     */
    private transient byte[] valves = new byte[10];
//    private transient byte[] valves = {0,0,0,0,0,0,0,0,0,0};

    /*
     * 阀,2字节,123为五路阀，4-10为平常的阀。
     */
//    private transient byte[] valves2 = new byte[2];

    /*
     * 是否上电
     */
    private boolean power = true;

    /** 是否加热 */
    private boolean heating = false;

    /** 是否制冷 */
    private boolean cooling = false;

    /*
     * 是否正在试验
     */
    private transient boolean Testing = false;

    /** 温控设置 */
    private boolean PIDAutoOn = false;

    /** 电压终点 */
    private byte[] voltageEndSetPoint = new byte[2];

    /** 电压零点 */
    private byte[] voltageZeroSetPoint = new byte[2];

    /** 功耗 */
    private int PowerConsumption;

    /** 上腔压力 */
    private transient int cellpresure_high = 1005;
    /** 上腔压力,记录用，作为中间数值计算时记录。 */
    private transient int cellpresure_high2;

    /** 电流 */
    private int current;

    /** 环境湿度 */
    private int humidity_ambinent;

    /** 室内湿度 */
    private int humidity_room;

    /** 环境温度 */
    private int temprature_ambinent;

    /** 下腔温度 */
    private int temprature_undercell;

    /** 电压 */
    private int voltage;

    {
	getCells()[0] = getCell1();
	getCells()[1] = getCell2();
	getCells()[2] = getCell3();
    }

    private Random rand = new Random(System.currentTimeMillis());

    /**
     * 构造函数
     *
     */
    public SHG01() {}

    public void PreSetSimulatorParameters(int balancetime,int muliplier) {
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
	
    }

    public static void main(String[] args) {
	SHG01 g = new SHG01();

//      byte a[] = new byte[5];
//      a[1] = 3;
//      a[2] = 4;
//      a[3] = 5;
//      a[4] = 6;
//      System.out.println(BytePlus.bytesToHexString(a));
//      BytePlus.fillcontent(a, 3);
//      System.out.println(BytePlus.bytesToHexString(a));
	byte k = 0x10;

	System.out.println(k);
	k = (byte) (k & 0x03);
	System.out.println(k);
    }

    /**
     * @return the power
     */
    public boolean isPower() {
	return power;
    }

    /**
     * @param power the power to set
     */
    public void setPower(boolean power) {
	this.power = power;
    }

    /**
     * @return the cells
     */
    public SHG01Cell[] getCells() {
	return cells;
    }

    /**
     * @return the cell3
     */
    public SHG01Cell getCell3() {
	return cell3;
    }

    /**
     * @return the cell2
     */
    public SHG01Cell getCell2() {
	return cell2;
    }

    /**
     * @return the cell1
     */
    public SHG01Cell getCell1() {
	return cell1;
    }

    /**
     * 返回1，2，3号腔
     * @param i 1,2,3表示1，2，3号腔
     * @return 返回腔对象
     */
    public SHG01Cell getCell(int i) {
	return cells[i - 1];
    }

    /**
     * @return the Testing
     */
    public boolean isTesting() {
	return Testing;
    }

    /**
     *
     * @param Testing the Testing to set
     */
    public void setTesting(boolean Testing) {
	if (Testing == false) {
	    cell1.setDotest(false);
	    cell2.setDotest(false);
	    cell3.setDotest(false);
	}

//      cell1.setDotest(Testing);
//      cell2.setDotest(Testing);
//      cell3.setDotest(Testing);
        cell1.setStartTimeSpot(0);
        cell2.setStartTimeSpot(0);
        cell3.setStartTimeSpot(0);
        startTimeSpot= 0 ;
	this.Testing = Testing;
    }

    /**
     * @return the valves
     */
    public byte[] getValves() {
        if(valves==null){
            valves = new byte[10];
        }
            return valves;
    }

    /**
     * 返回某个阀
     * @param i 1-10
     * @return
     */
    public byte getValve(int i) {

        BitSet bs = new BitSet();

	if (i < valves.length + 1) {
	    return valves[i - 1];
	} else {
	    return 0;
	}
    }

    /**
     * @deprecated 不要使用该方法。
     * @param valves the valves to set
     */
    public void setValves(byte[] valves) {
	this.valves = valves;
    }

    public void setValvesWithTwoBytes(byte[] bs) {
        if(bs.length<2) return;
        byte temp[] = new byte[2];
        temp[0] = bs[0];
        temp[1] = bs[1];
        short ts = (short) BytePlus.bytes2int(temp);
        setValvesWithTwoByte(ts);
        changeCurrentState(bs);

        if(currentState==1){
            //上腔置换气体
            startTimeSpot =System.currentTimeMillis();
        }
        if(currentState==2){
            //下腔抽真空
            startTimeSpot =System.currentTimeMillis();
        }
        if(currentState==3){
            //上下腔抽真空
            startTimeSpot =System.currentTimeMillis();
        }
        if(currentState==5){
            //充气
            startTimeSpot =System.currentTimeMillis();
        }
        if(currentState==6){
            //试验状态
            startTimeSpot =System.currentTimeMillis();
        }
    }

    private void changeCurrentState(byte[] bs) {
        short ts = (short) BytePlus.bytes2int(bs);
        if(ts==0x002a){
            setCurrentState(0);//待机
        }else if(getCurrentState()==0&&(ts==0x0669||ts==0x06a6||ts==0x071a||ts==0x06e5||ts==0x0759||ts==0x0796||ts==0x07ea)){
            setCurrentState(1);//上腔吹洗
        }else if(ts==0x1069||ts==0x10a6||ts==0x111a||ts==0x10e5||ts==0x1159||ts==0x1196||ts==0x11d5){
            setCurrentState(2);//下腔脱气
        }else if(ts==0x1869||ts==0x18a6||ts==0x191a||ts==0x18e5||ts==0x1959||ts==0x1996||ts==0x19d5){
            setCurrentState(3);//上下腔抽真空
        }else if(ts==0x1069||ts==0x10a6||ts==0x111a||ts==0x10e5||ts==0x1159||ts==0x1196||ts==0x11d5){
            setCurrentState(4);//关闭试验下腔
        }else if(getCurrentState()!=0&&(ts==0x0669||ts==0x06a6||ts==0x071a||ts==0x06e5||ts==0x0759||ts==0x0796||ts==0x07ea)){
            setCurrentState(5);//上腔充气
        }else if(ts==0x0069||ts==0x00a6||ts==0x011a||ts==0x00e5||ts==0x0159||ts==0x0196||ts==0x01ea){
            setCurrentState(6);//上腔压力到，停止充气,下腔压力清理，开始显示压力变化
        }else if(ts==0x0bd5){
            setCurrentState(7);//复位
        }
    }


    public void setValvesWithTwoByte(short in) {
//        System.arraycopy(BytePlus.int2bytes(in), 2, valves2, 0, 2);

	// (10# 9# 8# 7# 6# 5# 4# 3B 3A 2B 2A 1B 1A  1、2、3 A侧上电为开)
	valves[0] = (byte) ((in & 0x01)==0?0:1);
	valves[1] = (byte) ((in & 0x04)==0?0:1);
	valves[2] = (byte) ((in & 0x10)==0?0:1);
	valves[3] = (byte) ((in & 0x40)==0?0:1);
	valves[4] = (byte) ((in & 0x80)==0?0:1);
	valves[5] = (byte) ((in & 0x100)==0?0:1);
	valves[6] = (byte) ((in & 0x200)==0?0:1);
	valves[7] = (byte) ((in & 0x400)==0?0:1);
	valves[8] = (byte) ((in & 0x800)==0?0:1);
	valves[9] = (byte) ((in & 0x1000)==0?0:1);


    }

    /**
     * 设定某个阀
     * @param k 1-10
     * @param ba
     */
    public void setValves(int k, byte b) {
	if (k < valves.length + 1) {
	    valves[k - 1] = b;
	}
    }

    /**
     * @return the pressuZeroSetPoint
     */
    public byte[] getPressuZeroSetPoint() {
	return pressuZeroSetPoint;
    }

    /**
     * @param pressuZeroSetPoint the pressuZeroSetPoint to set
     */
    public void setPressuZeroSetPoint(byte[] pressuZeroSetPoint) {
	this.pressuZeroSetPoint = pressuZeroSetPoint;
    }

    /**
     * @return the pressuEndSetPoint
     */
    public byte[] getPressuEndSetPoint() {
	return pressuEndSetPoint;
    }

    /**
     * @param pressuEndSetPoint the pressuEndSetPoint to set
     */
    public void setPressuEndSetPoint(byte[] pressuEndSetPoint) {
	this.pressuEndSetPoint = pressuEndSetPoint;
    }

    /**
     * @return the ambientHumidityZeroSetPoint
     */
    public byte[] getAmbientHumidityZeroSetPoint() {
	return ambientHumidityZeroSetPoint;
    }

    /**
     * @param ambientHumidityZeroSetPoint the ambientHumidityZeroSetPoint to set
     */
    public void setAmbientHumidityZeroSetPoint(byte[] ambientHumidityZeroSetPoint) {
	this.ambientHumidityZeroSetPoint = ambientHumidityZeroSetPoint;
    }

    /**
     * @return the ambienthumidityEndSetPoint
     */
    public byte[] getAmbienthumidityEndSetPoint() {
	return ambienthumidityEndSetPoint;
    }

    /**
     * @param ambienthumidityEndSetPoint the ambienthumidityEndSetPoint to set
     */
    public void setAmbienthumidityEndSetPoint(byte[] ambienthumidityEndSetPoint) {
	this.ambienthumidityEndSetPoint = ambienthumidityEndSetPoint;
    }

    /**
     * @return the cooling
     */
    public boolean isCooling() {
	return cooling;
    }

    /**
     * @param cooling the cooling to set
     */
    public void setCooling(boolean cooling) {
	this.cooling = cooling;
    }

    /**
     * @return the heating
     */
    public boolean isHeating() {
	return heating;
    }

    /**
     * @param heating the heating to set
     */
    public void setHeating(boolean heating) {
	this.heating = heating;
    }

    /**
     * @return the temperatureSetPoint
     */
    public byte[] getTemperatureSetPoint() {
	return temperatureSetPoint;
    }

    /**
     * @param temperatureSetPoint the temperatureSetPoint to set
     */
    public void setTemperatureSetPoint(byte[] temperatureSetPoint) {
	this.temperatureSetPoint = temperatureSetPoint;
    }

    /**
     * @return the PIDs
     */
    public byte[][] getPIDs() {
	return PIDs;
    }

    public byte[] getPIDs(byte i) {
	return PIDs[i - 1];
    }

    /**
     * @param PIDs the PIDs to set
     */
    public void setPIDs(byte[][] PIDs) {
	this.PIDs = PIDs;
    }

    public void setPIDs(byte i, byte[] PID) {
	this.PIDs[i - 1] = PID;
    }

    /**
     * @return the PIDOn
     */
    public boolean isPIDAutoOn() {
	return PIDAutoOn;
    }

    /**
     * @param PIDOn the PIDOn to set
     */
    public void setPIDAutoOn(boolean PIDOn) {
	this.PIDAutoOn = PIDOn;
    }

    /**
     * @return the PowerConsumption
     */
    public int getPowerConsumption() {
	return PowerConsumption;
    }

    public byte[] getPowerConsumptionBA() {
	return BytePlus.int2bytes(PowerConsumption);
    }

    /**
     * @param PowerConsumption the PowerConsumption to set
     */
    public void setPowerConsumption(int PowerConsumption) {
	this.PowerConsumption = PowerConsumption;
    }

    /**
     * @return the voltage
     */
    public int getVoltage() {
	return voltage;
    }

    public byte[] getVoltageBA() {
	return BytePlus.int2bytes(voltage);
    }

    /**
     * @param voltage the voltage to set
     */
    public void setVoltage(int voltage) {
	this.voltage = voltage;
    }

    /**
     * @return the current
     */
    public int getCurrent() {
	return current;
    }

    public byte[] getCurrentBA() {
	return BytePlus.int2bytes(current);
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(int current) {
	this.current = current;
    }

    /**
     * @return the voltageZeroSetPoint
     */
    public byte[] getVoltageZeroSetPoint() {
	return voltageZeroSetPoint;
    }

    /**
     * @param voltageZeroSetPoint the voltageZeroSetPoint to set
     */
    public void setVoltageZeroSetPoint(byte[] voltageZeroSetPoint) {
	this.voltageZeroSetPoint = voltageZeroSetPoint;
    }

    /**
     * @return the voltageEndSetPoint
     */
    public byte[] getVoltageEndSetPoint() {
	return voltageEndSetPoint;
    }

    /**
     * @param voltageEndSetPoint the voltageEndSetPoint to set
     */
    public void setVoltageEndSetPoint(byte[] voltageEndSetPoint) {
	this.voltageEndSetPoint = voltageEndSetPoint;
    }

    /**
     * @return the currentZeroSetPoint
     */
    public byte[] getCurrentZeroSetPoint() {
	return currentZeroSetPoint;
    }

    /**
     * @param currentZeroSetPoint the currentZeroSetPoint to set
     */
    public void setCurrentZeroSetPoint(byte[] currentZeroSetPoint) {
	this.currentZeroSetPoint = currentZeroSetPoint;
    }

    /**
     * @return the currentEndSetPoint
     */
    public byte[] getCurrentEndSetPoint() {
	return currentEndSetPoint;
    }

    /**
     * @param currentEndSetPoint the currentEndSetPoint to set
     */
    public void setCurrentEndSetPoint(byte[] currentEndSetPoint) {
	this.currentEndSetPoint = currentEndSetPoint;
    }

    /**
     * @return the temprature_undercell
     */
    public int getTemprature_undercell() {
	return temprature_undercell;
    }

    public byte[] getTemprature_undercellBA() {
	return BytePlus.int2bytes(temprature_undercell);
    }

    /**
     * @param temprature_undercell the temprature_undercell to set
     */
    public void setTemprature_undercell(int temprature_undercell) {
	this.temprature_undercell = temprature_undercell;
    }

    /**
     * @return the temprature_ambinent
     */
    public int getTemprature_ambinent() {
	return temprature_ambinent;
    }

    public byte[] getTemprature_ambinentBA() {
	return BytePlus.int2bytes(temprature_ambinent);
    }

    /**
     * @param temprature_ambinent the temprature_ambinent to set
     */
    public void setTemprature_ambinent(int temprature_ambinent) {
	this.temprature_ambinent = temprature_ambinent;
    }

    /**
     * @return the humidity_room
     */
    public int getHumidity_room() {
	return humidity_room;
    }

    public byte[] getHumidity_roomBA() {
	return BytePlus.int2bytes(humidity_room);
    }

    /**
     * @param humidity_room the humidity_room to set
     */
    public void setHumidity_room(int humidity_room) {
	this.humidity_room = humidity_room;
    }

    /**
     * @return the humidity_ambinent
     */
    public int getHumidity_ambinent() {
	return humidity_ambinent;
    }

    public byte[] getHumidity_ambinentBA() {
	return BytePlus.int2bytes(humidity_ambinent);
    }

    /**
     * @param humidity_ambinent the humidity_ambinent to set
     */
    public void setHumidity_ambinent(int humidity_ambinent) {
	this.humidity_ambinent = humidity_ambinent;
    }

    /**
     * @return the cellpresure_high
     */
    public int getCellpresure_high() {
        adjustCellpresure_high();
	return cellpresure_high;
    }

    public byte[] getCellpresure_highBA() {
        adjustCellpresure_high();
	return BytePlus.int2bytes(cellpresure_high);
    }

    private void adjustCellpresure_high(){
        if(getCurrentState()==0||getCurrentState()==1){
            cellpresure_high = cellpresure_high2;
        }else if(getCurrentState()==3){
            long time = System.currentTimeMillis() - startTimeSpot;
            cellpresure_high = (int) (cellpresure_high2 - (time / (1000))*5);
            cellpresure_high = cellpresure_high>0?cellpresure_high:0;
        }else if(getCurrentState()==6){
            long time = System.currentTimeMillis() - startTimeSpot;
            cellpresure_high = (int) (cellpresure_high + (time / (1000))*5);
            cellpresure_high = cellpresure_high>0?cellpresure_high:0;
            if(cellpresure_high>1080)cellpresure_high=1080;
        }else if(getCurrentState()==5){
            long time = System.currentTimeMillis() - startTimeSpot;
            cellpresure_high = (int) (cellpresure_high + (time / (1000))*5);
            cellpresure_high = cellpresure_high>0?cellpresure_high:0;
            if(cellpresure_high>1080)cellpresure_high=1080;
        }
    }

    /**
     * @param cellpresure_high the cellpresure_high to set
     */
    public void setCellpresure_high(int cellpresure_high) {
	this.cellpresure_high = cellpresure_high;
	this.cellpresure_high2 = cellpresure_high;
    }

    /**
     * @return the datebyte
     */
    public byte[] getDatebyte() {
	return datebyte;
    }

    /**
     * @param datebyte the datebyte to set
     */
    public void setDatebyte(byte[] datebyte) {
	this.datebyte = datebyte;
    }

    /**
     * @return the timebyte
     */
    public byte[] getTimebyte() {
	return timebyte;
    }

    /**
     * @param timebyte the timebyte to set
     */
    public void setTimebyte(byte[] timebyte) {
	this.timebyte = timebyte;
    }

    /**
     * @return the currentState
     */
    public int getCurrentState() {
        return currentState;
    }

    /**
     * @param currentState the currentState to set
     */
    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    /**
     * @return the stateString
     */
    public String getStateString() {
        return stateString[currentState];
    }

    /**
     * @param stateString the stateString to set
     */
    public void setStateString(String[] stateString) {
        this.stateString = stateString;
    }


}
