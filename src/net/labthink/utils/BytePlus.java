package net.labthink.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import static net.labthink.instrument.device.intelligent.industrialpc.zigbee.ALCpacket.littleEndianbBytesToInt;

public class BytePlus {

    /**
     * 将byte数组ba从第a位（含）开始至末尾的所有位置0，
     *
     * @param ba
     * @param a
     */
    public static void fillcontent(byte[] ba, int a) {
        if (a < ba.length) {
            for (int idx = a; idx < ba.length; idx++) {
                ba[idx] = 0;
            }
        }
    }

    public static int bytes2int(byte[] b) {
        // byte[] b=new byte[]{1,2,3,4};
        int mask = 0xff;
        int temp = 0;
        int res = 0;
        for (int i = 0; i < (4 > b.length ? b.length : 4); i++) {
            res <<= 8;
            temp = b[i] & mask;
            res |= temp;
        }
        return res;
    }

    public static short bytes2short(byte[] b) {

        // byte[] b=new byte[]{1,2,3,4};
        int mask = 0xff;
        int temp = 0;
        short res = 0;
        for (int i = 0; i < (2 > b.length ? b.length : 2); i++) {
            res <<= 8;
            temp = b[i] & mask;
            res |= temp;
        }
        return res;
        
        
//        short s = (short) (b[1] & 0xFF);
//        s |= (b[0] << 8) & 0xFF00;
//        return s;
        
    }

    public static short littleEndianBytesToShort(byte[] in) {
        short a = 0;
        a = (short) ((in[0]) | (in[1] << 8));
        return a;
    }

    public static int littleEndianbBytesToInt(byte[] in) {
        int mask = 0xff;
        int temp = 0;
        int res = 0;
        for (int i = (4 > in.length ? in.length : 4) - 1; i >= 0; i--) {
            res <<= 8;
            temp = in[i] & mask;
            res |= temp;
        }
        return res;
    }

