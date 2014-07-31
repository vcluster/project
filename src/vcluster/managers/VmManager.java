package vcluster.managers;

import java.util.ArrayList;
import java.util.TreeMap;

import vcluster.Vcluster.uiType;
import vcluster.elements.Cloud;
import vcluster.elements.Vm;
import vcluster.elements.Vm.VMState;
import vcluster.ui.CmdComb;
import vcluster.ui.Command;

/**
 *A class representing the virtual machine manager. 
 *Since there only can be one vmmanager, all the functions in this class are static.
 */
public class VmManager extends Thread {
	
	/**
	 *A static block, initialize the instance of vmlist and the virtual machine's id 
	 *@see vmList
	 */
	static{
		
		id = 0;
	}

	public static Vm getVm(Integer id){
		for(Cloud cloud:CloudManager.getCloudList().values()){
			for(Vm vm : cloud.getVmList().values()){
				if(vm.getuId().equals(id))
					return vm;
			}
		}
		return null;
	}

	/**
	 * Get the virtual machine's status
	 * @param vmState, virtual machine's status described in string
	 * @see VMState
	 */
	public static VMState getVMState(String vmState) {
		
		if(vmState == null) return VMState.NOT_DEFINED;

		if(vmState.equalsIgnoreCase("runn")) return VMState.RUNNING;
		if(vmState.equalsIgnoreCase("penn")) return VMState.PENDING;
		if(vmState.equalsIgnoreCase("prol")) return VMState.PROLOG;
		
		return VMState.NOT_DEFINED;
	}

	/**
	 * Create certain number of virtual machines on specific cloud by following the command line
	 * @param cmdLine, the string of command line,
	 * @see Command
	 * 
	 */
	public static String createVM(CmdComb cmd) {
		// TODO Auto-generated method stub
		StringBuffer strBuff = new StringBuffer();
		Cloud currCloud = CloudManager.getCurrentCloud();
		int vms = 1;
		ArrayList<String> cmdList = cmd.getParaset();
		if(cmdList.isEmpty()){
			cmdList.add("-c");
			cmdList.add(currCloud.getCloudName());
			cmdList.add("-n");
			cmdList.add("1");
		}
		if(cmd.getParaset().get(0).equalsIgnoreCase("--help")||cmd.getParaset().get(0).equalsIgnoreCase("-h")){
			strBuff.append("[USAGE : ]"+System.getProperty("line.separator"));
			strBuff.append("vmman"+System.getProperty("line.separator"));
			strBuff.append("    create"+System.getProperty("line.separator"));
			strBuff.append("        [ --help] | "+System.getProperty("line.separator"));
			strBuff.append("        [ -n NUM_VMS | --num=NUM_VMS ] | "+System.getProperty("line.separator"));
			strBuff.append("        [ -c CLOUDNAME | --name=CLOUDNAME]"+System.getProperty("line.separator"));	
			if(cmd.getUi().equals(uiType.CMDLINE))System.out.println(strBuff);
			return strBuff.toString();
		}
		if(cmdList.size()==0){
			cmdList.add("-c");
			cmdList.add(currCloud.getCloudName());
			cmdList.add("-n");
			cmdList.add("1");
		}
		if(!cmdList.get(0).equals("-c")){
			cmdList.add(0,"-c");
			cmdList.add(1,currCloud.getCloudName());
		}

		if(cmdList.size()<4||!cmdList.get(2).equalsIgnoreCase("-n")){
			cmdList.add(2, "-n");
			cmdList.add(3, "1");
		}
		if(cmdList.size()<6 || !cmdList.get(4).equalsIgnoreCase("-h")){
			cmdList.add(4, "-h");
			cmdList.add(5, "host1");
		}
		
		System.out.println("");
		currCloud = CloudManager.getCloudList().get(cmdList.get(1));
		if(currCloud==null){
			strBuff.append("[ERROR : ] Cloud doesn't exist! please check the cloud name..."+System.getProperty("line.separator"));
			if(cmd.getUi().equals(uiType.CMDLINE))System.out.println(strBuff);
			return strBuff.toString();
		}
		try {
			vms = Integer.parseInt(cmdList.get(3));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			strBuff.append("Number format is not correct!"+System.getProperty("line.separator"));
			return strBuff.toString();
		}
		String result = currCloud.createVM(vms,cmdList.get(5));
		if(cmd.getUi().equals(uiType.CMDLINE))System.out.println(result);
			return result;

	}

