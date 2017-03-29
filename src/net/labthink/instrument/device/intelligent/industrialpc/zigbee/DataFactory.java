
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device.intelligent.industrialpc.zigbee;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import net.labthink.utils.BytePlus;
import net.labthink.utils.Randomer;

/**
 *
 * @author Mosliu
 */
public class DataFactory {

    static Randomer r = new Randomer();

    /**
     * 随机生成一个设备编号。
     */
    public static byte[] DeviceNumProducer() {
        return DeviceNumProducer((byte) r.nextInt(0, 255), (byte) r.nextInt(0, 255));
    }

    /**
     * 按照输入生成一个设备编号 年份与生产编号随机生成
     * @param devicekind 设备种类编码
     * @param devicetype 该种类下不同产品的编码
     * @return
     */
    public static byte[] DeviceNumProducer(byte devicekind, byte devicetype) {
        byte[] producesn = BytePlus.getPartBytes(BytePlus.int2bytes(Integer.reverseBytes(r.nextInt(0, 65536))), 0, 2);
        return DeviceNumProducer(devicekind, devicetype, (byte) r.nextInt(0, 255), producesn);

    }

    /**
     * 按照输入生成一个设备编号
     * @param devicekind 设备种类编码
     * @param devicetype 该种类下不同产品的编码
     * @param produceyear 设备的生产年份的后三位
     * @param producesn 表示当年同类产品的生产编号
     */
    public static byte[] DeviceNumProducer(byte devicekind, byte devicetype, byte produceyear, byte[] producesn) {
        /**
         * 设备编号：5字节，
         * 第一个字节为设备种类编码，
         * 第二个字节为该种类下不同产品的编码。（各具体设备对应的设备类型编码详见《设备类型编号对照表》）。
         * 第三个字节为设备的生产年份的后三位，使用一个字节代表自2000年至2255年的年份，
         * 第四、第五两个字节表示当年同类产品的生产编号，
         * 如当年生产的第61台该类型设备对应的编号为“00 3D”，之后依次累加。这5个字节共同组成设备出厂的唯一设备编号。
         */
        byte[] dn = new byte[5];
        dn[0] = devicekind;
        dn[1] = devicetype;
        dn[2] = produceyear;
        dn[3] = producesn[0];
        dn[4] = producesn[1];
        return dn;
    }

    /**
     * 随机生成试验编号 4位byte数组
     * @return 4位byte数组
     */
    public static byte[] TestNumProducer() {
        r.resetInt();
        int k = r.nextInt();
        return TestNumProducer(k);
    }

    public static byte[] TestNumProducer(int num) {
        return BytePlus.int2bytes(Integer.reverseBytes(num));
    }

    /**
     * 随机生成类型编号 2位byte数组
     * @return 2位byte数组
     */
    public static byte[] TestKindProducer() {
        r.resetInt();
        short k = (short) r.nextInt();
        return TestKindProducer(k);
    }

    public static byte[] TestKindProducer(short num) {
        return BytePlus.short2bytes(Short.reverseBytes(num));
    }

    public static byte[] ByteDateProducer() {
        return ByteDateProducer(new Date());
    }

    public static byte[] ByteDateProducer(int year, int month, int date, int hourOfDay, int minute,
            int second) {
        Calendar cld = Calendar.getInstance();
        cld.set(year, month, date, hourOfDay, minute, second);
        Date b = cld.getTime();
        return ByteDateProducer(b);
    }

    public static byte[] ByteRandomDateProducer() {
        Calendar cld = Calendar.getInstance();
        cld.set(r.nextInt(2000, 2255), r.nextInt(0, 11), r.nextInt(28), r.nextInt(23), r.nextInt(60), r.nextInt(60));
        Date b = cld.getTime();
        return ByteDateProducer(b);
    }

    public static Date convertDateFromBytes(byte[] bs){
        byte[] milliseconds = new byte[2];
        milliseconds[0] = bs[7];
        milliseconds[1] = bs[6];
        Calendar cld = Calendar.getInstance();
        cld.set(bs[0]+2000, bs[1]-1, bs[2], bs[3], bs[4], bs[5]);
        cld.set(Calendar.MILLISECOND, BytePlus.bytes2int(milliseconds));
        return cld.getTime();
    }

    public static byte[] ByteDateProducer(Date d) {
//        Date now = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        byte year = (byte) (now.get(Calendar.YEAR) - 2000);
        byte month = (byte) (now.get(Calendar.MONTH) + 1);//1月是0
        byte day = (byte) now.get(Calendar.DAY_OF_MONTH);
        byte hour = (byte) now.get(Calendar.HOUR_OF_DAY);
        byte minute = (byte) now.get(Calendar.MINUTE);
        byte second = (byte) now.get(Calendar.SECOND);
        short milisecond = (short) now.get(Calendar.MILLISECOND);
        byte b_milisecond[] = BytePlus.short2bytes(Short.reverseBytes(milisecond));
//        int day = now.get(now.YEAR);
//        System.out.println("year:" + year);
//        System.out.println("month:" + month);
//        System.out.println("day:" + day);
//        System.out.println("hour:" + hour);
//        System.out.println("minute:" + minute);
//        System.out.println("second:" + second);
//        System.out.println("milisecond:" + milisecond);

        byte rtn[] = new byte[8];
        rtn[0] = year;
        rtn[1] = month;
        rtn[2] = day;
        rtn[3] = hour;
        rtn[4] = minute;
        rtn[5] = second;
        rtn[6] = b_milisecond[0];
        rtn[7] = b_milisecond[1];

        return rtn;
    }

    public static byte[] ByteShortParameterProducer() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte num = (byte) r.nextInt(0, 255);
        baos.write(num);
        byte length = (byte) r.nextInt(0, 70);
        baos.write(length);
        for (int i = 0; i < length; i++) {
            baos.write(r.nextInt(0, 255));
        }

        return baos.toByteArray();
    }

    public static void main(String[] args) {
        //ByteDateProducer(new Date());
        short a = 999;
        byte c[] = BytePlus.short2bytes(Short.reverseBytes(a));
        for (int i = 0; i < c.length; i++) {
            byte b = c[i];
            System.out.println(b);
        }



    }
}
