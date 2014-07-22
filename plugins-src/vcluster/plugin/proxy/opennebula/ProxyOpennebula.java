package vcluster.plugin.proxy.opennebula;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import vcluster.elements.Cloud;
import vcluster.elements.Element;
import vcluster.elements.Vm;
import vcluster.elements.Vm.VMState;
import vcluster.plugInterfaces.CloudInterface;

public class ProxyOpennebula  extends Element implements CloudInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static void closeStream(BufferedReader in, DataOutputStream out, Socket socket)
	{
		try {
	        if (in != null) in.close();
	        if (out != null) out.close();
	        if (socket != null) socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<String> socketToproxy(String cmd){
		String cmdLine=cmd;
		ArrayList<String> feedBack = new ArrayList<String>();
		 Socket socket = null;
	        BufferedReader in = null;
	        DataOutputStream out = null;
	        //System.out.println(cmdLine);
	        try {
	        	socket = new Socket(addr, port);
	        	
	            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
	            out = new DataOutputStream(socket.getOutputStream());
	            out.flush();
	            /* make an integer to unsigned int */
	            int userInput = 5;
	            userInput <<= 8;
	            userInput |=  1;
	            userInput &= 0x7FFFFFFF;

	            String s = Integer.toString(userInput);
	            byte[] b = s.getBytes();
	            
	            out.write(b, 0, b.length);
	            out.write(cmdLine.getBytes(), 0, cmdLine.getBytes().length);
	            out.flush();
	            char[] cbuf = new char[1024];
	        	String temp = null;
	        	while (in.read(cbuf, 0, 1024) != -1) {
	            	String str = new String(cbuf);
	    	        str = str.trim();	    	        
	    	        if (!str.equals(temp)){
	    	        	//System.out.println(str);
	    	        	 feedBack.add(str);
	    	        }
	    	        
	    	        cbuf[0] = '\0';
	            	temp = str;
	            }
	            
	        } catch (UnknownHostException e) {
	    		System.out.print("ERROR: " +e.getMessage());
	            closeStream(in, out, socket);
	            return feedBack;
	        } catch (IOException e) {
	    		System.out.print("ERROR: " +e.getMessage());
	            closeStream(in, out, socket);
	            return feedBack;
	        }
	        
	        closeStream(in, out, socket);
	        return feedBack;
	}
	
	
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
			
			if (aKey.equalsIgnoreCase("username")) {
			} else if (aKey.equalsIgnoreCase("endpoint"))
				this.addr = aValue;
			else if (aKey.equalsIgnoreCase("port"))
				this.port = Integer.parseInt(aValue);
			else if (aKey.equalsIgnoreCase("template")){
				this.template = aValue;
			}			
			else if (aKey.equalsIgnoreCase("ipmiParas")){
				this.ipmiParas = aValue;
			}
			
		}
		ArrayList<String> dateR = socketToproxy("date -R");
		//if(dateR.get(0).split(regex))
		
		try {
			if(dateR!=null&&!dateR.isEmpty()) {
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean createVM(int maxCount) {
		// TODO Auto-generated method stub
		String cmdLine="onevm create "+template +" -m "+maxCount;	
		System.out.println(cmdLine);
		String hostName = template.replace(".one", "");
		ArrayList<String> feedBack = socketToproxy(cmdLine);
		if(feedBack!=null&&!feedBack.isEmpty()&&feedBack.get(0).contains("ID:")){
			for(int i = 0;i<feedBack.size();i++){
				
				String [] vmEle = feedBack.get(i).split("\\s+");
				Vm vm = new Vm();
				vm.setId(vmEle[1]);
				vm.setState(VMState.PROLOG);
				vm.setHostname(hostName);
				getVminf(vm);
				cloud.addVm(vm);					
			}
		}else{
			System.out.println(feedBack.get(0));
			return false;
		}
		return true;	
	}
	private boolean getVminf(Vm vm){
		ArrayList<String> feedBack = socketToproxy("onevm show "+vm.getId()+" | grep IP");

		
		if(feedBack!=null&&!feedBack.isEmpty()){
			
			for(String str : feedBack){
					try {
						if(str.contains("IP_PUBLIC")){
							vm.setPubicIP(str.split("=")[1].replace(",", "").replaceAll("\"", "").trim());
						}else if(str.contains("IP=")){
							vm.setPrivateIP(str.split("=")[1].trim().replace(",", "").replaceAll("\"", ""));
						}
						else if(str.contains("IP_PRIVATE")){
							vm.setPrivateIP(str.split("=")[1].replace(",", "").replaceAll("\"", ""));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						return false;
					}
				}
			return true;
		}
		return true;
	}

	@Override
	public boolean sync() {
		// TODO Auto-generated method stub
		String cmdLine="onevm list";
		ArrayList<String> feedBack = socketToproxy(cmdLine);
		boolean flag = true;
		while(flag){
		if(feedBack!=null&&!feedBack.isEmpty()&&feedBack.get(0).contains("ID")){
			for(int i = 1;i<feedBack.size();i++){
				//System.out.println(feedBack.get(i));
				String [] vmEle = feedBack.get(i).split("\\s+");
				if(vmEle.length<9){
					continue;
				}
				Vm vm = new Vm();
				try{
					vm.setId(vmEle[0]);
					//vm.setState(vmEle[4]);
					getVminf(vm);					
					if(vmEle[4].equalsIgnoreCase("runn")){
						vm.setState(VMState.RUNNING);
					}else if(vmEle[4].equalsIgnoreCase("stop")){
						vm.setState(VMState.STOP);
					}else if(vmEle[4].equalsIgnoreCase("Pend")){
						vm.setState(VMState.PENDING);
					}else if(vmEle[4].equalsIgnoreCase("Prol")){
						vm.setState(VMState.PROLOG);
					}else if(vmEle[4].equalsIgnoreCase("Susp")){
						vm.setState(VMState.SUSPEND);
					}else{
						vm.setState(VMState.NOT_DEFINED);
					}
					if(vm.getState()==VMState.STOP){vm.setHostname("");}else{
						vm.setHostname(vmEle[7]);
					}
				}catch(Exception e){
					e.printStackTrace();
					continue;
				}

				cloud.addVm(vm);
				
			}
			flag = false;
		}else if(feedBack.get(0).contains("ReadTimeout")){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("ReadTimeout, waiting 5s... ...");
			
		}else{
			System.out.println(feedBack.get(0));
			flag = false;
			return false;
		}
		}
		return true;
	}

	@Override
	public boolean destroyVM(String id) {
		// TODO Auto-generated method stub
		Vm vm = cloud.getVmList().get(id);
		String cmdLine = "onevm delete "+id;
		if(cloud.getCloudName().equalsIgnoreCase("Gcloud")&&vm.isIdle()!=3){
			String ip = cloud.getVmList().get(id).getPrivateIP();
			cmdLine = "./rmvm "+ip+" "+id; 
		}
		ArrayList<String> feedBack = socketToproxy(cmdLine);
		if(feedBack!=null&&!feedBack.isEmpty()){
			System.out.println(feedBack.get(0));
			
		}
		
		cloud.getVmList().remove(id);
		return true; 
	}

	@Override
	public boolean startVM(String id) {
		// TODO Auto-generated method stub
		String cmdLine="onevm resume "+id;
		
		ArrayList<String> feedBack = socketToproxy(cmdLine);
		if(feedBack!=null&&!feedBack.isEmpty()){
			System.out.println(feedBack.get(0));
			return false;
		}
		Vm vm = new Vm();
		vm.setId(id);
		vm.setState(VMState.PROLOG);
		cloud.addVm(vm);
		return true;
	}

	@Override
	public boolean suspendVM(String id) {
		// TODO Auto-generated method stub
		String cmdLine = "onevm suspend "+id;
		ArrayList<String> feedBack = socketToproxy(cmdLine);
		if(feedBack!=null&&!feedBack.isEmpty()){
			System.out.println(feedBack.get(0));
			return false;
		}
		Vm vm = new Vm();
		vm.setId(id);
		vm.setState(VMState.SUSPEND);
		cloud.addVm(vm);
		return true;
	}

	



	@Override
	public boolean migrate(String vmid, String hostid) {
		// TODO Auto-generated method stub
		String cmdLine = "onevm migrate "+ vmid + " "+ hostid;
		
	    socketToproxy(cmdLine);
		
	    return true;
	}

	@Override
	public boolean hoston(String ipmiID) {
		// TODO Auto-generated method stub
		String cmdLine = "ipmitool" + ipmiParas + " -H "+ipmiID+ " power on";
		
	    ArrayList<String> feedback = socketToproxy(cmdLine);
	    if(feedback.get(0).equalsIgnoreCase("Chassis Power Control: Up/On")){
	    	
	    	return true;
	    }else{
	    	return false;
	    }
	}

	@Override
	public boolean hostoff(String ipmiID) {
		// TODO Auto-generated method stub
		String cmdLine = "ipmitool" + ipmiParas + " -H "+ipmiID+ " power off";
		
	    ArrayList<String> feedback = socketToproxy(cmdLine);
	    if(feedback.get(0).equalsIgnoreCase("Chassis Power Control: Down/Off")){
	    	return true;
	    }
		return false;
	}

	private String addr;
	private int port;
	private String template;
	private String ipmiParas;
	private Cloud cloud;


}