	/**
	 * List up the virtual machines by following the command line
	 * @param cmdLine, the string of command line
	 * @see Command
	 */
	public static String listVM(CmdComb cmd) {
		// TODO Auto-generated method
		StringBuffer str = new StringBuffer();
		String filter ="";
		String cloudFilter="";
		if( cmd.getParaset().size()>0){
			String tmp = cmd.getParaset().get(0);
			TreeMap<String,Cloud> cc = new TreeMap<String,Cloud>();			
			if(tmp.equalsIgnoreCase("-refresh")){
				String name="";
				if(cmd.getParaset().size()>1){
					name = cmd.getParaset().get(1);
					if(!CloudManager.getCloudList().keySet().contains(name)){
						str.append("[ERROR : ] "+ name+ " has not been loaded!"+System.getProperty("line.separator"));
						System.out.println(str);
						return str.toString();						
					}
					cc.put(name, CloudManager.getCloudList().get(name));
					cloudFilter = name;
				}else{
					cc = CloudManager.getCloudList();
				}
				for(Cloud cloud : cc.values()){
					if(cloud.isLoaded()){
						cloud.listVMs();
					}
				}			
				
			}else if(tmp.equalsIgnoreCase("-a")||tmp.equalsIgnoreCase("--type=available")){
				filter="RUNNING";
			}else if(tmp.equalsIgnoreCase("-r")||tmp.equalsIgnoreCase("--type=running")){
				filter="RUNNING";
			}else if(tmp.equalsIgnoreCase("-s")||tmp.equalsIgnoreCase("--type=suspended")){
				filter="SUSPEND";
			}else if(tmp.equalsIgnoreCase("-n")){
				if(cmd.getParaset().size()>1){
					cloudFilter = cmd.getParaset().get(1);
				}else{
					str.append("[ERROR : ] Expected a cloud name!"+System.getProperty("line.separator"));
					System.out.println(str);
					return str.toString();
				}
			}
		}	
		
		String tName = String.format("%-10s", "CLOUD");
		String tHost = String.format("%-10s", "Host");
		String tID =String.format("%-5s", "ID");
		String tStat = String.format("%-10s", "STATUS");
		String tPrivateIp =  String.format("%-18s", "PrivateIP");
		String tPublicIp =  String.format("%-18s", "PublicIP");
		String tActivity =  String.format("%-10s", "Activity");
		String tInternalID =  String.format("%-12s", "IntnlID");
		String line =  "------------------------------------------------------------------------------------------------";
		str.append(line+System.getProperty("line.separator"));
		str.append(tID+tInternalID+tStat+tActivity+tPrivateIp+tPublicIp+tHost+tName+System.getProperty("line.separator"));
		str.append(line+System.getProperty("line.separator"));
			int runNum = 0;
			int totalNum = 0;
			for(Cloud cloud : CloudManager.getCloudList().values()){
				String cloudName = cloud.getCloudName();
				for(Vm vm: cloud.getVmList().values()){
				Integer id = vm.getuId();
				String acti = "busy";
				if(vm.isIdle()==0){acti = "idle";}else if(vm.isIdle()==3){acti="none";}
				if(!cloudFilter.equals("")&&cloudName.equals(cloudFilter)){
					String fName = String.format("%-10s",cloudName);
					String fID =String.format("%-5s", id);
					String fStat = String.format("%-10s",vm.getState());
					String fPrivateIp =  String.format("%-18s", vm.getPrivateIP());
					String fPublicIp =  String.format("%-18s", vm.getPubicIP());
					String fActivity =  String.format("%-10s", acti);
					String fInternalID =  String.format("%-12s", vm.getId());
					String fHost = String.format("%-10s", vm.getHostname());
					
					
					if(vm.getState().equals(VMState.RUNNING))runNum++;
					str.append(fID+fInternalID+fStat+fActivity+fPrivateIp+fPublicIp+fHost+fName+System.getProperty("line.separator"));
					totalNum++;
				}
				if(!filter.equals("")&&vm.getState().toString().equals(filter)){
					String fName = String.format("%-10s", cloudName);
					String fID =String.format("%-5s", id);
					String fStat = String.format("%-10s", vm.getState());
					String fPrivateIp =  String.format("%-18s", vm.getPrivateIP());
					String fPublicIp =  String.format("%-18s", vm.getPubicIP());
					String fActivity =  String.format("%-10s", acti);
					String fInternalID =  String.format("%-12s", vm.getId());
					String fHost = String.format("%-10s", vm.getHostname());
					
					if(vm.getState().equals(VMState.RUNNING))runNum++;
					str.append(fID+fInternalID+fStat+fActivity+fPrivateIp+fPublicIp+fHost+fName+System.getProperty("line.separator"));
					totalNum++;
				}
				if(filter.equals("")&cloudFilter.equals("")){
					String fName = String.format("%-10s", cloudName);
					String fID =String.format("%-5s", id);
					String fStat = String.format("%-10s", vm.getState());
					String fPrivateIp =  String.format("%-18s", vm.getPrivateIP());
					String fPublicIp =  String.format("%-18s", vm.getPubicIP());
					String fActivity =  String.format("%-10s", acti);
					String fInternalID =  String.format("%-12s", vm.getId());
					String fHost = String.format("%-10s", vm.getHostname());
					
					if(vm.getState().equals(VMState.RUNNING))runNum++;
					str.append(fID+fInternalID+fStat+fActivity+fPrivateIp+fPublicIp+fHost+fName+System.getProperty("line.separator"));
					totalNum++;
				}
				}
			}
			str.append(line+System.getProperty("line.separator"));
			str.append("      Running VMs :  "+runNum+"/"+totalNum+System.getProperty("line.separator"));
			str.append(line+System.getProperty("line.separator"));
			if(cmd.getUi().equals(uiType.CMDLINE))System.out.println(str);
		return str.toString();
	}

