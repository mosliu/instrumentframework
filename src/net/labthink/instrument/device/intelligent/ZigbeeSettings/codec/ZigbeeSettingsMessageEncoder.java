package net.labthink.instrument.device.intelligent.ZigbeeSettings.codec;

import net.labthink.instrument.device.intelligent.ZigbeeSettings.codec.*;
import net.labthink.instrument.common.CommonParameters;
import net.labthink.instrument.device.base.message.AbstractPacket;
import net.labthink.instrument.device.intelligent.ZigbeeSettings.ZigbeeSettingsPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * ZigbeeSettings's MessageEncoder
 *
 * @version 1.0.0.0, Nov 17, 2010
 * @author Moses
 */
public class ZigbeeSettingsMessageEncoder<T extends ZigbeeSettingsPacket> implements MessageEncoder<T> {

    /**
     *
     */
    public void encode(IoSession session, T message, ProtocolEncoderOutput out)
            throws Exception {
        if(message==null){
            return;
        }
        int l = ((ZigbeeSettingsPacket) message).getLength();

        IoBuffer buf = IoBuffer.allocate(l);
        buf.setAutoExpand(true); // Enable auto-expand for easier encoding

        // Encode a bodyc
        encodeBody(session, message, buf);

        buf.flip();
        out.write(buf);
    }

    public ZigbeeSettingsMessageEncoder() {
        //super(CommonParameters.PACKET_HEADER,CommonParameters.PACKET_TAIL);//添加包头
    }

    protected void encodeBody(IoSession session, T message, IoBuffer out) {
        out.put(message.getAllPacket());
    }

    public void dispose() throws Exception {
    }
}
