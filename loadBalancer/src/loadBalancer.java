import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import socket.abstractServer;
import socket.simpleClient;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.Reservation;

public class loadBalancer {

	private static int Serverport = 6233;
	private static int ProccessBufferPort =  6432;

	/**
	 * @param args
	 * Incoming - Query
	 * Outgoing - Results
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String [ ] args) throws IOException, InterruptedException, ClassNotFoundException{
		int mode;
		mode = 0;
		loadBalancerServer server;
		switch(mode){
		case 1:
			server = new loadBalancerServer(ProccessBufferPort,"external");
			break;
		case 2:
			server = new loadBalancerServer(ProccessBufferPort,"internal");
			break;
		default:
			server = new loadBalancerServer(ProccessBufferPort,"localhost");
			break;
		}
		server.start(Serverport);
	}
	
}

class loadBalancerServer extends abstractServer{
	
	
	private List<cloudMachine> list;
	private int processBufferPort;
	private AmazonEC2Client ec2;
	private static String instanceId1 = "i-439b0d20";
	private static String instanceId2 = "i-bac8ccda";
	private static String instanceId3 = "i-0cc9cd6c";
	private double maxCPU = 90.0;
	private double maxDiskReadLoad = 90.0;
	private String mode = "";
	
	public loadBalancerServer(int processBufferPort, String mode) throws IOException{
		this.mode = mode;
		this.processBufferPort = processBufferPort;
		this.list = new ArrayList<cloudMachine>();
        AWSCredentials credentials;
			credentials = new PropertiesCredentials(loadBalancerServer.class.getResourceAsStream("AwsCredentials.properties"));
		this.ec2 = new AmazonEC2Client(credentials);
		
		DescribeInstancesRequest instanceRequest = new DescribeInstancesRequest();
		Collection<String> instanceIds = new ArrayList<String>();
		instanceIds.add(instanceId1);
		instanceIds.add(instanceId2);
		instanceIds.add(instanceId3);
		instanceRequest.setInstanceIds(instanceIds);
		
		Iterator<Reservation> reservations = this.ec2.describeInstances(instanceRequest).getReservations().iterator();
		while(reservations.hasNext()){
			Reservation t = reservations.next();
			// casting a Instance to a cloudMachine
		    list.add(new cloudMachine(t.getInstances().get(0)));			
		}
		
	}

	
	
	
	public String handleRequest(Object query) throws IOException, InterruptedException{
	
		cloudMachine node=null;
		String host="localhost";
		String results;
		if(!this.mode.equals("localhost")){
			try {
				node = findNode();
			} catch (noMachines e) {
				return "Server is busy, please try again";
			}
			if(this.mode.equals("internal")){
				host = node.getPrivateDnsName();
			}else{
				host = node.getPublicDnsName();
			}
		}

		results = new simpleClient(host,processBufferPort).sendRequest(query.toString());
			
		return results.toString();
	}
	
	private cloudMachine findNode() throws noMachines{
		Iterator<cloudMachine> machinces = list.iterator();
		cloudMachine results=null;
		while(machinces.hasNext()){
			cloudMachine machine = machinces.next();
			// TODO: ADD HardDrive Load
			if(machine.getState().getName().equals("running") & machine.getCPULoad() <= maxCPU){
				results=machine;
				break;
			}
		}
		if(results==null){
			throw new noMachines();
		}
		return results;
	}




}

class noMachines extends Exception{

	/**
	 * Added by Eclipse
	 */
	private static final long serialVersionUID = -7929122619178578809L;
}





