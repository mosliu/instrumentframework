package net.labthink.instrument.device.${devicename}.codec;

//~--- non-JDK imports --------------------------------------------------------


import net.labthink.instrument.device.${devicename}.message.${devicename}OutMessage;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * ${devicename}'s ProtocolCodecFactory
 *
 * @version        ${version}, ${pp.now?date}
 * @author         ${author?cap_first}
 */
public class ${devicename}ProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public ${devicename}ProtocolCodecFactory() {
        super.addMessageDecoder(${devicename}ErrorMessageDecoder.class);
        super.addMessageDecoder(${devicename}MessageDecoder.class);
        
        super.addMessageEncoder(${devicename}OutMessage.class, ${devicename}MessageEncoder.class);
    }
}