	/**
	 * Show the virtual machine's information in detail,such as ID, public IP, private IP, launch time, etc.
	 * @param cmdLine, the command line string.
	 * @see Command
	 */
	public static String showVM(CmdComb cmd) {
		// TODO Auto-generated method stub
			
		String vmID = "";
		Integer vID;
		ArrayList<String> paras = cmd.getParaset();
		StringBuffer str = new StringBuffer();
		
		if(paras.size()==0){
				str.append("[ERROR : ] Expected a VM ID");
				System.out.println(str);
				return str.toString();
			}
		String tmp = paras.get(0);
		
		if(tmp.equalsIgnoreCase("-h")||tmp.equalsIgnoreCase("--help")){
			str.append("[USAGE : ]");
			str.append("vmman");
			str.append("    show VM_ID");
			str.append("        [ --help] |-h ");
			System.out.println(str);
			return str.toString();
		}else{
			vmID = tmp;
			try{
				vID =new Integer(vmID);
			}catch (NumberFormatException e){
				str.append("[ERROR : ] VM ID has to be a number!");
				System.out.println(str);
				return str.toString();
			}
	
					if(getVm(vID)==null){
						str.append("[ERROR : ] VM ID doesn't exist!");
						System.out.println(str);
						return str.toString();
					}
			

		}
		String tName = String.format("%-11s", "CLOUD");
		String tID =String.format("%-11s", "ID ");
		String tStat = String.format("%-11s", "STATUS");
		String tpriIP = String.format("%-11s", "privateIP");
		String tpubIP = String.format("%-11s", "publicIP");
		String tinID = String.format("%-11s", "internalID");
		String tlauTime = String.format("%-11s", "launchTime");
		String activity = String.format("%-11s", "Activity");
		Vm vm = getVm(vID);
		
		String fName = String.format("%-16s", vm.getCloudName());
		String fID =String.format("%-16s", vm.getuId());
		String fStat = String.format("%-16s", vm.getState());
		String fpriIP = String.format("%-16s", vm.getPrivateIP());
		String fpubIP =String.format("%-16s", vm.getPubicIP());
		String finID = String.format("%-16s", vm.getId());
		String flauTime = String.format("%-16s", vm.getTime());
		String factivity = String.format("%-16s", vm.isIdle());
		
		str.append("---------------------------------------"+System.getProperty("line.separator"));
		str.append(tID+" : "+fID+System.getProperty("line.separator"));
		str.append(tName+" : "+fName+System.getProperty("line.separator"));
		str.append(tinID+" : "+finID+System.getProperty("line.separator"));
		str.append(tStat+" : "+fStat+System.getProperty("line.separator"));
		str.append(tpriIP+" : "+fpriIP+System.getProperty("line.separator"));
		str.append(tpubIP+" : "+fpubIP+System.getProperty("line.separator"));	
		str.append(activity+" : "+factivity+System.getProperty("line.separator"));
		str.append(tlauTime+" : "+flauTime+System.getProperty("line.separator"));
		str.append("---------------------------------------"+System.getProperty("line.separator"));
		
		if(cmd.getUi().equals(uiType.CMDLINE))System.out.println(str);
		return str.toString();
	}

