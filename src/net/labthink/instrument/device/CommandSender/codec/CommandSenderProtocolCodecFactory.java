package net.labthink.instrument.device.CommandSender.codec;

//~--- non-JDK imports --------------------------------------------------------


import net.labthink.instrument.device.CommandSender.message.CommandSenderOutMessage;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * CommandSender's ProtocolCodecFactory
 *
 * @version        1.0.0.0, Oct 26, 2010
 * @author         Moses
 */
public class CommandSenderProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public CommandSenderProtocolCodecFactory() {
        super.addMessageDecoder(CommandSenderErrorMessageDecoder.class);
        super.addMessageDecoder(CommandSenderMessageDecoder.class);
        
        super.addMessageEncoder(CommandSenderOutMessage.class, CommandSenderMessageEncoder.class);
    }
}
