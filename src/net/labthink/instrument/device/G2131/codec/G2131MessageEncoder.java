package net.labthink.instrument.device.G2131.codec;

import net.labthink.instrument.device.G2131.message.G2131OutMessage;
import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


/**
 * @author Moses
 *
 * @param <T>
 */
public class G2131MessageEncoder<T extends G2131OutMessage> extends AbstractPacketEncoder<T> {
    /**
     * 
     */
    public G2131MessageEncoder() {
        //super(CommonParameters.PACKET_HEADER,CommonParameters.PACKET_TAIL);//添加包头
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
	out.put(message.getContent());
    }

    public void dispose() throws Exception {
    }
}
