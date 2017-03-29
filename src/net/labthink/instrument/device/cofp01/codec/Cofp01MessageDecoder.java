package net.labthink.instrument.device.cofp01.codec;

import net.labthink.instrument.common.CommonParameters;
import net.labthink.instrument.device.base.codec.AbstractPacketDecoder;
import net.labthink.instrument.device.base.message.AbstractPacket;
import net.labthink.instrument.device.cofp01.message.Cofp01Message;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class Cofp01MessageDecoder extends AbstractPacketDecoder {

    public Cofp01MessageDecoder() {
	super(CommonParameters.PACKET_HEADER, CommonParameters.PACKET_TAIL);// 添加包头
    }

    @Override
    protected AbstractPacket decodeBody(IoSession session, IoBuffer in) {
	int length = CommonParameters.PACKET_FULL_LEN-CommonParameters.PACKET_HEADER_LEN;
	if (in.remaining() < length) {
	    // 无法读取包长度字节
	    return null;
	    // } else {
	    // in.mark();
	    // length = in.getInt();
	    // if (in.remaining() < length) {
	    // in.reset();
	    // return null;
	    // }
	}

	Cofp01Message m = new Cofp01Message();
	byte[] content = new byte[CommonParameters.PACKET_BODY_LEN];
	short tail ;
	in.get(content);
	m.setContent(content);
	//TODO tail的读取应该拿到抽象包包装
//	tail = in.getShort();
//	m.setTail(tail);
	return m;
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
	    throws Exception {
    }
}
