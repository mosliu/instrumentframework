package net.labthink.instrument.device.FPTF1.simulator;

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
public class FPTF1 implements Serializable {

	//TODO 需要修改
    private static final long serialVersionUID = -685345324534534510L;

    private int currentState = 0;

    private String[] stateString =  {"待机","上腔吹洗"};

    private transient  long   startTimeSpot        = 0;

	/** 机器是否上电 */
	private boolean devicepower = false;
	/** 是否正在试验 */
	private boolean testing = false;
	/** 温度设定值 */
	private byte[] temperature = new byte[2];
	/** PIDP参数 */
	private byte[] pidP = new byte[2];
	/** PIDI参数 */
	private byte[] pidI = new byte[2];
	/** PIDD参数 */
	private byte[] pidD = new byte[2];
	/** 是否加热 */
	private byte heatflag = 0;
	/** 速度设定 D1=01  SPD=25mm/min D1=02  SPD=50mm/min D1=03  SPD=100mm/min D1=04  SPD=150mm/min D1=05  SPD=200mm/min D1=06  SPD=250mm/min D1=07  SPD=300mm/min D1=08  SPD=500mm/min */
	private byte speed = 0;
	/** 温度零点 */
	private byte[] tempzero = new byte[2];
	/** 温度终点 */
	private byte[] tempend = new byte[2];
	/** 压力零点 */
	private byte[] pressurezero = new byte[2];
	/** 压力终点 */
	private byte[] pressureend = new byte[2];
	/** 年 */
	private byte year = 0;
	/** 月 */
	private byte month = 0;
	/** 日 */
	private byte day = 0;
	/** 时 */
	private byte hour = 0;
	/** 分 */
	private byte minute = 0;
	/** 秒 */
	private byte second = 0;


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
	 * 获取 温度设定值 的值
     * @return the temperature
     */
    public byte[] getTemperature() {
		return temperature;
    }

    /**
	 * 设定 温度设定值 的值
     * @param temperature, the temperature to set
     */
    public void setTemperature(byte[] temperature) {
		this.temperature = temperature;
    }
	/**
	 * 获取 温度设定值 的int值
     * @return the temperature
     */
    public int getTemperatureInt() {
		return BytePlus.bytes2int(temperature);
    }

    /**
	 * 使用int值设定 温度设定值
     * @param temperature, the temperature to set
     */
    public void setTemperatureInt(int temperature) {
		this.temperature = BytePlus.getPartBytes(BytePlus.int2bytes(temperature), 2, 4-2);
    }


	/**
	 * 获取 PIDP参数 的值
     * @return the pidP
     */
    public byte[] getPidP() {
		return pidP;
    }

    /**
	 * 设定 PIDP参数 的值
     * @param pidP, the pidP to set
     */
    public void setPidP(byte[] pidP) {
		this.pidP = pidP;
    }
	/**
	 * 获取 PIDP参数 的int值
     * @return the pidP
     */
    public int getPidPInt() {
		return BytePlus.bytes2int(pidP);
    }

    /**
	 * 使用int值设定 PIDP参数
     * @param pidP, the pidP to set
     */
    public void setPidPInt(int pidP) {
		this.pidP = BytePlus.getPartBytes(BytePlus.int2bytes(pidP), 2, 4-2);
    }


	/**
	 * 获取 PIDI参数 的值
     * @return the pidI
     */
    public byte[] getPidI() {
		return pidI;
    }

    /**
	 * 设定 PIDI参数 的值
     * @param pidI, the pidI to set
     */
    public void setPidI(byte[] pidI) {
		this.pidI = pidI;
    }
	/**
	 * 获取 PIDI参数 的int值
     * @return the pidI
     */
    public int getPidIInt() {
		return BytePlus.bytes2int(pidI);
    }

    /**
	 * 使用int值设定 PIDI参数
     * @param pidI, the pidI to set
     */
    public void setPidIInt(int pidI) {
		this.pidI = BytePlus.getPartBytes(BytePlus.int2bytes(pidI), 2, 4-2);
    }


	/**
	 * 获取 PIDD参数 的值
     * @return the pidD
     */
    public byte[] getPidD() {
		return pidD;
    }

    /**
	 * 设定 PIDD参数 的值
     * @param pidD, the pidD to set
     */
    public void setPidD(byte[] pidD) {
		this.pidD = pidD;
    }
	/**
	 * 获取 PIDD参数 的int值
     * @return the pidD
     */
    public int getPidDInt() {
		return BytePlus.bytes2int(pidD);
    }

    /**
	 * 使用int值设定 PIDD参数
     * @param pidD, the pidD to set
     */
    public void setPidDInt(int pidD) {
		this.pidD = BytePlus.getPartBytes(BytePlus.int2bytes(pidD), 2, 4-2);
    }


