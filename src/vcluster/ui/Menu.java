package vcluster.ui;

/**
 * 
 * @author rsyoung
 *
 */
public class Menu {

	public static final int TYPE_QUIT = 0;
	public static final int TYPE_RUNINSTANCE = 1;
	public static final int TYPE_DESCRIBEINSTANCE = 2;
	public static final int TYPE_TERMINATEINSTANCE = 3;
	public static final int TYPE_DESCRIBEIMAGE = 4;

	public static final int TYPE_CONDOR = 5;

	public static final int TYPE_ONEVM = 6;

	public static final int TYPE_STARTINSTANCE = 7;
	public static final int TYPE_STOPINSTANCE = 8;
	
	

	public static final int TYPE_SHOW = 101;
	public static final int TYPE_LOAD = 102;
	public static final int TYPE_SET = 103;

	public static final int TYPE_CHECK_Q = 201;
	public static final int TYPE_CHECK_POOL = 202;

	public static final int TYPE_MONITOR = 301;
	public static final int TYPE_VMMAN = 401;
	
	public static final int TYPE_DEBUG_MODE = 901;
	public static final int TYPE_ENGINEERING_MODE = 902;
	
	
	public static final int TYPE_NOT_DEFINED = -1;
	
	public static final String CMD_RUNINSTANCE = "RunInstances";
	public static final String CMD_STARTINSTANCE = "StartInstances";
	public static final String CMD_STOPINSTANCE = "StopInstances";
	
	public static final String CMD_DESCRIBEINSTANCE = "DescribeInstances";
	public static final String CMD_TERMINATEINSTANCE = "TerminateInstances";
	public static final String CMD_DESCRIBEIMAGE = "DescribeImages";
	
	public static final String CMD_CONDOR = "condor_status";

	public static final String CMD_ONEVM = "onevm";
	public static final String CMD_LOAD = "load";
	public static final String CMD_SET = "set";
	public static final String CMD_CHECK_Q = "checkq";
	public static final String CMD_CHECK_POOL = "checkpool";
	public static final String CMD_MONITOR = "monitor";
	public static final String CMD_VMMAN = "vmman";

	public static final String CMD_DEBUG = "debug";
	public static final String CMD_ENGINEERING = "engmode";
	
	
	public static final String ARG_CONFIG = "config";
	
	public static void initMenuStruct()
	{
		cmdQuit = new KeyCmdMap(TYPE_QUIT, quitCmds);
		cmdRunInstance = new KeyCmdMap(TYPE_RUNINSTANCE, runCmds);
		cmdStartInstance = new KeyCmdMap(TYPE_STARTINSTANCE, startCmds);
		cmdStopInstance = new KeyCmdMap(TYPE_STOPINSTANCE, stopCmds);

		cmdDescribeInstance = new KeyCmdMap(TYPE_DESCRIBEINSTANCE, descInsCmds);
		cmdTerminateInstance = new KeyCmdMap(TYPE_TERMINATEINSTANCE, terminateInsCmds);
		cmdDescribeImage = new KeyCmdMap(TYPE_DESCRIBEIMAGE, descImgCmds);

		cmdCondor = new KeyCmdMap(TYPE_CONDOR, condorCmds);

		cmdOneVm = new KeyCmdMap(TYPE_ONEVM, onevmCmds);
		
		cmdShow = new KeyCmdMap(TYPE_SHOW, showCmds);
		cmdLoad = new KeyCmdMap(TYPE_LOAD, loadCmds);
		cmdSet = new KeyCmdMap(TYPE_SET, setCmds);
		cmdCheckQ = new KeyCmdMap(TYPE_CHECK_Q, checkQCmds);
		cmdCheckPool = new KeyCmdMap(TYPE_CHECK_POOL, checkPoolCmds);
		cmdMonitor = new KeyCmdMap(TYPE_MONITOR, monitorCmds);
		cmdVMMan = new KeyCmdMap(TYPE_VMMAN, vmManCmds);
		cmdDebugMode = new KeyCmdMap(TYPE_DEBUG_MODE, debugModeCmds);
		cmdEngMode = new KeyCmdMap(TYPE_ENGINEERING_MODE, engModeCmds);
		

	}
	
