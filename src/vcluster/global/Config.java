package vcluster.global;


import vcluster.monitoring.MonitoringMan;
import vcluster.util.HandleXML;

public class Config {

	public static String configFile = "vcluster.conf";
	public static final String xmlFile = "response.xml";
	public static final String initXML ="vcluster.xml";
	
	public static String CONDOR_IPADDR = "150.183.233.59";
	public static String ONE_IPADDR = "150.183.233.59";
	public static final int PORTNUM = 9734;
	
	/* interval for checking q in minutes */
	public static int MAX_PROBE_INTERVAL = 30;  
	
	public enum VMState {STOP, PENDING, RUNNING, SUSPEND, PROLOG,FAILED, NOT_DEFINED }; 
	public enum CloudType {PRIVATE, PUBLIC, NOT_DEFINED};

	public static MonitoringMan monMan = null;
	//public static VMManager vmMan = null;
	//public static CloudManager cloudMan = null;
	public static HandleXML hdXML = null;
	
	public static boolean DEBUG_MODE = false;
	
	public static int DEFAULT_SLEEP_SEC = 30;
	public static int SLEEP_SEC_INC = 30;
	public static int MAX_SLEEP_SEC = 60*30; /* 30 mins */
	

	//public static String ProxyExecutor_plugin = "";
	
	
	
}