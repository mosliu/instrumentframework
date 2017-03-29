/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device.intelligent.industrialpc.zigbee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.labthink.utils.BytePlus;

/**
 * Application Layer Comunication packet
 *
 * @author Mosliu
 */
public class ALCpacket {

    private byte[] packetsyncbyte = BytePlus.getPartBytes(BytePlus.int2bytes(0xFBFB), 2, 2);//6
    private byte[] devicenum = new byte[5];
    private byte[] testnum = new byte[4];
    private byte[] testkind = new byte[2];
    private byte[] packetbody;
    private byte[] allpacket;
    private byte tail = 0;
    boolean bodyfillflag = false;
    int error = 0; //错误号

    //禁用默认构造函数。
    private ALCpacket() {
    }

    /**
     *
     * @param _packetbody
     */
    public ALCpacket(byte[] _packetbody) {
        if ((_packetbody[0] > (byte) 0xa0) && (_packetbody[0] < (byte) 0xaf) && (_packetbody[5] == (byte) 0xfb && _packetbody[6] == (byte) 0xfb)) {
            _packetbody = BytePlus.getPartBytes(_packetbody, 5, _packetbody.length - 1 - 5);
        }
        if (_packetbody[0] == (byte) 0xfb && _packetbody[1] == (byte) 0xfb) {
            devicenum = BytePlus.getPartBytes(_packetbody, 2, 5);
            testnum = BytePlus.getPartBytes(_packetbody, 7, 4);
            testkind = BytePlus.getPartBytes(_packetbody, 11, 2);
//            setbody(BytePlus.getPartBytes(_packetbody, 13, _packetbody.length - 1 - 14));
            //14 改为 13
            setbody(BytePlus.getPartBytes(_packetbody, 13, _packetbody.length - 1 - 13));

//            packtail(assemblealldata());
            tail = _packetbody[_packetbody.length - 1];
        } else {
            packrandomhead();
            setbody(_packetbody);
            packtail(assemblealldata());
        }
    }

    /**
     *
     * @param _devicenum
     * @param _testnum
     * @param _testkind
     * @param _packetbody
     */
    public ALCpacket(byte[] _devicenum, byte[] _testnum, byte[] _testkind, byte[] _packetbody) {
        packhead(_devicenum, _testnum, _testkind);
        setbody(_packetbody);
        packtail(assemblealldata());
    }

    /**
     *
     * @param _devicenum
     * @param _testnum
     * @param _testkind
     */
    public void packhead(byte[] _devicenum, byte[] _testnum, byte[] _testkind) {
        if (_devicenum == null | _devicenum.length != getDevicenum().length) {
            error = 2;

        }
        if (_testnum == null | _testnum.length != getTestnum().length) {
            error = 3;
        }
        if (_testkind == null | _testkind.length != getTestkind().length) {
            error = 4;
        }
        if (error != 0) {
            return;
        }
        setDevicenum(_devicenum);
        setTestnum(_testnum);
        setTestkind(_testkind);
    }

    /**
     *
     */
    public void packrandomhead() {
        byte[] _devicenum = DataFactory.DeviceNumProducer();
        byte[] _testnum = DataFactory.TestNumProducer();
        byte[] _testkind = DataFactory.TestKindProducer();
        packhead(_devicenum, _testnum, _testkind);
    }

    /**
     *
     * @param _body
     */
    public void setbody(byte[] _body) {
        setPacketbody(_body);
        bodyfillflag = true;
    }

    /**
     *
     * @return
     */
    public byte[] getAllpacket() {
        allpacket = assemblealldata();
        return allpacket;
    }

