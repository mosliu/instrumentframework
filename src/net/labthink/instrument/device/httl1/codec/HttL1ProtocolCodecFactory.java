package net.labthink.instrument.device.httl1.codec;

//~--- non-JDK imports --------------------------------------------------------

import net.labthink.instrument.device.httl1.message.Httl1Message;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * A {@link ProtocolCodecFactory} that provides a protocol codec for SumUp
 * protocol.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class HttL1ProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public HttL1ProtocolCodecFactory() {
        super.addMessageDecoder(HttL1MessageDecoder.class);
        super.addMessageEncoder(Httl1Message.class, HttL1MessageEncoder.class);
    }
}
