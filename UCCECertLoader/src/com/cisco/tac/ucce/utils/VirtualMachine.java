package com.cisco.tac.ucce.utils;

import java.util.Objects;

public class VirtualMachine {
	String VMName;
	String HostName;
	String Annotation;
	String MachineType;
	String[] ports  = new String[2];
	String Details;
	
	VirtualMachine(String vmName, String hostName, String annotation, String machineType){
				MachineType = machineType;
				VMName = vmName;
				HostName = hostName;
				Annotation = annotation;
	}
	VirtualMachine(){
}
	public String getVMName() {
		return VMName;
	}
	public String getHostName() {
		if (Objects.isNull(this.HostName)){
			return "Not available";
		}
		else {
		return HostName;
		}
	}
	public String getAnnotation() {
		return Annotation;
	}
	public String getMachineType() {
		if (Objects.isNull(this.Annotation)){
			return "VM Annotation is not available";
		}
		else {
			VMType vmType = new VMType();
			this.MachineType=vmType.getVMdetails(this.Annotation)[0];
			this.ports[0] = vmType.getVMdetails(this.Annotation)[1];
			this.ports[1] = vmType.getVMdetails(this.Annotation)[2];
		return MachineType;
		}
	}
	public void setVMName(String vmName) {
		VMName = vmName;
	}
	public void setHostName(String hostName) {
		HostName = hostName;
	}
	public void setAnnotation(String annotation) {
		this.Annotation = annotation;
	}
	public void setMachineType(String machineType) {
		MachineType = machineType;
	}
		

}
