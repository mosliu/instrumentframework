package net.labthink.instrument.device.w3030.codec;

//~--- non-JDK imports --------------------------------------------------------

import net.labthink.instrument.device.w3030.message.W3030Message;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * A {@link ProtocolCodecFactory} that provides a protocol codec for SumUp
 * protocol.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class W3030ProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public W3030ProtocolCodecFactory() {
        super.addMessageDecoder(W3030MessageDecoder.class);
        super.addMessageEncoder(W3030Message.class, W3030MessageEncoder.class);
    }
}
