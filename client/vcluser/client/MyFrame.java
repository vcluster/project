package vcluser.client;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import vcluster.elements.Cloud;
import vcluster.elements.Vm;
import vcluster.executors.BatchExecutor;
import vcluster.managers.CloudManager;

public class MyFrame extends JFrame {

	/**
	 * 
	 */	
	private static final long serialVersionUID = 1L;
	
	public static void startFrame(){
		MyFrame mf = new MyFrame();
		while(true){
			mf.flashTable();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public MyFrame(){
		ImageIcon icon = new ImageIcon("icon.jpg"); 
		this.setIconImage(icon.getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		setLocation(800, 400);
		this.setBackground(Color.white);
		
		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setAlignmentX(LEFT_ALIGNMENT);
		scrollPane.setBackground(Color.white);
		getContentPane().setLayout(new GridLayout(1,2));
	    getContentPane().add(scrollPane);
	    getContentPane().setBackground(Color.white);
	    DefaultTableModel tmd=new DefaultTableModel(20,8);
        table = new JTable(tmd);
        scrollPane.setViewportView(table);        
        table.setValueAt("ID", 0, 0);
        table.setValueAt("itnlID", 0, 1);
        table.setValueAt("Stat", 0, 2);
        table.setValueAt("Activity", 0, 3);
		table.setValueAt("privateIP", 0, 4);
		table.setValueAt("publicIP", 0, 5);
		table.setValueAt("HostName", 0, 6);
		table.setValueAt("CloudName", 0, 7);
        table.setShowGrid(false);
        table.getTableHeader().setVisible(false);       
       
        add(scrollPane);       
	    setVisible(true);
	}
	
	public void flashTable(){
		BatchExecutor.getPoolStatus();
		int vmnums =1;
		ArrayList<Vm> vmlist = new ArrayList<Vm>();
		for(Cloud cloud : CloudManager.getCloudList().values()){
			vmnums+=cloud.getVmList().size();
			vmlist.addAll(cloud.getVmList().values());
		}
		
		DefaultTableModel dtm =(DefaultTableModel) table.getModel();
		dtm.setRowCount(vmnums);
		int row = 1;
		for(Vm vm:vmlist){	
			try {
				table.setValueAt(vm.getuId(), row, 0);
				table.setValueAt(vm.getId(), row, 1);
				table.setValueAt(vm.getState(), row, 2);
				String activity;
				switch(vm.isIdle()){
				case 0:
					activity="Idle";
					break;
				case 1:
					activity="Busy";
					break;
				default:
					activity="None";
				}
				table.setValueAt(activity, row, 3);
				table.setValueAt(vm.getPrivateIP(), row, 4);
				table.setValueAt(vm.getPubicIP(), row, 5);
				table.setValueAt(vm.getHostname(), row, 6);
				table.setValueAt(vm.getCloudName(), row, 7);			
				
				if(vm.isIdle()==1)
				table.setRowSelectionInterval(row, row);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				continue;
			}
			row++;
		}		
	}	

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}	

	private JTable table;

}
