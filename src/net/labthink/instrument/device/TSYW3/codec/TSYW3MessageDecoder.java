package net.labthink.instrument.device.TSYW3.codec;

import net.labthink.instrument.device.TSYW3.message.TSYW3Message;
import net.labthink.instrument.device.TSYW3.message.TSYW3MessageParams;
import net.labthink.instrument.device.base.codec.AbstractPacketDecoder;
import net.labthink.instrument.device.base.message.AbstractPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class TSYW3MessageDecoder extends AbstractPacketDecoder {

    public TSYW3MessageDecoder() {
	super(TSYW3MessageParams.PACKET_HEADER, TSYW3MessageParams.PACKET_TAIL);// 添加包头
    }

    @Override
    protected AbstractPacket decodeBody(IoSession session, IoBuffer in) {
	int length = TSYW3MessageParams.PACKET_FULL_LEN-TSYW3MessageParams.PACKET_HEADER_LEN;
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

	TSYW3Message m = new TSYW3Message();
	byte[] content = new byte[TSYW3MessageParams.PACKET_BODY_LEN];
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
