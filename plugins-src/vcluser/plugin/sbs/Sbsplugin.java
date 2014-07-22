package vcluser.plugin.sbs;

import java.io.File;
import java.util.ArrayList;

import vcluster.elements.PoolStatus;
import vcluster.elements.QStatus;
import vcluster.elements.Slot;
import vcluster.elements.Slot.IdType;
import vcluster.plugInterfaces.BatchInterface;

public class Sbsplugin implements BatchInterface{

	@Override
	public boolean ConnectTo(File conf) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PoolStatus getPoolStatus() {
		// TODO Auto-generated method stub
		ArrayList<String> slots = Client.getPool();
		int idle = 0;
		ArrayList<Slot> slotList = new ArrayList<Slot>();
		for(String str:slots){
			String [] paras = str.split(",");
			String id = paras[0].trim().split("@")[0].trim();
			String domain = paras[0].trim().split("@")[1].trim();
			String stat = paras[2].trim();
			String addr = paras[1].trim();
			Slot s = new Slot();
			s.setDomain(domain);
			s.setActivity(stat.equals("Busy")?1:0);
			s.setIdentifier(id);
			s.setIdType(IdType.VMID);
			slotList.add(s);
			if(s.getActivity()==0)idle++;
		}
		PoolStatus ps = new PoolStatus();
		ps.setSlotList(slotList);
		ps.setTotalSlot(slotList.size());
		ps.setClaimedSlot(slotList.size()-idle);
		ps.setUnClaimedSlot(idle);
		return ps;
	}

	@Override
	public QStatus getQStatus() {
		// TODO Auto-generated method stub
		ArrayList<String> queue = Client.getQueue();
		QStatus qs = new QStatus();
		String[] string=new String[2];
		for(String str : queue){
			string=str.split(",");
		}
		int total =Integer.parseInt(string[0].trim());
		int idle = Integer.parseInt(string[1].trim());
		int runn = total-idle;
		qs.setTotalJob(total);
		qs.setIdleJob(idle);
		qs.setRunningJob(runn);
		return qs;
	}



}
