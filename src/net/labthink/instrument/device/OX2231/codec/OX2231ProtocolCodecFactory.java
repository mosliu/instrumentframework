package net.labthink.instrument.device.OX2231.codec;

//~--- non-JDK imports --------------------------------------------------------

import net.labthink.instrument.device.OX2231.message.OX2231Message;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * A {@link ProtocolCodecFactory} that provides a protocol codec for SumUp
 * protocol.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class OX2231ProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public OX2231ProtocolCodecFactory() {
        super.addMessageDecoder(OX2231MessageDecoder.class);
        super.addMessageEncoder(OX2231Message.class, OX2231MessageEncoder.class);
    }
}
