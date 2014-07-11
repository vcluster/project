package vcluster.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	
	public static void main(String[] args){
		for(int i=0;i<10;i++){
			writeLog("This is the line "+i+",");
		}
	}
	private static final String LOG_FILE = "log"+File.separator+"vcluster.log";
	private static File log = new File(LOG_FILE);
	
	static{
		
		if(!log.exists()){
			try {
				File path = new File("log");
				if(!path.exists()){
					path.mkdir();
				}
				log.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static boolean writeLog(String event){
		StringBuffer strbr = new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currTime = df.format(new Date());
		strbr.append(currTime+System.lineSeparator());
		strbr.append(event);
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(log,true); 
			OutputStreamWriter osw=new OutputStreamWriter(fos);   
			BufferedWriter bw=new BufferedWriter(osw);
			bw.write(strbr.toString()); 
			bw.newLine();
			bw.newLine();
			bw.flush();
			bw.close();  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return true;
	}
	
}
