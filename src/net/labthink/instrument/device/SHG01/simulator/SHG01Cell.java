
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device.SHG01.simulator;

//~--- JDK imports ------------------------------------------------------------
import java.io.Serializable;
import java.nio.ByteBuffer;
import net.labthink.utils.BytePlus;

/**
 * 模拟一个单独的腔
 * @author Moses
 */
public class SHG01Cell implements Serializable {

    private static final long serialVersionUID = -1234562390768923465L;
    private SHG01 device = null;
    private int computeFlag = 0;
    private int balancetime = 100;
    private int multiplier = 5;
    /** 膜标定系数 */
    private byte[] factor = new byte[2];
    /** 压力值 */
    private transient byte[] pressure = new byte[2];
    /** 压力终点 */
    private byte[] pressureEndSetPoint = new byte[2];
    /** 压力零点 */
    private byte[] pressureZeroSetPoint = new byte[2];
    private transient long startTimeSpot = 0;
    /** 是否进行试验 */
    private transient boolean dotest;
    private transient boolean timeflag = false;

    public SHG01Cell(SHG01 _device) {
        device = _device;
    }

    public double computePressureByTime(long time) {
        //y = 2E-09x4 - 1E-05x3 + 0.0278x2 - 3.4281x - 5.9106
        time = time * multiplier;

        double rtn = 0;
        if (time > balancetime) {
            time = balancetime;
        }

        if (computeFlag == 0) {
            rtn = Math.abs(2e-10 * Math.pow(time, 4) - 1e-06 * Math.pow(time, 3) + 0.00278 * Math.pow(time, 2) - 0.34281 * time - 0.59106);
        } else if (computeFlag == 1) {
            time = time * 10;
//        y = 2E-12x3 - 1E-07x2 + 0.0121x - 0.349
//        真空镀铝	33158s	18.08pa
            rtn = Math.abs(2e-12 * Math.pow(time, 3) - 1E-7 * Math.pow(time, 2) + 0.0121 * time - 0.349);
//            if (rtn < time) {
//                rtn = time;
//            }
        } else if (computeFlag == 2) {
            time = time ;
            //y = -1E-06x3 + 0.005x2 + 0.0229x - 72.482
            //y = -9E-07x3 + 0.0037x2 + 0.7484x - 189.46
            //y = -7E-07x3 + 0.0032x2 + 0.8571x - 217.33
            //PC125	4300s 1095.37pa
            rtn = Math.abs(-1e-6 * Math.pow(time, 3) - 0.005 * Math.pow(time, 2) + 0.0229 * time - 72.482);
            //保证图像是上升的
//            if (time < 260) {
//                rtn = time;
//            }

        } else if (computeFlag == 3 || true) {
            time = time/5;
//        y = -0.0008x3 + 0.1152x2 + 23.299x - 57.539
//        y = -0.0009x3 + 0.1239x2 + 22.247x - 53.315
//        PE	150s 206.1pa
            rtn = Math.abs(-0.0008 * Math.pow(time, 3) + 0.1152 * Math.pow(time, 2) + +23.299 * time - 57.539);
            //保证图像是上升的
//            if (rtn < time) {
//                rtn = time;
//            }
        }
        return rtn + (System.currentTimeMillis() - startTimeSpot) / 5000;
//        return rtn;
    }

    /**
     * @return the dotest
     */
    public boolean isDotest() {
        return dotest;
    }

    /**
     * @param dotest the dotest to set
     */
    public void setDotest(boolean dotest) {
        if (dotest == true) {
            setStartTimeSpot(System.currentTimeMillis());
        }
        this.dotest = dotest;
    }

    /**
     *
     * @return the factor
     */
    public byte[] getFactor() {
        return factor;
    }

    /**
     * @param factor the factor to set
     */
    public void setFactor(byte[] factor) {
        this.factor = factor;
    }

    /**
     * @return the pressuZeroSetPoint
     */
    public byte[] getPressureZeroSetPoint() {
        return pressureZeroSetPoint;
    }

    /**
     * @param pressuZeroSetPoint the pressuZeroSetPoint to set
     */
    public void setPressureZeroSetPoint(byte[] pressuZeroSetPoint) {
        this.pressureZeroSetPoint = pressuZeroSetPoint;
    }

    /**
     * @return the pressuEndSetPoint
     */
    public byte[] getPressureEndSetPoint() {
        return pressureEndSetPoint;
    }

    /**
     * @param pressuEndSetPoint the pressuEndSetPoint to set
     */
    public void setPressureEndSetPoint(byte[] pressuEndSetPoint) {
        this.pressureEndSetPoint = pressuEndSetPoint;
    }

