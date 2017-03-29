package net.labthink.instrument.device.MXD02.codec;

import net.labthink.instrument.device.MXD02.message.MXD02OutMessage;
import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


/**
 * MXD02's MessageEncoder
 *
 * @version        1.0.0.0, Jun 13, 2011
 * @author         Moses
 */
public class MXD02MessageEncoder<T extends MXD02OutMessage> extends AbstractPacketEncoder<T> {
    /**
     * 
     */
    public MXD02MessageEncoder() {
        //super(CommonParameters.PACKET_HEADER,CommonParameters.PACKET_TAIL);//添加包头
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
		out.put(message.getContent());
    }

    public void dispose() throws Exception {
    }
}
