package net.labthink.instrument.device.SHG01.codec;

//~--- non-JDK imports --------------------------------------------------------


import net.labthink.instrument.device.SHG01.message.SHG01OutMessage;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * A {@link ProtocolCodecFactory} that provides a protocol codec for SumUp
 * protocol.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class SHG01ProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public SHG01ProtocolCodecFactory() {
        super.addMessageDecoder(SHG01ErrorMessageDecoder.class);
        super.addMessageDecoder(SHG01MessageDecoder.class);
        
        super.addMessageEncoder(SHG01OutMessage.class, SHG01MessageEncoder.class);
    }
}
