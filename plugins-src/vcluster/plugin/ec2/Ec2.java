package vcluster.plugin.ec2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import vcluster.elements.*;
import vcluster.plugInterfaces.CloudInterface;
import vcluster.util.Util;

import com.sun.xml.bind.StringInputStream;


public class Ec2 implements CloudInterface{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CloudElement cloudElement = new CloudElement();
	private Cloud cloud;

	private static String makeGETQuery(CloudElement cloudElement, QueryInfo ci) 
			throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException
			{
				List<String> allKeyNames = new ArrayList<String>(ci.getKeySet());
				Collections.sort(allKeyNames, String.CASE_INSENSITIVE_ORDER);

				String queryString = "";
				
				StringBuffer stringToSign = new StringBuffer("GET\n"+cloudElement.getShortEndPoint()+"\n"+"/"+"\n");
				stringToSign.append("AWSAccessKeyId="+cloudElement.getAccessKey() + "&");

				boolean first = true;
				for (String keyName : allKeyNames) {
		            if (first)
		                first = false;
		            else
		            	queryString += "&";

		            if(ci.getAttrValue(keyName) == null) {
		            	System.out.println("Keyname = "+ keyName+" is null");
		            }
		            
		   			queryString += keyName+"="+URLEncoder.encode(ci.getAttrValue(keyName).toString(), "UTF-8");
				}
				//System.out.println("after 22222");
				stringToSign.append(queryString);
		        String signature = GetSignature.calculateRFC2104HMAC(new String(stringToSign), cloudElement.getSecretKey(), cloudElement.getSignatureMethod());
		        
				String str = (queryString + "&Signature=" + URLEncoder.encode(signature, "UTF-8") 
						+ "&AWSAccessKeyId="+cloudElement.getAccessKey());
				//System.out.println(str);
				return str;
			}
			

		
	
	public static ArrayList<Vm> executeQuery(Command command,String fullURL, String httpQuery)
	{

		
		try {            
			URL endPoint = new URL(fullURL+"?"+httpQuery);
			return doHttpQuery(command,endPoint);
		} catch (Exception e)
		{
			e.printStackTrace();
			
		}
		return null;
	}

