package com.example.datamodle;


public class ClassItem {
	private String className;
	private String classLocation;
	private String classStartTime;
	private String classWeeks;
	public int Start;
	public int duration;
	public int classid;
	public int clockHour;
	public int clockMinute;
	public boolean isSetClock;
	
	public ClassItem(int classid)
	{
		this.classid = classid;
	}
	
	public void setClassName(String CN){className = CN;}
	public String getClassName(){return className;}
	
	public void setClassLocation(String CL){classLocation = CL;}
	public String getClassLocation(){return classLocation;}
	
	public void setClassStartTime(String CST){classStartTime = CST;}
	public void setClassStartTime(int i,int j,int[][] dupCount, String CST)
	{
		classStartTime = CST;
		Start = i;
		int a = Integer.valueOf(CST.substring(CST.indexOf('-')+1, CST.indexOf("½Ú"))).intValue();
		duration = a-i+1;
		for(i =i+1;i<=a;i++)
			dupCount[i][j]++;
	}
	public String getClassStartTime(){return classStartTime;}
	
	public void setClassWeeks(String CW){classWeeks = CW;}
	public String getClassWeeks(){return classWeeks;}
	
	@Override
	public String toString()
	{
		return className+"\t"+classLocation+"\t"+classStartTime+"\t"+classWeeks+"\t"+classid;
	}
}

