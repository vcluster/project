package vcluster.plugin.simulator;

import java.util.ArrayList;
import java.util.List;

import vcluster.elements.Vm;
import vcluster.plugInterfaces.CloudInterface;

public class SimCloud implements CloudInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean RegisterCloud(List<String> configurations) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Vm> createVM(int maxCount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Vm> listVMs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Vm> destroyVM(Vm vm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Vm> startVM(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Vm> suspendVM(String id) {
		// TODO Auto-generated method stub
		return null;
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

}
