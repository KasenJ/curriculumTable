package com.example.datamodle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.R.integer;

public class Classes {
	private int[][] duplicateCount ;
	public Map<Integer, List<ClassItem>> allClass;
	
	public Classes()
	{
		duplicateCount = new int[16][8];
		allClass = new HashMap<Integer, List<ClassItem>>();
		allClass.put(1, new ArrayList<ClassItem>());
		allClass.put(2, new ArrayList<ClassItem>());
		allClass.put(3, new ArrayList<ClassItem>());
		allClass.put(4, new ArrayList<ClassItem>());
		allClass.put(5, new ArrayList<ClassItem>());
		allClass.put(6, new ArrayList<ClassItem>());
		allClass.put(7, new ArrayList<ClassItem>());
	}
	
	public void clear()
	{
		for(int i = 0; i < 16;i++)
			for(int j = 0;j < 8;j++)
				duplicateCount[i][j] = 0;
		allClass.get(1).clear();
		allClass.get(2).clear();
		allClass.get(3).clear();
		allClass.get(4).clear();
		allClass.get(5).clear();
		allClass.get(6).clear();
		allClass.get(7).clear();
	}
	
	public void setClassFromXML(String xml)
	{
		Document doc = Jsoup.parse(xml,"GB2312");
		Elements trElements = doc.select("tr"); 
		int classid = 1;
		for (int i =0; i<trElements.size();i++) {
			Elements tdElements = trElements.get(i).select("td");
			for (int j =0;  j<tdElements.size(); j++){
				if(!tdElements.get(j).text().endsWith("\u00a0")){
					if(j!=0 && i!=0 ){
						int count = 0;
						for(int k = 1;k<=7;k++){
							if(duplicateCount[i][k]==1)	count++;
							else if(duplicateCount[i][k]==0 && k>=j)	break;
						}
						ClassItem classItem = new ClassItem(classid++);
						classItem.setClassName(tdElements.get(j).childNode(0).toString());
						classItem.setClassLocation(tdElements.get(j).childNode(2).toString());
						classItem.setClassStartTime(i,j+count,duplicateCount,tdElements.get(j).childNode(4).toString());
						classItem.setClassWeeks(tdElements.get(j).childNode(6).toString());
						allClass.get(j+count).add(classItem);
					}
				}
			}
		}
	}
	
	public List<ClassItem> getClassbyWeekindex(int dayofWeek)
	{
		return allClass.get(dayofWeek);
	}
	
}