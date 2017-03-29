package net.labthink.instrument.device.OxygenTransducer.codec;

//~--- non-JDK imports --------------------------------------------------------


import net.labthink.instrument.device.OxygenTransducer.message.OxygenTransducerOxyOutMessage;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * OxygenTransducer's ProtocolCodecFactory
 *
 * @version        1.0.0.0, Jul 29, 2011
 * @author         Moses
 */
public class OxygenTransducerOxyProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public OxygenTransducerOxyProtocolCodecFactory() {
        super.addMessageDecoder(OxygenTransducerOxyErrorMessageDecoder.class);
        super.addMessageDecoder(OxygenTransducerOxyMessageDecoder.class);
        
        super.addMessageEncoder(OxygenTransducerOxyOutMessage.class, OxygenTransducerOxyMessageEncoder.class);
    }
}
