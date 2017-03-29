package net.labthink.instrument.device.CHYCA.codec;

import net.labthink.instrument.device.CHYCA.message.CHYCAOutMessage;
import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


/**
 * CHYCA's MessageEncoder
 *
 * @version        1.0.0.0, Oct 26, 2010
 * @author         Moses
 */
public class CHYCAMessageEncoder<T extends CHYCAOutMessage> extends AbstractPacketEncoder<T> {
    /**
     * 
     */
    public CHYCAMessageEncoder() {
        //super(CommonParameters.PACKET_HEADER,CommonParameters.PACKET_TAIL);//添加包头
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
		out.put(message.getContent());
    }

    public void dispose() throws Exception {
    }
}