    public static byte[] int2bytes(int num) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (num >>> (24 - i * 8));
        }
        return b;
    }

    public static byte[] short2bytes(short num) {
        byte[] b = new byte[2];
        for (int i = 0; i < 2; i++) {
            b[i] = (byte) (num >>> (8 - i * 8));
        }
        return b;
        
//        byte[] bytes = new byte[2];
//        for (int i = 1; i >= 0; i--) {
//            bytes[i] = (byte) (num % 256);
//            num >>= 8;
//        }
//        return bytes;
        
        
    }

    public static boolean compareByteArray(byte[] a, byte[] b) {
        if (a.length == b.length) {
            for (int i = 0; i < b.length; i++) {
                if (a[i] != b[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 获取传入的byte数组的一部分
     *
     * @param source 原数组
     * @param start 开始位置
     * @param length 长度
     * @return
     */
    public static byte[] getPartBytes(byte[] source, int start, int length) {
        byte[] dest = new byte[length];
        System.arraycopy(source, start, dest, 0, length);
        return dest;
    }

    /**
     * 将指定byte数组以16进制字符串的形式返回
     *
     * @param ba
     * @return
     */
    public static String byteArray2String(byte[] ba) {
        String blank = " ";
        return byteArray2String(ba, blank);
    }

    public static String byteArray2String(byte[] ba, String blank) {
        StringBuilder sb = new StringBuilder(ba.length * 2);
        for (int i = 0; i < ba.length; i++) {
            String hex = Integer.toHexString(ba[i] & 0xFF);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex.toUpperCase());
            sb.append(blank);
        }
        return sb.toString();
    }

    public static int parseByteToUInt(byte a) {
        return a < 0 ? a + 256 : a;
    }

    /**
     * @param tempbytearray
     */
    public static int parseIntFromByteArray(byte[] bytearray) {
        int value = 0;
        for (int i = bytearray.length - 1; i >= 0; i--) {
            int t = parseByteToUInt(bytearray[i]);
            value += (t << 8 * (i - 1));
        }
        return value;
    }

    /**
     * 把16进制字符串转换成字节数组
     *
     * @param hex
     * @return
     */
    public static byte[] hexStringToByte(String hex) {


        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    /**
     * 把16进制字符串转换成字节数组
     *
     * @param hex
     * @return
     */
    public static byte[] hexStringToBytes(String hex) {

        return hexStringToByte(hex.replaceAll(" ", "").toUpperCase());
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 把字节数组转换成16进制字符串
     *
     * @param bArray
     * @return
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 把字节数组转换成16进制字符串
     *
     * @param bArray
     * @return
     */
    public static final String toHexString(int i) {
        String a = Integer.toHexString(i);
        if (a.length() == 1) {
            a = "0" + a;
        }
        return a;
    }

    // Returns a bitset containing the values in bytes.
    // The byte-ordering of bytes must be big-endian which means the most significant bit is in element 0.
    public static BitSet fromByteArray(byte[] bytes) {

        BitSet bits = new BitSet();
        for (int i = 0; i < bytes.length * 8; i++) {
            if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                bits.set(i);
            }
        }
        return bits;
    }

    // Returns a byte array of at least length 1.
    // The most significant bit in the result is guaranteed not to be a 1
    // (since BitSet does not support sign extension).
    // The byte-ordering of the result is big-endian which means the most significant bit is in element 0.
    // The bit at index 0 of the bit set is assumed to be the least significant bit.
    public static byte[] toByteArray(BitSet bits) {
        byte[] bytes = new byte[bits.length() / 8 + 1];
        for (int i = 0; i < bits.length(); i++) {
            if (bits.get(i)) {
                bytes[bytes.length - i / 8 - 1] |= 1 << (i % 8);
            }
        }
        return bytes;
    }

    /**
     * 把字节数组转换为对象
     *
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static final Object bytesToObject(byte[] bytes) throws IOException,
            ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream oi = new ObjectInputStream(in);
        Object o = oi.readObject();
        oi.close();
        return o;
    }

    /**
     * 把可序列化对象转换成字节数组
     *
     * @param s
     * @return
     * @throws IOException
     */
    public static final byte[] objectToBytes(Serializable s) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream ot = new ObjectOutputStream(out);
        ot.writeObject(s);
        ot.flush();
        ot.close();
        return out.toByteArray();
    }

    public static final String objectToHexString(Serializable s)
            throws IOException {
        return bytesToHexString(objectToBytes(s));
    }

    public static final Object hexStringToObject(String hex)
            throws IOException, ClassNotFoundException {
        return bytesToObject(hexStringToByte(hex));
    }

    /**
     * @函数功能: BCD码转为10进制串(阿拉伯数据) @输入参数: BCD码 @输出结果: 10进制串
     */
    public static String bcd2Str(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
    }

    /**
     * @函数功能: 10进制串转为BCD码 @输入参数: 10进制串 @输出结果: BCD码
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;

        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }

        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }

        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;

        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }

            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }

            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    public static String BCD2ASC(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; i++) {
            int h = ((bytes[i] & 0xf0) >>> 4);
            int l = (bytes[i] & 0x0f);
            temp.append(BToA[h]).append(BToA[l]);
        }
        return temp.toString();
    }

    /**
     * MD5加密字符串，返回加密后的16进制字符串
     *
     * @param origin
     * @return
     */
    public static String MD5EncodeToHex(String origin) {
        return bytesToHexString(MD5Encode(origin));
    }

    /**
     * MD5加密字符串，返回加密后的字节数组
     *
     * @param origin
     * @return
     */
    public static byte[] MD5Encode(String origin) {
        return MD5Encode(origin.getBytes());
    }

    /**
     * MD5加密字节数组，返回加密后的字节数组
     *
     * @param bytes
     * @return
     */
    public static byte[] MD5Encode(byte[] bytes) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            return md.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
    public final static char[] BToA = "0123456789abcdef".toCharArray();

    public static byte[] assembleBytes(byte[]... bs) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            for (byte[] bn : bs) {
                baos.write(bn);
            }
        } catch (IOException ex) {
            return null;
        }
        byte[] rtn = baos.toByteArray();
        baos.reset();
        return rtn;
    }

    /**
     *
     * @param all
     * @return
     */
    public static byte calcXor(byte[] all) {
        if (all == null) {
            return 0;
        }
        byte rtnbyte = all[0];
        for (int i = 1; i < all.length; i++) {
//            byte b = all[i];
            rtnbyte = (byte) (rtnbyte ^ all[i]);
        }
        return rtnbyte;

    }

    

    public static float littleEndianbBytesToFloat(byte[] in) {
        Integer k = littleEndianbBytesToInt(in);
        return Float.intBitsToFloat(k);
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
                
                return BytePlus.bytesToHexString(in);
            }
        } else {
            return BytePlus.bytesToHexString(in);
        }
        //处理出错返回null
//        return null;

    }

    /**
     * 不包含最后一个字节
     * @param inbyte
     * @return 返回计算后的byte
     */
    
    public static byte calcxorwithoutlast(byte[] inbyte) {
        byte tempbyte = inbyte[0];
        for (int i = 1; i < inbyte.length - 1; i++) {
            tempbyte = (byte) (tempbyte ^ inbyte[i]);
        }
        return tempbyte;
    }
    
    /**
     * 包含最后一个字节
     * @param inbyte
     * @return 返回计算后的byte
     */
    
    public static byte calcxor(byte[] inbyte) {
        byte tempbyte = inbyte[0];
        for (int i = 1; i < inbyte.length ; i++) {
            tempbyte = (byte) (tempbyte ^ inbyte[i]);
        }
        return tempbyte;
    }
    
    public static void main(String[] args) {
        String a = "AA AA 05 01 02 03 04 00 06 07 00 00 0a 0b BB BB ";
//        byte a[] = {34,56};
//        byte b[] = new byte[2];
//        byte aa = (byte) 0xAA;
//        System.out.println(aa);
//        byte[] b = hexStringToBytes(a);
//        System.out.println("aaa");
        String b = "AA 4 88 0 64 12    \r\n   *^&^ 4 BB ^";
        b = b.replaceAll("[^\\sa-zA-Z0-9]", "");
        System.out.println(b);
        b = b.replaceAll("[\\s]+", " ");
        System.out.println(b);
        b = b.replaceAll("\\s(\\d{1})\\s", " 0$1 ");
        System.out.println(b);

    }
    
    
    
    
}
