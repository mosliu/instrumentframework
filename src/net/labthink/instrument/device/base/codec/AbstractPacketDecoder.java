package net.labthink.instrument.device.base.codec;

import net.labthink.instrument.common.CommonParameters;
import net.labthink.instrument.device.base.message.AbstractPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

public abstract class AbstractPacketDecoder implements MessageDecoder {

    protected final short header;
    protected final short tail;
    protected boolean hasheader = true;
    protected boolean hastail = true;
    protected boolean readHeader = false;

    /**
     * 初始化一个decoder
     *
     * @param header
     *            包头
     * @param tail
     *            包尾
     */
    protected AbstractPacketDecoder(short header, short tail) {
        this.header = header;
        this.tail = tail;
    }

    /**
     * 初始化一个decoder
     *
     * @param header
     *            包头
     * @param tail
     *            包尾
     */
    protected AbstractPacketDecoder(short header) {
        this.header = header;
        this.tail = 0;
        this.hastail = false;
    }

    /**
     * 使用默认初始化参数
     */
    public AbstractPacketDecoder() {
        this(CommonParameters.PACKET_HEADER, CommonParameters.PACKET_TAIL);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.mina.filter.codec.demux.MessageDecoder#decodable(org.apache
     * .mina.core.session.IoSession, org.apache.mina.core.buffer.IoBuffer)
     */
    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        // Return NEED_DATA if the whole header is not read yet.
        // if (in.remaining() < CommonParameters.PACKET_HEADER_LEN) {
        // return MessageDecoderResult.NEED_DATA;
        // }

        if (in.remaining() < CommonParameters.PACKET_FULL_LEN) {
            return MessageDecoderResult.NEED_DATA;
        }

        short _header = in.getShort();
        // Return OK if type and bodyLength matches.
        if (header != _header) {
            // 设计：将多余的字节数从buffer中移除
            in.clear();
            return MessageDecoderResult.NEED_DATA;
        }
        if (header == _header) {
            if (hastail) {
                if (tail == in.getShort(11)) {
                    return MessageDecoderResult.OK;
                }
            } else {
                return MessageDecoderResult.OK;
            }
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
        pkt.setLength(CommonParameters.PACKET_FULL_LEN);
        out.write(pkt);

        return MessageDecoderResult.OK;
    }

    /**
     * @return <tt>null</tt> if the whole body is not read yet
     */
    protected abstract AbstractPacket decodeBody(IoSession session, IoBuffer in);
}
