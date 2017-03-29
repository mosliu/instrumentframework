package net.labthink.instrument.device.OxygenTransducer.message;

import net.labthink.instrument.device.base.message.AbstractPacket;
import net.labthink.utils.BytePlus;

/**
 * OxygenTransducer's 51-PC Message
 *
 * @version        1.0.0.0, Jul 29, 2011
 * @author         Moses
 */
public class OxygenTransducerOxyOutMessage extends AbstractPacket implements
        OxygenTransducerOxyOutMessageParams {

    public OxygenTransducerOxyOutMessage() {
    }

    public OxygenTransducerOxyOutMessage(byte[] _content) {
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

}
