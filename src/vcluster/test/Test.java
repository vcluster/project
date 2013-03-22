package vcluster.test;

import vcluster.control.VMManager;
import vcluster.global.Config.VMState;

import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;


public class Test {
	public Test() {
		
	}
	
	public static void testVMManager() {
		VMManager vmman = new VMManager();

		for(int i = 0; i < 10; i++) {
			vmman.addVMElement(i, VMState.PENDING);
		}
		
		vmman.dump();
		
	}
	
	public static void ocaLaunchVM() {
		Client oneClient;

		try {
			oneClient = new Client("rsyoung:1234", "http://fcl002.fnal.gov:2633/RPC2");

			OneResponse rc = VirtualMachine.allocate(oneClient, vmTemplate);
			
			   if( rc.isError() )
			    {
			        System.out.println( "failed!");
			        throw new Exception( rc.getErrorMessage() );
			    }
			 
			    // The response message is the new VM's ID
			    int newVMID = Integer.parseInt(rc.getMessage());
			    System.out.println("ok, ID " + newVMID + ".");

			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	public static void main(String[] args) throws Exception {
		//testVMManager();
		// ocaLaunchVM();

		
	}
	
	public static String vmTemplate =
        "NAME   = vm-wn \n"
        + "VCPU    = 1 \n"
        + "MEMORY = 256 \n"
        + "PUBLIC = YES \n"
        + "DISK   = [\n"
        + "\tsource   = /var/lib/one/image-repo/81056959931a3af14e6dd109f1a71cf4e3849ebe,\n"
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
