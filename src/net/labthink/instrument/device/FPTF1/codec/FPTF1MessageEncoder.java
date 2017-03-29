package net.labthink.instrument.device.FPTF1.codec;

import net.labthink.instrument.device.FPTF1.message.FPTF1OutMessage;
import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


/**
 * FPTF1's MessageEncoder
 *
 * @version        1.0.0.0, Aug 24, 2010
 * @author         Moses
 */
public class FPTF1MessageEncoder<T extends FPTF1OutMessage> extends AbstractPacketEncoder<T> {
    /**
     * 
     */
    public FPTF1MessageEncoder() {
        //super(CommonParameters.PACKET_HEADER,CommonParameters.PACKET_TAIL);//添加包头
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
		out.put(message.getContent());
    }

    public void dispose() throws Exception {
    }
}
