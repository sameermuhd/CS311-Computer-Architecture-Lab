package generic;

import java.io.PrintWriter;

public class Statistics {

	static int numberOfInstructions;
	static int numberOfCycles;
	static int numberOfOFStageInstructions;
	static int numberOfBranchesTaken;
	static int numberOfRWStageInstructions;
	static float IPC;
	

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);

			//Printing statistics in the statFile
			writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("IPC = " + IPC);
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}

	public static void setNumberOfInstructions(int numberOfInstructions) { Statistics.numberOfInstructions = numberOfInstructions; }
	public static void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}
	public static void setNumberOfOFStageInstructions(int numberOfInstructions ){ Statistics.numberOfOFStageInstructions = numberOfInstructions; }
	public static void setNumberOfBranchesTaken(int numberOfBranches){ Statistics.numberOfBranchesTaken = numberOfBranches; }
	public static void setNumberOfRWStageInstructions(int numberOfInstructions){ Statistics.numberOfRWStageInstructions = numberOfInstructions; }
	public static void setIPC() { IPC = (float) numberOfInstructions / (float) numberOfCycles; }

	public static int getNumberOfInstructions() { return numberOfInstructions; }
	public static int getNumberOfCycles() { return numberOfCycles; }
	public static int getNumberOfOFStageInstructions() { return numberOfOFStageInstructions; }
	public static int getNumberOfBranchesTaken() { return numberOfBranchesTaken; }
	public static int getNumberOfRWStageInstructions() { return numberOfRWStageInstructions; }
	public static float getIPC() { return IPC; }
}


