package net.labthink.instrument.device.G2131.codec;

//~--- non-JDK imports --------------------------------------------------------


import net.labthink.instrument.device.G2131.message.G2131OutMessage;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * A {@link ProtocolCodecFactory} that provides a protocol codec for SumUp
 * protocol.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class G2131ProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public G2131ProtocolCodecFactory() {
        super.addMessageDecoder(G2131ErrorMessageDecoder.class);
        super.addMessageDecoder(G2131MessageDecoder.class);
        
        super.addMessageEncoder(G2131OutMessage.class, G2131MessageEncoder.class);
    }
}
