package com.cisco.tac.ucce.utils;

public class InventoryHost {
	String MachineName;
	String HostName;
	String Address;
	String MachineType;
	
	InventoryHost(String machineName, String hostName, String address, String machineType){
				MachineType = machineType;
				MachineName = machineName;
				HostName = hostName;
				Address = address;
	}
	public String getMachineName() {
		return MachineName;
	}
	public String getHostName() {
		return HostName;
	}
	public String getAddress() {
		return Address;
	}
	public String getMachineType() {
		return MachineType;
	}
	public void setMachineName(String machineName) {
		MachineName = machineName;
	}
	public void setHostName(String hostName) {
		HostName = hostName;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public void setMachineType(String machineType) {
		MachineType = machineType;
	}
		

}
