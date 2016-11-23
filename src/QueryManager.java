import java.util.*;
import java.text.*;
public class QueryManager {
	
	Date date1;
	String MACaddress;
	String theDate;
	String time;
	Date PermissionTime_Enter;
	Date PermissionTime_Exit;
	Date LeavingTime_Enter;
	Date LeavingTime_Exit;
	
	SimpleDateFormat forDate;
	SimpleDateFormat forTime;
	DatabaseManager db=new DatabaseManager();
	public QueryManager()
	{
		
		date1=new Date();
		forDate=new SimpleDateFormat("dd_MM_yy");
		forTime=new SimpleDateFormat("hh.mm");
		Calendar cal=Calendar.getInstance();
		//setting permission time (start)
		cal.set(Calendar.HOUR_OF_DAY,8);
		cal.set(Calendar.MINUTE, 15);
		PermissionTime_Enter=cal.getTime();
		//setting permission time (end)
		cal.set(Calendar.HOUR_OF_DAY,9);
		cal.set(Calendar.MINUTE, 15);
		PermissionTime_Exit=cal.getTime();
		//setting leaving time(start)-cant leave before this time
		cal.set(Calendar.HOUR_OF_DAY, 13);
		cal.set(Calendar.MINUTE,45);
		LeavingTime_Enter=cal.getTime();
		//setting leaving time(end)-leaving before this time cuts permission by 1
		cal.set(Calendar.HOUR_OF_DAY, 14);
		cal.set(Calendar.MINUTE,45);
		LeavingTime_Exit=cal.getTime();	
	}
	
	
	public String getMACaddress(String MAC)
	{
		return MACaddress=MAC;
	}
	
	public String getDate()
	{
		 String Dated=forDate.format(date1);
	     return Dated;
	}
	public String getTime()
	{
		String Timed=forTime.format(date1);
		System.out.println(Timed);
		return Timed;
	}
	public int getemp_ID(String MACaddress)
	{
		String emp_ID="SELECT * FROM attendance_record WHERE MAC_address='"+MACaddress+"';";
		return db.int_executeQuery(emp_ID, "emp_ID");
		
	}
	public String check_IN()
	{ 
		
		if( db.int_executeQuery("SELECT "+getDate()+" FROM CheckIN_record WHERE MAC_address ='"+MACaddress+"';", getDate())==0)//checking whether already checked in
		{
			
		if(date1.after(PermissionTime_Enter)&&date1.before(PermissionTime_Exit))//if inbetween permission time
		{
			int check=db.int_executeQuery("SELECT Permission_Left FROM attendance_record WHERE MAC_address ='"+MACaddress+"';", "Permission_Left");
			if(check==0)//if permissions left==0
			{
				return "SEE HR";
			}
			else//else permission is reduced by one in database
			{
		      db.updateQuery("UPDATE Attendance_record SET "+getDate()+" =1 WHERE MAC_address = ('"+MACaddress+"');");
		      db.updateQuery("UPDATE CheckIN_record SET "+getDate()+" = "+getTime()+"WHERE MAC_address = ('"+MACaddress+"');" );
		      db.updateQuery("UPDATE Attendance_record SET permission_left=permission_left-1 WHERE MAC_address = ('"+MACaddress+"');");
		      return "Checked In with Permission";
			}
		}
		else if(date1.after(PermissionTime_Exit))//if too late to enter office
		{
			return "TOO LATE-CANT CHECK IN";
		}
		else//regular check in
		{
		  db.updateQuery("UPDATE Attendance_record SET "+getDate()+" =1 WHERE MAC_address = ('"+MACaddress+"');");
		  db.updateQuery("UPDATE CheckIN_record SET "+getDate()+" = "+getTime()+"WHERE MAC_address = ('"+MACaddress+"');" );
		  return "Successfully Checked In";
		  
		}
		}
		else//if already checkin
			return "ALREADY CHECKED IN";
	}
	public String check_OUT()
	{ 
		
		if(db.int_executeQuery("SELECT "+getDate()+" FROM CheckOUT_record WHERE MAC_address ='"+MACaddress+"';", getDate())==0 && db.int_executeQuery("SELECT "+getDate()+" FROM CheckIN_record WHERE MAC_address ='"+MACaddress+"';", getDate())!=0)
			//checking whether already checked out && checking whether checked in at the first place
		{
		
		if(date1.after(LeavingTime_Enter)&&date1.before(LeavingTime_Exit))//leaving in between permission time
		{
			int check=db.int_executeQuery("SELECT Permission_Left FROM attendance_record WHERE MAC_address ='"+MACaddress+"';", "Permission_Left");
			if(check==0)//zero permissions left
			{
				return "NO PERMISSIONS LEFT SEE HR";
			}
			else//checked out and permission is decremented
			{
		      db.updateQuery("UPDATE Attendance_record SET "+getDate()+" =1 WHERE MAC_address = ('"+MACaddress+"');");
		      db.updateQuery("UPDATE CheckOUT_record SET "+getDate()+" = "+getTime()+"WHERE MAC_address = ('"+MACaddress+"');" );
		      db.updateQuery("UPDATE Attendance_record SET permission_left=permission_left-1 WHERE MAC_address = ('"+MACaddress+"');");
		      return "Checked Out With permission";
			}
		}
		else if(date1.before(LeavingTime_Enter))//cant check out before this time
		{
			return "Too early to leave";
		}
		else//normal check out
		{
		  db.updateQuery("UPDATE Attendance_record SET "+getDate()+" =1 WHERE MAC_address in ('"+MACaddress+"');");
		  db.updateQuery("UPDATE CheckOUT_record SET "+getDate()+" = "+getTime()+"WHERE MAC_address in ('"+MACaddress+"');" );
		  return "Checked Out";
		  
		}
		}
		else if(db.int_executeQuery("SELECT "+getDate()+" FROM CheckOUT_record WHERE MAC_address ='"+MACaddress+"';", getDate())!=0)//already checked out
			return "ALREADY CHECKED OUT";
		else//never checked in
			return "NEVER CHECKED IN";
	}
		
}
