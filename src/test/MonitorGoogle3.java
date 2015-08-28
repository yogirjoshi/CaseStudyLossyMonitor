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
	final static short SCHEDULE = 1;
	final static short UPD_PENDING = 2;
	final static short UPD_RUNNING = 3;
	
	final static short FINISH_AND_SCHEDULE= 4;
	final static short FINISH_AND_UPD_PENDING= 5;
	final static short FINISH_AND_UPD_RUNNING= 6;
	final static short SCHEDULE_AND_UPD_PENDING= 7;
	final static short SCHEDULE_AND_UPD_RUNNING= 8;
	final static short UPD_PENDING_AND_UPD_RUNNING= 9;
	
	final static short FINISH_AND_SCHEDULE_AND_UPD_PENDING = 10;
	final static short FINISH_AND_SCHEDULE_AND_UPD_RUNNING = 11;
	final static short FINISH_AND_UPD_PENDING_AND_UPD_RUNNING = 12;
	final static short SCHEDULE_AND_UPD_PENDING_AND_UPD_RUNNING = 13;
	
	final static short FINISH_SCHEDULE_AND_UPD_PENDING_AND_UPD_RUNNING = 14;
	
	final static short EMPTY = 15;
	final static short CHI = 16;

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
			currOutput = "FP";
			break;	
		case 3:
			currOutput = "TP";
			break;
		case 4:
			currOutput = "?";
			break;
			
		default:
			System.out.println("Here by mitsake!");
			break;
		}
		return currOutput;
	}
	short performTransition(int predicateSTate)
	{
		switch (currState) {
			case 0:
				if(predicateSTate == FINISH_AND_SCHEDULE_AND_UPD_RUNNING ||
				predicateSTate == FINISH_AND_SCHEDULE_AND_UPD_PENDING ||
				predicateSTate == FINISH_AND_UPD_RUNNING ||
				predicateSTate == FINISH_SCHEDULE_AND_UPD_PENDING_AND_UPD_RUNNING||
				predicateSTate == EMPTY ||
				predicateSTate == FINISH ||
				predicateSTate == SCHEDULE ||
				predicateSTate == SCHEDULE_AND_UPD_PENDING ||
				predicateSTate == FINISH_AND_SCHEDULE
				)
					return 0;
				
				if(predicateSTate == UPD_PENDING||
						predicateSTate == FINISH_AND_UPD_PENDING ||
						predicateSTate == FINISH_AND_UPD_PENDING_AND_UPD_RUNNING
					)
					return 1;
				
				if(predicateSTate == SCHEDULE_AND_UPD_RUNNING ||
						predicateSTate == SCHEDULE_AND_UPD_PENDING_AND_UPD_RUNNING ||
						predicateSTate == UPD_RUNNING
						)
					return 2;
				if(predicateSTate == UPD_PENDING_AND_UPD_RUNNING)
					return 3;
				if(predicateSTate == CHI)
					return 4;
				break;
			case 1:  
				if(predicateSTate == FINISH_AND_SCHEDULE_AND_UPD_RUNNING ||
				predicateSTate == FINISH_AND_SCHEDULE_AND_UPD_PENDING ||
				predicateSTate == FINISH_SCHEDULE_AND_UPD_PENDING_AND_UPD_RUNNING ||
				predicateSTate ==  SCHEDULE ||
				predicateSTate == SCHEDULE_AND_UPD_RUNNING ||
				predicateSTate == FINISH_AND_SCHEDULE 
				)
					return 0;
				
				if(predicateSTate == UPD_PENDING||
						predicateSTate == FINISH_AND_UPD_RUNNING ||
						predicateSTate == FINISH_AND_UPD_PENDING ||
						predicateSTate == EMPTY ||
						predicateSTate == FINISH ||
						predicateSTate == FINISH_AND_UPD_PENDING_AND_UPD_RUNNING)
					return 1;

				if(predicateSTate == SCHEDULE_AND_UPD_PENDING_AND_UPD_RUNNING ||
						predicateSTate == SCHEDULE_AND_UPD_RUNNING)
					return 2;
				if(	predicateSTate == UPD_RUNNING ||
						predicateSTate == UPD_PENDING_AND_UPD_RUNNING)
					return 3;
				if(predicateSTate == CHI)
					return 4;
				break;
			case 2:
				if(predicateSTate == FINISH_AND_SCHEDULE_AND_UPD_RUNNING ||
					predicateSTate == FINISH_AND_SCHEDULE_AND_UPD_PENDING ||
					predicateSTate == FINISH_AND_UPD_RUNNING ||
					predicateSTate == FINISH_SCHEDULE_AND_UPD_PENDING_AND_UPD_RUNNING ||
					predicateSTate == FINISH ||
					predicateSTate == FINISH_AND_SCHEDULE)
					return 0;
				if(predicateSTate == FINISH_AND_UPD_PENDING||
					predicateSTate == FINISH_AND_UPD_PENDING_AND_UPD_RUNNING)
					return 1;
				if(predicateSTate == SCHEDULE_AND_UPD_PENDING||
						predicateSTate == SCHEDULE_AND_UPD_PENDING|| 
				predicateSTate == EMPTY ||
				predicateSTate == SCHEDULE ||
				predicateSTate == SCHEDULE_AND_UPD_PENDING_AND_UPD_RUNNING ||
				predicateSTate == UPD_RUNNING
				)
					return 2;
				if(predicateSTate == UPD_PENDING_AND_UPD_RUNNING ||
						predicateSTate ==UPD_PENDING)
					return 3;
				if(predicateSTate == CHI)
					return 4;
			case 3:
				if(predicateSTate ==FINISH_AND_SCHEDULE_AND_UPD_RUNNING ||
				predicateSTate == FINISH_AND_SCHEDULE_AND_UPD_PENDING ||
				predicateSTate == FINISH_SCHEDULE_AND_UPD_PENDING_AND_UPD_RUNNING ||
				predicateSTate == FINISH_AND_SCHEDULE )
					return 0;
				if(predicateSTate == FINISH_AND_UPD_RUNNING ||
						predicateSTate == FINISH_AND_UPD_PENDING ||
						predicateSTate == FINISH ||
						predicateSTate == FINISH_AND_UPD_PENDING_AND_UPD_RUNNING
						)
					return 1;
				if(predicateSTate == SCHEDULE_AND_UPD_RUNNING ||
						predicateSTate == SCHEDULE ||
						predicateSTate == SCHEDULE_AND_UPD_PENDING ||
						predicateSTate == SCHEDULE_AND_UPD_PENDING_AND_UPD_RUNNING
						)
					return 2;
				if(predicateSTate == UPD_PENDING ||
						predicateSTate == EMPTY ||
						predicateSTate == UPD_PENDING_AND_UPD_RUNNING ||
						predicateSTate == UPD_RUNNING
						)
					return 3;
				if(predicateSTate == CHI)
					return 4;
			case 4:	
				if(predicateSTate == FINISH_AND_SCHEDULE_AND_UPD_RUNNING ||
						predicateSTate == FINISH_AND_SCHEDULE_AND_UPD_PENDING ||
						predicateSTate == FINISH_SCHEDULE_AND_UPD_PENDING_AND_UPD_RUNNING ||
						predicateSTate == FINISH_AND_SCHEDULE  )
					return 0;
				if(predicateSTate == FINISH_AND_SCHEDULE_AND_UPD_PENDING ||
						predicateSTate == FINISH_AND_UPD_PENDING_AND_UPD_RUNNING 
						)
					return 1;
				if(predicateSTate == SCHEDULE_AND_UPD_RUNNING ||
						predicateSTate == SCHEDULE_AND_UPD_PENDING_AND_UPD_RUNNING)
					return 2;
				if(predicateSTate == UPD_PENDING_AND_UPD_RUNNING )
					return 3;
				if(predicateSTate ==UPD_PENDING ||
						predicateSTate ==FINISH_AND_UPD_RUNNING ||
						predicateSTate == EMPTY ||
						predicateSTate == FINISH ||
						predicateSTate == SCHEDULE ||
						predicateSTate == SCHEDULE_AND_UPD_PENDING ||
						predicateSTate == UPD_RUNNING ||
						predicateSTate == CHI)
					return 4;
			default:
				break;
		}
		System.out.println("Here by mitsake!");
		return -1;
	}

	public static short parseEvent(String csved[]){

		boolean finish = csved[5].equals("4");
		boolean sched = csved[5].equals("2");// || csved[5].equals("2");
		boolean upd_pend = csved[5].equals("7");
		boolean upd_runn = csved[5].equals("8");
//		System.out.println(Boolean.toString(sched) + Boolean.toString(submit) + Boolean.toString(upd_runn));
		if(finish && sched && upd_pend && upd_runn)
			return FINISH_SCHEDULE_AND_UPD_PENDING_AND_UPD_RUNNING;
		if(finish && sched && upd_pend )
			return FINISH_AND_SCHEDULE_AND_UPD_PENDING;
		if(finish && sched && upd_runn )
			return FINISH_AND_SCHEDULE_AND_UPD_RUNNING;
		if(sched && upd_runn && upd_runn )
			return SCHEDULE_AND_UPD_PENDING_AND_UPD_RUNNING;
		if(finish && upd_pend && upd_runn )
			return FINISH_AND_UPD_PENDING_AND_UPD_RUNNING;
		if(finish && sched)
			return FINISH_AND_SCHEDULE;
		if(finish && upd_pend)
			return FINISH_AND_UPD_PENDING;
		if(finish && upd_runn)
			return FINISH_AND_UPD_RUNNING;
		if(sched && upd_pend)
			return SCHEDULE_AND_UPD_PENDING;
		if(sched && upd_runn)
			return SCHEDULE_AND_UPD_RUNNING;
		if(upd_pend && upd_runn)
			return UPD_PENDING_AND_UPD_RUNNING;
		if(finish)
			return FINISH;
		if(sched)
			return SCHEDULE;
		if(upd_pend)
			return UPD_PENDING;
		if(upd_runn)
			return UPD_RUNNING;

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
				if(csved[1].equals("2") || csved[1].equals("1") || csved[1].equals("0"))
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
