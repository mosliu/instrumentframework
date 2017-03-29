package net.labthink.instrument.device.VACVBS.codec;

import net.labthink.instrument.device.VACVBS.message.VACVBSOutMessage;
import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


/**
 * VACVBS's MessageEncoder
 *
 * @version        1.0.0.0, 2010/08/20
 * @author         Moses
 */
public class VACVBSMessageEncoder<T extends VACVBSOutMessage> extends AbstractPacketEncoder<T> {
    /**
     * 
     */
    public VACVBSMessageEncoder() {
        //super(CommonParameters.PACKET_HEADER,CommonParameters.PACKET_TAIL);//添加包头
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
		out.put(message.getContent());
    }

    public void dispose() throws Exception {
    }
}
