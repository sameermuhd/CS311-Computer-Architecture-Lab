package main;
import java.util.ArrayList;

import configuration.Configuration;
import generic.Misc;
import generic.Statistics;
import processor.Processor;
import processor.memorysystem.MainMemory;
import processor.pipeline.RegisterFile;
import generic.Simulator;

public class Main {

	public static void main(String[] args) {
		if(args.length != 3)
		{
			Misc.printErrorAndExit("usage: java -jar <path-to-jar-file> <path-to-config-file> <path-to-stat-file> <path-to-object-file>\n");
		}
		
		Configuration.parseConfiguratioFile(args[0]);
		
		Processor processor = new Processor();
		
		Simulator.setupSimulation(args[2], processor);
		Simulator.simulate();
		
		processor.printState(0, 30); // ((0, 0) refers to the range of main memory addresses we wish to print. this is an empty set.
		
		Statistics.printStatistics(args[1]);
		
		System.out.println("Hash of the Processor State = "+getHashCode(processor.getRegisterFile(), processor.getMainMemory()));
	}
	
	static int getHashCode(RegisterFile registerState, MainMemory memoryState) {
		ArrayList<Integer> hash = new ArrayList<Integer>();
		
		hash.add(registerState.getProgramCounter());
		
		for(int i=0;i<32;i++) {
			hash.add(registerState.getValue(i));
		}
		
		for(int i=0;i<65536;i++) {
			hash.add(memoryState.getWord(i));
		}
		
		return hash.hashCode();
	}

}
