package com.spencersevilla.icnd.edge;
import com.spencersevilla.icnd.edge.*;

import java.io.File;

public class DataChunk extends DataObject {
	protected File file;

	public DataChunk(String s, File f) {
		fullName = s;
		file = f;
	}
}