	public static void printCmdAll()
	{
		cmdQuit.toPrint();
		cmdRunInstance.toPrint();
		cmdStartInstance.toPrint();
		cmdStopInstance.toPrint();
		
		cmdDescribeInstance.toPrint();
		cmdTerminateInstance.toPrint();
		cmdDescribeImage.toPrint();
		cmdCondor.toPrint();
		cmdOneVm.toPrint();

		cmdShow.toPrint();
		cmdLoad.toPrint();
		cmdSet.toPrint();
		cmdCheckQ.toPrint();
		cmdCheckPool.toPrint();
		cmdMonitor.toPrint();
		cmdVMMan.toPrint();
		cmdDebugMode.toPrint();
		cmdEngMode.toPrint();

	}
	
	public static void printCmd(int aType)
	{
		switch(aType) {
		case TYPE_QUIT: cmdQuit.toPrint(); break;
		case TYPE_RUNINSTANCE: cmdRunInstance.toPrint(); break;
		case TYPE_STARTINSTANCE: cmdStartInstance.toPrint(); break;
		case TYPE_STOPINSTANCE: cmdStopInstance.toPrint(); break;
		
		case TYPE_DESCRIBEINSTANCE: cmdDescribeInstance.toPrint(); break;
		case TYPE_TERMINATEINSTANCE: cmdTerminateInstance.toPrint(); break;
		case TYPE_DESCRIBEIMAGE: cmdDescribeImage.toPrint(); break;
		case TYPE_CONDOR: cmdCondor.toPrint(); break;
		case TYPE_ONEVM: cmdOneVm.toPrint(); break;

		case TYPE_SHOW: cmdShow.toPrint(); break;
		case TYPE_LOAD: cmdLoad.toPrint(); break;
		case TYPE_SET: cmdSet.toPrint(); break;
		case TYPE_CHECK_Q: cmdCheckQ.toPrint(); break;
		case TYPE_CHECK_POOL: cmdCheckPool.toPrint(); break;
		case TYPE_MONITOR: cmdMonitor.toPrint(); break;
		case TYPE_VMMAN: cmdVMMan.toPrint(); break;
		case TYPE_DEBUG_MODE: cmdDebugMode.toPrint(); break;
		case TYPE_ENGINEERING_MODE: cmdEngMode.toPrint(); break;
		}
	}
	
	public static boolean contains(int aKey, String aCmd)
	{
		switch(aKey) {
		case TYPE_QUIT: return cmdQuit.contains(aCmd);
		case TYPE_RUNINSTANCE: return cmdRunInstance.contains(aCmd);
		case TYPE_STARTINSTANCE: return cmdStartInstance.contains(aCmd);
		case TYPE_STOPINSTANCE: return cmdStopInstance.contains(aCmd);
		
		case TYPE_DESCRIBEINSTANCE: return cmdDescribeInstance.contains(aCmd);
		case TYPE_TERMINATEINSTANCE: return cmdTerminateInstance.contains(aCmd);
		case TYPE_DESCRIBEIMAGE: return cmdDescribeImage.contains(aCmd);
		case TYPE_CONDOR: return cmdCondor.contains(aCmd);
		case TYPE_ONEVM: return cmdOneVm.contains(aCmd);

		case TYPE_SHOW: return cmdShow.contains(aCmd);
		case TYPE_LOAD: return cmdLoad.contains(aCmd);
		case TYPE_SET: return cmdSet.contains(aCmd);
		case TYPE_CHECK_Q: return cmdCheckQ.contains(aCmd);
		case TYPE_CHECK_POOL: return cmdCheckPool.contains(aCmd);
		case TYPE_MONITOR: return cmdMonitor.contains(aCmd);
		case TYPE_VMMAN: return cmdVMMan.contains(aCmd);
		case TYPE_DEBUG_MODE: return cmdDebugMode.contains(aCmd);
		case TYPE_ENGINEERING_MODE: return cmdEngMode.contains(aCmd);
		}
		return false;
	}
	
