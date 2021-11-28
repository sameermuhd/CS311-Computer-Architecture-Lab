package configuration;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import generic.Misc;

public class Configuration {	
	public static int ALU_count;
	public static int ALU_latency;
	public static int ALU_reciprocal_of_throughput;
	public static int multiplier_count;
	public static int multiplier_latency;
	public static int multiplier_reciprocal_of_throughput;
	public static int divider_count;
	public static int divider_latency;
	public static int divider_reciprocal_of_throughput;
	
	public static int L1i_numberOfLines;
	public static int L1i_latency;
	public static int L1i_associativity;
	public static String L1i_replacementPolicy;
	
	public static int L1d_numberOfLines;
	public static int L1d_latency;
	public static int L1d_associativity;
	public static String L1d_replacementPolicy;
	
	public static int L2_numberOfLines;
	public static int L2_latency;
	public static int L2_associativity;
	public static String L2_replacementPolicy;
	
	public static int mainMemoryLatency;
	
	public static void parseConfiguratioFile(String configFileName)
	{
		Document doc = null;
		
		try 
		{
			File file = new File(configFileName);
			DocumentBuilderFactory DBFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder DBuilder = DBFactory.newDocumentBuilder();
			doc = DBuilder.parse(file);
			doc.getDocumentElement().normalize();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Misc.printErrorAndExit("Error in reading config file : " + e);
		}
		
		NodeList nodeLst = doc.getElementsByTagName("ALU");
		Element elmnt = (Element) nodeLst.item(0);
		ALU_count = Integer.parseInt(getImmediateString("Count", elmnt));
		ALU_latency = Integer.parseInt(getImmediateString("Latency", elmnt));
		ALU_reciprocal_of_throughput = Integer.parseInt(getImmediateString("ReciprocalOfThroughput", elmnt));
		
		nodeLst = doc.getElementsByTagName("Multiplier");
		elmnt = (Element) nodeLst.item(0);
		multiplier_count = Integer.parseInt(getImmediateString("Count", elmnt));
		multiplier_latency = Integer.parseInt(getImmediateString("Latency", elmnt));
		multiplier_reciprocal_of_throughput = Integer.parseInt(getImmediateString("ReciprocalOfThroughput", elmnt));
		
		nodeLst = doc.getElementsByTagName("Divider");
		elmnt = (Element) nodeLst.item(0);
		divider_count = Integer.parseInt(getImmediateString("Count", elmnt));
		divider_latency = Integer.parseInt(getImmediateString("Latency", elmnt));
		divider_reciprocal_of_throughput = Integer.parseInt(getImmediateString("ReciprocalOfThroughput", elmnt));
		
		nodeLst = doc.getElementsByTagName("L1iCache");
		elmnt = (Element) nodeLst.item(0);
		L1i_numberOfLines = Integer.parseInt(getImmediateString("NumberOfLines", elmnt));
		L1i_latency = Integer.parseInt(getImmediateString("Latency", elmnt));
		L1i_associativity = Integer.parseInt(getImmediateString("Associativity", elmnt));
		L1i_replacementPolicy = getImmediateString("ReplacementPolicy", elmnt);
		
		nodeLst = doc.getElementsByTagName("L1dCache");
		elmnt = (Element) nodeLst.item(0);
		L1d_numberOfLines = Integer.parseInt(getImmediateString("NumberOfLines", elmnt));
		L1d_latency = Integer.parseInt(getImmediateString("Latency", elmnt));
		L1d_associativity = Integer.parseInt(getImmediateString("Associativity", elmnt));
		L1d_replacementPolicy = getImmediateString("ReplacementPolicy", elmnt);
		
		nodeLst = doc.getElementsByTagName("L2Cache");
		elmnt = (Element) nodeLst.item(0);
		L2_numberOfLines = Integer.parseInt(getImmediateString("NumberOfLines", elmnt));
		L2_latency = Integer.parseInt(getImmediateString("Latency", elmnt));
		L2_associativity = Integer.parseInt(getImmediateString("Associativity", elmnt));
		L2_replacementPolicy = getImmediateString("ReplacementPolicy", elmnt);
		
		nodeLst = doc.getElementsByTagName("Configuration");
		elmnt = (Element) nodeLst.item(0);
		mainMemoryLatency = Integer.parseInt(getImmediateString("MainMemoryLatency", elmnt));
	}
	
	private static String getImmediateString(String tagName, Element parent) // Get the immediate string value of a particular tag name under a particular parent tag
	{
		NodeList nodeLst = parent.getElementsByTagName(tagName);
		if (nodeLst.item(0) == null)
		{
			Misc.printErrorAndExit("XML Configuration error : Item \"" + tagName + "\" not found inside the \"" + parent.getTagName() + "\" tag in the configuration file!!");
		}
	    Element NodeElmnt = (Element) nodeLst.item(0);
	    NodeList resultNode = NodeElmnt.getChildNodes();
	    return ((Node) resultNode.item(0)).getNodeValue();
	}
}
