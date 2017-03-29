package net.labthink.instrument.device.XLWB.codec;

//~--- non-JDK imports --------------------------------------------------------

import net.labthink.instrument.device.XLWB.message.XLWBMessage;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * A {@link ProtocolCodecFactory} that provides a protocol codec for SumUp
 * protocol.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class XLWBProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public XLWBProtocolCodecFactory() {
        super.addMessageDecoder(XLWBMessageDecoder.class);
        super.addMessageEncoder(XLWBMessage.class, XLWBMessageEncoder.class);
    }
}
