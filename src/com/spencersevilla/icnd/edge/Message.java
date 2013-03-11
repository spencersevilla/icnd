package com.spencersevilla.icnd.edge;
import com.spencersevilla.icnd.edge.*;

import java.io.*;
import java.util.*;

public class Message implements Serializable {

	public static final int DATA_REQUEST = 0;
	public static final int DATA_RESPONSE = 1;
	public static final int PUBLISH_REQUEST = 2;
	public static final int PUBLISH_RESPONSE = 3;

	public static final String NULL_NAME = new String("NOT_FOUND");
	public static final DataObject NULL_CONTENT = new DataChunk(NULL_NAME, null);

	public static final String OPT_PUBLISHED_SUCCESSFULLY = new String("ICN_PUBLISH_RESPONSE");

	int type;
	String requestName;
	String fromNode;
	String toNode;
	HashMap<String, Object> options;
	DataObject content;

	public Message(int t) {
		t = type;
		requestName = null;
		fromNode = null;
		toNode = null;
		options = null;
		content = null;
	}

	public void setRequestName(String r) {
		requestName = r;
	}

	public void setFromNode(String n) {
		fromNode = n;
	}

	public void setToNode(String n) {
		toNode = n;
	}

	public void setContent(DataObject o) {
		if (o == null) {
			content = NULL_CONTENT;
		} else {
			content = o;
		}
	}

	public void addOption(String key, Object value) {
		if (options == null) {
			options = new HashMap();
		}

		options.put(key, value);
	}

	public Object getOption(String key) {
		if (options == null) {
			return null;
		}

		return options.get(key);
	}

	public void removeOption(String key) {
		if (options == null) {
			return;
		}

		options.remove(key);
	}
}