    /**
     * 返回组装好的包，有可能返回空值
     *
     * @return
     */
    private byte[] assemblealldata() {
        if (bodyfillflag) {
            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream();
                baos.write(getPacketsyncbyte());
                baos.write(getDevicenum());
                baos.write(getTestnum());
                baos.write(getTestkind());
                baos.write(getPacketbody());
                baos.write(tail);
                allpacket = baos.toByteArray();
                return allpacket;
            } catch (IOException iOException) {
                return null;
            }

//            byte[] all = new byte[packetsyncbyte.length + devicenum.length + testnum.length + packetbody.length + 1];
//            System.arraycopy(packetsyncbyte, 0, all, 0, packetsyncbyte.length);
//            System.arraycopy(devicenum, 0, all, packetsyncbyte.length, devicenum.length);
//            System.arraycopy(testnum, 0, all, packetsyncbyte.length + devicenum.length, testnum.length);
//            System.arraycopy(packetbody, 0, all, packetsyncbyte.length + devicenum.length + testnum.length, packetbody.length);
//            all[all.length-1] = 0;//预置tail位

        } else {
            return null;
        }
    }

    /**
     * 返回带着校验和的包
     * @return
     */
    public byte[] getWholePacket() {
        return packtail(assemblealldata());
    }

    /**
     *
     * @return
     */
    public byte[] getLLPacket() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] all = getWholePacket();
        baos.write(0);
        baos.write(0);
        baos.write(0);
        baos.write(0);
        baos.write(all.length);
        try {
            baos.write(all);
        } catch (IOException ex) {
            Logger.getLogger(ALCpacket.class.getName()).log(Level.SEVERE, null, ex);
        }
        baos.write(0);
        return packtail(baos.toByteArray());
    }

    
    
    /**
     *
     * @param inbyte
     * @return
     */
    public byte[] packtail(byte[] inbyte) {
        if (inbyte != null) {
            byte tempbyte = calcxor(inbyte);
            inbyte[inbyte.length - 1] = tempbyte;
        }
        return inbyte;
    }
    //不计算最后数组一位

    private byte calcxor(byte[] inbyte) {
        byte tempbyte = inbyte[0];
        for (int i = 1; i < inbyte.length - 1; i++) {
            tempbyte = (byte) (tempbyte ^ inbyte[i]);
        }
        return tempbyte;
    }

    @Override
    public String toString() {
        return BytePlus.byteArray2String(getAllpacket());
//        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == getClass()) {
            return Arrays.equals(((ALCpacket) obj).getAllpacket(), getAllpacket());
        } else {
            return false;
        }
//        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return new String(getAllpacket()).hashCode();
        //return super.hashCode();
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public String toReadbleString() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(getAllpacket());
//        System.out.println(BytePlus.bytesToHexString(getAllpacket()));

        StringBuilder sb = new StringBuilder();
        byte[] tmpbs;
        sb.append("帧同步字:");
        sb.append(BytePlus.toHexString(bais.read()).toUpperCase());
        sb.append(BytePlus.toHexString(bais.read()).toUpperCase());
        sb.append(" 设备编号(种类:");
        sb.append(bais.read());
        sb.append(",产品:");
        sb.append(bais.read());
        sb.append(",年份:");
        sb.append(2000 + bais.read());
        sb.append(",编号:");
        tmpbs = new byte[2];

        bais.read(tmpbs);
        sb.append(littleEndianBytesToShort(tmpbs) & 0xffff);
        sb.append(") 试验编号:");
        tmpbs = new byte[4];
        bais.read(tmpbs);
        sb.append(littleEndianbBytesToInt(tmpbs));
        sb.append(" 实验类型:");
        sb.append(BytePlus.toHexString(bais.read()).toUpperCase());
        sb.append(BytePlus.toHexString(bais.read()).toUpperCase());
        sb.append(" 数据种类：");
        int idatakind = bais.read();
