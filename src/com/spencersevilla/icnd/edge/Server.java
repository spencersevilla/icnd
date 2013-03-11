package com.spencersevilla.icnd.edge;
import com.spencersevilla.icnd.edge.*;

import java.io.*;
import java.net.*;

public class Server implements Runnable {

	protected ICNDEdge edge;
	private Thread thread;
	public static int port = 2323;
	private ServerSocket listenSock;

	public Server(ICNDEdge e) {
		edge = e;
	}

	public void start() {
		System.out.println("ICND server running on port " + port);

		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void run() {

        try {
			listenSock = new ServerSocket(port);

			while(true) {

				Socket connSock = listenSock.accept();

				ObjectInputStream ois = new ObjectInputStream(connSock.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(connSock.getOutputStream());

				Message request = (Message) ois.readObject();
				Message response = generateResponse(request);

				oos.writeObject(response);

				ois.close();
				oos.close();
				connSock.close();
			}
 		} catch (Exception e) {
            e.printStackTrace();
        }
	}

	protected Message generateResponse(Message request) {
		if (request.type != Message.DATA_REQUEST) {
			System.out.println("Server error: incorrect message type: " + request.type);
			return null;
		}

		if (request.requestName == null) {
			System.out.println("Server error: null request!");
			return null;
		}

		Message response = new Message(Message.DATA_RESPONSE);
		response.setRequestName(request.requestName);

		DataObject o = edge.findDataObject(request.requestName);
		response.setContent(o);

		return response;
	}
}