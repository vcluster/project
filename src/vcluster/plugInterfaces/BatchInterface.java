package vcluster.plugInterfaces;

import java.io.File;

import vcluster.simulators.PoolStatus;
import vcluster.simulators.QStatus;

/**
 * All the batch system plug-ins should implement this interface.
 *
 */
public interface BatchInterface{
	/**
	 *Connect to batch system central manager,
	 *@param conf, A text file includes the connection informations.
	 */
	public boolean ConnectTo(File conf);
	/**
	 *Get the pool status .
	 *@return PoolStatus, return a instance of pool status, which includes all the pool status information.
	 *@see PoolStatus.
	 */
	public PoolStatus getPoolStatus();
	/**
	 * Get the queue status.
	 * @return QStatus, A instance of the job queue status, which includes the queue status information.
	 * @see QStatus.
	 */
	public QStatus getQStatus();

}
