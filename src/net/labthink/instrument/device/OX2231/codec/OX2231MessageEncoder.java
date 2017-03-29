package net.labthink.instrument.device.OX2231.codec;

import net.labthink.instrument.device.OX2231.message.OX2231Message;
import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


/**
 * @author Moses
 *
 * @param <T>
 */
public class OX2231MessageEncoder<T extends OX2231Message> extends AbstractPacketEncoder<T> {
    /**
     * 
     */
    public OX2231MessageEncoder() {
        //super(OX2231MessageParams.PACKET_HEADER,OX2231MessageParams.PACKET_TAIL);//添加包头
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
	out.put(message.getContent());
    }

    public void dispose() throws Exception {
    }
}
