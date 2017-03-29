package net.labthink.instrument.device.TSYW3.message;

import net.labthink.instrument.device.base.message.AbstractPacket;

public class TSYW3Message extends AbstractPacket implements TSYW3MessageParams {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8120264062316076858L;

	public TSYW3Message() {
		reset();
	}

	public TSYW3Message(byte[] _content) {
		defaultPacket(_content);
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
