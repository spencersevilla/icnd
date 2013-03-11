package com.spencersevilla.icnd.edge;
import com.spencersevilla.icnd.edge.*;

import java.util.*;
import java.io.File;

public class ICNDEdge {
	protected Node icnNode; /* the entry-point to the ICN system */
	protected ArrayList<DataObject> dataList;
	protected com.spencersevilla.fern.FERNInterface fern;

	public static void main(String[] args) throws Exception {
		System.out.println("edge-main.");

		// Initialization, command-line args, etc.

		ICNDEdge edge = new ICNDEdge();
		edge.start();
	}

	public ICNDEdge() throws Exception {
	}
	
	// Java Daemon interface here! ============================================

	// public void init(DaemonContext context) throws DaemonInitException, Exception {
	// 	System.out.println("daemon initialization not yet supported.");
	// }

	public void start() throws Exception {
		icnNode = null;
		dataList = new ArrayList<DataObject>();
		fern = new com.spencersevilla.fern.FERNInterface();

		System.out.println("icnd_edge started");
	}

	public void stop() throws Exception {
		System.out.println("stopped.");
	}

	public void destroy() {
		System.out.println("destroyed.");
	}

	public void signal() {
		System.out.println("signal called!");
	}

	// MAIN FUNCTIONS FOR INTERACTION: THIS IS OUR API ========================

	public DataObject getDataObject(String name) {
		EndNode[] locations = locateObject(name);
		if (locations == null) {
			return null;
		}

		EndNode best = bestChoice(locations);
		DataObject object = Client.requestContent(name, best);
		return object;
	}

	public boolean publishFile(String fullName, File file) {
		DataChunk chunk = new DataChunk(fullName, file);
		return publishDataObject(fullName, chunk);
	}

	// MAIN COMPONENT FUNCTIONS: THESE ARE INTERNAL ===========================

	protected boolean publishDataObject(String fullName, DataObject object) {
		if (findDataObject(fullName) != null) {
			// We already own an object with this exact name! 
			return false;
		}

		if (icnNode == null) {
			// we don't know anyone to publish to!
			return false;
		}

		// the ICN publish mechanism will take care of aggregation, pollution,
		// auth-checking, etc... so let's just go ahead and signal to our local
		// ICN node that we are willing to host this content.
		boolean result = Client.publishToICNCore(fullName, icnNode);

		// if it's approved, then add to the list!
		if (result == true) {
			dataList.add(object);
		}

		return result;
	}

	protected EndNode[] locateObject(String name) {
		// ALL this should do is issue a FERN CONTENT record request for the object!
		// This record request should return a set of FERN nodenames that own the object
		com.spencersevilla.fern.Request req = new com.spencersevilla.fern.Request(name);
		req.setType(com.spencersevilla.fern.Type.ICN);
		com.spencersevilla.fern.Response resp = fern.resolveRequest(req);

		com.spencersevilla.fern.Request gotreq = (com.spencersevilla.fern.Request) resp.getRequest();
		if (!req.getName().equals(gotreq.getName())) {
			System.err.println("icnd_edge error: locateObject: req != gotreq");
			return null;
		}

		ArrayList<ICNRecord> recordset = parseResponse(resp);

		// still not done...
		return null;
	}

	private ArrayList<ICNRecord> parseResponse(com.spencersevilla.fern.Response response) {
		com.spencersevilla.fern.FERNObject obj = response.getObject();
		ArrayList<com.spencersevilla.fern.Record> rset = obj.getRecordSet();

		ArrayList<ICNRecord> icn_rset = new ArrayList<ICNRecord>();

		for (com.spencersevilla.fern.Record r : rset) {
			if (r.getType() == com.spencersevilla.fern.Type.ICN) {
				ICNRecord icn_rec = new ICNRecord(r.getData());
				icn_rset.add(icn_rec);
			} else {
				System.err.println("icnd_edge error: parseResponse: received non-ICN record???");
			}
		}

		return icn_rset;
	}

	// add complexity to this function later...
	protected EndNode bestChoice(EndNode [] nodes) {
		return nodes[0];
	}

	protected DataObject findDataObject(String fullName) {
		for (int i = 0; i < dataList.size(); i++) {
			DataObject o = dataList.get(i);
			if (fullName.equals(o.fullName)) {
				return o;
			}
		}
		return null;
	}
}