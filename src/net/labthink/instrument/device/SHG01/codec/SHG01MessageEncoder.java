package net.labthink.instrument.device.SHG01.codec;

import net.labthink.instrument.device.SHG01.message.SHG01OutMessage;
import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


/**
 * @author Moses
 *
 * @param <T>
 */
public class SHG01MessageEncoder<T extends SHG01OutMessage> extends AbstractPacketEncoder<T> {
    /**
     * 
     */
    public SHG01MessageEncoder() {
        //super(CommonParameters.PACKET_HEADER,CommonParameters.PACKET_TAIL);//添加包头
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
	out.put(message.getContent());
    }

    public void dispose() throws Exception {
    }
}
