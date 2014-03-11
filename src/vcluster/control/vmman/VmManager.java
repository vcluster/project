package vcluster.control.vmman;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeMap;

import vcluster.control.cloudman.Cloud;
import vcluster.control.cloudman.CloudManager;
import vcluster.global.Config;
import vcluster.global.Config.VMState;

public class VmManager extends Thread {

	static{
		vmList = new TreeMap<Integer, Vm>();
		id = 0;
	}

	public static VMState getVMState(String vmState) {
		
		if(vmState == null) return VMState.NOT_DEFINED;

		if(vmState.equalsIgnoreCase("runn")) return VMState.RUNNING;
		if(vmState.equalsIgnoreCase("penn")) return VMState.PENDING;
		if(vmState.equalsIgnoreCase("prol")) return VMState.PROLOG;
		
		return VMState.NOT_DEFINED;
	}

	
	public static boolean createVM(String cmdLine) {
		// TODO Auto-generated method stub
		Cloud currCloud = CloudManager.getCurrentCloud();
		int vms = 1;
		String [] str = cmdLine.split(" ");
		ArrayList<String> cmdList = new ArrayList<String>();
		for(String s : str){
			cmdList.add(s);
			//System.out.println(s);
		}
		if(cmdList.size()>1&&cmdList.get(1).equalsIgnoreCase("--help")){
			System.out.println("[USAGE : ]");
			System.out.println("vmman");
			System.out.println("    create");
			System.out.println("        [ --help] | ");
			System.out.println("        [ -n NUM_VMS | --num=NUM_VMS ] | ");
			System.out.println("        [ -c CLOUDNAME | --name=CLOUDNAME]");	
			
			return false;
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
			System.out.println("[ERROR : ] Cloud doesn't exist! please check the cloud name...");
			System.out.println("");
			return false;
		}
		vms = Integer.parseInt(cmdList.get(4));
			return currCloud.createVM(vms,cmdList.get(6));

	}

