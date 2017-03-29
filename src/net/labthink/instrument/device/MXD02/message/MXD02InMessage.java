package net.labthink.instrument.device.MXD02.message;

import net.labthink.instrument.device.base.message.AbstractPacket;

/**
 * MXD02's PC-51 Message
 *
 * @version        1.0.0.0, Jun 24, 2014
 * @author         Moses
 */
public class MXD02InMessage extends AbstractPacket implements MXD02InMessageParams {

  

    public MXD02InMessage() {
    }

    public MXD02InMessage(byte[] _content) {
    }

    /**
     * 重置为默认包体内容
     * @param _content 重置时的content内容。如传入null则初始化为new byte[CommonParameters.PACKET_BODY_LEN];
     */
    @Override
    protected void defaultPacket(byte[] _content) {
        this.header = PACKET_HEADER;
        this.tail = PACKET_TAIL;
        this.length = PACKET_FULL_LEN;
        if (_content == null) {
            this.content = new byte[PACKET_BODY_LEN];
        } else {
            this.content = _content;
        }
    }
}
