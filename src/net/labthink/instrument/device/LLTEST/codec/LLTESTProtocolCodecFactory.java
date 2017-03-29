package net.labthink.instrument.device.LLTEST.codec;

//~--- non-JDK imports --------------------------------------------------------


import net.labthink.instrument.device.LLTEST.message.LLTESTMessage;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * LLTEST's ProtocolCodecFactory
 *
 * @version        1.0.0.0, Sep 16, 2010
 * @author         Moses
 */
public class LLTESTProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public LLTESTProtocolCodecFactory() {
//        super.addMessageDecoder(LLTESTErrorMessageDecoder.class);
        super.addMessageDecoder(LLTESTMessageDecoder.class);
        
        super.addMessageEncoder(LLTESTMessage.class, LLTESTMessageEncoder.class);
    }
}