	/**
	 * Terminate the given virtual machine by following the command line.
	 * @param cmdLine, the string of command line
	 * @see Command
	 */
	public static String destroyVM(CmdComb cmd) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		
		String vmID = "";
		Integer vID;
		Vm vm;
		if(cmd.getParaset().size()==0){
			str.append("[USAGE : ]");
			str.append("vmman");
			str.append("    destroy VM_ID");
			str.append("        [ --help] |-h ");
			if(cmd.getUi().equals(uiType.CMDLINE))System.out.println(str);
			return str.toString();
		}
		
		String tmp = cmd.getParaset().get(0);
		
		if(tmp.equalsIgnoreCase("-h")||tmp.equalsIgnoreCase("--help")){
			str.append("[USAGE : ]");
			str.append("vmman");
			str.append("    destroy VM_ID");
			str.append("        [ --help] |-h ");
			System.out.println(str);
			return str.toString();
		}else{
			vmID = tmp;
			try{
				vID =new Integer(vmID);
			}catch (NumberFormatException e){
				str.append("[ERROR : ] VM ID has to be a number!");
				return str.toString();
			}
			vm = getVm(vID);
			
			if(vm==null){
				str.append("[ERROR : ] VM ID doesn't exist!");
				System.out.println(str);
				return str.toString();
			}

		}
		
		String cloudName = vm.getCloudName();
		Cloud cloud = CloudManager.getCloudList().get(cloudName);
		cloud.destroyVM(vm);
	
