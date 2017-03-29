package net.labthink.instrument.device.TSYW3.codec;

import net.labthink.instrument.device.TSYW3.message.TSYW3Message;
import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


/**
 * @author Moses
 *
 * @param <T>
 */
public class TSYW3MessageEncoder<T extends TSYW3Message> extends AbstractPacketEncoder<T> {
    /**
     * 
     */
    public TSYW3MessageEncoder() {
        //super(TSYW3MessageParams.PACKET_HEADER,TSYW3MessageParams.PACKET_TAIL);//添加包头
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
	out.put(message.getContent());
    }

    public void dispose() throws Exception {
    }
}
