package net.labthink.instrument.device.OxygenTransducer.codec;

import net.labthink.instrument.device.OxygenTransducer.message.OxygenTransducerInMessage;
import net.labthink.instrument.device.OxygenTransducer.message.OxygenTransducerInMessageParams;
import net.labthink.instrument.device.base.codec.AbstractPacketDecoder;
import net.labthink.instrument.device.base.message.AbstractPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * OxygenTransducer's MessageDecoder
 *
 * @version        1.0.0.0, Jul 29, 2011
 * @author         Moses
 */
public class OxygenTransducerMessageDecoder extends AbstractPacketDecoder {

    public OxygenTransducerMessageDecoder() {
    }

    @Override
    protected AbstractPacket decodeBody(IoSession session, IoBuffer in) {
	int length = OxygenTransducerInMessageParams.PACKET_FULL_LEN-OxygenTransducerInMessageParams.PACKET_HEADER_LEN;
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

	OxygenTransducerInMessage m = new OxygenTransducerInMessage();
	byte[] content = new byte[OxygenTransducerInMessageParams.PACKET_BODY_LEN];
	in.get(content);
	m.setContent(content);
//        if(m.CheckState()==false){
//            return null;
//        }

	//TODO tail的读取应该拿到抽象包包装
//	tail = in.getShort();
//	m.setTail(tail);
	return m;
    }

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        // Return NEED_DATA if the whole header is not read yet.
//	if (in.remaining() < OxygenTransducerInMessageParams.PACKET_HEADER_LEN) {
//	    return MessageDecoderResult.NEED_DATA;
//	}
	
		
        if(in.remaining() < OxygenTransducerInMessageParams.PACKET_FULL_LEN){
            return MessageDecoderResult.NEED_DATA;
        }else{
            return MessageDecoderResult.OK;
        }

    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception {

        // Try to decode body
        AbstractPacket pkt = decodeBody(session, in);
        // Return NEED_DATA if the body is not fully read.
        if (pkt == null) {
            return MessageDecoderResult.NEED_DATA;
        }
        
        pkt.setLength(OxygenTransducerInMessageParams.PACKET_FULL_LEN);
        out.write(pkt);

        return MessageDecoderResult.OK;
    }
    
    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
	    throws Exception {
    }
}
