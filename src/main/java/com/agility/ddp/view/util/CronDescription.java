package com.agility.ddp.view.util;

public class CronDescription {
	
	public String getCronDescription(String cronExpression)
	{
		String desc = "Every ";
		String[] cronEx = cronExpression.split(" ");
		
		//checking
		if(! (cronEx[4].equals("*") || cronEx[4].equals("?")))
		{
			desc+=cronEx[4]+" months ";
		}
		if(! (cronEx[5].equals("*") || cronEx[5].equals("?")))
		{
			desc+=cronEx[5]+"th week";
		}
		if(! (cronEx[3].equals("*") || cronEx[3].equals("?")))
		{
			desc+=cronEx[3]+" th day";
		}
		if(! cronEx[2].equals("*"))
		{
			if(cronEx[2].length()==1)
				desc+=" at HH 0"+cronEx[2]+" : ";
			else
				desc+=" at HH "+cronEx[2]+" : ";
		}
		if(! cronEx[1].equals("*"))
		{
			if(cronEx[1].length()==1)
				desc+="0"+cronEx[1]+" MM";
			else
				desc+=cronEx[1]+" MM";
		}
//		if(cronEx[5].equalsIgnoreCase("?"))
//		{
//			if(cronEx[4].equals("*"))
//				desc+= " month ";
//			else
//				desc+=cronEx[4]+" months ";
//			if(! cronEx[3].equals("*"))
//				desc+=cronEx[3]+"th day";
//			
//		}
//		if(cronEx[3].equalsIgnoreCase("?"))
//		{
//			if(! cronEx[4].equals("*"))
//				desc+=cronEx[4]+" month ";
//			
//			if(cronEx[5].equals("*"))
//				desc+= " week";
//			else
//				desc+=cronEx[5]+"th day in week";
//			
//		}
//		
//		
//		if(! cronEx[2].equals("0"))
//			desc+=" at Hr"+ cronEx[1]+ ":";
//		else
//			desc+=" at 00:";
//		if(! cronEx[1].equals("0"))
//			desc+= cronEx[1]+"mm";
//		else
//			desc+="00 mm";
		return desc;
	}
	public static void main(String[] args) {
		String cronExpression="0 15 * ? 6 *";
		String[] arr = cronExpression.split(" ");
		for(String ar:arr)
		{
//			System.out.println(ar);
		}
//		System.out.println(arr[5]);
		CronDescription obj = new CronDescription();
		String desc = obj.getCronDescription(cronExpression);
		System.out.println(desc);
		
	}
}
