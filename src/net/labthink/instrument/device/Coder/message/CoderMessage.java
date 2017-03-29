package net.labthink.instrument.device.Coder.message;

import java.io.Serializable;

/**
 * LLTEST's PC-51 Message
 *
 * @version 1.0.0.0, Sep 16, 2010
 * @author Moses
 */
public class CoderMessage implements Serializable {

    private byte header = (byte) 0xFB;
    private byte tail = 0;
    private int length = 6;
    private byte contentlength = 4;
    private byte[] content;
    private byte[] all;

    public CoderMessage() {
    }

    public CoderMessage(byte[] _content) {
        defaultPacket(_content);
    }

    /**
     * 重置为默认包体内容
     *
     * @param _content 重置时的content内容。如传入null则初始化为new
     * byte[CommonParameters.PACKET_BODY_LEN];
     */
    protected void defaultPacket(byte[] _content) {
        if (_content == null) {
            this.content = new byte[contentlength];
        } else {
            this.content = _content;
        }
        this.setTail(caculateCheckValue());
    }

    private byte caculateCheckValue() {
        byte checksum = all[0];
        for (int i = 1; i < all.length - 1; i++) {
            byte b = all[i];
            checksum = (byte) (checksum ^ b);
        }
        return checksum;
    }

    public boolean CheckState() {
//        rebuildpacket();
        return caculateCheckValue() == getTail();
    }

    public byte calcTail() {
        byte _xor = content[0];
        for (int i = 1; i < content.length; i++) {
            _xor = (byte) (_xor + content[i]);
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
     * @return the length
     */
    public int getLength() {
        return length;
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
     * @return the all
     */
    public byte[] getAll() {
        if (all == null) {
            all = new byte[6];
        }
        all[0] = header;
        System.arraycopy(content, 0, all, 1, 4);
        all[5] = tail;

        return all;
    }

    /**
     * @param all the all to set
     */
    public void setAll(byte[] all) {
        this.all = all;
    }

    public static void main(String[] args) {
        byte[] con = {1, (byte) 253, 1, 3};
        CoderMessage cm = new CoderMessage(con);
        System.out.println(cm.calcTail());
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
