package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.MonitorInfo;
import java.util.ArrayList;


public class Monitor {
	int evtCounter = 0;
	int currState;
	ArrayList<Integer> events;
	long avgMontime;
	public Monitor()
	{
		events = new ArrayList<Integer>();
		currState = 1;
		avgMontime = 0;
	}
	public enum Statetype {
		A, B, AANDB, EMPTY, CHI;
	}
	public void runMon()
	{
		long startTime = System.currentTimeMillis();
		for(int i = 0 ; i < events.size();i++)
		{
			currState = performTransition(events.get(i));
			evtCounter++;
			System.out.println("Event : " + evtCounter + ", Verdict: "+ getOutputForState());
		}
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    if(avgMontime == 0)
	    	avgMontime = elapsedTime;
	    else
	    	avgMontime = (avgMontime + elapsedTime) / 2;
	}
	protected final String getOutputForState()
	{
		String currOutput = "";
		switch (currState) {
		case 1:
			currOutput = "FP";
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

			break;
		}
		return currOutput;
	}
	int performTransition(int predicateSTate)
	{
		switch (currState) {
		case 1: 
			if(predicateSTate == Statetype.A.ordinal() || predicateSTate == Statetype.EMPTY.ordinal())
				return 2;
			if(predicateSTate == Statetype.B.ordinal() || predicateSTate == Statetype.AANDB.ordinal())
				return 3;
			if(predicateSTate == Statetype.CHI.ordinal())
				return 4;
			break;
		case 2:
			if(predicateSTate == Statetype.A.ordinal() || predicateSTate == Statetype.EMPTY.ordinal())
				return 2;
			if(predicateSTate == Statetype.B.ordinal() || predicateSTate == Statetype.AANDB.ordinal())
				return 3;
			if(predicateSTate == Statetype.CHI.ordinal())
				return 4;
			break;
		case 3:
			if(predicateSTate == Statetype.A.ordinal() || predicateSTate == Statetype.EMPTY.ordinal())
				return 2;
			if(predicateSTate == Statetype.B.ordinal() || predicateSTate == Statetype.AANDB.ordinal())
				return 3;
			if(predicateSTate == Statetype.CHI.ordinal())
				return 4;
			break;
		case 4:
			if(predicateSTate == Statetype.A.ordinal() || predicateSTate == Statetype.EMPTY.ordinal())
				return 2;
			if(predicateSTate == Statetype.B.ordinal() || predicateSTate == Statetype.AANDB.ordinal())
				return 3;
			if(predicateSTate == Statetype.CHI.ordinal())
				return 4;
			break;	
		default:
			break;
		}
		return 0;
	}
	public class BTRVMonitor extends Thread{
		public void run()
		{
			
		}
	}
	public class TTRVMonitor extends Thread{
		public void run()
		{
			
		}
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader in = null;
		int currEvent = Integer.MAX_VALUE;
		Monitor m = new Monitor();
//		System.out.println(args[1]);
		int buffSize = Integer.parseInt(args[1]);
//		if(mype.equals("BTRV"))
//		else
		boolean isChi = false;	
		int skipCount=0;
		try {
			in = new BufferedReader(new FileReader(new File(args[0])));
			String line;
			while ((line = in.readLine()) != null) {
				currEvent = Integer.MAX_VALUE;
				if(line.contains("buffer"))
				{
					isChi=false;
					currEvent = Statetype.A.ordinal();
					m.events.add(currEvent);
					
				}
				if(line.contains("decode"))
				{
					currEvent = Statetype.B.ordinal();
					isChi=false;
					m.events.add(currEvent);
				}
				if(line.contains("?"))
				{
					currEvent = Statetype.CHI.ordinal();
					if(!isChi){
						m.events.add(currEvent);
						isChi = true;
					}
					else
						skipCount++;
				}
				if(currEvent != Statetype.A.ordinal() && 
				   currEvent != Statetype.B.ordinal() && 
				   currEvent != Statetype.CHI.ordinal())
				{
					currEvent = Statetype.EMPTY.ordinal();
					isChi=false;
					m.events.add(currEvent);
				}
				if(m.events.size() >= buffSize)
				{
					m.runMon();
					m.events.clear();
				}
			}
			if(m.events.size() >= 0)
			{
				m.runMon();
				m.events.clear();
			}
			System.out.println("Final Verdict:" + m.getOutputForState());
			System.out.println("Avg monitor running time:" + m.avgMontime);
			System.out.println("Skipped Events Count:" + skipCount);
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			if (in != null) {
				in.close();
			}
		}
	}

}
