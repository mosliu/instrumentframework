package net.labthink.instrument.device.Coder.codec;

import net.labthink.instrument.device.Coder.message.CoderMessage;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * LLTEST's MessageDecoder
 *
 * @version 1.0.0.0, Sep 16, 2010
 * @author Moses
 */
public class CoderMessageDecoder implements MessageDecoder {

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        // Return NEED_DATA if the whole header is not read yet.
        // if (in.remaining() < LLTESTMessageParams.PACKET_HEADER_LEN) {
        // return MessageDecoderResult.NEED_DATA;
        // }
        if (in.remaining() < 6) {
            return MessageDecoderResult.NEED_DATA;
        }

        byte a = in.get();
        if (a != (byte)0xFB) {
            return MessageDecoderResult.NOT_OK;
        }




        return MessageDecoderResult.OK;

    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception {
        // Try to skip header if not read.
        CoderMessage pkt = decodeBody(session, in);
//        if (pkt.CheckState()) {
        if (true) {
            //包正确
            out.write(pkt);
            return MessageDecoderResult.OK;
        } else {
            return MessageDecoderResult.NOT_OK;
        }
    }

    protected CoderMessage decodeBody(IoSession session, IoBuffer in) {


        if (in.remaining() < 1) {
            // 无法读取包长度字节
            return null;
        }
        CoderMessage m = new CoderMessage();
        byte head = in.get();//FB头
        if (head != (byte) 0xfb) {
        }
        byte[] content = new byte[m.getContentlength()];
        in.get(content);
        m.setContent(content);
        byte tail = in.get();
        m.setTail(tail);

        return m;
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
            throws Exception {
    }
}
