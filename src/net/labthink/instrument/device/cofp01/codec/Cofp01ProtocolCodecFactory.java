package net.labthink.instrument.device.cofp01.codec;

//~--- non-JDK imports --------------------------------------------------------

import net.labthink.instrument.device.cofp01.message.Cofp01Message;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * A {@link ProtocolCodecFactory} that provides a protocol codec for SumUp
 * protocol.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class Cofp01ProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public Cofp01ProtocolCodecFactory() {
        super.addMessageDecoder(Cofp01MessageDecoder.class);
        super.addMessageEncoder(Cofp01Message.class, Cofp01MessageEncoder.class);
    }
}
