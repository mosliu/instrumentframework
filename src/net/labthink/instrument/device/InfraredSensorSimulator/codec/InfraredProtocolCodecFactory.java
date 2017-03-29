package net.labthink.instrument.device.InfraredSensorSimulator.codec;

//~--- non-JDK imports --------------------------------------------------------


import net.labthink.instrument.device.InfraredSensorSimulator.message.InfraredMessage;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * LLTEST's ProtocolCodecFactory
 *
 * @version        1.0.0.0, Sep 16, 2010
 * @author         Moses
 */
public class InfraredProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public InfraredProtocolCodecFactory() {
//        super.addMessageDecoder(LLTESTErrorMessageDecoder.class);
        super.addMessageDecoder(InfraredMessageDecoder.class);
        super.addMessageDecoder(InfraredErrorMessageDecoder.class);
        
        super.addMessageEncoder(InfraredMessage.class, InfraredMessageEncoder.class);
    }
}
