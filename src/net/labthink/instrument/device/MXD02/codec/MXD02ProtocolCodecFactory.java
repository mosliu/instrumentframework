package net.labthink.instrument.device.MXD02.codec;

//~--- non-JDK imports --------------------------------------------------------


import net.labthink.instrument.device.MXD02.message.MXD02OutMessage;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * MXD02's ProtocolCodecFactory
 *
 * @version        1.0.0.0, Jun 13, 2011
 * @author         Moses
 */
public class MXD02ProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public MXD02ProtocolCodecFactory() {
        super.addMessageDecoder(MXD02ErrorMessageDecoder.class);
        super.addMessageDecoder(MXD02MessageDecoder.class);
        
        super.addMessageEncoder(MXD02OutMessage.class, MXD02MessageEncoder.class);
    }
}
