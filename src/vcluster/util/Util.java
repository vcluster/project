package vcluster.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeMap;

import vcluster.configs.Config;

public class Util {
	
	public static TreeMap<String, Boolean> listVSlist(ArrayList<String> arr1,ArrayList<String> arr2){
		TreeMap<String,Boolean> hsm = new TreeMap<String,Boolean>();
		ArrayList<String> temp = new ArrayList<String>();
		for(String str1 : arr1){
			for(String str2 : arr2){
				if(str1.equals(str2)){
					temp.add(str1);
				}
				
			}
		}
		
		arr1.removeAll(temp);
		arr2.removeAll(temp);
		for(String str : arr1){
			hsm.put(str, new Boolean(false));
		}
		for(String str : arr2){
			hsm.put(str, new Boolean(true));
		}
		return hsm;
	}

	public static boolean loadConfig(String configFile)
	{
		/* set config file */
		Config.configFile = configFile;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(Config.configFile));
			
			String aLine = "";
			while ((aLine = br.readLine()) != null) {
				lineProcess(aLine);
			}
			br.close();
	    } catch (Exception e) {
	    	//PrintMsg.print(DMsgType.ERROR, "while loading, "+Config.configFile+" file!");
	    	return false;
	    } 
	    
		return true;
	}
	
	private static void lineProcess(String aLine)
	{
		StringTokenizer st = new StringTokenizer(aLine, "=");
		
		if (!st.hasMoreTokens()) return;
		
		/* get a keyword */
		String aKey = st.nextToken().trim();
	
		/* get a value */
		if (!st.hasMoreTokens()) return;

		String aValue = st.nextToken().trim();

		if (aKey.equalsIgnoreCase("condor"))
			Config.CONDOR_IPADDR = aValue;
	}
	
	
	public static byte[] intToBytes(int i)
	{
		byte[] result = new byte[5];
		
		result[0] = (byte) (i >> 24);
		result[1] = (byte) (i >> 16);
		result[2] = (byte) (i >> 8);
		result[3] = (byte) (i /*>> 0*/);
		result[4] = '\0';
		
		return result;
	}
	
	public static String getTimestampFromLocalTime(Date date) {
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	        format.setTimeZone(TimeZone.getTimeZone("GMT"));
	        return format.format(date);
	}
	
	public static String formatTimestamp() {
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		cal.setTimeInMillis(cal.getTimeInMillis() - cal.get(Calendar.ZONE_OFFSET) -
				cal.get(Calendar.DST_OFFSET));
		return df.format(cal.getTime());
	}
	public static void print(StringBuffer arg){		
		print(arg.toString());
	}
	public static void print(String arg){
		switch(Config.currUI) {
		case CMDLINE: 
			vcluster.util.Util.print(arg);
			break;
		default:
			break;
		}
	}
}
