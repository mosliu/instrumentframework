package net.labthink.instrument.device.LLTEST.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * LLTEST's ErrorMessageDecoder 当有错误帧时，抛弃错误帧。
 * 
 * @version 1.0.0.0, Sep 16, 2010
 * @author Moses
 */
public class LLTESTErrorMessageDecoder implements MessageDecoder {

	boolean readHeader = false;

	public MessageDecoderResult decodable(IoSession session, IoBuffer in) {

		return MessageDecoderResult.OK;
	}

	public MessageDecoderResult decode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		// Try to skip header if not read.
		return MessageDecoderResult.OK;
		// if (!readHeader) {
		// in.getShort(); // 跳过 'header'.
		// readHeader = true;
		// }
		// if (in.get() == 0xBB) {
		// if (readHeader == false) {
		// readHeader = true;
		// return MessageDecoderResult.NEED_DATA;
		// } else {
		// return MessageDecoderResult.OK;
		// }
		// } else {
		// return MessageDecoderResult.NEED_DATA;
		// }
	}

	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
			throws Exception {
	}
}
