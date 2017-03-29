package net.labthink.instrument.device.VACVBS.codec;

//~--- non-JDK imports --------------------------------------------------------


import net.labthink.instrument.device.VACVBS.message.VACVBSOutMessage;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * VACVBS's ProtocolCodecFactory
 *
 * @version        1.0.0.0, 2010/08/20
 * @author         Moses
 */
public class VACVBSProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public VACVBSProtocolCodecFactory() {
        super.addMessageDecoder(VACVBSErrorMessageDecoder.class);
        super.addMessageDecoder(VACVBSMessageDecoder.class);
        
        super.addMessageEncoder(VACVBSOutMessage.class, VACVBSMessageEncoder.class);
    }
}
