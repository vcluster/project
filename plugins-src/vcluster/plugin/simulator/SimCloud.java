package vcluster.plugin.simulator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import vcluster.elements.Cloud;
import vcluster.elements.Vm;
import vcluster.plugInterfaces.CloudInterface;

public class SimCloud implements CloudInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Cloud cloud;

	@Override
	public void getCloud(Cloud cloud) {
		// TODO Auto-generated method stub
		this.cloud = cloud;
		List<String> configurations = cloud.getConf();
	for(String aLine : configurations){
			
			StringTokenizer st = new StringTokenizer(aLine, "=");
			
			if (!st.hasMoreTokens()) return;
			
			/* get a keyword */
			String aKey = st.nextToken().trim();
		
			/* get a value */
			if (!st.hasMoreTokens()) return;

			String aValue = st.nextToken().trim();
			
			if (aKey.equalsIgnoreCase("endpoint"))
				this.endPoint = aValue;
			else if (aKey.equalsIgnoreCase("Name"))
				this.cloudName = aValue;
			
		}
	
		Client.setEndPoint(endPoint);
	}

	@Override
	public boolean createVM(int maxCount) {
		// TODO Auto-generated method stub
		String cmdLine = cloudName+":simvm create "+maxCount;
		for(String str :Client.executeRemote(cmdLine)){
			String [] paras = str.split(",");
			Vm vm = new Vm();
			vm.setId(paras[0].trim());
			vm.setHostname(paras[1].trim());
			vm.setPrivateIP(paras[2].trim());
			vm.setState(vm.toState(paras[3].trim()));
			vm.setTime(paras[4].trim());	
			cloud.addVm(vm);
			
		}
		return true;
	}

	@Override
	public boolean sync() {
		// TODO Auto-generated method stub
		String cmdLine = cloudName+":simvm list";
		ArrayList<String> idlist = new ArrayList<String> ();
		ArrayList<String> rmlist = new ArrayList<String> ();
		for(String str :Client.executeRemote(cmdLine)){
			String [] paras = str.split(",");
			Vm vm = new Vm();
			vm.setId(paras[0].trim());
			vm.setHostname(paras[1].trim());
			vm.setPrivateIP(paras[2].trim());
			vm.setState(vm.toState(paras[3].trim()));
			vm.setTime(paras[4].trim());
			idlist.add(vm.getId());
			cloud.addVm(vm);
			
		}
		Iterator<String> it = cloud.getVmList().keySet().iterator();
		
		while(it.hasNext()){
			String id = it.next();
			if(!idlist.contains(id))rmlist.add(id);
		}
		for(String id:rmlist){
			cloud.getVmList().remove(id);
		}
		return true;
	}

	@Override
	public boolean destroyVM(String id) {
		// TODO Auto-generated method stub
		String cmdLine = cloudName+":simvm delete "+id;
		
		if(Client.executeRemote(cmdLine).get(0).equalsIgnoreCase("true")){		
			cloud.getVmList().remove(id);
			return true;
		}
		else
			return false;
	}

	@Override
	public boolean startVM(String id) {
		// TODO Auto-generated method stub
		String cmdLine = cloudName+":simvm start "+id;
		Client.executeRemote(cmdLine);	
		
		return true;
	}

	@Override
	public boolean suspendVM(String id) {
		// TODO Auto-generated method stub
		String cmdLine = cloudName+":simvm suspend "+id;
		Client.executeRemote(cmdLine);	
		
		return true;
	}

	@Override
	public boolean hoston(String ipmiID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hostoff(String ipmiID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean migrate(String vmid, String hostid) {
		// TODO Auto-generated method stub
		return false;
	}

	private String endPoint;
	private String cloudName;
	
	
}
