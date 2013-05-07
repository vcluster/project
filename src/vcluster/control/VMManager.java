package vcluster.control;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;

import vcluster.control.VMMessage.VMMsgType;
import vcluster.control.cloudman.CloudElement;
import vcluster.engine.groupexecutor.CloudExecutor;
import vcluster.global.Config;
import vcluster.global.Config.CloudType;
import vcluster.global.Config.VMState;
import vcluster.ui.Command;
import vcluster.util.PrintMsg;
import vcluster.util.PrintMsg.DMsgType;

public class VMManager extends Thread {

	public VMManager()
	{
		vmList = new Hashtable<Integer, VMElement>();
		
		msgQueue =  new ArrayBlockingQueue <VMMessage>(100);
		vecTempID = new Vector<Integer>();
	}

	public BlockingQueue <VMMessage> getMsgQueue() {
		return msgQueue;
	}

	public void run() {
		VMMessage aMsg = null;
		while(!done) {
			try {
				aMsg = msgQueue.take();
				PrintMsg.print(DMsgType.MSG, "message type = "+aMsg.toString());

				processMessage(aMsg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean processMessage(VMMessage aMsg) {
		
		VMMsgType aType = aMsg.getMessageType();
		int numVMs = aMsg.getPrivateData();
		
		switch(aType) {
		case LAUNCH: return launchVM(numVMs, null);
		}
		return false;
	}
	
	public void dump()
	{
		System.out.println("----------------------------------------");
		System.out.println("\tVM Manager");
		System.out.println("----------------------------------------");

		Vector <Integer>v = new Vector <Integer>(vmList.keySet());
		Collections.sort(v);
		Iterator <Integer> it = v.iterator();
		while(it.hasNext()) {
			Integer key = it.next();
	    	VMElement vm = (VMElement)vmList.get(key);
			System.out.println("\tID: "+vm.getInstanceId()+"\t"+vm.stringVMState());
		}
		System.out.println("----------------------------------------");
	}
	
	public void shutDwon() 
	{
		/* notify to the other services, e.g. monitoring */
		msgQueue.clear();
		msgQueue = null;
		
		done = true;
	}
	
	public boolean addVMElement(int id, VMState state) 
	{
		VMElement vm = new VMElement(id, state);
		Object ret = vmList.put(new Integer(id), (VMElement)vm);
		if (ret != null) return false;
		return true;
	}

	
	public boolean launchVM(int vms, CloudElement c) 
	{
		CloudElement cloud;
		if (c == null) {
			cloud = Config.cloudMan.findCloudSystem(vms);
		}
		else {
			cloud = c;
		}
		
		CloudType ctype = cloud.getCloudType();
		
		switch(ctype) {
		case PRIVATE: return OCALaunch(cloud, vms);
		case PUBLIC: return RESTLaunch(cloud, vms);
		}
		return true;
	}


	private boolean OCALaunch(CloudElement cloud, int numVMs) 
	{
		
		Client oneClient = null;
		
		String account = ""+cloud.getAccessKey()+":"+cloud.getSecretKey();
		try {
			oneClient = new Client(account, cloud.getEndPoint()); 
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		for(int i = 0; i < numVMs; i++) {
			OneResponse rc = VirtualMachine.allocate(oneClient, vmTemplate);
			
			if(rc.isError()) {
				PrintMsg.print(DMsgType.ERROR, "failed to launch vms"+rc.getErrorMessage());
			}
			int newVMID = Integer.parseInt(rc.getMessage());
			vecTempID.add(newVMID);

			PrintMsg.print(DMsgType.MSG, "O.K. ID = " + newVMID);
			
			VMElement vmElement = new VMElement(newVMID, VMState.NOT_DEFINED);
			vmList.put(newVMID, vmElement);
		}


		/* this algorithm has to be modified for better
		 * performance.
		 * 
		 * it also has to handle only some vms running...
		 */
		int sleepsec = 20; /* 20 seconds as default */

		while (!isAllVMRunning(oneClient)) {
			try {
				PrintMsg.print(DMsgType.MSG, "Going to sleep....");
				Thread.sleep(sleepsec*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		vecTempID.removeAllElements();
		PrintMsg.print(DMsgType.MSG, "All running.....");
		PrintMsg.print(DMsgType.MSG, "Notified to Monitor Manager");
		
		/* have to increase currently running vms 
		 * because all cloud systems have to be managed by cloud manager
		 */
		Config.cloudMan.incCurrentVMs(cloud, numVMs);
		
		synchronized (Config.monMan) {
			Config.monMan.notify();
		}
		
		return true;
	}


	
	private boolean RESTLaunch(CloudElement cloud, int numVMs)
	{
		PrintMsg.print(DMsgType.MSG, "launching "+numVMs+" vms using REST API.");
	    
	    for(int i = 0 ; i < numVMs; i++) {
	    	CloudExecutor.rest_launch(cloud);
	    }
	    
	    return true;
	}

	
	private boolean isAllVMRunning(Client oneClient) {

		int vmID;
		boolean allRunning = true;
		for(int i = 0; i < vecTempID.size(); i++) {
			vmID = vecTempID.elementAt(i).intValue();
			
			VirtualMachine vm = new VirtualMachine(vmID, oneClient);
			
			/* to load the data first using the info method */
			OneResponse rc = vm.info();
			String status = vm.status();
			
			PrintMsg.print(DMsgType.MSG, "vm "+vm.getName()+", id " + vm.getId()
					+", status = "+status);

			VMElement element = findVMElement(vmID);
			VMState newVMState = getVMState(status);
			if (newVMState != element.getState()) {
				
				PrintMsg.print(DMsgType.MSG, "state update: "+element.getState()
						+" --> "+newVMState);

				/* update vm state */
				updateVMState(vmList.get(vmID), newVMState);
				
				PrintMsg.print(DMsgType.MSG, "new state, " + vmList.get(vmID).getState());
			}
			
			if(status == null || !status.equalsIgnoreCase("runn")) 
				allRunning = false;
		}
		return allRunning;
	}
	
	private void updateVMState(VMElement e, VMState state) {
		e.setVMState(state);
	}
	
	public VMState getVMState(String vmState) {
		
		if(vmState == null) return VMState.NOT_DEFINED;

		if(vmState.equalsIgnoreCase("runn")) return VMState.RUNNING;
		if(vmState.equalsIgnoreCase("penn")) return VMState.PENDING;
		if(vmState.equalsIgnoreCase("prol")) return VMState.PROLOG;
		
		return VMState.NOT_DEFINED;
	}

	
	private VMElement findVMElement(int id) {
		return vmList.get(id);
	}
	
	
	private class VMElement {
		public VMElement(int id, VMState state) {
			instanceID = id;
			vmstate = state;
		}
		
		public int getInstanceId() {
			return instanceID;
		}
		
		
		public VMState getState() {
			return vmstate;
		}
		
		public void setVMState(VMState state) {
			vmstate = state;
		}
		
		public String stringVMState() {
			
			switch(vmstate) {
			case PENDING: return "PENDING";
			case RUNNING: return "RUNNING";
			case STOP: return "STOP";
			case SUSPEND: return "SUSPEND";
			case PROLOG: return "PROLOG";
			case NOT_DEFINED: return "NOT_DEFINED";
			}
			return "NOT_DEFINED";
		}
		
		
		private int instanceID = -1;
		private VMState vmstate = VMState.PENDING;
	}
	

	private boolean done = false;
	private BlockingQueue <VMMessage> msgQueue;

	private Vector<Integer> vecTempID = null;
    protected Hashtable <Integer, VMElement> vmList = null;
   

    /* it has to be moved to a configuration file later */
    
	private static String vmTemplate =
        "NAME   = vcl-wn \n"
        + "VCPU    = 1 \n"
        + "MEMORY = 256 \n"
        + "PUBLIC = YES \n"
        + "DISK   = [\n"
        + "\tsource   = /var/lib/one/image-repo/b9140219c2cacb22fa01396122d1446ce14ca5e3,\n"
        + "\tsave	   = yes, \n"
        + "\ttarget   = vda, \n"
        + "\treadonly = no ]\n"
        + "DISK   = [ \n"
        + "\ttype     = swap,\n"
        + "\tsize     = 2048,\n"
        + "\ttarget   = vdb ]\n"
        + "NIC    = [ NETWORK = \"FermiCloud\", \n"
        + "\tMODEL = virtio ]\n"
        + "FEATURES=[ acpi=\"yes\" ]\n"
        + "GRAPHICS = [\n"
        + "\ttype    = \"vnc\", \n"
        + "\tlisten  = \"127.0.0.1\", \n"
        + "\tport    = \"-1\", \n"
        + "\tautoport = \"yes\", \n"
        + "\tkeymap = \"en-us\"]\n"
        + "CONTEXT = [ \n"
        + "\tip_public   = \"$NIC[IP, NETWORK=\\\"FermiCloud\\\"]\", \n"
        + "\tnetmask     = \"255.255.254.0\", \n"
        + "\tgateway     = \"131.225.154.1\", \n"
        + "\tns          = \"131.225.8.120\", \n"
        + "\tfiles      = \"/cloud/images/OpenNebula/templates/init.sh /home/rsyoung/OpenNebula/k5login\",\n"
        + "\ttarget      = \"hdc\", \n"
        + "\troot_pubkey = \"id_dsa.pub\", \n"
        + "\tusername    = \"opennebula\", \n"
        + "\tuser_pubkey = \"id_dsa.pub\" \n"
        + "]\n"
        + "REQUIREMENTS = \"HYPERVISOR=\\\"kvm\\\"\"\n"
        + "RANK =\"- RUNNING_VMS\"";
}


