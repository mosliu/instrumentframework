package net.labthink.instrument.device.CommandSender.codec;

import net.labthink.instrument.common.CommonParameters;
import net.labthink.instrument.device.CommandSender.message.CommandSenderOutMessage;
import net.labthink.instrument.device.base.codec.AbstractPacketEncoder;
import net.labthink.instrument.device.base.message.AbstractPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * CommandSender's MessageEncoder
 *
 * @version 1.0.0.0, Oct 26, 2010
 * @author Moses
 */
public class CommandSenderMessageEncoder<T extends CommandSenderOutMessage> extends AbstractPacketEncoder<T> {

    /**
     *
     */
    public CommandSenderMessageEncoder() {
        //super(CommonParameters.PACKET_HEADER,CommonParameters.PACKET_TAIL);//添加包头
    }

    @Override
    protected void encodeBody(IoSession session, CommandSenderOutMessage message, IoBuffer out) {
        out.put(message.getContent());
    }

    public void encode(IoSession session, CommandSenderOutMessage message, ProtocolEncoderOutput out)
            throws Exception {
        IoBuffer buf = IoBuffer.allocate(message.getContent().length);
        buf.setAutoExpand(true); // Enable auto-expand for easier encoding

        // Encode a header
        // buf.putShort(header);
        // 测试用语句
//        buf.putShort(message.getHeader());

        // Encode a bodyc
        encodeBody(session, message, buf);

//        buf.putShort(message.getTail());
        buf.flip();
        out.write(buf);
    }

    public void dispose() throws Exception {
    }
}
