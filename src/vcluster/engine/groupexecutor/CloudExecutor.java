package vcluster.engine.groupexecutor;

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
import java.util.StringTokenizer;

import com.sun.xml.bind.StringInputStream;
import vcluster.control.cloudman.CloudElement;
import vcluster.engine.QueryInfo;
import vcluster.engine.ResponseDataHandler;
import vcluster.global.Config;
import vcluster.ui.Command;
import vcluster.util.GetSignature;
import vcluster.util.PrintMsg;
import vcluster.util.Util;
import vcluster.util.PrintMsg.DMsgType;

public class CloudExecutor {

	public static boolean run_instance()
	{
		Thread t = new Thread() {
			public void run() {
				PrintMsg.print(DMsgType.MSG, "launch vm in a separated thread");
				Config.vmMan.launchVM(1, null);
				PrintMsg.print(DMsgType.MSG, "launching is done in a separated thread");
			}
		};
		t.start();

		return true;
	}

	/**
	 * run instance
	 * @param cmdLine
	 * @return
	 */
	public static boolean run_instance(final CloudElement cloud)
	{
		Thread t = new Thread() {
			public void run() {
				PrintMsg.print(DMsgType.MSG, "launch vm in a separated thread");
				Config.vmMan.launchVM(1, cloud);
				PrintMsg.print(DMsgType.MSG, "launching is done in a separated thread");
			}
		};
		t.start();

		return true;

	}
	
	
	public static void executeQuery(Command command, String fullURL, String httpQuery)
	{
		try {
			URL endPoint = new URL(fullURL+"?"+httpQuery);
			doHttpQuery(command, endPoint);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void doHttpQuery(Command command, URL endPoint) throws Exception {
		HttpURLConnection con = (HttpURLConnection) endPoint.openConnection();

		con.setRequestMethod("GET");
		con.setDoOutput(true);
		con.connect();
		String respStr;

		if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
			ResponseDataHandler.handlError(con.getErrorStream());
		} else {
			respStr = getResponseString(con.getInputStream());
			ResponseDataHandler.handleResponse(command, new StringInputStream(respStr));
		}
		con.disconnect();
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
	
	public static boolean rest_launch(CloudElement cloud)
	{
		
		QueryInfo qi = new QueryInfo();
		
		qi.putValue("Action", Command.RUN_INSTANCE.getCommand());
		qi.putValue("ImageId", cloud.getImageName());

		String timestamp = Util.getTimestampFromLocalTime(Calendar.getInstance().getTime());

		/* fill the default values */
		qi.putValue("MinCount", "1");
		qi.putValue("MaxCount", "1");
		qi.putValue("InstanceType", cloud.getInstaceType());

		/* fill the default values */
        qi.putValue("Timestamp", timestamp);
		qi.putValue("Version", cloud.getVersion());
		
		if (cloud.getKeyName() != null) 
			qi.putValue("KeyName", cloud.getKeyName());
		
		
		qi.putValue("SignatureVersion", cloud.getSignatureVersion());
		qi.putValue("SignatureMethod", cloud.getSignatureMethod());
		

		String query = null;
		try {
			query = makeGETQuery(cloud, qi);
		} catch (Exception e) {
			e.printStackTrace();
		}

    	executeQuery(Command.RUN_INSTANCE, cloud.getEndPoint(), query);
    	
    	return true;
 	}

	
	public static boolean describe_instance(CloudElement cloud, String cmdLine)
	{
		
		QueryInfo qi = new QueryInfo();
		StringTokenizer st = new StringTokenizer(cmdLine);

		/* skip command */
		st.nextToken();
		
		String id = null;
		if(st.hasMoreTokens()) id = st.nextToken(); 
		
		qi.putValue("Action", Command.DESCRIBE_INSTANCE.getCommand());

		if (id != null)
			qi.putValue("InstanceId.1", id);
		
		String timestamp = Util.getTimestampFromLocalTime(Calendar.getInstance().getTime());

		/* fill the default values */
        qi.putValue("Timestamp", timestamp);
		qi.putValue("Version", cloud.getVersion());

		qi.putValue("SignatureVersion", cloud.getSignatureVersion());
		qi.putValue("SignatureMethod", cloud.getSignatureMethod());


		String query = null;
		try {
			query = makeGETQuery(cloud, qi);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
    	executeQuery(Command.DESCRIBE_INSTANCE, cloud.getEndPoint(), query);

		
    	
    	return true;
 	}

	public static boolean terminate_instance(CloudElement cloud, String cmdLine)
	{

		QueryInfo qi = new QueryInfo();
		StringTokenizer st = new StringTokenizer(cmdLine);
		
		/* skip the command */
		st.nextToken();
		
		String id = null;
		if (st.hasMoreTokens()) id = st.nextToken();
		
		qi.putValue("Action", Command.TERMINATE_INSTANCE.getCommand());
		qi.putValue("InstanceId.1", id);

		/* fill the default values */
		qi.putValue("SignatureVersion", cloud.getSignatureVersion());
		qi.putValue("SignatureMethod", cloud.getSignatureMethod());

		
		String query = null;
		try {
			query = makeGETQuery(cloud, qi);
		} catch (Exception e) {
			e.printStackTrace();
		}

    	executeQuery(Command.TERMINATE_INSTANCE, cloud.getEndPoint(), query);
		
		
    	return true;
 	}
	
	
	
	private static String makeGETQuery(CloudElement cloud, QueryInfo ci) 
	throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException
	{
		List<String> allKeyNames = new ArrayList<String>(ci.getKeySet());
		Collections.sort(allKeyNames, String.CASE_INSENSITIVE_ORDER);

		String queryString = "";
		
		StringBuffer stringToSign = new StringBuffer("GET\n"+cloud.getShortEndPoint()+"\n"+"/"+"\n");
		stringToSign.append("AWSAccessKeyId="+cloud.getAccessKey() + "&");

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

		stringToSign.append(queryString);
        String signature = GetSignature.calculateRFC2104HMAC(new String(stringToSign), 
        		cloud.getSecretKey(), cloud.getSignatureMethod());

		return (queryString + "&Signature=" + URLEncoder.encode(signature, "UTF-8") 
				+ "&AWSAccessKeyId="+cloud.getAccessKey());
	}
	
	
	

	
}
