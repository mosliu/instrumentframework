package net.labthink.instrument.device.LLTEST.codec;

import net.labthink.instrument.common.CommonParameters;
import net.labthink.instrument.device.LLTEST.message.LLTESTMessage;
import net.labthink.instrument.device.base.message.AbstractPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * LLTEST's MessageEncoder
 * 
 * @version 1.0.0.0, Sep 16, 2010
 * @author Moses
 */
public class LLTESTMessageEncoder<T extends LLTESTMessage> implements
		MessageEncoder<T> {
	/**
     * 
     */
	public LLTESTMessageEncoder() {
		// super(CommonParameters.PACKET_HEADER,CommonParameters.PACKET_TAIL);//添加包头
	}

	// protected void encodeBody(IoSession session, T message, IoBuffer out) {
	// out.put(message.getContent());
	// }

	public void dispose() throws Exception {
	}

	public void encode(IoSession session, T message, ProtocolEncoderOutput out)
			throws Exception {

		IoBuffer buf = IoBuffer.allocate(8);
		buf.setAutoExpand(true); // Enable auto-expand for easier encoding

		// Encode a header
		// buf.putShort(header);
		// 测试用语句
		buf.put(message.getAll());

		// Encode a bodyc
		// encodeBody(session, message, buf);

		// buf.putShort(message.getTail());
		buf.flip();
		out.write(buf);

	}
}