//        sb.append("\r\n调试信息（kind号）："+idatakind);
//        sb.append("\r\n调试信息（idatakind长度）："+datakind.length);
//        sb.append("\r\n");
        sb.append(idatakind);
        if (idatakind < datakind.length) {
            sb.append(datakind[idatakind]);
        } else {
            sb.append("试验数据种类编号错误！！！！！！需要检查！！！！");
        }
        sb.append(" ");
        switch (idatakind) {
            case 0:
                sb.append(" 开机时间：");
                tmpbs = new byte[8];
                bais.read(tmpbs);
                Date start = convertDateFromBytes(tmpbs, sb);
                sb.append(dateFmt.format(start));
                sb.append(" 关机时间：");
                tmpbs = new byte[8];
                bais.read(tmpbs);
                Date shut = convertDateFromBytes(tmpbs, sb);
                sb.append(dateFmt.format(shut));
                sb.append("\r\n");
                break;
            case 1:
                sb.append(" 开始时间：");
                tmpbs = new byte[8];
                bais.read(tmpbs);
                start = convertDateFromBytes(tmpbs, sb);
                sb.append(dateFmt.format(start));
                sb.append("\r\n");
                break;
            case 2:
                sb.append(" 停止时间：");
                tmpbs = new byte[8];
                bais.read(tmpbs);
                Date stop = convertDateFromBytes(tmpbs, sb);
                sb.append(dateFmt.format(stop));
                sb.append("\r\n");
                break;
            case 3:
                int parametersnum = bais.read();
                sb.append(" 参数个数：");
                sb.append(parametersnum);
                for (int i = 0; i < parametersnum; i++) {
                    sb.append(" 参数编号：");
                    int paramnum = bais.read();
                    sb.append(paramnum);
                    if (paramnum < paramstr.length) {
                        sb.append(" 参数名称：");
                        sb.append(paramstr[paramnum]);
                    }
                    int parameterslength = bais.read();
                    sb.append(" 参数长度：");
                    sb.append(parameterslength);
                    sb.append(" ");
                    if (parameterslength != 0) {
                        tmpbs = new byte[parameterslength];
                        bais.read(tmpbs);

                        sb.append(BytePlus.byteArray2String(tmpbs, ""));

                        sb.append("(");
                        if (parameterslength == 4) {
                            //4位 判断是可能是数值型
                            sb.append(littleEndianbBytesToInt(tmpbs));
                            sb.append(",");
                            sb.append(littleEndianbBytesToFloat(tmpbs));
                            sb.append("F,");

//                        } else {
//                            sb.append(littleEndianUTF16String(tmpbs));
                        }
                        sb.append(littleEndianUTF16String(tmpbs));
                        sb.append(") ");
                    }
                }
                break;
            case 4:
                sb.append(" 参数编号：");
                int paramnum = bais.read();
                sb.append(paramnum);
                if (paramnum < paramstr.length) {
                    sb.append(" 参数名称：");
                    sb.append(paramstr[paramnum]);
                }


//                sb.append(" 参数编号：");
//                sb.append(bais.read());
                sb.append(" 分段量：");
                sb.append(bais.read());
                sb.append(" 段号：");
                sb.append(bais.read());
                sb.append(" 本段长：");
                int segmentlength = bais.read();
                sb.append(segmentlength);
                sb.append(" 参数编码：");
                tmpbs = new byte[segmentlength];
                sb.append(" ");
                bais.read(tmpbs);
                sb.append(BytePlus.byteArray2String(tmpbs, ""));
                sb.append("(");
                sb.append(littleEndianUTF16String(tmpbs));
                sb.append(") ");
                break;
            case 5:

                sb.append(" 时间：");
                tmpbs = new byte[8];
                bais.read(tmpbs);
                Date sendtime = convertDateFromBytes(tmpbs, sb);
                sb.append(dateFmt.format(sendtime));

                int switchvalue = bais.read();

                if (switchvalue == 1) {
                    //有开关量
                    tmpbs = new byte[4];
                    bais.read(tmpbs);
                    int tempint = BytePlus.bytes2int(tmpbs);
                    String tempstr = Integer.toBinaryString(tempint);
                    sb.append(" 开关量(");
                    for (int j = 0; j < 32 - tempstr.length(); j++) {
                        sb.append("0");
                    }
                    sb.append(tempstr);
                    sb.append(") ");

                } else if (switchvalue == 0) {
                    sb.append(" 无开关量 ");
                    //无开关量
                } else {
                    sb.append(" 解析错误！！！！！！");
                    //错误
                }

                parametersnum = bais.read();
                sb.append(" 数值个数：");
                sb.append(parametersnum);
                for (int i = 0; i < parametersnum; i++) {

                    sb.append(" 数值");
                    sb.append(i);
                    sb.append("：");
                    tmpbs = new byte[4];
                    bais.read(tmpbs);

                    sb.append(BytePlus.byteArray2String(tmpbs, ""));

                    sb.append("(");
                    sb.append(littleEndianbBytesToInt(tmpbs));
                    sb.append(",");
                    sb.append(littleEndianbBytesToFloat(tmpbs));
                    sb.append("F) ");

                }
                break;
            case 6:
                sb.append(" 时间：");
                tmpbs = new byte[8];
                bais.read(tmpbs);
                sendtime = convertDateFromBytes(tmpbs, sb);
                sb.append(dateFmt.format(sendtime));

                switchvalue = bais.read();

                if (switchvalue == 1) {
                    //有开关量
                    tmpbs = new byte[4];
                    bais.read(tmpbs);
                    int tempint = BytePlus.bytes2int(tmpbs);
                    String tempstr = Integer.toBinaryString(tempint);
                    sb.append(" 开关量(");
                    for (int j = 0; j < 32 - tempstr.length(); j++) {
                        sb.append("0");
                    }
                    sb.append(tempstr);
                    sb.append(") ");

                } else if (switchvalue == 0) {
                    sb.append(" 无开关量 ");
                    //无开关量
                } else {
                    sb.append(" 解析错误！！！！！！");
                    //错误
                }

                parametersnum = bais.read();
                sb.append(" 数值个数：");
                sb.append(parametersnum);
                sb.append(" ");

                for (int i = 0; i < parametersnum; i++) {
                    sb.append(" 数值编号：");
                    sb.append(bais.read());
                    sb.append(" ");
                    tmpbs = new byte[4];
                    bais.read(tmpbs);
                    sb.append(BytePlus.byteArray2String(tmpbs, ""));
                    sb.append("(");
                    sb.append(littleEndianbBytesToInt(tmpbs));
                    sb.append(",");
                    sb.append(littleEndianbBytesToFloat(tmpbs));
                    sb.append("F) ");
//                    sb.append(") ");


                }
                break;
            case 7:
                sb.append(" 起始时间：");
                tmpbs = new byte[8];
                bais.read(tmpbs);
                sendtime = convertDateFromBytes(tmpbs, sb);
                sb.append(dateFmt.format(sendtime));

                sb.append(" 间隔时间：");
                tmpbs = new byte[2];
                bais.read(tmpbs);
                sb.append(littleEndianBytesToShort(tmpbs));
                sb.append(" 数值编号：");
                sb.append(bais.read());

                parametersnum = bais.read();
                sb.append(" 数值个数：");
                sb.append(parametersnum);
                sb.append(" ");

                for (int i = 0; i < parametersnum; i++) {
                    sb.append(" 数值");
                    sb.append(i);
                    sb.append("：");
                    tmpbs = new byte[4];
                    bais.read(tmpbs);
                    sb.append(BytePlus.byteArray2String(tmpbs, ""));
                    sb.append("(");
                    sb.append(littleEndianbBytesToInt(tmpbs));
                    sb.append(",");
                    sb.append(littleEndianbBytesToFloat(tmpbs));
                    sb.append("F) ");
                }
                break;
            case 8:


                switchvalue = bais.read();

                if (switchvalue == 1) {
                    //有开关量
                    tmpbs = new byte[4];
                    bais.read(tmpbs);
                    int tempint = BytePlus.bytes2int(tmpbs);
                    String tempstr = Integer.toBinaryString(tempint);
                    sb.append(" 开关量(");
                    for (int j = 0; j < 32 - tempstr.length(); j++) {
                        sb.append("0");
                    }
                    sb.append(tempstr);
                    sb.append(") ");

                } else if (switchvalue == 0) {
                    sb.append(" 无开关量 ");
                    //无开关量
                } else {
                    sb.append(" 解析错误！！！！！！");
                    //错误
                }
                parametersnum = bais.read();
                sb.append(" 结果个数：");
                sb.append(parametersnum);
                for (int i = 0; i < parametersnum; i++) {
                    sb.append(" 结果");
                    sb.append(i);
                    sb.append("：");
                    tmpbs = new byte[4];
                    bais.read(tmpbs);
                    sb.append(BytePlus.byteArray2String(tmpbs, ""));
                    sb.append("(");
                    sb.append(littleEndianbBytesToInt(tmpbs));
                    sb.append(",");
                    sb.append(littleEndianbBytesToFloat(tmpbs));
                    sb.append("F");
                    sb.append(") ");
                }
                break;
            case 9:
                switchvalue = bais.read();

                if (switchvalue == 1) {
                    //有开关量
                    tmpbs = new byte[4];
                    bais.read(tmpbs);
                    int tempint = BytePlus.bytes2int(tmpbs);
                    String tempstr = Integer.toBinaryString(tempint);
                    sb.append("开关量(");
                    for (int j = 0; j < 32 - tempstr.length(); j++) {
                        sb.append("0");
                    }
                    sb.append(tempstr);
                    sb.append(") ");

                } else if (switchvalue == 0) {
                    sb.append("无开关量 ");
                    //无开关量
                } else {
                    sb.append("解析错误！！！！！！");
                    //错误
                }
                parametersnum = bais.read();
                sb.append("结果个数：");
                sb.append(parametersnum);
                for (int i = 0; i < parametersnum; i++) {
                    sb.append(" 结果编号：");
                    sb.append(bais.read());
                    tmpbs = new byte[4];
                    bais.read(tmpbs);
                    sb.append(" ");
                    sb.append(BytePlus.byteArray2String(tmpbs, ""));
                    sb.append("(");
                    sb.append(littleEndianbBytesToInt(tmpbs));
                    sb.append(",");
                    sb.append(littleEndianbBytesToFloat(tmpbs));
                    sb.append("F) ");
                }
                break;
            case 0x0A: //事件信息

                //多帧用识别码：8个字节，当事件信息需要多帧上送时该编码用作识别多条数据帧的分组，该编码可以随机生成，但是1个月内不允许重复.
                tmpbs = new byte[8];
                bais.read(tmpbs);
                sb.append(" 多帧用识别码(");
                sb.append(BytePlus.byteArray2String(tmpbs, ""));
                sb.append(") ");
                //时间编码：8字节，全长度时间，记录数据采样时间。按YYY,MM,DD,HH,mm,ss,fff方式记录，
                //其中首字节记年隐含千位，使用一个Byte代表自2000年至2255年的年份，其余代表相应数据，最尾2字节加和为毫秒部分，小时按24H制。
                tmpbs = new byte[8];
                bais.read(tmpbs);
                sendtime = convertDateFromBytes(tmpbs, sb);
                sb.append(dateFmt.format(sendtime));
                //事件编号，8字节，每事件唯一，可另行编码。
                tmpbs = new byte[8];
                bais.read(tmpbs);
                sb.append(" 事件编号(");
                sb.append(BytePlus.byteArray2String(tmpbs, ""));
                sb.append(") ");
                //分段量：1字节，辅助计数，代表该事件描述被分为几段上报.
                int segmentcount = bais.read();
                sb.append("分段量：");
                sb.append(segmentcount);
                sb.append(" ");
                //段号：1字节，辅助计数，代表当前帧是本事件描述的第几段
                int segmentno = bais.read();
                sb.append("分段号：");
                sb.append(segmentno);
                sb.append(" ");
                //本段长：1字节，辅助计数，代表本帧事件描述长度，取值在1-50之间，没有事件描述为0.
                int segmentsize = bais.read();
                sb.append("段长：");
                sb.append(segmentsize);
                sb.append(" ");
                if (segmentsize > 0) {
                    //事件描述编码：字符串型。记录事件的具体编码，Unicode编码，长度不超过50个字节，没有事件描述时该码为空.
                    tmpbs = new byte[segmentsize];
                    bais.read(tmpbs);
                    sb.append(" 事件描述编码(");
//                    sb.append(BytePlus.byteArray2String(tmpbs, ""));
//                    sb.append("(");
                    sb.append(littleEndianUTF16String(tmpbs));
//                    sb.append(") ");
                    sb.append(") ");
                }

                break;
            case 0x0B: //通讯验证

                //用于设备验证通讯状态，无实际作用，不解析。
                sb.append(" 通讯验证 ");

                //时间编码：8字节，全长度时间，记录数据采样时间。按YYY,MM,DD,HH,mm,ss,fff方式记录，
                //其中首字节记年隐含千位，使用一个Byte代表自2000年至2255年的年份，其余代表相应数据，最尾2字节加和为毫秒部分，小时按24H制。
                tmpbs = new byte[8];
                bais.read(tmpbs);
                sendtime = convertDateFromBytes(tmpbs, sb);
                sb.append(dateFmt.format(sendtime));


                break;
            case 0x9D: //授权控制回应

                //用于设备验证通讯状态，无实际作用，不解析。
                int tempi = sb.indexOf("试验编号:");
                sb.replace(tempi, tempi + 5, "服务器编号");
                sb.append(" 授权控制回应帧  ");

                //时间编码：8字节，全长度时间，记录数据采样时间。按YYY,MM,DD,HH,mm,ss,fff方式记录，
                //其中首字节记年隐含千位，使用一个Byte代表自2000年至2255年的年份，其余代表相应数据，最尾2字节加和为毫秒部分，小时按24H制。
                tmpbs = new byte[8];
                bais.read(tmpbs);
                sendtime = convertDateFromBytes(tmpbs, sb);
                sb.append(dateFmt.format(sendtime));

                tmpbs = new byte[2];
                bais.read(tmpbs);
                sb.append(" 动作指令: ");
                sb.append(littleEndianBytesToShort(tmpbs));

                tmpbs = new byte[8];
                bais.read(tmpbs);
                sb.append(" 备用1 （");
                sb.append(BytePlus.byteArray2String(tmpbs, ""));
                sb.append("） ");
                tmpbs = new byte[4];
                bais.read(tmpbs);
                sb.append(" 备用2 （");
                sb.append(BytePlus.byteArray2String(tmpbs, ""));
                sb.append("） ");

                break;
            default:

        }
        sb.append("校验和:");
        int checksum = bais.read() & 0xff;
        int checksumcalc = calcxor(allpacket) & 0xff;
        sb.append(BytePlus.toHexString(checksum));
        if (checksum == checksumcalc) {
            sb.append("校验和正确");
        } else {
            sb.append("校验和不正确，计算校验和应为");
            sb.append(checksumcalc);
        }
        if (bais.available() > 0) {
            sb.append("所有字节解析后仍有数据，数据可能有问题");
        }
        sb.append("\r\n");
