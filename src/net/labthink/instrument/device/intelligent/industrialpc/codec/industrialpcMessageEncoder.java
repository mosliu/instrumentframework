package net.labthink.instrument.device.intelligent.industrialpc.codec;

import net.labthink.instrument.common.CommonParameters;
import net.labthink.instrument.device.base.message.AbstractPacket;
import net.labthink.instrument.device.intelligent.industrialpc.zigbee.ZigbeePacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * industrialpc's MessageEncoder
 *
 * @version 1.0.0.0, Nov 17, 2010
 * @author Moses
 */
public class industrialpcMessageEncoder<T extends ZigbeePacket> implements MessageEncoder<T> {

    /**
     *
     */
    public void encode(IoSession session, T message, ProtocolEncoderOutput out)
            throws Exception {
        if(message==null){
            return;
        }
        int l = ((ZigbeePacket) message).getLength();

        IoBuffer buf = IoBuffer.allocate(l);
        buf.setAutoExpand(true); // Enable auto-expand for easier encoding

        // Encode a bodyc
        encodeBody(session, message, buf);

        buf.flip();
        out.write(buf);
    }

    public industrialpcMessageEncoder() {
        //super(CommonParameters.PACKET_HEADER,CommonParameters.PACKET_TAIL);//添加包头
    }

    protected void encodeBody(IoSession session, T message, IoBuffer out) {
        out.put(message.getAllPacket());
    }

    public void dispose() throws Exception {
    }
}
