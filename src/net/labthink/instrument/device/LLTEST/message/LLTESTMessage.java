package net.labthink.instrument.device.LLTEST.message;

import java.io.Serializable;
import java.nio.ByteBuffer;
import net.labthink.instrument.device.intelligent.industrialpc.zigbee.ZigbeePacket;

/**
 * LLTEST's PC-51 Message
 *
 * @version        1.0.0.0, Sep 16, 2010
 * @author         Moses
 */
public class LLTESTMessage implements Serializable {

    private byte header = 0;
    private byte lqi = 0;
    private short address = 0;
    private int length = 0;
    private byte contentlength = 0;
    private byte[] content;
    private byte xor = 0;
    private byte[] all;

    public LLTESTMessage() {
    }

    public LLTESTMessage(byte[] _content) {
        defaultPacket(_content);
    }

   

    /**
     * 重置为默认包体内容
     * @param _content 重置时的content内容。如传入null则初始化为new byte[CommonParameters.PACKET_BODY_LEN];
     */
    protected void defaultPacket(byte[] _content) {
        this.setHeader((byte) 0);
        this.setLqi((byte) 0);
        this.setLength(0);
        this.setAddress((short) 0);
        this.setContentlength((byte) (_content == null ? 0 : _content.length));
        if (_content == null) {
            this.setContent(new byte[0]);
        } else {
            this.setContent(_content);
        }
        this.setXor((byte) 0);
    }

    private byte caculateCheckValue() {
        byte _xor = all[0];
        for (int i = 1; i < all.length - 1; i++) {
            byte b = all[i];
            _xor = (byte) (_xor ^ b);
        }
        return _xor;
    }

    public ZigbeePacket getZigbeePacket() {
        ByteBuffer bb = ByteBuffer.allocate(5);
        bb.put(header);
        bb.put(lqi);
        bb.putShort(address);
        bb.put(contentlength);
        byte headerall[] = bb.array();
        ZigbeePacket zp = new ZigbeePacket(content);
        zp.setZigbeehead(headerall);
        zp.setZigbeetail(xor);
        return zp;
    }

    public boolean CheckState() {
//        rebuildpacket();
        return caculateCheckValue() == xor;
    }

    public void rebuildpacket() {
        if (content == null) {
            all = null;
            return;
        }
        ByteBuffer bb = ByteBuffer.allocate(1 + 1 + 2 + 1 + content.length + 1);
        bb.put(header);
        bb.put(lqi);
        bb.putShort(address);
        bb.put(contentlength);
        bb.put(content);
        byte[] a = bb.array();
        byte _xor = calcXor(a);
        if (_xor != xor) {
//            System.out.println("校验和测试失败，校验和错误");
        }
        bb.put(_xor);

        all = bb.array();

    }

    public byte calcXor(byte[] a) {
        byte _xor = a[0];
        for (int i = 1; i < a.length; i++) {
            byte b = a[i];
            _xor = (byte) (_xor ^ b);
        }
        return _xor;
    }

    /**
     * @return the header
     */
    public byte getHeader() {
        return header;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(byte header) {
        this.header = header;
    }

    /**
     * @return the lqi
     */
    public byte getLqi() {
        return lqi;
    }

    /**
     * @param lqi the lqi to set
     */
    public void setLqi(byte lqi) {
        this.lqi = lqi;
    }

    /**
     * @return the address
     */
    public short getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(short address) {
        this.address = address;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return all.length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the contentlength
     */
    public byte getContentlength() {
        return contentlength;
    }

    /**
     * @param contentlength the contentlength to set
     */
    public void setContentlength(byte contentlength) {
        this.contentlength = contentlength;
    }

    /**
     * @return the content
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
     * @return the xor
     */
    public byte getXor() {
        return xor;
    }

    /**
     * @param xor the xor to set
     */
    public void setXor(byte xor) {
        this.xor = xor;
    }

    /**
     * @return the all
     */
    public byte[] getAll() {
        return all;
    }

    /**
     * @param all the all to set
     */
    public void setAll(byte[] all) {
        this.all = all;
    }

    public static void main(String[] args) {
//        byte a = (byte) 0xaa;
//        byte b = (byte) 0xf0;
//        byte a = (byte) 170;
//        byte b = (byte) 15;
//        byte c = (byte) (a ^ b);
//        System.out.println(c);
//        System.out.println(a ^ b);
        ByteBuffer bb = ByteBuffer.allocate(103);
        bb.put((byte) 0xa0);
        
        System.out.println(bb.position());
        System.out.println(bb.limit());
        bb.put((byte) 0xa0);
        
        System.out.println(bb.position());
        System.out.println(bb.limit());

//        bb.mark();
        bb.flip();
        System.out.println(bb.position());
        System.out.println(bb.limit());
        System.out.println("==============");
//        bb = bb.compact();
        byte[] array = bb.array();
        System.out.println(array.length);
    }
}
