package net.labthink.instrument.device.OX2230W3330.codec;

//~--- non-JDK imports --------------------------------------------------------

import net.labthink.instrument.device.OX2230W3330.message.OX2230W3330OutMessage;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * A {@link ProtocolCodecFactory} that provides a protocol codec for SumUp
 * protocol.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class OX2230W3330ProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public OX2230W3330ProtocolCodecFactory() {
        super.addMessageDecoder(OX2230W3330MessageDecoder.class);
        super.addMessageEncoder(OX2230W3330OutMessage.class, OX2230W3330MessageEncoder.class);
    }
}
