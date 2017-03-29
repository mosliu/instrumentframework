package net.labthink.instrument.device.CommandSender.message;

import java.io.Serializable;
import net.labthink.instrument.device.base.message.AbstractPacket;

/**
 * CommandSender's 51-PC Message
 *
 * @version 1.0.0.0, Oct 26, 2010
 * @author Moses
 */
public class CommandSenderOutMessage extends AbstractPacket  implements
        Serializable {
    private byte[] content;
    public CommandSenderOutMessage() {
    }

    public CommandSenderOutMessage(byte[] _content) {
    }

    /**
     * 重置为默认包体内容
     *
     * @param _content 重置时的content内容。如传入null则初始化为new
     * byte[CommonParameters.PACKET_BODY_LEN];
     */
    protected void defaultPacket(byte[] _content) {
        if (_content == null) {
            this.setContent(new byte[1]);
        } else {
            this.setContent(_content);
        }
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
}