    /**
     * @return the pressure
     */
    public byte[] getPressure() {
        if (getStartTimeSpot() == 0) {
            setStartTimeSpot(System.currentTimeMillis());
        }
        int ipressure = BytePlus.bytes2int(pressure);
        if (device.getCurrentState() == 0 || device.getCurrentState() == 1 || device.getCurrentState() == 4 || device.getCurrentState() == 5) {
            timeflag = false;
            //待机/上腔置换气体/关下腔/上腔充气，返回最后的pressure
        } else if (device.getCurrentState() == 2 || device.getCurrentState() == 3) {
            timeflag = false;
            //下腔抽真空/上下腔抽真空
            //按照每秒5帕开始抽
            if (ipressure > 5000) {
                ipressure = 5000;
            }
            ipressure -= Math.abs((System.currentTimeMillis() - startTimeSpot) / 1000) * 5;
            pressure = BytePlus.getPartBytes(BytePlus.int2bytes(ipressure), 2, 2);
        } else if (device.getCurrentState() == 6) {
            if (timeflag == false) {
                setStartTimeSpot(System.currentTimeMillis());
                timeflag = true;
            }

            //正式试验
            long t = System.currentTimeMillis() - getStartTimeSpot();
            double k = computePressureByTime(t / 1000);
            int i = (int) Math.round(k);
            System.out.print(t / 1000 + ":" + i + "\t\t");
            byte[] tmpbyte = BytePlus.int2bytes(i);
            System.arraycopy(tmpbyte, 2, pressure, 0, 2);
            System.out.println(BytePlus.byteArray2String(pressure));
        }

        if (ipressure > 60000) {
            ipressure = 60000;
            pressure = BytePlus.getPartBytes(BytePlus.int2bytes(ipressure), 2, 2);
        }
        if (ipressure < 0) {
            ipressure = 0;
            pressure = BytePlus.getPartBytes(BytePlus.int2bytes(ipressure), 2, 2);
        }


        return pressure;
    }

    /**
     * @param pressure the pressure to set
     */
    public void setPressure(byte[] pressure) {
        this.pressure = pressure;
    }

    public void setPressureInt(int pressure) {
        this.pressure = BytePlus.getPartBytes(BytePlus.int2bytes(pressure), 2, 2);
    }

    /**
     * @return the startTimeSpot
     */
    public long getStartTimeSpot() {
        return startTimeSpot;
    }

    /**
     * @param startTimeSpot the startTimeSpot to set
     */
    public void setStartTimeSpot(long startTimeSpot) {
        this.startTimeSpot = startTimeSpot;
    }

    public static void main(String[] args) {
//        SHG01Cell a = new SHG01Cell();
//        for (int i = 0; i < 2000; i+=10) {
//            System.out.println(a.computePressureByTime(i));
//
//        }
        byte a[] = {0x12, 0x34};
        byte b[] = new byte[2];
        b[0] = 0;
        b[1] = 0;
        for (int i = 0; i < 0x12; i++) {
            b[0]++;
        }
        for (int i = 0; i < 0x34; i++) {
            b[1]++;
        }
        System.out.println(a == b);
        String a1 = new String(a);
        String b1 = new String(b);
        System.out.println(a1.equals(b1));
        ByteBuffer bba = ByteBuffer.wrap(a);
        ByteBuffer bbb = ByteBuffer.wrap(b);
        System.out.println(bba.compareTo(bbb));
        System.out.println(BytePlus.compareByteArray(a, b));
        boolean bool = (BytePlus.bytes2int(a)) == (BytePlus.bytes2int(b));
        System.out.println(bool);
        System.out.println(BytePlus.bytes2int(a));
        System.out.println(0x3412);
        System.out.println(0x1234);
        bool = (BytePlus.bytes2int(a)) == 0x1234;
        System.out.println(bool);
    }

    /**
     * @return the balancetime
     */
    public int getBalancetime() {
        return balancetime;
    }

    /**
     * @param balancetime the balancetime to set
     */
    public void setBalancetime(int balancetime) {

        this.balancetime = balancetime;
    }

    /**
     * @return the multiplier
     */
    public int getMultiplier() {
        return multiplier;
    }

    /**
     * @param multiplier the multiplier to set
     */
    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    /**
     * @return the computeFlag
     */
    public int getComputeFlag() {
        return computeFlag;
    }

    /**
     * @param computeFlag the computeFlag to set
     */
    public void setComputeFlag(int computeFlag) {
        this.computeFlag = computeFlag;
    }
}
