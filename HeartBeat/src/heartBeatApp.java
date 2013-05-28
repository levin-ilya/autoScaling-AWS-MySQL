import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.cloudwatch.model.ListMetricsResult;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

import socket.abstractServer;



public class heartBeatApp {
	
	/*
	 * Description - heartBeat lives on each node.
	 * 
	 *  Incoming request - will listen to a port for a request
	 *  Outgoing response - true or false 
	 */
	public static void main(String[] args) throws IOException, JSONException {
		
		int ServerPort = 6222;
		//server.start(ServerPort);
		System.out.println("heartBeat Server started.");
		//ListMetricsResult results = server.watch.listMetrics();
		//System.out.println(server.getDiskLoad("vol-53c6f539"));	
		
		
	}

}

