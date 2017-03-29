package net.labthink.instrument.device.XLWB.codec;

import net.labthink.instrument.device.XLWB.message.XLWBMessage;
import net.labthink.instrument.device.XLWB.message.XLWBMessageParams;
import net.labthink.instrument.device.base.codec.AbstractPacketDecoder;
import net.labthink.instrument.device.base.message.AbstractPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class XLWBMessageDecoder extends AbstractPacketDecoder {

    public XLWBMessageDecoder() {
	super(XLWBMessageParams.PACKET_HEADER, XLWBMessageParams.PACKET_TAIL);// 添加包头
    }

    @Override
    protected AbstractPacket decodeBody(IoSession session, IoBuffer in) {
	int length = XLWBMessageParams.PACKET_FULL_LEN-XLWBMessageParams.PACKET_HEADER_LEN;
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

	XLWBMessage m = new XLWBMessage();
	byte[] content = new byte[XLWBMessageParams.PACKET_BODY_LEN];
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
