package com.spencersevilla.icnd.edge;
import com.spencersevilla.icnd.edge.*;

import java.io.*;
import java.net.*;

public class Client {

	// INPUT is a tuple (contentName, (InetAddress, Port)) that has ALREADY
	// been resolved! This function merely wraps the TCP connection and then
	// decodes the result into a DataChunk object.
	static DataObject requestContent(String contentName, EndNode node) {
		try {
			Socket client = new Socket(node.addr, node.port);

			ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());

			Message request = generateRequest(contentName);
			oos.writeObject(request);
			Message response = (Message) ois.readObject();
			DataObject object = parseResponse(response, contentName);

			ois.close();
			oos.close();
			client.close();

			return object;
		} catch (Exception e) {
			e.printStackTrace();
        }
        return null;
	}

	static boolean publishToICNCore(String fullName, Node node) {
		try {
			Socket client = new Socket(node.addr, node.port);

			ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());

			Message message = generatePublishRequest(fullName);
			oos.writeObject(message);
			Message response = (Message) ois.readObject();
			boolean result = parsePublishResponse(response);

			ois.close();
			oos.close();
			client.close();

			return result;
		} catch (Exception e) {
			e.printStackTrace();
        }
        // something went wrong somewhere along the way? abort!
        System.out.println("error: publishToICNCore: bailed out!");
        return false;
	}

	// These are the main message-parsing-generating functions ================

	static Message generateRequest(String contentName) {
		Message m = new Message(Message.DATA_REQUEST);
		m.setRequestName(contentName);
		return m;
	}

	static DataObject parseResponse(Message response, String contentName) {
		if (response.type != Message.DATA_RESPONSE) {
			System.out.println("Client error: incorrect message type: " + response.type);
			return null;
		}

		if (response.requestName == null) {
			System.out.println("Client error: null requestName!");
			return null;
		}

		if (!response.requestName.equals(contentName)) {
			System.out.println("Client error: " + response.requestName + " != " + contentName);
			return null;
		}

		if (response.content == null) {
			System.out.println("Client error: null content!");
			return null;
		}

		if (response.content.fullName.equals(Message.NULL_NAME)) {
			System.out.println("Client error: content not found!");
			return null;
		}

		if (!response.content.fullName.equals(contentName)) {
			System.out.println("Client error: content not what we asked for!");
			return null;
		}

		return response.content;
	}

	static Message generatePublishRequest(String fullName) {
		Message m = new Message(Message.PUBLISH_REQUEST);
		m.setRequestName(fullName);
		return m;
	}

	static boolean parsePublishResponse(Message response) {
		if (response.type != Message.PUBLISH_RESPONSE) {
			System.out.println("Client error: incorrect message type: " + response.type);
			return false;
		}

		Boolean result = (Boolean) response.getOption(Message.OPT_PUBLISHED_SUCCESSFULLY);
		return result.booleanValue();
	}
}