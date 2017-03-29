package net.labthink.instrument.device.w3030.codec;

import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;
import net.labthink.instrument.device.w3030.message.W3030Message;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


/**
 * @author Moses
 *
 * @param <T>
 */
public class W3030MessageEncoder<T extends W3030Message> extends AbstractPacketEncoder<T> {
    /**
     * 
     */
    public W3030MessageEncoder() {
        //super(CommonParameters.PACKET_HEADER,CommonParameters.PACKET_TAIL);//添加包头
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
	out.put(message.getContent());
    }

    public void dispose() throws Exception {
    }
}
