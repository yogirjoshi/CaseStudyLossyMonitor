package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class MonitorGoogle3 {
	int evtCounter = 0;
	int currState; 
	ArrayList<Short> events;
	long avgMontime;
	int lostEvents = 0;
	public MonitorGoogle3()
	{
		events = new ArrayList<Short>();
		currState = 0;
		avgMontime = 0;
	}

	final static short FINISH = 0; 
	final static short KFE = 1;
	final static short SCHEDULE = 2;
	final static short FINISH_AND_KFE = 3;
	final static short FINISH_AND_SCHEDULE = 4;
	final static short KFE_AND_SCHEDULE = 5;
	
	final static short FINISH_AND_KFE_AND_SCHEDULE = 6;
	final static short EMPTY = 7;
	final static short CHI = 8;

	public void runMon()
	{
//		long startTime = System.currentTimeMillis();
		for(int i = 0 ; i < events.size();i++)
		{
			currState = performTransition(events.get(i));
			evtCounter++;
//			System.out.println("Event : " + evtCounter + ", Verdict: "+ getOutputForState());
		}
//		long stopTime = System.currentTimeMillis();
//	    long elapsedTime = stopTime - startTime;
//	    if(avgMontime == 0)
//	    	avgMontime = elapsedTime;
//	    else
//	    	avgMontime = (avgMontime + elapsedTime) / 2;
	}
	protected final String getOutputForState()
	{
		String currOutput = "";
		switch (currState) {
		case 0:
			currOutput = "TP";
			break;
		case 1:
			currOutput = "TP";
			break;
			
		case 2:
			currOutput = "?";
			break;
	
		default:

			break;
		}
		return currOutput;
	}
	short performTransition(int predicateSTate)
	{
		switch (currState) {
			case 0:
				if(predicateSTate == KFE ||
				predicateSTate == KFE_AND_SCHEDULE|| 
				predicateSTate == SCHEDULE ||
				predicateSTate == EMPTY )
					return 0;
				if(predicateSTate == FINISH ||
					predicateSTate == FINISH_AND_SCHEDULE ||
					predicateSTate == FINISH_AND_KFE ||
					predicateSTate == FINISH_AND_KFE_AND_SCHEDULE)
					return 1;
				if(predicateSTate == CHI)
					return 2;
				break;
			case 1:  
				if(	predicateSTate == KFE_AND_SCHEDULE|| 
				predicateSTate == SCHEDULE)
					return 0;
				if(predicateSTate == FINISH ||
					predicateSTate == KFE ||
					predicateSTate == FINISH_AND_SCHEDULE ||
					predicateSTate == FINISH_AND_KFE ||
					predicateSTate == FINISH_AND_KFE_AND_SCHEDULE ||
					predicateSTate == EMPTY)
					return 1;
				if(predicateSTate == CHI)
					return 2;
				break;
			case 2:
				if(	predicateSTate == KFE_AND_SCHEDULE|| 
				predicateSTate == SCHEDULE)
					return 0;
				if(predicateSTate == FINISH ||
						predicateSTate == FINISH_AND_SCHEDULE ||
						predicateSTate == FINISH_AND_KFE ||
						predicateSTate == FINISH_AND_KFE_AND_SCHEDULE)
						return 1;
				return 2;
				
			default:
				break;
		}
		return 0;
	}

	public static short parseEvent(String csved[]){
		boolean kfe = csved[5].equals("3") || csved[5].equals("5") || csved[5].equals("2");
		boolean sched = csved[5].equals("1");
		boolean fin = csved[5].equals("4");
		if(kfe && sched && fin)
			return FINISH_AND_KFE_AND_SCHEDULE;
		if(fin && kfe )
			return FINISH_AND_KFE;
		if(fin && sched )
			return FINISH_AND_SCHEDULE;
		if(kfe && fin)
			return FINISH_AND_KFE;
		if(fin)
			return FINISH;
		if(kfe)
			return KFE;
		if(sched)
			return SCHEDULE;
		return EMPTY;
			
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		HashMap<String,MonitorGoogle3> foMonitorMAP = new HashMap<>();
//		System.out.println(args[1]);
//		if(mype.equals("BTRV"))
//		else
		boolean isChi = false;	
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			String line;

			while ((line = br.readLine()) != null) {
				boolean uknown = false;
				String csved[] = line.split(",");
				short currEvent; 
				if(csved[1].equals("2"))
				{
					uknown = true;
					currEvent = CHI;
				}else{
					uknown = false;
					currEvent = parseEvent(csved);
				}


				String jobid = csved[2], taskId = csved[3];
				MonitorGoogle3 currMon = null;	
				if(!foMonitorMAP.containsKey(jobid+"-"+taskId)){
					foMonitorMAP.put(jobid+"-"+taskId, new MonitorGoogle3());
				}
				currMon = foMonitorMAP.get(jobid+"-"+taskId);
				
				currMon.events.add(currEvent);
//				if(currEvent != Statetype.A.ordinal() && 
//				   currEvent != Statetype.B.ordinal() && 
//				   currEvent != Statetype.CHI.ordinal())
//				{
//					currEvent = Statetype.EMPTY.ordinal();
//					isChi=false;
//					m.events.add(currEvent);
//				}
				currMon.runMon();
				currMon.events.clear();
			}
//			System.out.println("Final Verdict:" + m.getOutputForState());
//			System.out.println("Avg monitor running time:" + m.avgMontime);
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			if (br != null) {
				br.close();
			}
		}
		long TLossMonitors = 0, PLossMonitors = 0, satMonitors = 0, unsatMonitor = 0, totalSkipped = 0;
		for(MonitorGoogle3 cMon:foMonitorMAP.values()){
			if(cMon.getOutputForState().equals("TP")){
				TLossMonitors++; satMonitors++;
			}
			if(cMon.getOutputForState().equals("FP")){
				TLossMonitors++; unsatMonitor++;
			}
			if(cMon.getOutputForState().equals("?")){
				PLossMonitors++; 
			}
			totalSkipped+=cMon.lostEvents;
					
		}
		System.out.println("TLOSSMonitors=" + TLossMonitors +
							",PLOSSMonitors=" + PLossMonitors +
							",PRESUMABLY TRUE=" + satMonitors +
							",PRESUMABLY FALSE=" + unsatMonitor +
							",SKIPPED EVENTS=" + totalSkipped);
	}

}