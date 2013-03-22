package vcluster.monitoring;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import vcluster.control.VMManager;
import vcluster.control.VMMessage;
import vcluster.control.VMMessage.VMExeType;
import vcluster.control.VMMessage.VMMsgType;
import vcluster.global.Config;
import vcluster.util.PrintMsg;
import vcluster.util.PrintMsg.DMsgType;

public class MonitoringMan extends Thread {
	
	public MonitoringMan(VMManager vmman)
	{
		if (vmman == null) {
			PrintMsg.print(DMsgType.ERROR, "vmman is null");
		}
		msgQueue =  new ArrayBlockingQueue <MonMessage>(100);
		qc = new QStatusChecker(Config.DEFAULT_SLEEP_SEC, msgQueue);
		
		vmMan = vmman;
		vmManMsgQueue = vmMan.getMsgQueue();
	}
	
	public void dump()
	{
		qc.printQStatusChecker();
		
	}
	
	public void run() {
		qc.start();
		MonMessage aMsg = null;

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
	
	private boolean processMessage(MonMessage aMsg) {
		
		switch(aMsg.getMessageType()) {
		case QCHECKER: return processQCheckerMsg(aMsg.getPrivateData());
		}
		return false;
	}

	private synchronized 
	boolean processQCheckerMsg(int numVM) {

		if (numVM < 1) return false;
		
		/* need to launch vms */
		if(vmManMsgQueue != null) {
			VMMessage msgType = new VMMessage(VMMsgType.LAUNCH, VMExeType.OCA, numVM); 
			
			try {
				vmManMsgQueue.put(msgType);
				PrintMsg.print(DMsgType.MSG, "Mon Manager...I am waiting from VMMan....");
				this.wait();
				PrintMsg.print(DMsgType.MSG, "Mon Manager...woke up");
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		wakeUpQStatusChecker();
		
		return true;
	}
	
	private void wakeUpQStatusChecker() {
		
		/* sleep time has to be optimized */
		synchronized (qc) {
			/* set time delay to default */
			qc.setSleepTime(Config.DEFAULT_SLEEP_SEC);
			qc.wakeUp();
		}			
	}
	
	public void shutDwon() 
	{
		qc.shutDwon();
		done = true;
	
		/*
		try {
			qc.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}
	
	public void setVMMsgQueue(BlockingQueue <VMMessage> msgQ) {
		vmManMsgQueue = msgQ;
	}
	
	private boolean done = false;
	private QStatusChecker qc = null;
	private BlockingQueue <MonMessage> msgQueue;
	private BlockingQueue <VMMessage> vmManMsgQueue;
	private VMManager vmMan;



	
}