//        bais.read(tmpbs);
//
//        sb.append(2000+);

        return sb.toString();
    }
    /**
     *
     */
    public static final String[] datakind = {"设备上电", "试验开始", "实验结束", "短参数", "长参数", "实验数据全部上送", "实验数据指定上送", "实验数据序列上送", "试验结果全部上送", "试验结果指定上送", "事件信息", "通讯验证", "预留12号数据类型", "预留13号数据类型"};
    /**
     *
     */
    public static final SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    /**
     *
     */
    public static final String[] paramstr = {"名称", "试验编号", "试验员编号", "样品编号", "扫描编号", "设备型号", "试验名称", "试验批号", "试验牌号", "测试编号", "试验日期", "试验时间", "试验温度", "试验湿度", "执行标准", "试验方法", "试验模式", "试验类型", "试样名称", "试样材料", "试样类型", "试样批号", "试样牌号", "试样编号", "试样制备", "试样来源", "试样厂商", "检方名称", "试验员", "试验员姓名", "试验员部门", "试验员职务", "公司名称", "备注1", "备注2", "备注3", "试样长度", "无效头长度", "无效尾长度", "试样宽度", "试样厚度", "试样面积", "试样层数", "试样件数/试样数量", "试验次数", "伸长率点1", "伸长率点2", "伸长率点3", "伸长率点4", "伸长率点5", "量程", "试验温度", "试验湿度", "试验方法", "试验模式", "执行标准", "循环次数", "测试时间", "测试间隔", "重复零状态", "循环间隔", "吹零时间", "预热时间", "补偿状态", "实际浓度", "补偿浓度", "试验类型", "试样类型", "独立零类型", "独立零时间", "试验比例", "判断比例", "测试状态", "测试面积", "起始压力", "结束压力", "试验间隔", "阻隔性预计", "比对测试状态", "透过率预计值", "预计值的允许浮动值", "系统保压", "下腔脱气时间", "上下腔脱气时间", "上腔吹洗时间", "压力修正", "加热状态", "制冷状态", "加湿状态", "试验速度", "试验延时", "静摩擦时间", "分段区间", "滑块质量", "热封时间", "热封压力", "上封头温度", "下封头温度", "空摆角度", "冲击能量", "试验单位", "上位点", "摆体容量", "测量速度", "测量间距", "测量时间", "测量点数", "测量样速", "质量", "量差", "标准膜透湿量", "旁吹时间", "试验湿度", "湿度偏差", "透湿量预计值", "试样材质", "试样种类", "试样描述", "送检单位", "检方单位", "试验标准", "试验参考区间", "灵敏度", "阀值", "设定压力", "修正", "幅值", "单位", "设定时间", "试验压力", "净化时间", "下腔脱气时间", "气体渗透时间", "压力平衡时间", "样气温度", "上腔充气方式", "测试气体", "试样1厚度", "试样1宽度", "试样1长度", "试样2厚度", "试样2宽度", "试样2长度", "试样3厚度", "试样3宽度", "试样3长度", "试样制备", "计量管截面积", "试样状态", "密封测试压强", "保压时间", "试验区间（MIN）", "试验区间（MAX）", "进样模式", "测量间距", "测量时间", "测量样速", "试验气体类型", "备注", "系统平衡时间", "系统预热时间", "试验温度", "实验室温度", "实验室湿度","测试方法","电机速度", "氧气浓度", "167号参数", "168号参数", "169号参数"};
    /**
     *
     */
    public static Calendar cld = Calendar.getInstance();

    /**
     *
     * @param bs
     * @param sb
     * @return
     */
    public static Date convertDateFromBytes(byte[] bs, StringBuilder sb) {


        byte[] milliseconds = new byte[2];
        milliseconds[0] = bs[7];
        milliseconds[1] = bs[6];
//        Calendar cld = Calendar.getInstance();
        cld = Calendar.getInstance();
        cld.set((bs[0] & 0xff) + 2000, bs[1] - 1, bs[2], bs[3], bs[4], bs[5]);
        cld.set(Calendar.MILLISECOND, BytePlus.bytes2int(milliseconds));
        if ((bs[1] + 256) % 256 > 12) {
            sb.append(bs[1] + "，在哪个星球上一年有这么多月？" + "（时间字节码" + BytePlus.byteArray2String(bs) + ") ");
        } else if ((bs[2] + 256) % 256 > 31) {
            sb.append(bs[2] + "，在哪个星球上一月有这么多天？" + "（时间字节码" + BytePlus.byteArray2String(bs) + ") ");
        } else if ((bs[3] + 256) % 256 > 24) {
            sb.append(bs[3] + "，好幸福啊！！一天这么多小时？" + "（时间字节码" + BytePlus.byteArray2String(bs) + ") ");
        } else if ((bs[4] + 256) % 256 > 60) {
            sb.append(bs[4] + "，做梦呢？一小时这么多分钟？" + "（时间字节码" + BytePlus.byteArray2String(bs) + ") ");
        } else if ((bs[5] + 256) % 256 > 60) {
            sb.append(bs[5] + "，睡晕了？一分钟这么多秒？" + "（时间字节码" + BytePlus.byteArray2String(bs) + ") ");
        } else if (BytePlus.bytes2int(milliseconds) > 999) {
            sb.append(BytePlus.byteArray2String(bs));
            sb.append(",时间编码毫秒处可能发生错误，请检查\r\n");
        }
        return cld.getTime();
    }

    /**
     *
     * @param bs
     * @return
     */
    public static Date convertDateFromBytes(byte[] bs) {
        StringBuilder sb = new StringBuilder();
        Date a = convertDateFromBytes(bs, sb);
        System.out.println(sb);
        return a;

    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        byte[] tmpbs = {0x0D, 0x09, 0x0B, 0x12, 0x2E, 0x7B, 0x00, 0x04};
        Date sendtime = convertDateFromBytes(tmpbs);
        System.out.println(dateFmt.format(sendtime));
//        byte[] bs = {0x0B,0x09,0x07,0x0A,0x19,0x3A,0x02,(byte)0x9A};
//        Date sendtime = convertDateFromBytes(bs);
//        System.out.println((dateFmt.format(sendtime)));
//        byte a[] = {03, 06};
//        System.out.println(littleEndianUTF16String(a));
//        System.out.println((33 + 256) % 256);
//        byte a = 1;        
//        System.out.println(BytePlus.toHexString(a));
//        System.out.println(paramstr.length);
//        for (int i = 0; i < paramstr.length; i++) {
//            String string = paramstr[i];
//            System.out.println(i+":"+string);
//        }
//        System.out.println("============================");
//        Arrays.sort(paramstr);
//        String a = "";
//        for (int i = 0; i < paramstr.length; i++) {
//            String string = paramstr[i];
//            if (a.equals(string)) {
//                System.out.println("重复！！！！！！！！：" + a);
//            } else {
//                a = string;
//            }
//
////            System.out.println(i+":"+string);
//        }
        int[] a = {1, 2, 3, 4, 5};
        int[] b = new int[5];
        b[0] = 1;
        b[1] = 2;
        b[2] = 3;
        b[3] = 4;
        b[4] = 5;
        System.out.println(Arrays.hashCode(a));
        System.out.println(Arrays.hashCode(b));
        ;

    }

    /**
     *
     * @param in
     * @return
     */
    public static String littleEndianUTF16String(byte[] in) {
        if (in.length % 2 == 0) {
            try {
                for (int i = 0; i
                        < in.length; i += 2) {
                    byte b = in[i];
                    in[i] = in[i + 1];
                    in[i + 1] = b;
                }
                return new String(in, "unicode");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ALCpacket.class.getName()).log(Level.SEVERE, null, ex);
                return BytePlus.bytesToHexString(in);
            }
        } else {
            return BytePlus.bytesToHexString(in);
        }
        //处理出错返回null
