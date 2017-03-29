package net.labthink.instrument.device.TSYW3.codec;

//~--- non-JDK imports --------------------------------------------------------

import net.labthink.instrument.device.TSYW3.message.TSYW3Message;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * A {@link ProtocolCodecFactory} that provides a protocol codec for SumUp
 * protocol.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class TSYW3ProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public TSYW3ProtocolCodecFactory() {
        super.addMessageDecoder(TSYW3MessageDecoder.class);
        super.addMessageEncoder(TSYW3Message.class, TSYW3MessageEncoder.class);
    }
}
