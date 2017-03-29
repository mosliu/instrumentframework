package net.labthink.instrument.device.OX2230W3330.codec;

import net.labthink.instrument.device.OX2230W3330.message.OX2230W3330OutMessage;
import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


/**
 * @author Moses
 *
 * @param <T>
 */
public class OX2230W3330MessageEncoder<T extends OX2230W3330OutMessage> extends AbstractPacketEncoder<T> {
    /**
     * 
     */
    public OX2230W3330MessageEncoder() {
        //super(CommonParameters.PACKET_HEADER,CommonParameters.PACKET_TAIL);//添加包头
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
	out.put(message.getContent());
    }

    public void dispose() throws Exception {
    }
}
