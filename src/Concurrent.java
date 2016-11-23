import java.io.*;
import java.net.Socket;
import java.net.*;

public class Concurrent implements Runnable{
	
	 private final Socket clientSocket;
	 QueryManager qm=new QueryManager();
	 StatsManager sm=new StatsManager();
	 String message="not connected/not connected/not connected/not connected/not connected";
	 
   
	   public Concurrent(Socket clientSock)
	   {
	       clientSocket=clientSock;

	   }
	   public void run() {
	       try{
	    	   
	    	   DataOutputStream dataOutputStream = null;
               DataInputStream dataInputStream = null;
	  
               dataInputStream = new DataInputStream(clientSocket.getInputStream());
	    	   dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());   
	    	   
	    	   
               String messageFromClient = dataInputStream.readUTF();   
               String[] part=messageFromClient.split("-");
               qm.getMACaddress(part[1]);
               sm.getMACaddress(part[1]);
              
               
               if(part[0].equals("1"))
            	   message=qm.check_IN();
               else if(part[0].equals("2"))
            	   message=qm.check_OUT();
               else if(part[0].equals("3"))
               {
            	 
                   sm.getStats();
                   message=sm.returnStats();
               }
            	   
               else if(part[0].equals("4"))
               {
            	   sm.getStatsHR(part[1]);
            	   message=sm.returnStatsHR();
               }
            	   
               
               
               if (message !=null) {
            	  
                   dataOutputStream.writeUTF(message);
                   System.out.println(message);
                 
               }

           } catch (UnknownHostException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           } finally {
               if (clientSocket != null) {
                   try {
                       clientSocket.close();
                   } catch (IOException e) {
                      
                       e.printStackTrace();
                   }
               }
           }
		  


		   }
		
        } 


