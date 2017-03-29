package net.labthink.instrument.device.InfraredSensorSimulator.message;

import net.labthink.instrument.device.base.message.AbstractPacket;

/**
 * MXD02's PC-51 Message
 *
 * @version        1.0.0.0, Jun 13, 2011
 * @author         Moses
 */
public class InfraredMessage extends AbstractPacket implements InfraredMessageParams {

  

    public InfraredMessage() {
        defaultPacket(null);
    }

    public InfraredMessage(byte[] _content) {
        defaultPacket(_content);
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
    
    @Override
    public void resetState() {
	return;
    }
}
