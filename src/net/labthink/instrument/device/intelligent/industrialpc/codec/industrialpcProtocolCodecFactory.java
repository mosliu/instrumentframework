package net.labthink.instrument.device.intelligent.industrialpc.codec;

//~--- non-JDK imports --------------------------------------------------------


import net.labthink.instrument.device.intelligent.industrialpc.zigbee.ZigbeePacket;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * industrialpc's ProtocolCodecFactory
 *
 * @version        1.0.0.0, Nov 17, 2010
 * @author         Moses
 */
public class industrialpcProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public industrialpcProtocolCodecFactory() {
        super.addMessageDecoder(industrialpcErrorMessageDecoder.class);
        super.addMessageDecoder(industrialpcMessageDecoder.class);
        
        
        super.addMessageEncoder(ZigbeePacket.class, industrialpcMessageEncoder.class);
    }
}
