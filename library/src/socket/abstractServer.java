package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class abstractServer {

	public void start(int port) throws InterruptedException, ClassNotFoundException{
		try {
			Socket connectionSocket;
			Object clientData;
			ServerSocket listener = new ServerSocket(port);
			String output;
			while (true) {
				connectionSocket = listener.accept();
				System.out.println("connected!");
				// Read INPUT
				ObjectInputStream ois = new ObjectInputStream(connectionSocket.getInputStream());
		        clientData =  ois.readObject();
				System.out.println("Received: " + clientData);
				// handle request
	            output = handleRequest(clientData);
	            // write results back 
	            ObjectOutputStream oos = new ObjectOutputStream(connectionSocket.getOutputStream());
	            System.out.println("Sending Results:"+output.toString());
	            oos.writeObject(output);
	            connectionSocket.close();
			}
		} catch (IOException ioe) {
			System.out.println("IOException on socket listen: " + ioe);
			ioe.printStackTrace();
		}
	}
	
	abstract public String handleRequest(Object clientData) throws IOException, InterruptedException;
	
}
