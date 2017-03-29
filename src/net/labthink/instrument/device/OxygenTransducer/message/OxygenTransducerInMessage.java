package net.labthink.instrument.device.OxygenTransducer.message;

import net.labthink.instrument.device.base.message.AbstractPacket;
import net.labthink.utils.BytePlus;

/**
 * OxygenTransducer's PC-51 Message
 *
 * @version        1.0.0.0, Jul 29, 2011
 * @author         Moses
 */
public class OxygenTransducerInMessage extends AbstractPacket implements OxygenTransducerInMessageParams {

    public OxygenTransducerInMessage() {
    }

    public OxygenTransducerInMessage(byte[] _content) {
    }

    /**
     * 重置为默认包体内容
     * @param _content 重置时的content内容。如传入null则初始化为new byte[CommonParameters.PACKET_BODY_LEN];
     */
    @Override
    protected void defaultPacket(byte[] _content) {
        this.length = PACKET_FULL_LEN;
        if (_content == null) {
            this.content = new byte[PACKET_BODY_LEN];
        } else {
            this.content = _content;
        }
    }

    public void setContent(byte[] content) {
        this.content = content;
        
    }
    public void setContentWithRecalculateCRC(byte[] content) {
        this.content = content;
        resetState();
    }

    public void resetState() {
        byte bytes[] = caculateCRCValue();
        content[content.length - 2] = bytes[0];
        content[content.length - 1] = bytes[1];
    }

    public boolean CheckState() {
        byte old[] = new byte[2];
        old[0] = content[content.length - 2];
        old[1] = content[content.length - 1];
        byte now[] = caculateCRCValue();
        if (old[0] == now[0] &&old[1] == now[1]) {
            return true;
        } else {
            return false;
        }
    }

    public byte[] caculateCRCValue() {
        byte n0 = (byte) (content[0] - 0x80);
        byte[] tempbyte = new byte[2];
        tempbyte[0] = content[3];
        tempbyte[1] = 0;
        short tempshort = BytePlus.bytes2short(tempbyte);
        tempshort += 0x43;
        tempbyte[0] = content[4];
        tempbyte[1] = content[5];
        tempshort += BytePlus.bytes2short(tempbyte);
        return BytePlus.short2bytes(tempshort);
    }

    public static void main(String[] args) {
        byte n0 = (byte) (0x81 - 0x80);
        byte[] tempbyte = new byte[2];
        tempbyte[0] = 0x17;
        tempbyte[1] = 0;
        short short1 = BytePlus.bytes2short(tempbyte);
        tempbyte[0] = 0x0;
        tempbyte[1] = (byte) 0x91;
        short1 += BytePlus.bytes2short(tempbyte);
        System.out.println(short1);
        byte[] bs = BytePlus.short2bytes(short1);
        System.out.println(BytePlus.byteArray2String(bs));

    }
}
