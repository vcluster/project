package vcluser.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import vcluster.ui.remote.RemoteInterface;
import vcluster.elements.*;
/**
 *This class is a vcluster client 
 */
public class VclusterClient {

	/**
	 *A prompt which is waiting for accepting command line from command shell input.
	 */
	private  String promptGen() {
		
	    String userCmd = "";

	    /* prompt */
	    System.out.print("vcluter@"+"KISTI"+"> ");
		
	    InputStreamReader input = new InputStreamReader(System.in);
	    BufferedReader reader = new BufferedReader(input);
	    
	    try {
		    /* get a command string */
	    	userCmd = reader.readLine(); 
	    }
	    catch(Exception e){return null;}
	    /* execution here */
	   
	    return userCmd;
	    
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {		    
			VclusterClient vc = new VclusterClient();			
			RemoteInterface ri = (RemoteInterface) Naming.lookup("rmi://"+serverAddr+":"+serverPort+"/command");
			String more = "";
			do {
				more = vc.promptGen();
				if(more.equalsIgnoreCase("quit")||more.equalsIgnoreCase("exit")){
					System.exit(0);
				}
				if(more.equalsIgnoreCase("cloudlist")){					
					ArrayList<Cloud> cloudList = ri.getCloudList();
					for(Cloud c : cloudList){
						System.out.println(c.getCloudName());
						for(Vm vm : c.getVmList().values()){
							System.out.println(vm.getId());
						}
					}
					continue;
				}				
				if(more.equalsIgnoreCase("getdata")){
					Document doc = ri.getDataStructure();
					if(doc==null)return;
					Element cloudlist = (Element)doc.getElementsByTagName("cloudList").item(0);
					NodeList nl = cloudlist.getChildNodes();
					for(int i = 0; i<nl.getLength(); i++){
						Element cloud = (Element)nl.item(i);
						
						String cloudName = cloud.getAttribute("Name");
						System.out.println(cloudName);
						String cloudType = cloud.getAttribute("Type");
						String cloudPlugin = cloud.getAttribute("Plugin");
						String cloudPriority = cloud.getAttribute("Priority");
						String cloudstat = cloud.getAttribute("isloaded");
						Element hostlist = (Element)cloud.getElementsByTagName("HostList").item(0);
						NodeList hostnl = hostlist.getChildNodes();
						for(int j = 0; j<hostnl.getLength(); j++){
							Element host = (Element)hostnl.item(j);
							String hostName = host.getNodeName();
							String hostID = host.getAttribute("ID");
							String MaxVm = host.getAttribute("MaxVm");
							String Power = host.getAttribute("Power");
							String ipmiID = host.getAttribute("ipmiID");
							Element vmList = (Element)host.getElementsByTagName("VmList").item(0);
							NodeList VMnl = vmList.getChildNodes();
							for(int k = 0 ; k < VMnl.getLength() ; k++){
								Element vm = (Element)VMnl.item(k);
								String vmID=vm.getAttribute("ID");
								String intlID = vm.getAttribute("intlID");
								String vmstat = vm.getAttribute("Stat");
								String priIP = vm.getAttribute("PrivateIP");
								String pubIP = vm.getAttribute("PublicIP");
								String acti = vm.getAttribute("ActivityStat");
								String launchTime = vm.getAttribute("LaunchTime");
								String memory = vm.getAttribute("Memory");
								String user = vm.getAttribute("User");
								String ucpu = vm.getAttribute("ucpu");
								
								System.out.println(vmID+"  "+intlID+"  "+vmstat+"  "+cloudName+"   "+hostName);
							}
						}
					}
					continue;
				}
				String str = ri.getRemoteCommand(more);
				
				System.out.println(str);
			}while (true);		
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(" Connection refused to server");

		}
		

	}
	
	private static String serverAddr = "150.183.233.59";
	private static String serverPort = "1099";

}
