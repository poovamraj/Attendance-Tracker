import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
public class StatsManager {

	Date currentDate=new Date();
	SimpleDateFormat sdf=new SimpleDateFormat("dd_MM_yy");
	Date firstDate;
	Calendar start= Calendar.getInstance();
	Calendar end=Calendar.getInstance();
	String MACaddress;
	DatabaseManager db=new DatabaseManager();
	String returndate;
    int counter=0;
    String dates[]=new String[100];
	int emp_ID;
	int permission_left;
	int days_came=0;
	int attendance_percentage;
	int Leaveleft;
	String returnDates;
	int total=0;
	
	public StatsManager()
	{
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.YEAR,2016);
		cal.set(Calendar.MONTH, 3);
		cal.set(Calendar.DAY_OF_MONTH, 6);
		firstDate=cal.getTime();	
		
		
		start.setTime(firstDate);
		
	}
	public String getMACaddress(String MAC)
	{
		return MACaddress=MAC;
	}
	
	
	 public void  getStats()
	{ 
        int attendance;	
        
     
        emp_ID=db.int_executeQuery("SELECT Emp_ID FROM Attendance_record WHERE MAC_address ='"+MACaddress+"';" , "Emp_id");//gets emp id
        permission_left=db.int_executeQuery("SELECT Permission_left FROM Attendance_record WHERE MAC_address ='"+MACaddress+"';" , "Permission_left");
        //gets permissions left
		while(firstDate.before(currentDate))
		{
			firstDate=start.getTime();
			start.add(Calendar.DATE,1);
			Date d=firstDate;
			String date=sdf.format(d);
			attendance=db.int_executeQuery("SELECT "+date+" FROM Attendance_record WHERE MAC_address ='"+MACaddress+"';" , date);
			System.out.println(attendance+"+"+date);
			if(attendance==0)
			{
				int day=counter+1;
				dates[counter]="Day "+day+":"+date+"\n";//date on leave
				counter++; 	
			}
			
			days_came=days_came+attendance;
			total=total+1;
		
		}
		
		Leaveleft=6-counter;//no. of leave
	
	
	}
		
	 public void  getStatsHR(String emp_id)
	{ 
        int attendance;	
        System.out.println(emp_id);
   
        permission_left=db.int_executeQuery("SELECT Permission_left FROM Attendance_record WHERE emp_id ="+emp_id+";" , "Permission_left");
        //gets permissions left
		while(firstDate.before(currentDate))
		{
			firstDate=start.getTime();
			start.add(Calendar.DATE,1);
			Date d=firstDate;
			String date=sdf.format(d);
			attendance=db.int_executeQuery("SELECT "+date+" FROM Attendance_record WHERE emp_id ="+emp_id+";" , date);
			System.out.println(attendance+"+"+date);
			if(attendance==0)
			{
				int day=counter+1;
				dates[counter]="Day "+day+":"+date+"\n";//gets date on leave
				counter++; 	
			}
			
			days_came=days_came+attendance;
			total=total+1;
		
		}
		
		Leaveleft=6-counter;//checks no of days leave
		
	
	}
	 public String returnStats()
	 {
		 returndate=dates[0];
		 int LOOP_COUNTER=1;
		 while(LOOP_COUNTER<counter)
		 {
		 returndate=returndate+dates[LOOP_COUNTER]+"\t";
		 LOOP_COUNTER++;
		 }
		 
		 String response= emp_ID+"-"+days_came+"-"+total+"-"+Leaveleft+"-"+permission_left+"-"+returndate;
		 System.out.println(response);
		 return response;
		 
	 }
	 
	 public String returnStatsHR()
	 {
		 returndate=dates[0];
		 int LOOP_COUNTER=1;
		 while(LOOP_COUNTER<counter)
		 {
		 returndate=returndate+dates[LOOP_COUNTER]+"\t";
		 LOOP_COUNTER++;
		 }
		 
		 String response= days_came+"-"+total+"-"+Leaveleft+"-"+permission_left+"-"+returndate;
		 System.out.println(response);
		 return response;
		 
	 }
	
}
