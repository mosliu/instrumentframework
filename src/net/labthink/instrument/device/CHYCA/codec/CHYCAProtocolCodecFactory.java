package net.labthink.instrument.device.CHYCA.codec;

//~--- non-JDK imports --------------------------------------------------------


import net.labthink.instrument.device.CHYCA.message.CHYCAOutMessage;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * CHYCA's ProtocolCodecFactory
 *
 * @version        1.0.0.0, Oct 26, 2010
 * @author         Moses
 */
public class CHYCAProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public CHYCAProtocolCodecFactory() {
        super.addMessageDecoder(CHYCAErrorMessageDecoder.class);
        super.addMessageDecoder(CHYCAMessageDecoder.class);
        
        super.addMessageEncoder(CHYCAOutMessage.class, CHYCAMessageEncoder.class);
    }
}
