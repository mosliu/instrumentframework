package net.labthink.instrument.device.OX2231.simulator;

import java.util.Random;

import net.labthink.utils.BytePlus;

public class OX2231 {

    Random rand = new Random(System.currentTimeMillis());
    // 温度
    int temperature = 0;
    // 湿度
    int humidity = 0;
    // 透氧
    int OTR = 0;
    // 流量
    int flux = 0;

    //设备类型()
    int kind = 0;
    private byte[] content = new byte[12];

    public OX2231() {
    }

    public void preSetParams(int _temp, int _hum, int _ort, int _flux) {
        temperature = _temp;
        humidity = _hum;
        OTR = _ort;
        flux = _flux;
        // TestParams_Range = range;
        // TestParams_Num = num > 10 || num < 1 ? 10 : num;
        // TestParams_Speed = speed > 5 || speed < 1 ? 1 : speed;
        // TestParams_SampleWidth = width % 65536;
    }

    public byte[] fetchCurrentValue(int otr_trend, int otr_range, int flux_trend,
            int flux_range) {
        int tempnum = rand.nextInt(otr_range);
        OTR = OTR + tempnum * otr_trend;
        tempnum = rand.nextInt(flux_range);
        flux = flux + tempnum * flux_trend;

        //X1:01	数据(X2X3温度；X4X5湿度；X7X8X9透氧量 X6小数点 X10 X11 流量)
        content[0] = 1;
        byte[] tempbytes = BytePlus.int2bytes(temperature);
        System.arraycopy(tempbytes, 2, content, 1, 2);
        tempbytes = BytePlus.int2bytes(humidity);
        System.arraycopy(tempbytes, 2, content, 3, 2);
        tempbytes = BytePlus.int2bytes(OTR);
//        content[5] = 13;
        content[5] = (byte) this.kind;
        System.arraycopy(tempbytes, 1, content, 6, 3);
        tempbytes = BytePlus.int2bytes(flux);
        System.arraycopy(tempbytes, 2, content, 9, 2);
//		for (int i = 11; i < content.length; i++) {
//			content[i] = 0;
//		}
        return content;
    }

    // X1:02	系统复位
    public byte[] getReset() {
        content[0] = 2;

        for (int i = 1; i < content.length; i++) {
            content[i] = 0;
        }
        return content;
    }

    // X1:03   件数(X2)
    public byte[] getSampleCount() {
        content[0] = 3;
        content[1] = (byte) (rand.nextInt(3) + 1);
        for (int i = 2; i < content.length; i++) {
            content[i] = 0;
        }
        return content;
    }

    public void setKind(int kind) {
        this.kind=kind;
    }

    // X1:04   开始 (X2=01薄膜,X2=02容器)
    public byte[] getStart(int type) {
//        type = type % 2 == 0 ? 2 : 1;
        content[0] = 4;
        content[1] = (byte) type;
        for (int i = 2; i < content.length; i++) {
            content[i] = 0;
        }
        return content;
    }

    //X1:05   停止
    public byte[] getTestEnd() {
        content[0] = 5;
        for (int i = 1; i < content.length; i++) {
            content[i] = 0;
        }
        return content;
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getORT() {
        return OTR;
    }

    public void setORT(int oRT) {
        OTR = oRT;
    }

    public int getFlux() {
        return flux;
    }

    public void setFlux(int flux) {
        this.flux = flux;
    }
}
