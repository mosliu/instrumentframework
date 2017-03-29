package net.labthink.instrument.device.SHG01.codec;

import net.labthink.instrument.device.SHG01.message.SHG01InMessage;
import net.labthink.instrument.device.SHG01.message.SHG01InMessageParams;
import net.labthink.instrument.device.base.codec.AbstractPacketDecoder;
import net.labthink.instrument.device.base.message.AbstractPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

public class SHG01MessageDecoder extends AbstractPacketDecoder {

    public SHG01MessageDecoder() {
	super(SHG01InMessageParams.PACKET_HEADER, SHG01InMessageParams.PACKET_TAIL);// 添加包头
    }

    @Override
    protected AbstractPacket decodeBody(IoSession session, IoBuffer in) {
	int length = SHG01InMessageParams.PACKET_FULL_LEN-SHG01InMessageParams.PACKET_HEADER_LEN;
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

	SHG01InMessage m = new SHG01InMessage();
	byte[] content = new byte[SHG01InMessageParams.PACKET_BODY_LEN];
	short tail ;
	in.get(content);
	m.setContent(content);
	//TODO tail的读取应该拿到抽象包包装
//	tail = in.getShort();
//	m.setTail(tail);
	return m;
    }

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        // Return NEED_DATA if the whole header is not read yet.
//	if (in.remaining() < SHG01InMessageParams.PACKET_HEADER_LEN) {
//	    return MessageDecoderResult.NEED_DATA;
//	}
	
		
        if(in.remaining() < SHG01InMessageParams.PACKET_FULL_LEN){
            return MessageDecoderResult.NEED_DATA;
        }
        
        
        
        short _header = in.getShort();
        // Return OK if type and bodyLength matches.
        if(header != _header){
            //设计：将多余的字节数从buffer中移除
	    in.clear();
	    return MessageDecoderResult.NEED_DATA;
	}
        if (header == _header&&tail == in.getShort(SHG01InMessageParams.PACKET_FULL_LEN-SHG01InMessageParams.PACKET_TAIL_LEN)) {
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
        pkt.setLength(SHG01InMessageParams.PACKET_FULL_LEN);
        out.write(pkt);

        return MessageDecoderResult.OK;
    }
    
    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
	    throws Exception {
    }
}
