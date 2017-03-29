package net.labthink.instrument.device.w3030.codec;

import net.labthink.instrument.device.base.codec.AbstractPacketDecoder;
import net.labthink.instrument.device.base.message.AbstractPacket;
import net.labthink.instrument.device.w3030.message.W3030Message;
import net.labthink.instrument.device.w3030.message.W3030MessageParams;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

public class W3030MessageDecoder extends AbstractPacketDecoder {

	public W3030MessageDecoder() {
		super(W3030MessageParams.PACKET_HEADER, W3030MessageParams.PACKET_TAIL);// 添加包头
	}

	@Override
	protected AbstractPacket decodeBody(IoSession session, IoBuffer in) {
		int length = W3030MessageParams.PACKET_FULL_LEN
				- W3030MessageParams.PACKET_HEADER_LEN;
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

		W3030Message m = new W3030Message();
		byte[] content = new byte[W3030MessageParams.PACKET_BODY_LEN];
		short tail;
		in.get(content);
		m.setContent(content);
		return m;
	}

	public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
		// Return NEED_DATA if the whole header is not read yet.
//		if (in.remaining() < W3030MessageParams.PACKET_HEADER_LEN) {
//			return MessageDecoderResult.NEED_DATA;
//		}

		if (in.remaining() < W3030MessageParams.PACKET_FULL_LEN) {
			return MessageDecoderResult.NEED_DATA;
		}

		short _header = in.getShort();
		// Return OK if type and bodyLength matches.
		if (header != _header) {
			// 设计：将多余的字节数从buffer中移除
			in.clear();
			return MessageDecoderResult.NEED_DATA;
		}
		if (header == _header
				&& tail == in.getShort(W3030MessageParams.PACKET_FULL_LEN
						- W3030MessageParams.PACKET_TAIL_LEN)) {
			return MessageDecoderResult.OK;
		}

		// Return NOT_OK if not matches.
		return MessageDecoderResult.NOT_OK;
	}

	public MessageDecoderResult decode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		// Try to skip header if not read.
		if (!readHeader) {
			in.getShort(); // 跳过 'header'.
			readHeader = true;
		}

		// Try to decode body
		AbstractPacket pkt = decodeBody(session, in);
		// Return NEED_DATA if the body is not fully read.
		if (pkt == null) {
			return MessageDecoderResult.NEED_DATA;
		} else {
			readHeader = false; // reset readHeader for the next decode
		}
		in.getShort(); // 跳过 'tail'.

		pkt.setHeader(header);
		pkt.setTail(tail);
		pkt.setLength(W3030MessageParams.PACKET_FULL_LEN);
		out.write(pkt);

		return MessageDecoderResult.OK;
	}

	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
			throws Exception {
	}
}
