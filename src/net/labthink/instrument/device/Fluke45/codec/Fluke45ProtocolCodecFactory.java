package net.labthink.instrument.device.Fluke45.codec;

//~--- non-JDK imports --------------------------------------------------------


import net.labthink.instrument.device.Fluke45.packet.Fluke45Pkt;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 *
 * @version        1.0.0.0
 * @author         Moses
 */
public class Fluke45ProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public Fluke45ProtocolCodecFactory() {
        super.addMessageDecoder(Fluke45MessageDecoder.class);
        super.addMessageEncoder(Fluke45Pkt.class, Fluke45MessageEncoder.class);
    }
}
