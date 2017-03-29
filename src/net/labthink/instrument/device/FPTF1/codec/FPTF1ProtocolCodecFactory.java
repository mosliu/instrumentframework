package net.labthink.instrument.device.FPTF1.codec;

//~--- non-JDK imports --------------------------------------------------------


import net.labthink.instrument.device.FPTF1.message.FPTF1OutMessage;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * FPTF1's ProtocolCodecFactory
 *
 * @version        1.0.0.0, Aug 24, 2010
 * @author         Moses
 */
public class FPTF1ProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public FPTF1ProtocolCodecFactory() {
        super.addMessageDecoder(FPTF1ErrorMessageDecoder.class);
        super.addMessageDecoder(FPTF1MessageDecoder.class);
        
        super.addMessageEncoder(FPTF1OutMessage.class, FPTF1MessageEncoder.class);
    }
}