//        return null;

    }

    /**
     *
     * @param in
     * @return
     */
    public static short littleEndianBytesToShort(byte[] in) {
        short a = 0;
        a = (short) ((in[0]) & 0xff | (in[1] << 8));
//        System.out.println("(in[0])"+(in[0]&0xff));
//        System.out.println("in[1] << 8)"+(in[1] << 8));
        return a;
    }

    /**
     *
     * @param in
     * @return
     */
    public static int littleEndianbBytesToInt(byte[] in) {
        int mask = 0xff;
        int temp = 0;
        int res = 0;
        for (int i = (4 > in.length ? in.length : 4) - 1; i
                >= 0; i--) {
            res <<= 8;
            temp = in[i] & mask;
            res |= temp;
        }
        return res;
    }

    /**
     *
     * @param in
     * @return
     */
    public static float littleEndianbBytesToFloat(byte[] in) {
        Integer k = littleEndianbBytesToInt(in);
        return Float.intBitsToFloat(k);
    }

//    public static void main(String[] args) {
//        int a = 13;
//        String b = Integer.toBinaryString(a);
//        System.out.println(String.format("%032d", a));
//        byte a[] = {(byte)0xe3,3};
//        byte a[] = {(byte)0x3,3};
//        System.out.println(littleEndianBytesToShort(a));
//        Float f = -13.764f;
//        int k = Float.floatToIntBits(f);
//        System.out.println(k);
//        byte[] tmpbs = BytePlus.int2bytes(k);
//        for (int j = 0; j < tmpbs.length; j++) {
//            byte b = tmpbs[j];
//            System.out.println(BytePlus.toHexString(b & 0xff));
//        }
//        k = Integer.reverseBytes(k);
//        tmpbs = BytePlus.int2bytes(k);
//        float f1 = ALCpacket.littleEndianbBytesToFloat(tmpbs);
//        System.out.println("f1:" + f1);
//        k = Float.floatToIntBits(f1);
//        System.out.println(k);
//        tmpbs = BytePlus.int2bytes(k);
//        for (int j = 0; j < tmpbs.length; j++) {
//            byte b = tmpbs[j];
//            System.out.println(BytePlus.toHexString(b & 0xff));
//        }
//        ALCpacket A = new ALCpacket();
//        A.A();
//        byte[] a = BytePlus.int2bytes(0xFBFB);
//        byte[] b = BytePlus.getPartBytes(BytePlus.int2bytes(0xFBFB), 2, 2);
//        for (int i = 0; i < b.length; i++) {
//            System.out.println(b[i]);
//
//        }
//        byte[] a = {1, 0, 0};
//        ALCpacket.packtail(a);
//        for (int i = 0; i < a.length; i++) {
//            byte b = a[i];
//            System.out.println(b);
//        }
//        System.out.println(a ^ b);
//        System.out.println(a ^ c);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        baos.write(250);
//        byte a[] = baos.toByteArray();
//        System.out.println(a.length);
//        for (int i = 0; i < a.length; i++) {
//            byte b = a[i];
//            System.out.print(b + "\t");
//        }
//        byte[] a = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 1, 41, 51, 61, 71, 81};
//        ByteArrayInputStream bais = new ByteArrayInputStream(a);
//        while (bais.available() > 0) {
//            System.out.println(BytePlus.toHexString(bais.read()));
//
//        }
//    }
    /**
     * @return the packetsyncbyte
     */
    public byte[] getPacketsyncbyte() {
        return packetsyncbyte;
    }

    /**
     * @param packetsyncbyte the packetsyncbyte to set
     */
    public void setPacketsyncbyte(byte[] packetsyncbyte) {
        this.packetsyncbyte = packetsyncbyte;
    }

    /**
     * @return the devicenum
     */
    public byte[] getDevicenum() {
        return devicenum;
    }

    /**
     * @param devicenum the devicenum to set
     */
    public void setDevicenum(byte[] devicenum) {
        this.devicenum = devicenum;
    }

    /**
     * @return the testnum
     */
    public byte[] getTestnum() {
        return testnum;
    }

    /**
     * @param testnum the testnum to set
     */
    public void setTestnum(byte[] testnum) {
        this.testnum = testnum;
    }

    /**
     * @return the testkind
     */
    public byte[] getTestkind() {
        return testkind;
    }

    /**
     * @param testkind the testkind to set
     */
    public void setTestkind(byte[] testkind) {
        this.testkind = testkind;
    }

    /**
     * @return the packetbody
     */
    public byte[] getPacketbody() {
        return packetbody;
    }

    /**
     * @param packetbody the packetbody to set
     */
    public void setPacketbody(byte[] packetbody) {
        this.packetbody = packetbody;
    }

    /**
     * @return the tail
     */
    public byte getTail() {
        return tail;
    }

    /**
     * @param tail the tail to set
     */
    public void setTail(byte tail) {
        this.tail = tail;
    }
}
