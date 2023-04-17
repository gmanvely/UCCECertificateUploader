package com.cisco.tac.ucce.utils;


import java.io.IOException;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MachineTypeName {
	//static String MachineType;
	
	 MachineTypeName (){
		// MachineType = machineType;
	 }
	
	public String getMachineTypeName(String machineType) {
		//MachineType = machineType;
		
		{
		    try {
		    	
		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder;
		        dBuilder = dbFactory.newDocumentBuilder();
		        Document doc = dBuilder.parse(UCCECertificateUploader.class.getResourceAsStream("/com/cisco/tac/ucce/utils/machinetypes.xml"));
		        doc.getDocumentElement().normalize();
		        XPath xPath =  XPathFactory.newInstance().newXPath();
		        String expression = ("/MachineTypes/MachineType[@Id=")+machineType+("]/TypeName[@name]");            
		        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
		        for (int i = 0; i < nodeList.getLength(); i++) {
		            Node nNode = nodeList.item(i);
		           // System.out.println("\nCurrent Element :" + nNode.getNodeName());
		            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		                Element eElement = (Element) nNode;
		            //    System.out.println("Machine Type: " + eElement.getAttribute("name"));
		                return eElement.getAttribute("name");
		            }
		        }
		    } catch (ParserConfigurationException e) {
		        System.out.println(e);
		    } catch (SAXException e) {
		        System.out.println(e);
		    } catch (IOException e) {
		        System.out.println(e);
		    } catch (XPathExpressionException e) {
		        System.out.println(e);
		    }
		
	}
		return "not found";
	}	
	
	public String[] getMachinePorts(String machineType) {
		//MachineType = machineType;
		
		{
		    try {
		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder;
		        dBuilder = dbFactory.newDocumentBuilder();
		        Document doc = dBuilder.parse(UCCECertificateUploader.class.getResourceAsStream("/com/cisco/tac/ucce/utils/machinetypes.xml"));
		        doc.getDocumentElement().normalize();
		        XPath xPath =  XPathFactory.newInstance().newXPath();
		        String expression = ("/MachineTypes/MachineType[@Id=")+machineType+("]/port");            
		        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
		        if (nodeList.getLength()==0) {
		        	String[] portSet = new String[1];
		        	portSet[0]="0";
		        	return portSet;
		        }
		        
		        String[] portSet = new String[nodeList.getLength()];
		        for (int i = 0; i < nodeList.getLength(); i++) {
		            Node nNode = nodeList.item(i);
		           // System.out.println("\nCurrent Element :" + nNode.getNodeName());
		            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		                Element eElement = (Element) nNode;
		            //    System.out.println("Machine Type: " + eElement.getAttribute("name"));
		                System.out.println(eElement.getTextContent());
		                portSet[i]=eElement.getTextContent();
		                System.out.println (String.valueOf(nodeList.getLength()));
		               
		            }
		            
		        }
		        Arrays.stream(portSet).forEach(System.out::println);
		        
		        return portSet;
		        
		    } catch (ParserConfigurationException e) {
		        System.out.println(e);
		    } catch (SAXException e) {
		        System.out.println(e);
		    } catch (IOException e) {
		        System.out.println(e);
		    } catch (XPathExpressionException e) {
		        System.out.println(e);
		    }
		
	}
		String[] portSet = new String[1];
		portSet[0]="0";
    	return portSet;
	}	
	
}