		return str.toString();
	}

	
	/**
	 * Suspend the given virtual machine by following the command line.the virtual machine's original status must be running.
	 * @param cmdLine, the string of command line
	 * @see Command
	 */
	public static String suspendVM(CmdComb cmd) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();	
		String cloudName = "";
		String vmID = "";
		Vm vm;
		if(cmd.getParaset().size()==0){
			str.append("[USAGE : ]"+System.getProperty("line.separator"));
			str.append("vmman"+System.getProperty("line.separator"));
			str.append("    suspend VM_ID"+System.getProperty("line.separator"));
			str.append("        [ --help] |-h "+System.getProperty("line.separator"));
			System.out.println(str);
			return str.toString();
		}
		Integer vID;
		
		String tmp = cmd.getParaset().get(0);
		
		if(tmp.equalsIgnoreCase("-h")||tmp.equalsIgnoreCase("--help")){
			str.append("[USAGE : ]"+System.getProperty("line.separator"));
			str.append("vmman"+System.getProperty("line.separator"));
			str.append("    suspend VM_ID"+System.getProperty("line.separator"));
			str.append("        [ --help] |-h "+System.getProperty("line.separator"));
			System.out.println(str);
			return str.toString();
		}else{
			vmID = tmp;
			try{
				vID =new Integer(vmID);
			}catch (NumberFormatException e){
				str.append("[ERROR : ] VM ID has to be a number!");
				System.out.println(str);
				return str.toString();
			}
			vm = getVm(vID);
			if(vm==null){
				str.append("[ERROR : ] VM ID doesn't exist!");
				System.out.println(str);
				return str.toString();
			}

		}
		cloudName = vm.getCloudName();
		String inId = vm.getId();
		Cloud cloud = CloudManager.getCloudList().get(cloudName);
		cloud.suspendVM(inId);
		if(cmd.getUi().equals(uiType.CMDLINE))System.out.println(str);
		return str.toString();
	}

	
	/**
	 * Start the given suspended virtual machine.
	 * @param cmdLine, the string of command line.
	 * @see Command
	 */
	public static String startVM(CmdComb cmd) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();	
		String cloudName = "";
		String vmID = "";
		Vm vm;
		if(cmd.getParaset().size()==0){
			str.append("[USAGE : ]"+System.getProperty("line.separator"));
			str.append("vmman"+System.getProperty("line.separator"));
			str.append("    start VM_ID"+System.getProperty("line.separator"));
			str.append("        [ --help] |-h "+System.getProperty("line.separator"));
			System.out.println(str);
			return str.toString();
		}
		Integer vID;
		
		String tmp = cmd.getParaset().get(0);
		
		if(tmp.equalsIgnoreCase("-h")||tmp.equalsIgnoreCase("--help")){
			str.append("[USAGE : ]"+System.getProperty("line.separator"));
			str.append("vmman"+System.getProperty("line.separator"));
			str.append("    start VM_ID"+System.getProperty("line.separator"));
			str.append("        [ --help] |-h "+System.getProperty("line.separator"));
			System.out.println(str);
			return str.toString();
		}else{
			vmID = tmp;
			try{
				vID =new Integer(vmID);
			}catch (NumberFormatException e){
				str.append("[ERROR : ] VM ID has to be a number!");
				System.out.println(str);
				return str.toString();
			}
			vm = getVm(vID);
			if(vm==null){
				str.append("[ERROR : ] VM ID doesn't exist!");
				System.out.println(str);
				return str.toString();
			}

		}
		cloudName = vm.getCloudName();
		String inId = vm.getId();
		Cloud cloud = CloudManager.getCloudList().get(cloudName);
		cloud.startVM(inId);
		
		return str.toString();
	}

	
	/**
	 * Get the current Id of virtual machine.when a virtual machine is created, this function would be invoked.
	 * @return id, 
	 */
	public static int generateId() {
		return id++;
	}


	/**
	 *Migrate a given virtual machine to a specific host by following the command line
	 *@param cmdLine, the string of command line. 
	 */
	public static String migrate(CmdComb cmd) {
		// TODO Auto-generated method stub
		StringBuffer  str = new StringBuffer();
		if (cmd.getParaset().size()==0) {
			str.append("[ERROR : ] Expect a cloud name!");
			System.out.println(str);
			return str.toString();
		}
		String cloudname = cmd.getParaset().get(0).trim();
		if (cmd.getParaset().size()<2) {
			str.append("[ERROR : ] Expect the source VM id!");
			System.out.println(str);
			return str.toString();
		}
		String vmID = cmd.getParaset().get(1);
		if (cmd.getParaset().size()<3) {
			str.append("[ERROR : ] Expect a target host id!");
			System.out.println(str);
			return str.toString();
		}
		String hostID =cmd.getParaset().get(2);
		
		
		Cloud cloud = CloudManager.getCloudList().get(cloudname);
		
		
		Integer vID;
		Vm vm ;
		try{
			vID =new Integer(vmID);
		}catch (NumberFormatException e){
			str.append("[ERROR : ] VM ID has to be a number!");
			System.out.println(str);
			return str.toString();
		}
		vm = getVm(vID);
		if(vm==null){
			str.append("[ERROR : ] VM ID doesn't exist!");
			System.out.println(str);
			return str.toString();
		}

	String inId = vm.getId();
		
		String result = cloud.migrate(inId, hostID);
		if(cmd.getUi().equals(uiType.CMDLINE))System.out.println(result);
		return result;
	}	
	
	public Vm getVm(String uid){
		for(Cloud cloud : CloudManager.getCloudList().values()){
			for(Vm vm :cloud.getVmList().values()){
				if(vm.getuId().toString().equalsIgnoreCase(uid))
					return vm;
			}
		}
		return null;
	}
	
	public ArrayList<Vm> getVmList(){
		ArrayList<Vm> vmlist = new ArrayList<Vm>();
		for(Cloud cloud : CloudManager.getCloudList().values()){
			vmlist.addAll(cloud.getVmList().values());
		}
		return vmlist;
	}
	
	/**
	 *The virtual machine's id, it's the global id in vcluster,  cloud independent.
	 */
	private static int id;
 
   }


