import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

class Server {

	private static int port = 6234;
	private static ProcessBuffer buffer = new ProcessBuffer();

	// Listen for incoming connections and handle them
	public static void main(String[] args) throws IOException {

		try {
			ServerSocket listener = new ServerSocket(port);
			Socket connectionSocket;
			String clientData;

			while (true) {
				connectionSocket = listener.accept();
				System.out.println("connected!");
				BufferedReader inFromClient =
		               new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				clientData = inFromClient.readLine();
	            System.out.println("Received: " + clientData);
	            
	            //PB instance, add SQL query, socket
	            //add to buffer
	            
	            DataOutputStream outToServer = new DataOutputStream(connectionSocket.getOutputStream());
	    		outToServer.writeBytes("Got query");
				connectionSocket.close();
			}
		} catch (IOException ioe) {
			System.out.println("IOException on socket listen: " + ioe);
			ioe.printStackTrace();
		}
	}
}

class ProcessBuffer {
	
	private static int BUFFER_SIZE = 10000;
	private ArrayBlockingQueue<HashMap<String,Object>> buffer 
		= new ArrayBlockingQueue<HashMap<String,Object>>(BUFFER_SIZE, false);
	private HashMap<String,Object> element = null;
	
	ProcessBuffer(){ }
	
	ProcessBuffer(Socket socket, String query) throws Exception{ 
		if(!add(socket,query)){
			throw new BufferException("Error: ProcessBuffer instance could not add to buffer.");
		}
	}
	
	ProcessBuffer(HashMap<String,Object> element){
		this.element = element;
		add(element);
	}
	
	public boolean add(Socket socket, String query){
		if(socket != null && query != null){
			HashMap<String,Object> temp = new HashMap<String,Object>();
			temp.put("socket", socket);
			temp.put("query", query);
			add(temp);
			return true;
		}
		return false;
	}
	
	public boolean add(HashMap<String,Object> element){
		try {
			if(buffer.add(element)){
				return true;
			}
		} catch(Exception err){
			System.out.println(err.getMessage());
			err.printStackTrace();
		}
		return false;
	}
	
	public HashMap<String,Object> remove(){
		try {
			buffer.remove();
			return element;
		} catch(Exception err){
			System.out.println(err.getMessage());
			err.printStackTrace();
		}
		return null;
	}
}

class BufferException extends Exception{
	private static final long serialVersionUID = 1L;
	public BufferException(){
		
	}
	public BufferException(String msg){
		
	}	
}
