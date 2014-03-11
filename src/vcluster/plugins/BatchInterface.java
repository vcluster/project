package vcluster.plugins;

import java.io.File;

import vcluster.control.batchsysman.PoolStatus;
import vcluster.control.batchsysman.QStatus;


/**
 * BatchInterface interface,all the batch system plug-in should implements this interface.
 *
 */
public interface BatchInterface{
	//connect to batch system,conf is a file includes the connection informations.
	public boolean ConnectTo(File conf);
	public PoolStatus getPoolStatus();
	public QStatus getQStatus();

}
