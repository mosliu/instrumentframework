package net.labthink.instrument.device.InfraredSensorSimulator.codec;


import net.labthink.instrument.device.InfraredSensorSimulator.message.InfraredMessage;
import net.labthink.instrument.device.InfraredSensorSimulator.message.InfraredMessageParams;
import net.labthink.instrument.device.base.codec.AbstractPacketDecoder;
import net.labthink.instrument.device.base.message.AbstractPacket;
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
public class InfraredMessageDecoder extends AbstractPacketDecoder  {

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        // Return NEED_DATA if the whole header is not read yet.
        // if (in.remaining() < LLTESTMessageParams.PACKET_HEADER_LEN) {
        // return MessageDecoderResult.NEED_DATA;
        // }
        
        if (in.remaining() < InfraredMessageParams.PACKET_HEADER_LEN) {	    
	    return MessageDecoderResult.NEED_DATA;
	}
	
		
        if(in.remaining() < InfraredMessageParams.PACKET_FULL_LEN){
            return MessageDecoderResult.NEED_DATA;
        }
        
        
        
        short _header = in.getShort();
        // Return OK if type and bodyLength matches.
        if(header != _header){
            //设计：将多余的字节数从buffer中移除
	    in.clear();
	    return MessageDecoderResult.NEED_DATA;
	}
        if (header == _header&&tail == in.getShort(InfraredMessageParams.PACKET_FULL_LEN-InfraredMessageParams.PACKET_TAIL_LEN)) {
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
        pkt.setLength(InfraredMessageParams.PACKET_FULL_LEN);
        out.write(pkt);

        return MessageDecoderResult.OK;
    }

    protected InfraredMessage decodeBody(IoSession session, IoBuffer in) {
        if (in.remaining() < 1) {
            // 无法读取包长度字节
            return null;
        }
        int length = InfraredMessageParams.PACKET_FULL_LEN-InfraredMessageParams.PACKET_HEADER_LEN;
	if (in.remaining() < length) {
	    // 无法读取包长度字节
	    return null;
        }

        
        InfraredMessage m = new InfraredMessage();
        
        byte[] content = new byte[InfraredMessageParams.PACKET_BODY_LEN];
        in.get(content);
	m.setContent(content);
        return m;
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
            throws Exception {
    }
}
