package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;



public class simpleClient {
	
	private Socket clientSocket;
	private String IP;
	private int port = 6234;;
	
	public simpleClient(String IP,int port){
		this.IP = IP;
		this.port = port;
	}
	
	public simpleClient(String IP){
		this.IP = IP;
	}
	
	public String sendRequest(String message) throws IOException{
		String results =null;
		ArrayList<ArrayList<Object>> temp;
		try	{
			clientSocket = new Socket(this.IP,this.port);
			System.out.println(clientSocket);
			ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
	        System.out.println("Query sent to server --> "+message);
	        oos.writeObject(message);

	        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
	        results = ois.readObject().toString();
	        //temp = (ArrayList<ArrayList<Object>>) ois.readObject();
	        System.out.println("Results received to server --> " + results);

	        clientSocket.close();
	     

		} catch(Exception err){
			System.out.println(err.getMessage());
		}finally{
			clientSocket.close();
		}
		
		return results;
	}

}
