package com.spencersevilla.icnd.core;
import com.spencersevilla.icnd.*;

import java.util.*;

public class ICNDCore {

	public static void main(String[] args) throws Exception {
		System.out.println("core-main.");

		// Initialization, command-line args, etc.

		ICNDCore core = new ICNDCore();
		core.start();
	}

	public ICNDCore() throws Exception {
	}

	// Java Daemon interface here! ============================================

	// public void init(DaemonContext context) throws DaemonInitException, Exception {
	// 	System.out.println("daemon initialization not yet supported.");
	// }

	public void start() throws Exception {
		System.out.println("started.");
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
	public boolean publishDataObject(String fullName) {
		return false;
	}
}