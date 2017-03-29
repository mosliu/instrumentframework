package net.labthink.instrument.device.${devicename}.codec;

import net.labthink.instrument.device.${devicename}.message.${devicename}OutMessage;
import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


/**
 * ${devicename}'s MessageEncoder
 *
 * @version        ${version}, ${pp.now?date}
 * @author         ${author?cap_first}
 */
public class ${devicename}MessageEncoder<T extends ${devicename}OutMessage> extends AbstractPacketEncoder<T> {
    /**
     * 
     */
    public ${devicename}MessageEncoder() {
        //super(CommonParameters.PACKET_HEADER,CommonParameters.PACKET_TAIL);//添加包头
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
		out.put(message.getContent());
    }

    public void dispose() throws Exception {
    }
}
