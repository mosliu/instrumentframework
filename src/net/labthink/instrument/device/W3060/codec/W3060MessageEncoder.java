package net.labthink.instrument.device.W3060.codec;

import net.labthink.instrument.device.W3060.message.W3060OutMessage;
import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


/**
 * @author Moses
 *
 * @param <T>
 */
public class W3060MessageEncoder<T extends W3060OutMessage> extends AbstractPacketEncoder<T> {
    /**
     * 
     */
    public W3060MessageEncoder() {
        //super(CommonParameters.PACKET_HEADER,CommonParameters.PACKET_TAIL);//添加包头
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
	out.put(message.getContent());
    }

    public void dispose() throws Exception {
    }
}
