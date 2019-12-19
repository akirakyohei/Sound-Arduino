package protocol;

public enum ProtocolType {
	
	HEADER((byte)0x80),
	DATA_SAMPLES((byte)0x00),
	SIZE_8_BITS((byte)0x00),
	SIZE_16_BITS((byte)0x40),
	BIG_ENDIAN((byte)0x20),
	LITTLE_ENDIAN((byte)0x00);

	private final byte value;
	ProtocolType(byte value) {
		this.value=value;
	}
        byte getValue() {
		return this.value;
	}
}
