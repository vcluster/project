package vcluster.managers;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeMap;

import vcluster.configs.Config;
import vcluster.simulators.Cloud;
import vcluster.simulators.Vm;
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
		vmList = new TreeMap<Integer, Vm>();
		id = 0;
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
	public static String createVM(String cmdLine) {
		// TODO Auto-generated method stub
		StringBuffer strBuff = new StringBuffer();
		Cloud currCloud = CloudManager.getCurrentCloud();
		int vms = 1;
		String [] str = cmdLine.split(" ");
		ArrayList<String> cmdList = new ArrayList<String>();
		for(String s : str){
			cmdList.add(s);
			//System.out.println(s);
		}
		if(cmdList.size()>1&&cmdList.get(1).equalsIgnoreCase("--help")){
			strBuff.append("[USAGE : ]"+System.getProperty("line.separator"));
			strBuff.append("vmman"+System.getProperty("line.separator"));
			strBuff.append("    create"+System.getProperty("line.separator"));
			strBuff.append("        [ --help] | "+System.getProperty("line.separator"));
			strBuff.append("        [ -n NUM_VMS | --num=NUM_VMS ] | "+System.getProperty("line.separator"));
			strBuff.append("        [ -c CLOUDNAME | --name=CLOUDNAME]"+System.getProperty("line.separator"));	
			System.out.println(strBuff);
			return strBuff.toString();
		}
		if(cmdList.size()==1){
			cmdList.add("-c");
			cmdList.add(currCloud.getCloudName());
			cmdList.add("-n");
			cmdList.add("1");
		}
		if(!cmdList.get(1).equals("-c")){
			cmdList.add(1,"-c");
			cmdList.add(2,currCloud.getCloudName());
		}

		if(cmdList.size()<4||!cmdList.get(3).equalsIgnoreCase("-n")){
			cmdList.add(3, "-n");
			cmdList.add(4, "1");
		}
		if(cmdList.size()<6 || !cmdList.get(5).equalsIgnoreCase("-h")){
			cmdList.add(5, "-h");
			cmdList.add(6, "host1");
		}
		
		System.out.println("");
		currCloud = CloudManager.getCloudList().get(cmdList.get(2));
		if(currCloud==null){
			strBuff.append("[ERROR : ] Cloud doesn't exist! please check the cloud name..."+System.getProperty("line.separator"));
			System.out.println(strBuff);
			return strBuff.toString();
		}
		vms = Integer.parseInt(cmdList.get(4));
			return currCloud.createVM(vms,cmdList.get(6));

	}

	/**
	 * List up the virtual machines by following the command line
	 * @param cmdLine, the string of command line
	 * @see Command
	 */
	public static String listVM(String cmdLine) {
		// TODO Auto-generated method stu
		StringBuffer str = new StringBuffer();
		String filter ="";
		String cloudFilter="";
		StringTokenizer st = new StringTokenizer(cmdLine);		
		st.nextToken();
//		if(!st.hasMoreTokens()){System.out.println("[ERROR : ] Expected a VM ID");			return false;}
		if( st.hasMoreTokens()){
			String tmp = st.nextToken();
			TreeMap<String,Cloud> cc = new TreeMap<String,Cloud>();			
			if(tmp.equalsIgnoreCase("-refresh")){
				String name="";
				if(st.hasMoreTokens()){
					name = st.nextToken();
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
				TreeMap<Integer,Vm> temp = new TreeMap<Integer,Vm>();
				for(Cloud cloud : cc.values()){
					if(cloud.isLoaded()){
						cloud.listVMs();
						if(getVmList()==null)continue;
						for(Vm vm : cloud.getVmList().values()){	
							boolean flag = true;
							for(Integer id:vmList.keySet()){
								if(vm.getId().equals(vmList.get(id).getId())){
									temp.put(id, vm);
									flag = false;
									break;
								}
							}	
							if(flag){
								Integer uId = new Integer(VmManager.getcurrId());
									vm.setuId(uId);
									temp.put(uId, vm);
							}
						}
					}
				}

				for(Integer id:temp.keySet()){
					
					vmList.put(id, temp.get(id));
					
					
				}
				ArrayList<Integer> ids = new ArrayList<Integer>();
				
				for(Integer id:vmList.keySet()){
					boolean flag = true;
					boolean flagx = false;
					for(Cloud cloud:CloudManager.getCloudList().values()){
						if(cloud.isLoaded()){
							for(Vm vm : cloud.getVmList().values()){
								if(vm.getId().equals(vmList.get(id).getId())){
									flag = false;
									flagx = true;
									
									break;
								}
							}
							if(flagx){flagx = false;break;}
					}
					}
					if(flag)ids.add(id);					
				}
				for(Integer id:ids){
					vmList.remove(id);
				}
				
			}else if(tmp.equalsIgnoreCase("-a")||tmp.equalsIgnoreCase("--type=available")){
				filter="RUNNING";
			}else if(tmp.equalsIgnoreCase("-r")||tmp.equalsIgnoreCase("--type=running")){
				filter="RUNNING";
			}else if(tmp.equalsIgnoreCase("-s")||tmp.equalsIgnoreCase("--type=suspended")){
				filter="SUSPEND";
			}else if(tmp.equalsIgnoreCase("-n")){
				if(st.hasMoreTokens()){
					cloudFilter = st.nextToken();
				}else{
					str.append("[ERROR : ] Expected a cloud name!"+System.getProperty("line.separator"));
					System.out.println(str);
					return str.toString();
				}
			}
		}	
		
		String tName = String.format("%-10s", "CLOUD");
		String tID =String.format("%-5s", "ID");
		String tStat = String.format("%-10s", "STATUS");
		String tPrivateIp =  String.format("%-18s", "PrivateIP");
		String tPublicIp =  String.format("%-18s", "PublicIP");
		String tActivity =  String.format("%-10s", "Activity");
		String tInternalID =  String.format("%-12s", "IntnlID");
		String line =  "---------------------------------------------------------------------------------------";
		str.append(line+System.getProperty("line.separator"));
		str.append(tID+tInternalID+tStat+tActivity+tPrivateIp+tPublicIp+tName+System.getProperty("line.separator"));
		str.append(line+System.getProperty("line.separator"));
			int runNum = 0;
			int totalNum = 0;
			for(Integer id : vmList.keySet()){
				Vm vm = vmList.get(id);
				if(!cloudFilter.equals("")&&vmList.get(id).getCloudName().equals(cloudFilter)){
					String fName = String.format("%-10s", vmList.get(id).getCloudName());
					String fID =String.format("%-5s", id);
					String fStat = String.format("%-10s", vmList.get(id).getState());
					String fPrivateIp =  String.format("%-18s", vm.getPrivateIP());
					String fPublicIp =  String.format("%-18s", vm.getPubicIP());
					String fActivity =  String.format("%-10s", vm.isIdle());
					String fInternalID =  String.format("%-12s", vm.getId());
					
					
					if(vmList.get(id).getState().equals(VMState.RUNNING))runNum++;
					str.append(fID+fInternalID+fStat+fActivity+fPrivateIp+fPublicIp+fName+System.getProperty("line.separator"));
					totalNum++;
				}
				if(!filter.equals("")&&vmList.get(id).getState().toString().equals(filter)){
					String fName = String.format("%-10s", vmList.get(id).getCloudName());
					String fID =String.format("%-5s", id);
					String fStat = String.format("%-10s", vmList.get(id).getState());
					String fPrivateIp =  String.format("%-18s", vm.getPrivateIP());
					String fPublicIp =  String.format("%-18s", vm.getPubicIP());
					String fActivity =  String.format("%-10s", vm.isIdle());
					String fInternalID =  String.format("%-12s", vm.getId());
					
					
					if(vmList.get(id).getState().equals(VMState.RUNNING))runNum++;
					str.append(fID+fInternalID+fStat+fActivity+fPrivateIp+fPublicIp+fName+System.getProperty("line.separator"));
					totalNum++;
				}
				if(filter.equals("")&cloudFilter.equals("")){
					String fName = String.format("%-10s", vmList.get(id).getCloudName());
					String fID =String.format("%-5s", id);
					String fStat = String.format("%-10s", vmList.get(id).getState());
					String fPrivateIp =  String.format("%-18s", vm.getPrivateIP());
					String fPublicIp =  String.format("%-18s", vm.getPubicIP());
					String fActivity =  String.format("%-10s", vm.isIdle());
					String fInternalID =  String.format("%-12s", vm.getId());
					
					
					if(vmList.get(id).getState().equals(VMState.RUNNING))runNum++;
					str.append(fID+fInternalID+fStat+fActivity+fPrivateIp+fPublicIp+fName+System.getProperty("line.separator"));
					totalNum++;
				}
			}
			str.append(line+System.getProperty("line.separator"));
			str.append("      Running VMs :  "+runNum+"/"+totalNum+System.getProperty("line.separator"));
			str.append(line+System.getProperty("line.separator"));
			System.out.println(str);
		return str.toString();
	}

	/**
	 * Show the virtual machine's information in detail,such as ID, public IP, private IP, launch time, etc.
	 * @param cmdLine, the command line string.
	 * @see Command
	 */
	public static String showVM(String cmdLine) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		StringTokenizer st = new StringTokenizer(cmdLine);		
		String vmID = "";
		Integer vID;
		st.nextToken();
		if(!st.hasMoreTokens()){
			str.append("[USAGE : ]"+System.getProperty("line.separator"));
			str.append("vmman"+System.getProperty("line.separator"));
			str.append("    show VM_ID"+System.getProperty("line.separator"));
			str.append("        [ --help] |-h "+System.getProperty("line.separator"));
			System.out.println(str);
			return str.toString();
		}

		if(!st.hasMoreTokens()){
				str.append("[ERROR : ] Expected a VM ID");
				System.out.println(str);
				return str.toString();
			}
		String tmp = st.nextToken();
		
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
			if(!vmList.keySet().contains(vID)){
				
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
		Integer id = vID;
		
		String fName = String.format("%-16s", vmList.get(id).getCloudName());
		String fID =String.format("%-16s", id);
		String fStat = String.format("%-16s", vmList.get(id).getState());
		String fpriIP = String.format("%-16s", vmList.get(id).getPrivateIP());
		String fpubIP =String.format("%-16s", vmList.get(id).getPubicIP());
		String finID = String.format("%-16s", vmList.get(id).getId());
		String flauTime = String.format("%-16s", vmList.get(id).getTime());
		String factivity = String.format("%-16s", vmList.get(id).isIdle());
		
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
		
		System.out.println(str);
		return str.toString();
	}

	/**
	 * Terminate the given virtual machine by following the command line.
	 * @param cmdLine, the string of command line
	 * @see Command
	 */
	public static String destroyVM(String cmdLine) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		StringTokenizer st = new StringTokenizer(cmdLine);		
		String cloudName = "";
		String vmID = "";
		Integer vID;
		st.nextToken();
		if(!st.hasMoreTokens()){
			str.append("[USAGE : ]");
			str.append("vmman");
			str.append("    destroy VM_ID");
			str.append("        [ --help] |-h ");
			System.out.println(str);
			return str.toString();
		}
		if(!st.hasMoreTokens()){str.append("[ERROR : ] Expected a VM ID");	System.out.println(str);		return str.toString();}
		String tmp = st.nextToken();
		
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
			if(!vmList.keySet().contains(vID)){
				str.append("[ERROR : ] VM ID doesn't exist!");
				System.out.println(str);
				return str.toString();
			}

		}
		cloudName = vmList.get(vID).getCloudName();
		String inId = vmList.get(vID).getId();
		Cloud cloud = CloudManager.getCloudList().get(cloudName);
		cloud.destroyVM(inId);
		vmList.remove(vID);
		return str.toString();
	}

	
	/**
	 * Suspend the given virtual machine by following the command line.the virtual machine's original status must be running.
	 * @param cmdLine, the string of command line
	 * @see Command
	 */
	public static String suspendVM(String cmdLine) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		StringTokenizer st = new StringTokenizer(cmdLine);		
		String cloudName = "";
		String vmID = "";
		st.nextToken();
		if(!st.hasMoreTokens()){
			str.append("[USAGE : ]"+System.getProperty("line.separator"));
			str.append("vmman"+System.getProperty("line.separator"));
			str.append("    suspend VM_ID"+System.getProperty("line.separator"));
			str.append("        [ --help] |-h "+System.getProperty("line.separator"));
			System.out.println(str);
			return str.toString();
		}
		Integer vID;
		if(!st.hasMoreTokens()){str.append("[ERROR : ] Expected a VM ID");	System.out.println(str);		return str.toString();}
		String tmp = st.nextToken();
		
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
			if(!vmList.keySet().contains(vID)){
				str.append("[ERROR : ] VM ID doesn't exist!");
				System.out.println(str);
				return str.toString();
			}

		}
		cloudName = vmList.get(vID).getCloudName();
		String inId = vmList.get(vID).getId();
		Cloud cloud = CloudManager.getCloudList().get(cloudName);
		cloud.suspendVM(inId);
		
		return str.toString();
	}

	
	/**
	 * Start the given suspended virtual machine.
	 * @param cmdLine, the string of command line.
	 * @see Command
	 */
	public static String startVM(String cmdLine) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		StringTokenizer st = new StringTokenizer(cmdLine);		
		String cloudName = "";
		String vmID = "";
		st.nextToken();
		if(!st.hasMoreTokens()){
			str.append("[USAGE : ]"+System.getProperty("line.separator"));
			str.append("vmman"+System.getProperty("line.separator"));
			str.append("    start VM_ID"+System.getProperty("line.separator"));
			str.append("        [ --help] |-h "+System.getProperty("line.separator"));
			System.out.println(str);
			return str.toString();
		}
		Integer vID;
		if(!st.hasMoreTokens()){str.append("[ERROR : ] Expected a VM ID");System.out.println(str);			return str.toString();}
		String tmp = st.nextToken();
		
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
			if(!vmList.keySet().contains(vID)){
				str.append("[ERROR : ] VM ID doesn't exist!");
				System.out.println(str);
				return str.toString();
			}

		}
		cloudName = vmList.get(vID).getCloudName();
		String inId = vmList.get(vID).getId();
		Cloud cloud = CloudManager.getCloudList().get(cloudName);
		cloud.startVM(inId);
		
		return str.toString();
	}

	
	/**
	 * Get the current Id of virtual machine.when a virtual machine is created, this function would be invoked.
	 * @return id, 
	 */
	public static int getcurrId() {
		return id++;
	}


	/**
	 * Get the virtual machines list
	 * @return vmList, a TreeMap of virtual machine's instance, the key set is the virtual machine's id.
	 */
	public static TreeMap<Integer,Vm> getVmList() {
		return vmList;
	}

	/**
	 *Migrate a given virtual machine to a specific host by following the command line
	 *@param cmdLine, the string of command line. 
	 */
	public static String migrate(String cmdLine) {
		// TODO Auto-generated method stub
		StringBuffer  str = new StringBuffer();
		StringTokenizer st = new StringTokenizer(cmdLine);
		st.nextToken();
		if (!st.hasMoreTokens()) {
			str.append("[ERROR : ] Expect a cloud name!");
			System.out.println(str);
			return str.toString();
		}
		String cloudname = st.nextToken().trim();
		if (!st.hasMoreTokens()) {
			str.append("[ERROR : ] Expect the source VM id!");
			System.out.println(str);
			return str.toString();
		}
		String vmID = st.nextToken().trim();
		if (!st.hasMoreTokens()) {
			str.append("[ERROR : ] Expect a target host id!");
			System.out.println(str);
			return str.toString();
		}
		String hostID = st.nextToken().trim();
		
		
		Cloud cloud = CloudManager.getCloudList().get(cloudname);
		
		
		Integer vID;
		try{
			vID =new Integer(vmID);
		}catch (NumberFormatException e){
			str.append("[ERROR : ] VM ID has to be a number!");
			System.out.println(str);
			return str.toString();
		}
		if(!vmList.keySet().contains(vID)){
			str.append("[ERROR : ] VM ID doesn't exist!");
			System.out.println(str);
			return str.toString();
		}

	String inId = vmList.get(vID).getId();
		
		
		return cloud.migrate(inId,hostID);
	}	

	public enum VMState {STOP, PENDING, RUNNING, SUSPEND, PROLOG, NOT_DEFINED, FAILED }; 
	
	/**
	 *The virtual machine's id, it's the global id in vcluster,  cloud independent.
	 */
	private static int id;
	/**
	 * The virtual machines's list, the key is the id of the virtual machine, the value is the instance of a virtual machine.
	 */
	private static TreeMap <Integer, Vm> vmList;
 
   }


