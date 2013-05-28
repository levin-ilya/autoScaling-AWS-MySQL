import java.io.IOException;
import java.util.ArrayList;

import socket.simpleClient;

public class clientApp {

	static private String loadBalancer = "localhost";
	static private int portNumber = 6233;
	/**
	 * @param args
	 * Description - 
	 * Incoming - None (Query folder later on)
	 * Outgoing - Request to Load Balancer
	 * 
	 * NOTE: ADD TIMER 
	 */
	public static void main(String[] args) {
		String results = null;
		String error = "All clear";
		simpleClient sender = new simpleClient(loadBalancer,portNumber);
		try {
			results = sender.sendRequest("SELECT count(*) FROM customer;");
		} catch (IOException e) {
			error = "I/O exception";
		}
		System.out.println(error);
	}
	
	

}
