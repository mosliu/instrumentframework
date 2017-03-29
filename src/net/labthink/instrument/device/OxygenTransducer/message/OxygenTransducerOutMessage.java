package net.labthink.instrument.device.OxygenTransducer.message;

import net.labthink.instrument.device.base.message.AbstractPacket;
import net.labthink.utils.BytePlus;

/**
 * OxygenTransducer's 51-PC Message
 *
 * @version        1.0.0.0, Jul 29, 2011
 * @author         Moses
 */
public class OxygenTransducerOutMessage extends AbstractPacket implements
        OxygenTransducerOutMessageParams {

    public OxygenTransducerOutMessage() {
    }

    public OxygenTransducerOutMessage(byte[] _content) {
    }

    /**
     * 重置为默认包体内容
     *
     * @param _content
     *            重置时的content内容。如传入null则初始化为new
     *            byte[CommonParameters.PACKET_BODY_LEN];
     */
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

    public void resetState1() {
        byte bytes[] = caculateCRCValue1();
        content[content.length - 2] = bytes[0];
        content[content.length - 1] = bytes[1];
    }

    public boolean CheckState1() {
        byte old[] = new byte[2];
        old[0] = content[content.length - 2];
        old[1] = content[content.length - 1];
        byte now[] = caculateCRCValue1();
        if (old[0] == now[0] &&old[1] == now[1]) {
            return true;
        } else {
            return false;
        }
    }

    public byte[] caculateCRCValue1() {
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
}
