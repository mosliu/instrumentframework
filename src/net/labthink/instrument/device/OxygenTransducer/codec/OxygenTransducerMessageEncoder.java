package net.labthink.instrument.device.OxygenTransducer.codec;

import net.labthink.instrument.common.CommonParameters;
import net.labthink.instrument.device.OxygenTransducer.message.OxygenTransducerOutMessage;
import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;
import net.labthink.instrument.device.base.message.AbstractPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * OxygenTransducer's MessageEncoder
 *
 * @version        1.0.0.0, Jul 29, 2011
 * @author         Moses
 */
public class OxygenTransducerMessageEncoder<T extends OxygenTransducerOutMessage> extends AbstractPacketEncoder<T> {

    /**
     * 
     */
    public OxygenTransducerMessageEncoder() {
        //super(CommonParameters.PACKET_HEADER,CommonParameters.PACKET_TAIL);//添加包头
    }

    @Override
    public void encode(IoSession session, T message, ProtocolEncoderOutput out)
            throws Exception {
        int l = ((AbstractPacket) message).getLength() != 0 ? ((AbstractPacket) message).getLength()
                : CommonParameters.PACKET_BODY_LEN;

        IoBuffer buf = IoBuffer.allocate(l);
        buf.setAutoExpand(true); // Enable auto-expand for easier encoding

      

        // Encode a bodyc
        encodeBody(session, message, buf);

        buf.flip();
        out.write(buf);
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
        out.put(message.getContent());
    }

    public void dispose() throws Exception {
    }
}
