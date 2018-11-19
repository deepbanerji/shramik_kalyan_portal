package org.cris.clip.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.cris.clip.dto.WageSMSDTO;

public class WageSMS implements Runnable {
	
	HashMap<String,String> map=new HashMap<String,String>();
	ArrayList smsarl=new ArrayList();
	
	public WageSMS(ArrayList smsarl)
	{
		this.smsarl=smsarl;
		
	}

	
	public void run() {
		SSms();
	}
	
	public void SSms () {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss"); 
		String server = "10.64.26.139";
		File file = new File("WA_"+sdf.format(c.getTime())+".bmg");
		try {  
			//Create the file
			//if (file.createNewFile()) {
				//System.out.println("File is created!");
			//}
			//else{
				//System.out.println("File already exists.");
			//}
			 
			//Write Content
			FileWriter fw = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(fw);
			writer.write("MessageDistribution:multiple");
			writer.newLine();
			writer.write("Request-Type: SMSSubmitReq");
			writer.newLine();
			writer.write("Content-ProviderID: msdp");//
			writer.newLine();
			writer.write("Sender-ID: msdp");//
			writer.newLine();
			writer.write("Message-Type: Text");//
			writer.newLine();
			writer.write("MessageList:");//
			writer.newLine();
			writer.write("#9999999999|Test Message");//			
		 /*   for(Entry<String, String> entry: map.entrySet()) 
		    {	
		     writer.newLine();	
		     System.out.println(entry.getKey() +"|"+entry.getValue());	
			 writer.write(entry.getKey() +"|"+entry.getValue());//
			 
		    } */
			
			for(int i=0;i<smsarl.size();i++)
			{
				WageSMSDTO smsdto=(WageSMSDTO)smsarl.get(i);
				writer.newLine();	
			    System.out.println(smsdto.getMobno() +"|"+smsdto.getMessage());	
				writer.write(smsdto.getMobno() +"|"+smsdto.getMessage());//				
			}	
				
			writer.close();
			
			FTPClient ftp = new FTPClient();
			
			ftp.connect(server);
			//System.out.println("Connected to " + server + ".");
		    //System.out.print(ftp.getReplyString());
		    
			boolean success = ftp.login("ftp_user", "password");
			if (!success) {
                System.err.println("Could not login to the server");
                return;
            }
			success = ftp.changeWorkingDirectory("/prd/MSDP/ftp/sms/process");
            ftp.setFileType(FTP.ASCII_FILE_TYPE);
            FileInputStream fis = new FileInputStream(file);
 
            //System.out.println("Start uploading first file");
            boolean done = ftp.storeFile(file.getName(), fis);
            boolean fileDeleted = file.delete();
            ftp.logout();
            ftp.disconnect();
            fis.close();
            if (done) {
            	System.out.println("SMS File "+file.getName()+" is uploaded successfully to "+server);
            }		                
            //if (fileDeleted) {
              //  System.out.println("Sms file is deleted.");
            //}
		}
		catch(SocketException e) {
			e.printStackTrace();
		}
		catch(IOException io) {
			System.err.println("IOException in uploading SMS File "+file.getName()+" to "+server);
			//io.printStackTrace();
		}
	}

}