	/**
	 * 获取 是否加热 的值
     * @return the heatflag
     */
    public byte getHeatflag() {
		return heatflag;
    }

    /**
	 * 设定 是否加热 的值
     * @param heatflag, the heatflag to set
     */
    public void setHeatflag(byte heatflag) {
		this.heatflag = heatflag;
    }


	/**
	 * 获取 速度设定 D1=01  SPD=25mm/min D1=02  SPD=50mm/min D1=03  SPD=100mm/min D1=04  SPD=150mm/min D1=05  SPD=200mm/min D1=06  SPD=250mm/min D1=07  SPD=300mm/min D1=08  SPD=500mm/min 的值
     * @return the speed
     */
    public byte getSpeed() {
		return speed;
    }

    /**
	 * 设定 速度设定 D1=01  SPD=25mm/min D1=02  SPD=50mm/min D1=03  SPD=100mm/min D1=04  SPD=150mm/min D1=05  SPD=200mm/min D1=06  SPD=250mm/min D1=07  SPD=300mm/min D1=08  SPD=500mm/min 的值
     * @param speed, the speed to set
     */
    public void setSpeed(byte speed) {
		this.speed = speed;
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
		this.tempzero = BytePlus.getPartBytes(BytePlus.int2bytes(tempzero), 2, 4-2);
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
		this.tempend = BytePlus.getPartBytes(BytePlus.int2bytes(tempend), 2, 4-2);
    }


	/**
	 * 获取 压力零点 的值
     * @return the pressurezero
     */
    public byte[] getPressurezero() {
		return pressurezero;
    }

    /**
	 * 设定 压力零点 的值
     * @param pressurezero, the pressurezero to set
     */
    public void setPressurezero(byte[] pressurezero) {
		this.pressurezero = pressurezero;
    }
	/**
	 * 获取 压力零点 的int值
     * @return the pressurezero
     */
    public int getPressurezeroInt() {
		return BytePlus.bytes2int(pressurezero);
    }

    /**
	 * 使用int值设定 压力零点
     * @param pressurezero, the pressurezero to set
     */
    public void setPressurezeroInt(int pressurezero) {
		this.pressurezero = BytePlus.getPartBytes(BytePlus.int2bytes(pressurezero), 2, 4-2);
    }


	/**
	 * 获取 压力终点 的值
     * @return the pressureend
     */
    public byte[] getPressureend() {
		return pressureend;
    }

    /**
	 * 设定 压力终点 的值
     * @param pressureend, the pressureend to set
     */
    public void setPressureend(byte[] pressureend) {
		this.pressureend = pressureend;
    }
	/**
	 * 获取 压力终点 的int值
     * @return the pressureend
     */
    public int getPressureendInt() {
		return BytePlus.bytes2int(pressureend);
    }

    /**
	 * 使用int值设定 压力终点
     * @param pressureend, the pressureend to set
     */
    public void setPressureendInt(int pressureend) {
		this.pressureend = BytePlus.getPartBytes(BytePlus.int2bytes(pressureend), 2, 4-2);
    }


	/**
	 * 获取 年 的值
     * @return the year
     */
    public byte getYear() {
		return year;
    }

    /**
	 * 设定 年 的值
     * @param year, the year to set
     */
    public void setYear(byte year) {
		this.year = year;
    }


	/**
	 * 获取 月 的值
     * @return the month
     */
    public byte getMonth() {
		return month;
    }

    /**
	 * 设定 月 的值
     * @param month, the month to set
     */
    public void setMonth(byte month) {
		this.month = month;
    }


	/**
	 * 获取 日 的值
     * @return the day
     */
    public byte getDay() {
		return day;
    }

    /**
	 * 设定 日 的值
     * @param day, the day to set
     */
    public void setDay(byte day) {
		this.day = day;
    }


	/**
	 * 获取 时 的值
     * @return the hour
     */
    public byte getHour() {
		return hour;
    }

    /**
	 * 设定 时 的值
     * @param hour, the hour to set
     */
    public void setHour(byte hour) {
		this.hour = hour;
    }


	/**
	 * 获取 分 的值
     * @return the minute
     */
    public byte getMinute() {
		return minute;
    }

    /**
	 * 设定 分 的值
     * @param minute, the minute to set
     */
    public void setMinute(byte minute) {
		this.minute = minute;
    }


	/**
	 * 获取 秒 的值
     * @return the second
     */
    public byte getSecond() {
		return second;
    }

    /**
	 * 设定 秒 的值
     * @param second, the second to set
     */
    public void setSecond(byte second) {
		this.second = second;
    }


    private Random rand = new Random(System.currentTimeMillis());

    /**
     * 构造函数
     *
     */
    public FPTF1() {}

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
