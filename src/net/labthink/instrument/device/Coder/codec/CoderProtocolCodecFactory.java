package net.labthink.instrument.device.Coder.codec;

//~--- non-JDK imports --------------------------------------------------------


import net.labthink.instrument.device.Coder.message.CoderMessage;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * LLTEST's ProtocolCodecFactory
 *
 * @version        1.0.0.0, Sep 16, 2010
 * @author         Moses
 */
public class CoderProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public CoderProtocolCodecFactory() {
//        super.addMessageDecoder(LLTESTErrorMessageDecoder.class);
        super.addMessageDecoder(CoderMessageDecoder.class);
        super.addMessageDecoder(CoderMessageDecoder.class);
        
        super.addMessageEncoder(CoderMessage.class, CoderMessageEncoder.class);
    }
}
