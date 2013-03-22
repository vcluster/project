package vcluster.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

import vcluster.global.Config;
import vcluster.util.PrintMsg.DMsgType;

public class Util {

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
	    } catch (Exception e) {
	    	PrintMsg.print(DMsgType.ERROR, "while loading, "+Config.configFile+" file!");
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
		else if (aKey.equalsIgnoreCase("one"))
			Config.ONE_IPADDR = aValue;
	}
	
	public static void printConfig()
	{
		System.out.println("---------------------------------------");
		System.out.println("             Configuration             ");
		System.out.println("---------------------------------------");
		
		System.out.println("Condor: "+Config.CONDOR_IPADDR);
		System.out.println("One: "+Config.ONE_IPADDR);
		System.out.println("---------------------------------------");
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
	

}
