package net.labthink.instrument.device.httl1.codec;

import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;
import net.labthink.instrument.device.httl1.message.Httl1Message;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


/**
 * @author Moses
 *
 * @param <T>
 */
public class HttL1MessageEncoder<T extends Httl1Message> extends AbstractPacketEncoder<T> {
    /**
     * 
     */
    public HttL1MessageEncoder() {
        //super(Httl1MessageParams.PACKET_HEADER,Httl1MessageParams.PACKET_TAIL);//添加包头
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
	out.put(message.getContent());
    }

    public void dispose() throws Exception {
    }
}
