package net.labthink.instrument.device.W3060.codec;

//~--- non-JDK imports --------------------------------------------------------

import net.labthink.instrument.device.W3060.message.W3060OutMessage;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * A {@link ProtocolCodecFactory} that provides a protocol codec for SumUp
 * protocol.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class W3060ProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public W3060ProtocolCodecFactory() {
        super.addMessageDecoder(W3060MessageDecoder.class);
        super.addMessageEncoder(W3060OutMessage.class, W3060MessageEncoder.class);
    }
}
