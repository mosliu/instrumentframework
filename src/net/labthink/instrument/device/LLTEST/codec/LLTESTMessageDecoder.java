package net.labthink.instrument.device.LLTEST.codec;

import net.labthink.instrument.device.LLTEST.message.LLTESTMessage;
import net.labthink.instrument.device.base.message.AbstractPacket;
import net.labthink.instrument.device.intelligent.industrialpc.message.industrialpcParams;

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
public class LLTESTMessageDecoder implements MessageDecoder {

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        // Return NEED_DATA if the whole header is not read yet.
        // if (in.remaining() < LLTESTMessageParams.PACKET_HEADER_LEN) {
        // return MessageDecoderResult.NEED_DATA;
        // }

        if (in.remaining() > 0) {
            byte a = in.get(0);

            if ((a & (byte) 0xf0) != (byte) 0xA0) {
//                return MessageDecoderResult.NOT_OK;
            }
        }

        //未取出zigbee头时，返回缺少数据，继续等待。
        if (in.remaining() < 5) {
            return MessageDecoderResult.NEED_DATA;
        }
        byte _bodylength = in.get(4);
        //如果包没有全部传输过来的话，返回缺少数据，继续等待。
        if (in.remaining() < 5 + _bodylength + 1) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            return MessageDecoderResult.OK;
        }

//
//        if (in.remaining() > 0) {
//            byte a = in.get(0);
//
//            if ((a & (byte) 0xf0) != (byte) 0xA0) {
//                return MessageDecoderResult.NOT_OK;
//            }
//        }


    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception {
        // Try to skip header if not read.
        LLTESTMessage pkt = decodeBody(session, in);
        if (pkt.CheckState()) {
            //包正确
            out.write(pkt);
            return MessageDecoderResult.OK;
        } else {
            return MessageDecoderResult.NOT_OK;
        }
    }

    protected LLTESTMessage decodeBody(IoSession session, IoBuffer in) {


        if (in.remaining() < 1) {
            // 无法读取包长度字节
            return null;
        }
        LLTESTMessage m = new LLTESTMessage();
        m.setHeader(in.get());
        m.setLqi(in.get());
        m.setAddress(in.getShort());
        m.setContentlength(in.get());
        byte[] content = new byte[m.getContentlength()];
        in.get(content);
        m.setContent(content);
        m.setXor(in.get());
        m.rebuildpacket();
        return m;
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
            throws Exception {
    }
}
