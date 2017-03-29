package net.labthink.instrument.device.cofp01.message;



import net.labthink.instrument.device.base.message.AbstractPacket;

import org.apache.mina.core.buffer.IoBuffer;

public class Cofp01Message extends AbstractPacket implements
		Cofp01MessageParams {

	/**
     * 
     */
	private static final long serialVersionUID = -5730588636359536875L;

	public Cofp01Message() {
		reset();
	}

	public Cofp01Message(byte[] _content) {
		defaultPacket(_content);
	}

	public Cofp01Message(byte status, short expnum, short fuk, short degree,
			short maxvalue, short minvalue, short avgvalue) {
		byte[] buf = packDataPacket(status, expnum, fuk, degree, maxvalue,
				minvalue, avgvalue);
		defaultPacket(buf);
	}

	public byte[] packDataPacket(byte status, short expnum, short fuk,
			short degree, short maxvalue, short minvalue, short avgvalue) {
		IoBuffer buf = IoBuffer.allocate(14);
		buf.setAutoExpand(false); // Enable auto-expand for easier encoding
		buf.put(status);
		buf.putShort(expnum);
		buf.putShort(fuk);
		buf.putShort(degree);
		buf.putShort(maxvalue);
		buf.putShort(minvalue);
		buf.putShort(avgvalue);
		buf.put((byte) 0);
		return buf.array();
	}

	public byte[] packDataPacket(short expnum, short fuk, short degree,
			short maxvalue, short minvalue, short avgvalue) {
		return packDataPacket((byte) 1, expnum, fuk, degree, maxvalue,
				minvalue, avgvalue);
	}

	public Cofp01Message(short expnum, short fuk, short degree, short maxvalue,
			short minvalue, short avgvalue) {
		this((byte) 1, expnum, fuk, degree, maxvalue, minvalue, avgvalue);
	}

	public Cofp01Message(short speed, short totaltimes) {
		byte[] bts = packControlPacket(speed, totaltimes);
		defaultPacket(bts);
	}

	public byte[] packControlPacket(short speed, short totaltimes) {
		IoBuffer buf = IoBuffer.allocate(14);
		buf.setAutoExpand(false); // Enable auto-expand for easier encoding
		buf.put((byte) 0);
		buf.putShort(speed);
		buf.putShort(totaltimes);
		buf.put(new byte[7]);
		return buf.array();
	}

	public void reset() {
		defaultPacket(null);
	}

	/**
	 * 重置为默认包体内容
	 * 
	 * @param _content
	 *            重置时的content内容。如传入null则初始化为new
	 *            byte[CommonParameters.PACKET_BODY_LEN];
	 */
	protected void defaultPacket(byte[] _content) {
		this.header = PACKET_HEADER;
		this.tail = PACKET_TAIL;
		this.length = PACKET_FULL_LEN;
		if (_content == null) {
			this.content = new byte[PACKET_BODY_LEN];
		} else {
			this.content = _content;
		}
	}

}
