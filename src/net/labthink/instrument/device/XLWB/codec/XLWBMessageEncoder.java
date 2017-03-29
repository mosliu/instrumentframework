package net.labthink.instrument.device.XLWB.codec;

import net.labthink.instrument.common.CommonParameters;
import net.labthink.instrument.device.XLWB.message.XLWBMessage;
import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;
import net.labthink.instrument.device.base.message.AbstractPacket;
import net.labthink.utils.BytePlus;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * @author Moses
 * 
 * @param <T>
 */
public class XLWBMessageEncoder<T extends XLWBMessage> extends
		AbstractPacketEncoder<T> {
	/**
     * 
     */
	public XLWBMessageEncoder() {
		// super(XLWBMessageParams.PACKET_HEADER,XLWBMessageParams.PACKET_TAIL);//添加包头
	}

	@Override
	protected void encodeBody(IoSession session, T message, IoBuffer out) {
		out.put(message.getContent());
	}

	public void dispose() throws Exception {
	}
	@Override
	public void encode(IoSession session, T message, ProtocolEncoderOutput out)
			throws Exception {
		int l = ((AbstractPacket) message).getLength() != 0 ? ((AbstractPacket) message)
				.getLength()
				: CommonParameters.PACKET_BODY_LEN;

		IoBuffer buf = IoBuffer.allocate(l);
		buf.setAutoExpand(true); // Enable auto-expand for easier encoding

		// Encode a header
		// buf.putShort(header);
		// 测试用语句
		short a =0;
		byte header = 0;
		header = (BytePlus.int2bytes(message.getHeader())[3]);
		buf.put(header);

		// Encode a bodyc
		encodeBody(session, message, buf);
		buf.flip();
		out.write(buf);
	}

}
