# UCCECertificateUploader

# Introduction

 

This document describes the tool for getting and uploading certificates in the Unified Contact Center Enterprise (UCCE) solution.

 
# Problem

 

Exchanging certificates can be a difficult task for people who are not familiar with the java keytool utility, especially when self-service certificates are used.

Wrong actions can cause issues with solution configuration and its health.

Certificates can be expired, and renewing them is another trick.

There should be a way to do it with fewer efforts and more quickly.

 
# Solution

The article contains a tool written in Java that will help you with the task.

 

The tool can connect to the UCCE database or to the ESXi host, get the data about all hosts from there, get a certificate from each host and upload it to the java cacerts trust store.


The fields descriptions:

AW database name: provide the name of the AW database, Logger, or pcceinvetory database. There must be data in the t_Machine tables.
If the tool is running on the UCCE host where database com[onent is not installed, the remote SQL server name can be added as a prefix to the database name.
For example AWHDS-A\pcce_awdb
This is applicable for PG or ROUTER machines.
Username and Password for SQL user with access right to read the database data. Check the "Windows Authentification" for using integrated windows authentication instead of SQL.
UCCE version: depending on the version patch to cacerts file is different.
Path to cacerts: Location of the cacerts file. In the UCCE 12.6.X the system uses "C:\icm\ssl\cacerts", but UCCE 12.5 uses the default Java truststore (%CCE_JAVA_HOME%\lib\security\cacert).
Keystore Password: default password for the cacerts store is "changeit"
Store Type: UCCE uses JKS  type of the store, while CVP uses JCEKS
Load Inventory button: The tool connects to the mentioned database and shows the inventory data.
Upload all certificates button: The button is available after getting data from the database.

The inventory data consists of 6 columns:

    Hostname
    IP-Address
    Machine type
    Status of the certificate's data or error during getting/uploading the certificate.
    Certificate's expiration date
    Details
    
    
    
# ESXi Mode

 

ESXi mode can be used for PCCE/UCCE fresh installation when the Invenory is not yet configured and t_Machine tables don't contain any data.

The tool connects to the ESXi host and gets the data about all virtual machines from there.

It requests VM name, VM annotations, and hostname from the guest operating system.

Depending on the VM annotations, the machine type is identified.

VMWare tools must be running on the Virtual machines, otherwise, the hostname will not be populated.

VCenter is not supported for connections.

# Free mode

 

Another mode of the tool is "Free mode".

There is no requirement to have UCCE Database available and the tool can be used to upload any certificates to CVP or ECE.

Example use cases:

-Get and upload 3-rd party web-service certificate to CVP

-Get and upload mail servers' certificates to ECE services server

-Get and upload IDS certificates to ECE application server


# Technical details

 

    The tool connects to the host and checks if the certificate is trusted or not. If it is not trusted, then the certificate will be uploaded.
    The certificate will be uploaded with the alias "util-[hostname]-[port]", for example util-vvb125.cc.lab-8443
    A host can send more than one certificate. In this case, the tool will upload all these certificates as root and/or intermediate prefixes.
    The tool is compiled with java 1.8.
    The tool connects to the database by localhost:1433 by default
    The minimum screen resolution is 1024x768. Scaled mode is not supported.

 
