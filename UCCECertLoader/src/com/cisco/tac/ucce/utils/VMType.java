package com.cisco.tac.ucce.utils;

public class VMType {
	//static String MachineType;
	
	 VMType (){
		// MachineType = machineType;
	 }
	
	public String[] getVMdetails(String annotatations) {
		
		String[] DataArray = new String[3];
		DataArray[0]="";
		DataArray[1]="";
		DataArray[2]="";
				
		
		if (annotatations.contains("for Unified Contact Center Enterprise components")){
			DataArray[0]="UCCE";
			DataArray[1]="443";
			DataArray[2]="7890";
			return DataArray;
		}
		else if( annotatations.contains("CVP VM template")) {
			DataArray[0]="CVP";
			DataArray[1]="8111";
			return DataArray;
		}
		else if( annotatations.contains("Cisco Finesse VM")) {
			DataArray[0]="Finesse";
			DataArray[1]="8443";
			return DataArray;
		}
		else if( annotatations.contains("CUIC VM")) {
			DataArray[0]="CUIC";
			DataArray[1]="8443";
			return DataArray;
		}
		else if( annotatations.contains("Live Data (Standalone) VM Template")) {
			DataArray[0]="Live-Data";
			DataArray[1]="8443";
			return DataArray;
		}
		else if( annotatations.contains("Cisco Identity Service (IdS) stanalone")) {
			DataArray[0]="Identity Service";
			DataArray[1]="8443";
		}
		else if( annotatations.contains("VVB VM template")) {
			DataArray[0]="VVB";
			DataArray[1]="8443";
			return DataArray;
		}
		else if( annotatations.contains("Application Version: CUCM")) {
			DataArray[0]="CUCM";
			DataArray[1]="8443";
			return DataArray;
		}
		else if( annotatations.contains("CUCM IM and Presence")) {
			DataArray[0]="IM and Presence";
			DataArray[1]="8443";
			return DataArray;
		}
		else if( annotatations.contains("Templates for Cisco SocialMiner")) {
			DataArray[0]="SocialMiner";
			DataArray[1]="8443";
		}
		else if( annotatations.contains("Templates for Cisco CCP")) {
			DataArray[0]="Cisco CCP";
			DataArray[1]="8443";
			return DataArray;
		}
		else if( annotatations.contains("Template for Cloud Connect standalone")) {
			DataArray[0]="Cloud Connect";
			DataArray[1]="8443";
			return DataArray;
		}
		DataArray[0]="Unknown";
		return DataArray;
	}		
	
}