	public static int getKey(String aCmd)
	{
		if (contains(TYPE_RUNINSTANCE, aCmd)) return TYPE_RUNINSTANCE;
		if (contains(TYPE_STARTINSTANCE, aCmd)) return TYPE_STARTINSTANCE;
		if (contains(TYPE_STOPINSTANCE, aCmd)) return TYPE_STOPINSTANCE;
		
		if (contains(TYPE_DESCRIBEINSTANCE, aCmd)) return TYPE_DESCRIBEINSTANCE;
		if (contains(TYPE_TERMINATEINSTANCE, aCmd)) return TYPE_TERMINATEINSTANCE;
		if (contains(TYPE_DESCRIBEIMAGE, aCmd)) return TYPE_DESCRIBEIMAGE;
		if (contains(TYPE_CONDOR, aCmd)) return TYPE_CONDOR;
		if (contains(TYPE_ONEVM, aCmd)) return TYPE_ONEVM;

		if (contains(TYPE_SHOW, aCmd)) return TYPE_SHOW;
		if (contains(TYPE_LOAD, aCmd)) return TYPE_LOAD;
		if (contains(TYPE_SET, aCmd)) return TYPE_SET;
		if (contains(TYPE_CHECK_Q, aCmd)) return TYPE_CHECK_Q;
		if (contains(TYPE_CHECK_POOL, aCmd)) return TYPE_CHECK_POOL;

		if (contains(TYPE_MONITOR, aCmd)) return TYPE_MONITOR;
		if (contains(TYPE_VMMAN, aCmd)) return TYPE_VMMAN;

		if (contains(TYPE_DEBUG_MODE, aCmd)) return TYPE_DEBUG_MODE;
		if (contains(TYPE_ENGINEERING_MODE, aCmd)) return TYPE_ENGINEERING_MODE;

		if (contains(TYPE_QUIT, aCmd)) return TYPE_QUIT;
		return TYPE_NOT_DEFINED;
	}

	public static KeyCmdMap cmdQuit;
	public static KeyCmdMap cmdRunInstance;
	public static KeyCmdMap cmdStartInstance;
	public static KeyCmdMap cmdStopInstance;
	
	public static KeyCmdMap cmdDescribeInstance;
	public static KeyCmdMap cmdTerminateInstance;
	public static KeyCmdMap cmdDescribeImage;
	public static KeyCmdMap cmdCondor;
	
	public static KeyCmdMap cmdOneVm;

	public static KeyCmdMap cmdShow;
	public static KeyCmdMap cmdLoad;
	public static KeyCmdMap cmdSet;
	public static KeyCmdMap cmdCheckQ;
	public static KeyCmdMap cmdCheckPool;
	
	public static KeyCmdMap cmdMonitor;
	public static KeyCmdMap cmdVMMan;
	public static KeyCmdMap cmdDebugMode;
	public static KeyCmdMap cmdEngMode;
	

	
	private static final String quitCmds = "quit, exit, done, stop";
	private static final String runCmds = "runinstance, ri, runinst, runins, run";
	private static final String startCmds = "startinstance, si, startinst, startins, start";
	private static final String stopCmds = "stopinstance, stop";
	
	
	private static final String descInsCmds = "describeinstance, din, dins, descinst, descins";
	private static final String terminateInsCmds = "terminateinstance, stop, terminate, ti, kill, killins";
	private static final String descImgCmds = "describeimage, dim, dimg, descimg";

	private static final String condorCmds = "condor_status, condor_q";

	private static final String onevmCmds = "onevm, oneimage, onevnet";

	private static final String showCmds = "show, sh";

	private static final String loadCmds = "load";
	private static final String setCmds = "set";

	private static final String monitorCmds = "monitor, mon";
	private static final String vmManCmds = "vmman, vman";

	private static final String checkQCmds = "chkq, chkqueue, checkqueue, checkq";
	private static final String checkPoolCmds = "chkp, chkpool, checkpool, checkp";

	private static final String debugModeCmds = "debug";
	private static final String engModeCmds = "engmode";
	

}
