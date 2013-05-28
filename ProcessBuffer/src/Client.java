import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	public static void main(String[] args) throws IOException, UnknownHostException {
		//Client
		
		while(true){
		Socket clientSocket = null;
		
		try	{
			clientSocket = new Socket("localhost", 6234);
			System.out.println(clientSocket);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			outToServer.writeBytes("SELECT * FROM USERS" + '\n');
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			System.out.println("FROM SERVER: " + inFromServer.readLine());
			clientSocket.close();
		} catch(Exception err){
			System.out.println(err.getMessage());
			clientSocket.close();
		}
		
		}
		//End Client
	}

}