	public static boolean listVM(String cmdLine) {
		// TODO Auto-generated method stu
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
						System.out.println("[ERROR : ] "+ name+ " has not been loaded!");
						return false;						
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
					System.out.println("[ERROR : ] Expected a cloud name!");
					return false;
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
		System.out.println(line);
		System.out.println(tID+tInternalID+tStat+tActivity+tPrivateIp+tPublicIp+tName);
		System.out.println(line);
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
					
					
					if(vmList.get(id).getState().equals(Config.VMState.RUNNING))runNum++;
					System.out.println(fID+fInternalID+fStat+fActivity+fPrivateIp+fPublicIp+fName);
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
					
					
					if(vmList.get(id).getState().equals(Config.VMState.RUNNING))runNum++;
					System.out.println(fID+fInternalID+fStat+fActivity+fPrivateIp+fPublicIp+fName);
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
					
					
					if(vmList.get(id).getState().equals(Config.VMState.RUNNING))runNum++;
					System.out.println(fID+fInternalID+fStat+fActivity+fPrivateIp+fPublicIp+fName);
					totalNum++;
				}
			}
			System.out.println(line);
			System.out.println("      Running VMs :  "+runNum+"/"+totalNum);
			System.out.println(line);
		return true;
	}

	public static boolean showVM(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);		
		String vmID = "";
		Integer vID;
		st.nextToken();
		if(!st.hasMoreTokens()){
			System.out.println("[USAGE : ]");
			System.out.println("vmman");
			System.out.println("    show VM_ID");
			System.out.println("        [ --help] |-h ");
			return false;
		}

		if(!st.hasMoreTokens()){System.out.println("[ERROR : ] Expected a VM ID");			return false;}
		String tmp = st.nextToken();
		
		if(tmp.equalsIgnoreCase("-h")||tmp.equalsIgnoreCase("--help")){
			System.out.println("[USAGE : ]");
			System.out.println("vmman");
			System.out.println("    show VM_ID");
			System.out.println("        [ --help] |-h ");
			return false;
		}else{
			vmID = tmp;
			try{
				vID =new Integer(vmID);
			}catch (NumberFormatException e){
				System.out.println("[ERROR : ] VM ID has to be a number!");
				return false;
			}
			if(!vmList.keySet().contains(vID)){
				System.out.println("[ERROR : ] VM ID doesn't exist!");
				return false;
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
		
		System.out.println("---------------------------------------");
		System.out.println(tID+" : "+fID);
		System.out.println(tName+" : "+fName);
		System.out.println(tinID+" : "+finID);
		System.out.println(tStat+" : "+fStat);
		System.out.println(tpriIP+" : "+fpriIP);
		System.out.println(tpubIP+" : "+fpubIP);	
		System.out.println(activity+" : "+factivity);
		System.out.println(tlauTime+" : "+flauTime);
		System.out.println("---------------------------------------");
		
		return false;
	}

	public static boolean destroyVM(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);		
		String cloudName = "";
		String vmID = "";
		Integer vID;
		st.nextToken();
		if(!st.hasMoreTokens()){
			System.out.println("[USAGE : ]");
			System.out.println("vmman");
			System.out.println("    destroy VM_ID");
			System.out.println("        [ --help] |-h ");
			return false;
		}
		if(!st.hasMoreTokens()){System.out.println("[ERROR : ] Expected a VM ID");			return false;}
		String tmp = st.nextToken();
		
		if(tmp.equalsIgnoreCase("-h")||tmp.equalsIgnoreCase("--help")){
			System.out.println("[USAGE : ]");
			System.out.println("vmman");
			System.out.println("    destroy VM_ID");
			System.out.println("        [ --help] |-h ");
			return false;
		}else{
			vmID = tmp;
			try{
				vID =new Integer(vmID);
			}catch (NumberFormatException e){
				System.out.println("[ERROR : ] VM ID has to be a number!");
				return false;
			}
			if(!vmList.keySet().contains(vID)){
				System.out.println("[ERROR : ] VM ID doesn't exist!");
				return false;
			}

		}
		cloudName = vmList.get(vID).getCloudName();
		String inId = vmList.get(vID).getId();
		Cloud cloud = CloudManager.getCloudList().get(cloudName);
		cloud.destroyVM(inId);
		vmList.remove(vID);
		return true;
	}

	public static boolean suspendVM(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);		
		String cloudName = "";
		String vmID = "";
		st.nextToken();
		if(!st.hasMoreTokens()){
			System.out.println("[USAGE : ]");
			System.out.println("vmman");
			System.out.println("    suspend VM_ID");
			System.out.println("        [ --help] |-h ");
			return false;
		}
		Integer vID;
		if(!st.hasMoreTokens()){System.out.println("[ERROR : ] Expected a VM ID");			return false;}
		String tmp = st.nextToken();
		
		if(tmp.equalsIgnoreCase("-h")||tmp.equalsIgnoreCase("--help")){
			System.out.println("[USAGE : ]");
			System.out.println("vmman");
			System.out.println("    suspend VM_ID");
			System.out.println("        [ --help] |-h ");
			return false;
		}else{
			vmID = tmp;
			try{
				vID =new Integer(vmID);
			}catch (NumberFormatException e){
				System.out.println("[ERROR : ] VM ID has to be a number!");
				return false;
			}
			if(!vmList.keySet().contains(vID)){
				System.out.println("[ERROR : ] VM ID doesn't exist!");
				return false;
			}

		}
		cloudName = vmList.get(vID).getCloudName();
		String inId = vmList.get(vID).getId();
		Cloud cloud = CloudManager.getCloudList().get(cloudName);
		cloud.suspendVM(inId);
		
		return true;
	}

	public static boolean startVM(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);		
		String cloudName = "";
		String vmID = "";
		st.nextToken();
		if(!st.hasMoreTokens()){
			System.out.println("[USAGE : ]");
			System.out.println("vmman");
			System.out.println("    start VM_ID");
			System.out.println("        [ --help] |-h ");
			return false;
		}
		Integer vID;
		if(!st.hasMoreTokens()){System.out.println("[ERROR : ] Expected a VM ID");			return false;}
		String tmp = st.nextToken();
		
		if(tmp.equalsIgnoreCase("-h")||tmp.equalsIgnoreCase("--help")){
			System.out.println("[USAGE : ]");
			System.out.println("vmman");
			System.out.println("    start VM_ID");
			System.out.println("        [ --help] |-h ");
			return false;
		}else{
			vmID = tmp;
			try{
				vID =new Integer(vmID);
			}catch (NumberFormatException e){
				System.out.println("[ERROR : ] VM ID has to be a number!");
				return false;
			}
			if(!vmList.keySet().contains(vID)){
				System.out.println("[ERROR : ] VM ID doesn't exist!");
				return false;
			}

		}
		cloudName = vmList.get(vID).getCloudName();
		String inId = vmList.get(vID).getId();
		Cloud cloud = CloudManager.getCloudList().get(cloudName);
		cloud.startVM(inId);
		
		return true;
	}

	
	
	public static int getcurrId() {
		return id++;
	}



	public static TreeMap<Integer,Vm> getVmList() {
		return vmList;
	}

	public static boolean migrate(String cmdLine) {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(cmdLine);
		st.nextToken();
		if (!st.hasMoreTokens()) {
			System.out.println("[ERROR : ] Expect a cloud name!");
			return false;
		}
		String cloudname = st.nextToken().trim();
		if (!st.hasMoreTokens()) {
			System.out.println("[ERROR : ] Expect the source VM id!");
			return false;
		}
		String vmID = st.nextToken().trim();
		if (!st.hasMoreTokens()) {
			System.out.println("[ERROR : ] Expect a target host id!");
			return false;
		}
		String hostID = st.nextToken().trim();
		
		
		Cloud cloud = CloudManager.getCloudList().get(cloudname);
		
		
		Integer vID;
		try{
			vID =new Integer(vmID);
		}catch (NumberFormatException e){
			System.out.println("[ERROR : ] VM ID has to be a number!");
			return false;
		}
		if(!vmList.keySet().contains(vID)){
			System.out.println("[ERROR : ] VM ID doesn't exist!");
			return false;
		}

	String inId = vmList.get(vID).getId();
		
		
		return cloud.migrate(inId,hostID);
	}	

	private static int id;
	private static TreeMap <Integer, Vm> vmList;
 
   }


