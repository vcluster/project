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

import vcluster.elements.Vm;
import vcluster.managers.VmManager.VMState;
import vcluster.plugInterfaces.CloudInterface;

public class ProxyOpennebula implements CloudInterface {
	
	public static void main(String[] arg){
		
	    String cmdLine = "";
	    ProxyOpennebula proxyOpennebula = new ProxyOpennebula();
	    //proxyOpennebula.RegisterCloud(new ArrayList<String>());
	    proxyOpennebula.addr="150.183.233.60";
	    proxyOpennebula.port=9734;
	    File f = new File("e:"+File.separator+"vmlist.txt");
	    /* prompt */
	   do{
		    System.out.print("proxyTest > ");
			
		    InputStreamReader input = new InputStreamReader(System.in);
		    BufferedReader reader = new BufferedReader(input);
		    
		    try {
			    /* get a command string */
		    	cmdLine = reader.readLine(); 
		    	//reader.close();
		    	System.out.println(cmdLine);
		    }
		    catch(Exception e){e.printStackTrace();}	
		   ArrayList<String> feedback = proxyOpennebula.socketToproxy(cmdLine);
		
		   if(!f.exists()){
			   try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   }
		   try {
			   FileWriter fw=new FileWriter(f);
			   BufferedWriter bw=new BufferedWriter(fw); 			   
			   for(String line : feedback){
				   System.out.println(line);
				   bw.write(line); 
			       bw.newLine();//
			   }
			   bw.close();
			   fw.close();
			   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    
		    
	   }while(!cmdLine.equals("quit"));
	}
	
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
	public boolean RegisterCloud(List<String> configurations) {
		// TODO Auto-generated method stub
		/*configurations.add("username=amol");
		configurations.add("endpoint=fcl301.fnal.gov");
		configurations.add("port=9734");
		configurations.add("template = OpenNebula/clean.one");*/
		for(String aLine : configurations){
			
			StringTokenizer st = new StringTokenizer(aLine, "=");
			
			if (!st.hasMoreTokens()) return false;
			
			/* get a keyword */
			String aKey = st.nextToken().trim();
		
			/* get a value */
			if (!st.hasMoreTokens()) return false;

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

		return true;
	}

	@Override
	public ArrayList<Vm> createVM(int maxCount) {
		// TODO Auto-generated method stub
		String cmdLine="onevm create "+template +" -m "+maxCount;	
		System.out.println(cmdLine);
		String hostName = template.replace(".one", "");
		ArrayList<Vm> vmList = new ArrayList<Vm>();
		ArrayList<String> feedBack = socketToproxy(cmdLine);
		if(feedBack!=null&&!feedBack.isEmpty()&&feedBack.get(0).contains("ID:")){
			for(int i = 0;i<feedBack.size();i++){
				
				String [] vmEle = feedBack.get(i).split("\\s+");
				Vm vm = new Vm();
				vm.setId(vmEle[1]);
				vm.setState(VMState.PROLOG);
				vm.setHostname(hostName);
				vmList.add(vm);				
			}
		}else{
			System.out.println(feedBack.get(0));
			return null;
		}
			return vmList;
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
	public ArrayList<Vm> listVMs() {
		// TODO Auto-generated method stub
		String cmdLine="onevm list";
		ArrayList<Vm> vmList = new ArrayList<Vm>();
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

				vmList.add(vm);		
				
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
			return null;
		}
		}
		//System.out.println("opennebula plugin:"+vmList.size());
		return vmList;
	}

	@Override
	public ArrayList<Vm> destroyVM(Vm vm) {
		// TODO Auto-generated method stub
			
		String cmdLine = "./rmvm "+vm.getPrivateIP()+" "+vm.getId();
		ArrayList<Vm> vmList = new ArrayList<Vm>();
		ArrayList<String> feedBack = socketToproxy(cmdLine);
		if(feedBack!=null&&!feedBack.isEmpty()){
			System.out.println(feedBack.get(0));
			return null;
		}
		Vm vm_1 = new Vm();
		vm_1.setId(vm.getId());
		vm_1.setState(VMState.STOP);
		vmList.add(vm_1);
		return vmList; 
	}

	@Override
	public ArrayList<Vm> startVM(String id) {
		// TODO Auto-generated method stub
		String cmdLine="onevm resume "+id;
		ArrayList<Vm> vmList = new ArrayList<Vm>();
		ArrayList<String> feedBack = socketToproxy(cmdLine);
		if(feedBack!=null&&!feedBack.isEmpty()){
			System.out.println(feedBack.get(0));
			return null;
		}
		Vm vm = new Vm();
		vm.setId(id);
		vm.setState(VMState.PROLOG);
		vmList.add(vm);
		return vmList;
	}

	@Override
	public ArrayList<Vm> suspendVM(String id) {
		// TODO Auto-generated method stub
		String cmdLine = "onevm suspend "+id;
		ArrayList<Vm> vmList = new ArrayList<Vm>();
		ArrayList<String> feedBack = socketToproxy(cmdLine);
		if(feedBack!=null&&!feedBack.isEmpty()){
			System.out.println(feedBack.get(0));
			return null;
		}
		Vm vm = new Vm();
		vm.setId(id);
		vm.setState(VMState.SUSPEND);
		vmList.add(vm);
		return vmList;
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


}