	private static ArrayList<Vm> doHttpQuery(Command command,URL endPoint) throws Exception {
		HttpURLConnection con = (HttpURLConnection) endPoint.openConnection();
		ArrayList<Vm> vmList = null;
		con.setRequestMethod("GET");
		con.setDoOutput(true);
		con.connect();
		String respStr;
	//	System.out.println(endPoint.toString());
		if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
			ResponseDataHandler.handlError(con.getErrorStream());
		} else {
			//System.out.println("MARK 2");
			respStr = getResponseString(con.getInputStream());
			//System.out.println(respStr);
			vmList = ResponseDataHandler.handleResponse(command, new StringInputStream(respStr));
		}
		con.disconnect();
		return vmList;

	}

	
	private static String getResponseString(InputStream queryResp) throws Exception 
	{
		final InputStreamReader inputStreamReader = new InputStreamReader(queryResp);
		BufferedReader buffReader = new BufferedReader(inputStreamReader);

		String line = new String();
		StringBuffer stringBuff = new StringBuffer();

		while ((line = buffReader.readLine()) != null) {
			stringBuff.append(line);
		}

		return stringBuff.toString();
	}

	@Override
	public void getCloud(vcluster.elements.Cloud cloud) {
		// TODO Auto-generated method stub
		this.cloud = cloud;
		

	}

	@Override
	public boolean createVM(int maxCount) {
		
		//System.out.println("Under devoleping.......");
		QueryInfo qi = new QueryInfo();
		
		qi.putValue("Action", Command.RUN_INSTANCE.getCommand());
		qi.putValue("ImageId", cloudElement.getImageName());

		String timestamp = Util.getTimestampFromLocalTime(Calendar.getInstance().getTime());

		/* fill the default values */
		qi.putValue("MinCount", "1");
		qi.putValue("MaxCount", Integer.toString(maxCount));
		qi.putValue("InstanceType", cloudElement.getInstaceType());

		/* fill the default values */
        qi.putValue("Timestamp", timestamp);
		qi.putValue("Version", cloudElement.getVersion());
		
		if (cloudElement.getKeyName() != null) 
			qi.putValue("KeyName", cloudElement.getKeyName());
		
		
		qi.putValue("SignatureVersion", cloudElement.getSignatureVersion());
		qi.putValue("SignatureMethod", cloudElement.getSignatureMethod());
		
		//System.out.println("before");
		String query = null;
		try {
			query = makeGETQuery(cloudElement, qi);
			//System.out.println("before2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(cloud.getEndPoint());
	
		for(Vm vm : executeQuery(Command.RUN_INSTANCE, cloudElement.getEndPoint(), query)){
			cloud.addVm(vm);
		}
		return true;
	}

	@Override
	public boolean sync() {
		// TODO Auto-generated method stub
		//System.out.println("MARK 1...................................");
		//ArrayList<VMelement> vmList = null;
		QueryInfo qi = new QueryInfo();

		//qi.putValue("Action", "DescribeInstances");
		qi.putValue("Action", "DescribeInstances");
		String timestamp = Util.getTimestampFromLocalTime(Calendar.getInstance().getTime());

		/* fill the default values */
        qi.putValue("Timestamp", timestamp);
		qi.putValue("Version", cloudElement.getVersion());

		qi.putValue("SignatureVersion", cloudElement.getSignatureVersion());
		qi.putValue("SignatureMethod", cloudElement.getSignatureMethod());


		String query = null;
		try {
			query = makeGETQuery(cloudElement, qi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(query);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Vm vm : executeQuery(Command.RUN_INSTANCE, cloudElement.getEndPoint(), query)){
			cloud.addVm(vm);
		}
		return true;
	}

	@Override
	public boolean destroyVM(String id) {
		// TODO Auto-generated method stub

		QueryInfo qi = new QueryInfo();
		String timestamp = Util.getTimestampFromLocalTime(Calendar.getInstance().getTime());
		qi.putValue("Action", "TerminateInstances");
		qi.putValue("InstanceId.1", id);

		/* fill the default values */
		qi.putValue("Version", cloudElement.getVersion());
		qi.putValue("SignatureVersion", cloudElement.getSignatureVersion());
		qi.putValue("SignatureMethod", cloudElement.getSignatureMethod());
        qi.putValue("Timestamp", timestamp);
		
		String query = null;
		try {
			query = makeGETQuery(cloudElement, qi);
		} catch (Exception e) {
			e.printStackTrace();
		}

        executeQuery(Command.TERMINATE_INSTANCE, cloudElement.getEndPoint(), query);
        cloud.getVmList().remove(id);
        return true;

	}

	@Override
	public boolean startVM(String id) {
		// TODO Auto-generated method stub
		QueryInfo qi = new QueryInfo();

		qi.putValue("Action", "StartInstances");
		qi.putValue("InstanceId.1", id);

		/* fill the default values */
		qi.putValue("SignatureVersion", cloudElement.getSignatureVersion());
		qi.putValue("SignatureMethod", cloudElement.getSignatureMethod());

		String timestamp = Util.getTimestampFromLocalTime(Calendar.getInstance().getTime());
		qi.putValue("Timestamp", timestamp);
		qi.putValue("Version", cloudElement.getVersion());
		String query = null;
		try {
			query = makeGETQuery(cloudElement, qi);
		} catch (Exception e) {
			e.printStackTrace();
		}

    	executeQuery(Command.TERMINATE_INSTANCE, cloudElement.getEndPoint(), query);
    	return true;
	
	}

	@Override
	public boolean suspendVM(String id) {
		// TODO Auto-generated method stub
		QueryInfo qi = new QueryInfo();

		qi.putValue("Action", "StopInstances");
		qi.putValue("InstanceId.1", id);

		/* fill the default values */
		qi.putValue("SignatureVersion", cloudElement.getSignatureVersion());
		qi.putValue("SignatureMethod", cloudElement.getSignatureMethod());
		String timestamp = Util.getTimestampFromLocalTime(Calendar.getInstance().getTime());
		qi.putValue("Timestamp", timestamp);
		qi.putValue("Version", cloudElement.getVersion());
		
		String query = null;
		try {
			query = makeGETQuery(cloudElement, qi);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	executeQuery(Command.TERMINATE_INSTANCE, cloudElement.getEndPoint(), query);   
    	return true;
	}


	@Override
	public boolean hoston(String ipmiID) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean hostoff(String ipmiID) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean migrate(String vmid, String hostid) {
		// TODO Auto-generated method stub
		return false;
	}	
	

	
}
