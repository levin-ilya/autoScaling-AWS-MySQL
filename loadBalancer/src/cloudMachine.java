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
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;


public class cloudMachine{
	
	private  AmazonCloudWatchClient watch;
	private double maxReadMBperSecond = 65.0;
	private static int nubmerOfBytesInMB =1048576;
	private Instance machine;	

	public cloudMachine(Instance machine) throws IOException{
		super();
        AWSCredentials credentials = new PropertiesCredentials(cloudMachine.class.getResourceAsStream("AwsCredentials.properties"));
		this.watch = new AmazonCloudWatchClient(credentials);
		this.machine = machine;
		
	}
	
	public InstanceState getState(){
		return this.machine.getState();
	}
	
	public void setMaxDiskRead(double input){
		this.maxReadMBperSecond = input;
	}
	
	
	public double getCPULoad(){
		return getMetric("AWS/EC2", "CPUUtilization", "InstanceId", this.machine.getInstanceId());
	}


	public double getDiskLoad(String volumeID){
//		{Namespace: AWS/EBS, MetricName: VolumeReadBytes, Dimensions: [{Name: VolumeId, Value: vol-53c6f539, }]
		// converts bytes to megaBytes;
		double megabytesSecond =  getMetric("AWS/EBS", "VolumeReadBytes", "VolumeId", volumeID)/nubmerOfBytesInMB;
		double percentageLoad = megabytesSecond/maxReadMBperSecond;
		return percentageLoad;
	}
	
	private double getMetric(String NameSpace,String MetricName,String DimensionName, String DimensionValue){
		GetMetricStatisticsRequest request = new GetMetricStatisticsRequest();
		double results = 0.0;
		request.setNamespace(NameSpace);
		request.setMetricName(MetricName);
		ArrayList<Dimension> dimArray = new ArrayList<Dimension>();
		Dimension dimension = new Dimension();
		dimension.setName(DimensionName);
		dimension.setValue(DimensionValue);
		dimArray.add(dimension);
		request.setDimensions(dimArray);
		if(MetricName.equals("CPUUtilization")){
			request.setUnit("Percent");
		}
		Date startTime = new Date();
		// Go back 2 minutes
		startTime.setTime(startTime.getTime() - (2*60*1000));
		request.setStartTime(startTime);
		Date endTime = new Date();
		request.setEndTime(endTime);
		request.setPeriod(60);
		Collection<String> statistics = new ArrayList<String>();
		statistics.add("Average");
		request.setStatistics(statistics);
		GetMetricStatisticsResult AWSresults = this.watch.getMetricStatistics(request);
		if(AWSresults.getDatapoints().size() != 0){
			results = AWSresults.getDatapoints().get(AWSresults.getDatapoints().size()-1).getAverage();
		}
		return results;
	}
	
	public String getPublicDnsName() {
		return machine.getPublicDnsName();
	}
	
	public String getPrivateDnsName(){
		return machine.getPrivateDnsName();
	}
	

	

}

