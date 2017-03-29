package net.labthink.instrument.device.OxygenTransducer.codec;

//~--- non-JDK imports --------------------------------------------------------


import net.labthink.instrument.device.OxygenTransducer.message.OxygenTransducerOutMessage;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * OxygenTransducer's ProtocolCodecFactory
 *
 * @version        1.0.0.0, Jul 29, 2011
 * @author         Moses
 */
public class OxygenTransducerProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public OxygenTransducerProtocolCodecFactory() {
        super.addMessageDecoder(OxygenTransducerErrorMessageDecoder.class);
        super.addMessageDecoder(OxygenTransducerMessageDecoder.class);
        
        super.addMessageEncoder(OxygenTransducerOutMessage.class, OxygenTransducerMessageEncoder.class);
    }
}
