package com.spencersevilla.icnd.edge;
import com.spencersevilla.icnd.edge.*;

public class ICNRecord {
	public static final String NAME = new String("NAME");

	String name;

	public ICNRecord(byte[] inData) {
		name = NAME;
	}

	public byte[] toBytes() {
		return NAME.getBytes();
	}

	public static ICNRecord fromBytes(byte[] inData) {
		return new ICNRecord(inData);
	}
}