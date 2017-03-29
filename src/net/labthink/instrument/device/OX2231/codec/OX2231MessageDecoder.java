package net.labthink.instrument.device.OX2231.codec;

import net.labthink.instrument.device.OX2231.message.OX2231Message;
import net.labthink.instrument.device.OX2231.message.OX2231MessageParams;
import net.labthink.instrument.device.base.codec.AbstractPacketDecoder;
import net.labthink.instrument.device.base.message.AbstractPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class OX2231MessageDecoder extends AbstractPacketDecoder {

	public OX2231MessageDecoder() {
		super(OX2231MessageParams.PACKET_HEADER,
				OX2231MessageParams.PACKET_TAIL);// 添加包头
	}

	@Override
	protected AbstractPacket decodeBody(IoSession session, IoBuffer in) {
		int length = OX2231MessageParams.PACKET_FULL_LEN
				- OX2231MessageParams.PACKET_HEADER_LEN;
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

		OX2231Message m = new OX2231Message();
		byte[] content = new byte[OX2231MessageParams.PACKET_BODY_LEN];
		in.get(content);
		m.setContent(content);
		return m;
	}

	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
			throws Exception {
	}
}
