package vcluster.plugin.proxy.htcondor;

import java.io.File;

import vcluster.elements.PoolStatus;
import vcluster.elements.QStatus;
import vcluster.plugInterfaces.BatchInterface;

public class ProxyHTCondor implements BatchInterface{



	@Override
	public boolean ConnectTo(File conf) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public PoolStatus getPoolStatus() {
		// TODO Auto-generated method stub
		return new CheckCondor().getPool();
	}


	@Override
	public QStatus getQStatus() {
		// TODO Auto-generated method stub
		return new CheckCondor().getQ();
	}

}
