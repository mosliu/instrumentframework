package net.labthink.instrument.device.G2131.codec;

import net.labthink.instrument.device.G2131.message.G2131InMessageParams;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

public class G2131ErrorMessageDecoder implements MessageDecoder {

    boolean readHeader = false;

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        // Return NEED_DATA if the whole header is not read yet.
//		if (in.remaining() < W3030MessageParams.PACKET_HEADER_LEN) {
//			return MessageDecoderResult.NEED_DATA;
//		}
//        short _header = in.getShort();
//        // Return OK if type and bodyLength matches.
//        if (_header==0xAAAA) {
//            return MessageDecoderResult.NEED_DATA;
//        }
        if (in.remaining() > G2131InMessageParams.PACKET_FULL_LEN) {
            return MessageDecoderResult.OK;
        } else {
            return MessageDecoderResult.NEED_DATA;
        }
    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception {
        // Try to skip header if not read.
        return MessageDecoderResult.OK;
//        if (!readHeader) {
//            in.getShort(); // 跳过 'header'.
//            readHeader = true;
//        }
//        if (in.get() == 0xBB) {
//            if (readHeader == false) {
//                readHeader = true;
//                return MessageDecoderResult.NEED_DATA;
//            } else {
//                return MessageDecoderResult.OK;
//            }
//        } else {
//            return MessageDecoderResult.NEED_DATA;
//        }
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
            throws Exception {
    }
}
