package net.labthink.instrument.device.httl1.codec;

import net.labthink.instrument.device.base.codec.AbstractPacketDecoder;
import net.labthink.instrument.device.base.message.AbstractPacket;
import net.labthink.instrument.device.httl1.message.Httl1Message;
import net.labthink.instrument.device.httl1.message.Httl1MessageParams;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class HttL1MessageDecoder extends AbstractPacketDecoder {

    public HttL1MessageDecoder() {
	super(Httl1MessageParams.PACKET_HEADER, Httl1MessageParams.PACKET_TAIL);// 添加包头
    }

    @Override
    protected AbstractPacket decodeBody(IoSession session, IoBuffer in) {
	int length = Httl1MessageParams.PACKET_FULL_LEN-Httl1MessageParams.PACKET_HEADER_LEN;
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

	Httl1Message m = new Httl1Message();
	byte[] content = new byte[Httl1MessageParams.PACKET_BODY_LEN];
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
