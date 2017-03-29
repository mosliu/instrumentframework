package net.labthink.instrument.device.cofp01.codec;

import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;
import net.labthink.instrument.device.cofp01.message.Cofp01Message;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

/**
 * @author Moses
 * 
 * @param <T>
 */
public class Cofp01MessageEncoder<T extends Cofp01Message> extends
	AbstractPacketEncoder<T> {
    /**
     * 
     */
    public Cofp01MessageEncoder() {
	//super(CommonParameters.PACKET_HEADER, CommonParameters.PACKET_TAIL);// 添加包头
    }



    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
	out.put(message.getContent());
    }

    public void dispose() throws Exception {
    }
}
