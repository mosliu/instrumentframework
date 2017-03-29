package net.labthink.instrument.device.intelligent.ZigbeeSettings.codec;

//~--- non-JDK imports --------------------------------------------------------


import net.labthink.instrument.device.intelligent.ZigbeeSettings.ZigbeeSettingsPacket;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * ZigbeeSettings's ProtocolCodecFactory
 *
 * @version        1.0.0.0, Nov 17, 2010
 * @author         Moses
 */
public class ZigbeeSettingsProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public ZigbeeSettingsProtocolCodecFactory() {
//        super.addMessageDecoder(ZigbeeSettingsErrorMessageDecoder.class);
        super.addMessageDecoder(ZigbeeSettingsMessageDecoder.class);
        
        
        super.addMessageEncoder(ZigbeeSettingsPacket.class, ZigbeeSettingsMessageEncoder.class);
    }
}
