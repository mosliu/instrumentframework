package net.labthink.instrument.device.intelligent.industrialpc.codec;

import net.labthink.instrument.device.intelligent.industrialpc.message.industrialpcParams;
import net.labthink.instrument.device.base.message.AbstractPacket;
import net.labthink.instrument.device.intelligent.industrialpc.zigbee.ZigbeePacket;
import net.labthink.utils.BytePlus;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * industrialpc's MessageDecoder
 *
 * @version 1.0.0.0, Nov 17, 2010
 * @author Moses
 */
public class industrialpcMessageDecoder implements MessageDecoder {

    private static byte START = (byte) 0xA0; //判断包头
    private static byte END = (byte) 0xAF;//判断包头
    public static boolean badhead = false;

    public industrialpcMessageDecoder() {
//	super(industrialpcParams.PACKET_HEADER, industrialpcParams.PACKET_TAIL);// 添加包头
    }

    protected ZigbeePacket decodeBody(IoSession session, IoBuffer in) {
        byte[] zigbeeheader = new byte[5];
        in.get(zigbeeheader);
        int bodylength = zigbeeheader[4] & 0xff;
        byte[] body = new byte[bodylength];
//        if (bodylength < in.remaining() - 1) {
        in.get(body);
//        }else{
//            
//        }
        byte zigbeetail = in.get();


        ZigbeePacket m = new ZigbeePacket(body);
        m.setZigbeehead(zigbeeheader);
        m.setZigbeetail(zigbeetail);
        return m;
    }

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        // Return NEED_DATA if the whole header is not read yet.
//	if (in.remaining() < industrialpcParams.PACKET_HEADER_LEN) {
//	    return MessageDecoderResult.NEED_DATA;
//	}

        //未取出zigbee头时，返回缺少数据，继续等待。
        // System.out.println("position:"+in.position());
        int position = in.position(); //记录缓冲区位置 
        byte C = in.get(position); //判断首字节，不改变position位置
        // System.out.println("position:"+in.position());
        if (!(START <= C && C <= END)) {
            return MessageDecoderResult.NOT_OK;
        }

        if (in.remaining() < industrialpcParams.zigbeehead) {
            return MessageDecoderResult.NEED_DATA;
        }
        //取包体长度
//        System.out.println("position:"+in.position());
//        System.out.println("limit:"+in.limit());

        byte bodylength = in.get((in.position()) + 4);//取负载长度，不改变position位置

        //如果包没有全部传输过来的话，返回缺少数据，继续等待。
        if (in.remaining() < 5 + (bodylength + 256) % 256 + 1) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            //System.out.println("position:" + in.position());
            byte[] pktwithouttail = new byte[5 + (bodylength + 256) % 256];
            in.get(pktwithouttail);//取出除尾部的一整包，改变position位置
            //System.out.println("position:" + in.position());
            byte xor = in.get();
            if (calcxor(pktwithouttail) == xor) {
            } else {
                badhead = true;
            }
            return MessageDecoderResult.OK;


        }
        // Return NOT_OK if not matches.
        //return MessageDecoderResult.NOT_OK;
    }

    private byte calcxor(byte[] inbyte) {
        byte tempbyte = inbyte[0];
        for (int i = 1; i < inbyte.length; i++) {
            tempbyte = (byte) (tempbyte ^ inbyte[i]);
        }
        return tempbyte;
    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception {

        if (badhead == true) {
            //出错时，清空缓冲
            int total = in.remaining();
            int pos = 0;
            for (int i = 1; i < total; i++) {
                byte C = in.get(i);
                if ((START <= C && C <= END)) {
                    pos = i;
                    break;
                }
            }
            if(pos==0){
                pos= total;
            }
            
            byte[] kkk = new byte[pos];
            in.get(kkk);
            System.out.println("错误数据：" + BytePlus.byteArray2String(kkk));

            badhead = false;//回复状态位
            return MessageDecoderResult.OK;
        }
        // Try to decode body
        ZigbeePacket pkt = decodeBody(session, in);
        // Return NEED_DATA if the body is not fully read.
        if (pkt == null) {
            return MessageDecoderResult.NEED_DATA;
        }
//        in.getShort(); // 跳过 'tail'.
        out.write(pkt);

        return MessageDecoderResult.OK;
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
            throws Exception {
    }

    public static void main(String[] args) {
        byte b = (byte) 0xA4;
        System.out.println(b);
        System.out.println((b + 256) % 256);
    }
}
