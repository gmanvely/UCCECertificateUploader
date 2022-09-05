package com.cisco.tac.ucce.utils;


import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;


public class UCCECertificateUploader {
    public static int row = 0;
    public static String[] machinetypeID = new String[101];
    public static int freehostcount = 0;
    public static String storeType;
    public static VirtualMachine[] VMs_array = new VirtualMachine[50];
    public static UploadCert UploadAll[] = new UploadCert[50];
    public static UploadCert UploadAllUCCE[] = new UploadCert[101];
    public static GetDataFromAW inventoryData = new GetDataFromAW();
    public static InventoryHost[] host = new InventoryHost[100];
    

    public static void main(String[] args) {

        Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setText("CCUT: Contact Center Uploader Tool");
        shell.setSize(1000, 750);
        FormLayout gl = new FormLayout();
        shell.setLayout(gl);
        
        
        Image image = new Image(display, UCCECertificateUploader.class.getResourceAsStream("/com/cisco/tac/ucce/utils/cx-color-logo.png"));
        Label label = new Label (shell, SWT.NONE);
        label.setImage (image);
        final FormData imageFD = new FormData();
        imageFD.bottom = new FormAttachment(0, 20+42);
        imageFD.right = new FormAttachment(0, 900);
        imageFD.top = new FormAttachment(0, 20);
        imageFD.left = new FormAttachment(0, 600);
        
        label.setLayoutData(imageFD); 

        final Text awdb_name;
        final Text db_user;
        final Text db_password;
        final Text esxi_hostname;
        final Text esxi_user;
        final Text esxi_password;
        final Text[] address = new Text[101];
        final Text[] hostnames = new Text[101];
        final Text[] machinename = new Text[101];
        final Text[] hostname = new Text[10];
        final Text[] port = new Text[10];
        final Button[] btnUpload = new Button[10];
        final Label[] lblArray = new Label[10];
        final Text[] freeResult = new Text[10];
        final Text[] free_cert_expire = new Text[10]; 
        final Text[] vmname = new Text[50];
        final Text[] vmhostname = new Text[50];
        final Text[] vmtype = new Text[50];
        final Text[] vm_status = new Text[50];
        final Text[] vm_cert_expire = new Text[50];
        final Button[] btnVm_details = new Button[50];
        final Text[] vm_ports = new Text[50];
        final Text[] vm_details = new Text[50];
        final Text[] details = new Text[101];

        final MachineTypeName machinetypeDesc = new MachineTypeName();
        final Text cacertPath;
        final Text cacertPassw;
        final Text[] result = new Text[101];
        final Text[] cert_expire = new Text[101];
        final Button[] btn_details = new Button[101];
        

        final ScrolledComposite scrolled_composite_UCCE = new ScrolledComposite( shell, SWT.V_SCROLL | SWT.BORDER );

        final Composite compositeUCCE = new Composite(scrolled_composite_UCCE, SWT.NONE);
        compositeUCCE.setVisible(false);
        final FormData fd_compositeUCCE = new FormData();
        fd_compositeUCCE.bottom = new FormAttachment(0, 700);
        fd_compositeUCCE.right = new FormAttachment(0, 980);
        fd_compositeUCCE.top = new FormAttachment(0, 230);
        fd_compositeUCCE.left = new FormAttachment(0, 10);
        
        scrolled_composite_UCCE.setLayoutData(fd_compositeUCCE);
                
        compositeUCCE.setLayout(new GridLayout(6, false));
        
        scrolled_composite_UCCE.setContent(compositeUCCE);
        scrolled_composite_UCCE.setExpandVertical( true );
        scrolled_composite_UCCE.setExpandHorizontal( true );
        scrolled_composite_UCCE.setVisible(true);
        
        Label lbl_hostname = new Label(compositeUCCE, SWT.BORDER);
        lbl_hostname.setText("  Hostname       ");

        Label lbl_ipaddress = new Label( compositeUCCE, SWT.BORDER);
        lbl_ipaddress.setText("  IP-address  ");

        Label lbl_type = new Label( compositeUCCE, SWT.BORDER);
        lbl_type.setText("      Machine Type          ");

        Label lbl_ucce_status = new Label( compositeUCCE, SWT.BORDER);
        lbl_ucce_status.setText("        Status                                                ");

        Label lbl_ucce_cert_expire = new Label( compositeUCCE, SWT.BORDER);
        lbl_ucce_cert_expire.setText("     Expiration date       ");

        Label lbl_ucce_details = new Label( compositeUCCE, SWT.BORDER);
        lbl_ucce_details.setText("   Details...");
        


        //////////////////////////////////////////////////////////////////////////////////////////		
        final Composite compositeESXi = new Composite(shell, SWT.NONE);
        compositeESXi.setVisible(false);
        final FormData fd_compositeESXi = new FormData();
        fd_compositeESXi.bottom = new FormAttachment(0, 300);
        fd_compositeESXi.right = new FormAttachment(0, 980);
        fd_compositeESXi.top = new FormAttachment(0, 230);
        fd_compositeESXi.left = new FormAttachment(0, 10);
        compositeESXi.setLayoutData(fd_compositeESXi);
        compositeESXi.setLayout(new GridLayout(7, false));

        Label lbl_vmname = new Label(compositeESXi, SWT.BORDER);
        lbl_vmname.setText("  VM name       ");

        Label lbl_vmtype = new Label(compositeESXi, SWT.BORDER);
        lbl_vmtype.setText("  VM Type      ");

        Label lbl_vmhostname = new Label(compositeESXi, SWT.BORDER);
        lbl_vmhostname.setText("      Hostname          ");

        Label lbl_vm_ports = new Label(compositeESXi, SWT.BORDER);
        lbl_vm_ports.setText("  Ports  ");

        Label lbl_vm_status = new Label(compositeESXi, SWT.BORDER);
        lbl_vm_status.setText("        Status                                                ");

        Label lbl_vm_cert_expire = new Label(compositeESXi, SWT.BORDER);
        lbl_vm_cert_expire.setText("     Expiration date       ");

        Label lbl_vm_details = new Label(compositeESXi, SWT.BORDER);
        lbl_vm_details.setText("   Details...");

        //////////////////////////////////////////////////////////////////////////////////////////////		
        final FormData fd_compositeFree = new FormData();

        final Composite compositeFree = new Composite(shell, SWT.BORDER);
        fd_compositeFree.bottom = new FormAttachment(0, 190);
        fd_compositeFree.right = new FormAttachment(0, 980);
        fd_compositeFree.top = new FormAttachment(0, 110);
        fd_compositeFree.left = new FormAttachment(0, 10);
        compositeFree.setVisible(false);
        compositeFree.setLayoutData(fd_compositeFree);

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 6;
        compositeFree.setLayout(gridLayout);
        
        Group groupUCCEversion = new Group(shell, SWT.NONE);
        groupUCCEversion.setText("UCCE version");
        
        final FormData fd_groupUCCEversion = new FormData();
        fd_groupUCCEversion.bottom = new FormAttachment(0, 105);
        fd_groupUCCEversion.right = new FormAttachment(0, 90);
        fd_groupUCCEversion.top = new FormAttachment(0, 10);
        fd_groupUCCEversion.left = new FormAttachment(0, 10);
        
        
        Button btnRadio_12_5 = new Button(groupUCCEversion, SWT.RADIO);
        btnRadio_12_5.setText("12.0/12.5");

        Button btnRadio_12_6 = new Button(groupUCCEversion, SWT.RADIO);
        btnRadio_12_6.setText("12.6");
        btnRadio_12_6.setSelection(true);
        
        groupUCCEversion.setLayoutData(fd_groupUCCEversion);
        groupUCCEversion.setLayout(new RowLayout(SWT.VERTICAL));

        Group groupJKSdata = new Group(shell, SWT.NONE);
        groupJKSdata.setText("Keystore details");

        final FormData fd_groupJKSdata = new FormData();
        fd_groupJKSdata.bottom = new FormAttachment(0, 105);
        fd_groupJKSdata.right = new FormAttachment(0, 475);
        fd_groupJKSdata.top = new FormAttachment(0, 10);
        fd_groupJKSdata.left = new FormAttachment(0, 100);

        groupJKSdata.setLayoutData(fd_groupJKSdata);

        Group grpStoreType = new Group(groupJKSdata, SWT.NONE);
        grpStoreType.setText("Store Type");
        grpStoreType.setBounds(267, 10, 90, 65);
        grpStoreType.setLayout(new RowLayout(SWT.VERTICAL));

        Group grpMode = new Group(shell, SWT.NONE);
        grpMode.setText("Mode");
        grpMode.setBounds(400, 95, 100, 70);
        grpMode.setLayout(new RowLayout(SWT.VERTICAL));

        final FormData fd_grpMode = new FormData();
        fd_grpMode.bottom = new FormAttachment(0, 105);
        fd_grpMode.right = new FormAttachment(0, 580);
        fd_grpMode.top = new FormAttachment(0, 10);
        fd_grpMode.left = new FormAttachment(0, 480);
        grpMode.setLayoutData(fd_grpMode);
        grpMode.setVisible(true);

        Group groupUCCEdata = new Group(shell, SWT.NONE);
        groupUCCEdata.setVisible(true);

        final FormData fd_groupUCCEdata = new FormData();
        fd_groupUCCEdata.bottom = new FormAttachment(0, 225);
        fd_groupUCCEdata.right = new FormAttachment(0, 680);
        fd_groupUCCEdata.top = new FormAttachment(0, 110);
        fd_groupUCCEdata.left = new FormAttachment(0, 10);

        groupUCCEdata.setLayoutData(fd_groupUCCEdata);



        Group groupESXIdata = new Group(shell, SWT.NONE);

        groupESXIdata.setVisible(false);

        final FormData fd_groupESXIdata = new FormData();
        fd_groupESXIdata.bottom = new FormAttachment(0, 225);
        fd_groupESXIdata.right = new FormAttachment(0, 680);
        fd_groupESXIdata.top = new FormAttachment(0, 110);
        fd_groupESXIdata.left = new FormAttachment(0, 10);

        groupESXIdata.setLayoutData(fd_groupESXIdata);
        /////////////////////////////////////////////////
        esxi_hostname = new Text(groupESXIdata, SWT.BORDER);
        esxi_hostname.setBounds(121, 22, 131, 21);

        esxi_hostname.setText("");

        Label lblESXIhostname = new Label(groupESXIdata, SWT.NONE);
        lblESXIhostname.setBounds(10, 25, 105, 15);
        lblESXIhostname.setText("ESXI server address");

        Label lblESXIUsername = new Label(groupESXIdata, SWT.NONE);
        lblESXIUsername.setBounds(10, 60, 55, 15);
        lblESXIUsername.setText("Username");

        esxi_user = new Text(groupESXIdata, SWT.BORDER);
        esxi_user.setEnabled(true);
        esxi_user.setBounds(121, 60, 131, 21);
        esxi_user.setText("");

        Label lblESXIpassword = new Label(groupESXIdata, SWT.NONE);
        lblESXIpassword.setBounds(267, 60, 55, 15);
        lblESXIpassword.setText("Password");

        esxi_password = new Text(groupESXIdata, SWT.BORDER | SWT.PASSWORD);
        esxi_password.setEnabled(true);
        esxi_password.setBounds(345, 60, 131, 21);
        esxi_password.setText("");

        ProgressBar ESXi_progressBar = new ProgressBar(groupESXIdata, SWT.NONE);
        ESXi_progressBar.setBounds(10, 90, 630, 10);
        ESXi_progressBar.setMinimum(0);


        /////////////////////////////////////////////////

        final Button btnAddHost = new Button(compositeFree, SWT.NONE);
        btnAddHost.setEnabled(true);
        btnAddHost.setText("Add host");

        Label lblhostname = new Label(compositeFree, SWT.NONE);
        lblhostname.setText("Hostname                      ");

        Label lblPort = new Label(compositeFree, SWT.NONE);
        lblPort.setText("Port");

        Label lblEmpty3 = new Label(compositeFree, SWT.NONE);
        lblEmpty3.setText("                      ");

        Label lblEmpty4 = new Label(compositeFree, SWT.NONE);
        lblEmpty4.setText("                      ");
        
        Label lblEmpty5 = new Label(compositeFree, SWT.NONE);
        lblEmpty5.setText("                          "); 

        Label lblCApath = new Label(groupJKSdata, SWT.NONE);
        lblCApath.setBounds(10, 20, 105, 15);
        lblCApath.setText("Path to cacerts");

        cacertPath = new Text(groupJKSdata, SWT.BORDER);
        cacertPath.setBounds(121, 20, 131, 21);
        cacertPath.setText("C:\\icm\\ssl\\cacerts");

        Label lblCApassword = new Label(groupJKSdata, SWT.NONE);
        lblCApassword.setBounds(10, 50, 100, 15);
        lblCApassword.setText("Keystore Password");

        cacertPassw = new Text(groupJKSdata, SWT.BORDER | SWT.PASSWORD);
        cacertPassw.setEnabled(true);
        cacertPassw.setBounds(121, 50, 131, 21);
        cacertPassw.setText("changeit");

        Button btnJCEKS = new Button(grpStoreType, SWT.RADIO);
        btnJCEKS.setText("JCEKS");
        btnJCEKS.setEnabled(false);

        Button btnJKS = new Button(grpStoreType, SWT.RADIO);
        btnJKS.setText("JKS");
        btnJKS.setSelection(true);

        btnJCEKS.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
            	if (btnJCEKS.getSelection()){
                    MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
                    messageBox.setMessage("JCEKS store type used only in CVP solution");
                    messageBox.setText("JCEKS store type selected");
                    messageBox.open();
            	}

            	
            }
        });
        
        
        btnRadio_12_6.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
            	if (btnRadio_12_6.getSelection()){
            		 cacertPath.setText("C:\\icm\\ssl\\cacerts");
            	}

            	
            }
        });
        
        btnRadio_12_5.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
            	if (btnRadio_12_5.getSelection()){
            		String cce_java_home = System.getenv("CCE_JAVA_HOME");
            		String java_home = System.getenv("JAVA_HOME");
            		if (cce_java_home.length()>0) {
            			cacertPath.setText(cce_java_home+"\\lib\\security\\cacerts");
            		}
            		else {
            			cacertPath.setText(java_home+"\\lib\\security\\cacerts");
            		}
            	}

            	
            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////////

        btnAddHost.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {



                if (freehostcount < 10) {

                    lblArray[freehostcount] = new Label(compositeFree, SWT.NONE);
                    hostname[freehostcount] = new Text(compositeFree, SWT.BORDER);
                    hostname[freehostcount].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
                    port[freehostcount] = new Text(compositeFree, SWT.BORDER);
                    btnUpload[freehostcount] = new Button(compositeFree, SWT.TOGGLE);
                    btnUpload[freehostcount].setText("Get and upload certificate");
                    btnUpload[freehostcount].setData("key", String.valueOf(freehostcount));
                    btnUpload[freehostcount].getData();
                    freeResult[freehostcount] = new Text(compositeFree, SWT.BORDER);
                    freeResult[freehostcount].setEditable(false);
                    freeResult[freehostcount].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
                    freeResult[freehostcount].setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
                    freeResult[freehostcount].setText("Unknown yet                     ");
                    lblEmpty4.setText("Result");
                    lblEmpty5.setText("Expiration date");
                    free_cert_expire[freehostcount] = new Text(compositeFree, SWT.BORDER);
                    free_cert_expire[freehostcount].setEditable(false);
                    free_cert_expire[freehostcount].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
                    free_cert_expire[freehostcount].setText("                                ");
                    
                    btnUpload[freehostcount].addSelectionListener(new SelectionAdapter() {
                        @Override
                        public void widgetSelected(SelectionEvent arg0) {
                            int i = 0;

                            while (i < 10) {
                                boolean state = btnUpload[i].getSelection();
                                if (state) {
                                    UploadCert load = new UploadCert();
                                    try {

                                        if (btnJKS.getSelection()) {
                                            storeType = "JKS";
                                        }
                                        if (btnJCEKS.getSelection()) {
                                            storeType = "JCEKS";
                                        }

                                        freeResult[i].setText("In Progress... ");
                                        compositeFree.update();
                                        freeResult[i].setText(
                                            load.doUpload(hostname[i].getText(), Integer.valueOf(port[i].getText()),
                                                cacertPath.getText(), cacertPassw.getText(), storeType));
                                        if (freeResult[i].getText().contains("Error")) {
                                            freeResult[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
                                            freeResult[i].setText(
                                                freeResult[i].getText() + " on port " + Integer.valueOf(port[i].getText()));
                                            compositeFree.update();
                                        } else if (freeResult[i].getText().contains("Done")) {
                                            freeResult[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
                                            free_cert_expire[i].setText(load.getCertExpiration());
                                            if ((load.getCertDateDiff() < 180) && (load.getCertDateDiff() > 30)) {
                                            	free_cert_expire[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
                                            } else if (load.getCertDateDiff() < 30) {
                                            	free_cert_expire[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
                                            } else if (load.getCertDateDiff() > 180) {
                                            	free_cert_expire[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
                                            }
                                            compositeFree.update();
                                        }
                                    } catch (KeyManagementException | NumberFormatException | KeyStoreException |
                                        NoSuchAlgorithmException | CertificateException | IOException e) {
                                        e.printStackTrace();
                                    }
                                    btnUpload[i].setSelection(false);
                                    break;

                                }
                                i++;
                            }
                        }
                    });
                    freehostcount++;

                } else {
                    MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
                    messageBox.setMessage("Maximum 10 hosts can be processed in Free mode");
                    messageBox.setText("Reached maximum hosts count");
                    messageBox.open();
                }
                compositeFree.requestLayout();
            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////////
        awdb_name = new Text(groupUCCEdata, SWT.BORDER);
        awdb_name.setBounds(121, 22, 131, 21);

        awdb_name.setText("_awdb");

        Label lblAW_db = new Label(groupUCCEdata, SWT.NONE);
        lblAW_db.setBounds(10, 25, 105, 15);
        lblAW_db.setText("AW database name");

        Label lblUsername = new Label(groupUCCEdata, SWT.NONE);
        lblUsername.setBounds(10, 60, 55, 15);
        lblUsername.setText("Username");

        db_user = new Text(groupUCCEdata, SWT.BORDER);
        db_user.setEnabled(false);
        db_user.setBounds(121, 60, 131, 21);
        db_user.setText("");

        Label lblpassword = new Label(groupUCCEdata, SWT.NONE);
        lblpassword.setBounds(267, 60, 55, 15);
        lblpassword.setText("Password");

        db_password = new Text(groupUCCEdata, SWT.BORDER | SWT.PASSWORD);
        db_password.setEnabled(false);
        db_password.setBounds(345, 60, 131, 21);
        db_password.setText("");

        ProgressBar progressBar = new ProgressBar(groupUCCEdata, SWT.NONE);
        progressBar.setBounds(10, 90, 630, 10);
        progressBar.setMinimum(0);
        ////////////////////////////////////////////////////////////////////////
        final Button btnWindowsAuth = new Button(groupUCCEdata, SWT.CHECK);
        btnWindowsAuth.setSelection(true);
        btnWindowsAuth.setBounds(267, 27, 162, 16);
        btnWindowsAuth.setText("Windows Authentification");

        btnWindowsAuth.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {
                if (btnWindowsAuth.getSelection()) {
                    db_password.setEnabled(false);
                    db_user.setEnabled(false);
                } else {
                    db_password.setEnabled(true);
                    db_user.setEnabled(true);
                }
            }

        });
        ///////////////////////////////////////////////////////////////////////////////////////////
        final Button btnUploadAllCerts = new Button(groupUCCEdata, SWT.NONE);
        btnUploadAllCerts.setEnabled(false);
        btnUploadAllCerts.setBounds(510, 59, 130, 25);
        btnUploadAllCerts.setText("Upload all certificates");

        btnUploadAllCerts.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (btnJKS.getSelection()) {
                    storeType = "JKS";
                }
                if (btnJCEKS.getSelection()) {
                    storeType = "JCEKS";
                }
                btnUploadAllCerts.setEnabled(false);
                cacertPassw.setEnabled(false);
                cacertPath.setEnabled(false);
                int ObjRows = result.length;
                int count = 0;
                while (ObjRows > 0) {
                    ObjRows--;
                    if (Objects.isNull(address[ObjRows])) {} else {
                        count = count + 1;
                    }
                }

                progressBar.setSelection(0);
                progressBar.setMaximum(count);

                while (row < count) {
                    String[] portsArray = machinetypeDesc.getMachinePorts(machinetypeID[row]);
                    UploadAllUCCE[row] = new UploadCert();
               //     UploadCert loadAll = new UploadCert();
                    try {
                        int i = 0;
                        if (portsArray[i] == "0") {
                            System.out.println("Not requred");
                            result[row].setText("Not requred for this machine type");
                        } else {
                            int y = 0;
                            while (y < portsArray.length) {
                                result[row].setText("In progress");
                                compositeUCCE.update();
                                result[row].setText(
                                		UploadAllUCCE[row].doUpload(hostnames[row].getText(), Integer.parseInt(portsArray[y]),
                                        cacertPath.getText(), cacertPassw.getText(), storeType));

                                if (result[row].getText().contains("Error")) {
                                    result[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
                                    result[row].setText(
                                        result[row].getText() + " on port " + Integer.parseInt(portsArray[y]));
                                    compositeUCCE.update();
                                    break;
                                } else if (result[row].getText().contains("Done")) {
                                    result[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
                                    cert_expire[row].setText(UploadAllUCCE[row].getCertExpiration());
                                    if ((UploadAllUCCE[row].getCertDateDiff() < 180) && (UploadAllUCCE[row].getCertDateDiff() > 30)) {
                                        cert_expire[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
                                    } else if (UploadAllUCCE[row].getCertDateDiff() < 30) {
                                        cert_expire[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
                                    } else if (UploadAllUCCE[row].getCertDateDiff() > 180) {
                                        cert_expire[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
                                    }
                                    
                                    
                                    
                                    compositeUCCE.update();
                                }
                                y++;
                            }
                        }

                    } catch (KeyManagementException | NumberFormatException | KeyStoreException |
                        NoSuchAlgorithmException | CertificateException | IOException e) {
                        result[row].setText(e.getMessage());
                        e.printStackTrace();
                    }
                    progressBar.setSelection(row + 1);

                    row++;
                }
                btnUploadAllCerts.setEnabled(true);
                cacertPassw.setEnabled(true);
                cacertPath.setEnabled(true);
                row = 0;
                compositeUCCE.requestLayout();

            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////////
        final Button btnLoadInventory = new Button(groupUCCEdata, SWT.NONE);
        btnLoadInventory.setBounds(510, 22, 131, 25);
        btnLoadInventory.setText("Load Inventory");

        btnLoadInventory.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {

                btnLoadInventory.setEnabled(false);
                btnUploadAllCerts.setEnabled(true);
                compositeUCCE.setVisible(true);

                int b = 30;
                if (btnWindowsAuth.getSelection() == true) {
                
                	inventoryData.SetDataFromAW_ConnectionWin(awdb_name.getText());

                    String SQLtest = inventoryData.testSQLconnection();
                    if (SQLtest == "done") {

                        int rows = inventoryData.getRowsCount();
                        host = inventoryData.getHosts(rows).clone();
                        progressBar.setSelection(0);
                        progressBar.setMaximum(rows);
                        while (row < rows) {

                            hostnames[row] = new Text(compositeUCCE, SWT.NONE);
                            hostnames[row].setText(host[row].getHostName());
                            hostnames[row].setEditable(false);
                            address[row] = new Text(compositeUCCE, SWT.NONE);
                            address[row].setText(host[row].getAddress());
                            address[row].setEditable(false);
                            machinename[row] = new Text(compositeUCCE, SWT.NONE);
                            machinename[row].setText(machinetypeDesc.getMachineTypeName(host[row].getMachineType()));
                            machinename[row].setEditable(false);
                            machinetypeID[row] = host[row].getMachineType();
                            result[row] = new Text(compositeUCCE, SWT.MULTI | SWT.WRAP);
                            result[row].setText("Unknown yet                                     ");
                            result[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
                            result[row].setEditable(false);
                            
                            cert_expire[row] = new Text(compositeUCCE, SWT.MULTI | SWT.WRAP);
                            cert_expire[row].setText("Unknown yet                    ");
                            cert_expire[row].setEditable(false);
                            
                            btn_details[row] = new Button(compositeUCCE, SWT.TOGGLE);
                            btn_details[row].setText("   Details...");
                            btn_details[row].setData("key", String.valueOf(row));
                            //???????????????????????????????????
                            
                            
                            btn_details[row].addSelectionListener(new SelectionAdapter() {
                                @Override
                                public void widgetSelected(SelectionEvent arg0) {
                                    int i = 0;
                                    while (i < 50) {
                                        boolean state = btn_details[i].getSelection();
                                        if (state) {
                                            Shell dialog = new Shell(shell,
                                                SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
                                            dialog.setLayout(new FillLayout());
                                            final Composite compositeChild = new Composite(dialog, SWT.NONE);
                                            compositeChild.setLayout(new FillLayout());
                                            details[i] = new Text(compositeChild, SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL);
                                            if ((Objects.isNull(UploadAllUCCE[i]) == false)) {                                   	
                                                details[i].append(UploadAllUCCE[i].Details);
                                                details[i].append("\n");
                                            }
                                            compositeChild.requestLayout();
                                            compositeChild.update();
                                            dialog.setSize(450, 480);
                                            dialog.setText(" details:");


                                            dialog.open();
                                            Display display = shell.getDisplay();
                                            while (!dialog.isDisposed()) {
                                                if (!display.readAndDispatch()) display.sleep();
                                            }

                                            btn_details[i].setSelection(false);
                                            break;
                                        }
                                        i++;
                                    }
                                }
                            });
                            
                            
                            
////////////////////////////////////////////
                            b = b + 30;
                            scrolled_composite_UCCE.setMinHeight(b);

                            row++;
                            progressBar.setSelection(row);
                        }
                        row = 0;
                        compositeUCCE.requestLayout();

                    } else {
                        MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
                        messageBox.setMessage(SQLtest);
                        messageBox.setText("SQL error");
                        btnLoadInventory.setEnabled(true);
                        messageBox.open();
                    }

                } else {
                	
                	inventoryData.SetDataFromAW_ConnectionSQL(awdb_name.getText(), db_user.getText(),
                        db_password.getText());

                    String SQLtest = inventoryData.testSQLconnection();
                    if (SQLtest == "done") {
                        int rows = inventoryData.getRowsCount();
                        host = inventoryData.getHosts(rows).clone();
                        progressBar.setSelection(0);
                        progressBar.setMaximum(rows);
                        while (row < rows) {
                            hostnames[row] = new Text(compositeUCCE, SWT.NONE);
                            hostnames[row].setText(host[row].getHostName());
                            hostnames[row].setEditable(false);
                            address[row] = new Text(compositeUCCE, SWT.NONE);
                            address[row].setText(host[row].getAddress());
                            address[row].setEditable(false);
                            machinename[row] = new Text(compositeUCCE, SWT.NONE);
                            machinename[row].setText(machinetypeDesc.getMachineTypeName(host[row].getMachineType()));
                            machinename[row].setEditable(false);
                            machinetypeID[row] = host[row].getMachineType();
                            result[row] = new Text(compositeUCCE, SWT.NONE);
                            result[row].setText("Unknown yet                                    ");
                            result[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
                            result[row].setEditable(false);
                            
                            cert_expire[row] = new Text(compositeUCCE, SWT.MULTI | SWT.WRAP);
                            cert_expire[row].setText("Unknown yet                    ");
                            cert_expire[row].setEditable(false);
                            
                            btn_details[row] = new Button(compositeUCCE, SWT.TOGGLE);
                            btn_details[row].setText("   Details...");
                            btn_details[row].setData("key", String.valueOf(row));
                            
                            btn_details[row].addSelectionListener(new SelectionAdapter() {
                                @Override
                                public void widgetSelected(SelectionEvent arg0) {
                                    int i = 0;
                                    while (i < 50) {
                                        boolean state = btn_details[i].getSelection();
                                        if (state) {
                                            Shell dialog = new Shell(shell,
                                                SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
                                            dialog.setLayout(new FillLayout());
                                            final Composite compositeChild = new Composite(dialog, SWT.NONE);
                                            compositeChild.setLayout(new FillLayout());
                                            details[i] = new Text(compositeChild, SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL);
                                            if ((Objects.isNull(UploadAllUCCE[i]) == false)) {                                   	
                                                details[i].append(UploadAllUCCE[i].Details);
                                                details[i].append("\n");
                                            }
                                            compositeChild.requestLayout();
                                            compositeChild.update();
                                            dialog.setSize(450, 480);
                                            dialog.setText(" details:");


                                            dialog.open();
                                            Display display = shell.getDisplay();
                                            while (!dialog.isDisposed()) {
                                                if (!display.readAndDispatch()) display.sleep();
                                            }

                                            btn_details[i].setSelection(false);
                                            break;
                                        }
                                        i++;
                                    }
                                }
                            });
                            

                            b = b + 30;
                            scrolled_composite_UCCE.setMinHeight(b);
                            row++;
                            progressBar.setSelection(row);
                        }
                        row = 0;
                        compositeUCCE.requestLayout();
                    } else {
                        MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
                        messageBox.setMessage(SQLtest);
                        messageBox.setText("SQL error");
                        btnLoadInventory.setEnabled(true);
                        messageBox.open();

                    }

                }

            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////
        final Button btnUploadAllVMsCerts = new Button(groupESXIdata, SWT.NONE);
        btnUploadAllVMsCerts.setEnabled(false);
        btnUploadAllVMsCerts.setBounds(510, 59, 130, 25);
        btnUploadAllVMsCerts.setText("Upload all certificates");
        ///////////////////////////////////////////////////////////////////////////////////////////
        final Button btnLoadVMs = new Button(groupESXIdata, SWT.NONE);
        btnLoadVMs.setBounds(510, 22, 131, 25);
        btnLoadVMs.setText("Load VMs");

        btnLoadVMs.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {

                btnLoadVMs.setEnabled(false);
                GetDataFromESXi ESXiData = new GetDataFromESXi();

                try {
                    VirtualMachine[] VMs_array = (ESXiData.getVMs(esxi_hostname.getText(), esxi_user.getText(), esxi_password.getText())).clone();
                    int rows = VMs_array.length;
                    while (row < rows) {

                        GridData gd_status = new GridData();
                        gd_status.heightHint = 30;

                        GridData gd_cert = new GridData();
                        gd_cert.heightHint = 30;
                        gd_status.widthHint = 180;

                        vmname[row] = new Text(compositeESXi, SWT.NONE);
                        vmname[row].setText(VMs_array[row].getVMName());
                        vmname[row].setEditable(false);
                        
                        vmtype[row] = new Text(compositeESXi, SWT.NONE);
                        vmtype[row].setText(VMs_array[row].getMachineType());
                        vmtype[row].setEditable(false);

                        vmhostname[row] = new Text(compositeESXi, SWT.NONE);
                        vmhostname[row].setText(VMs_array[row].getHostName());
                        vmhostname[row].setEditable(true);
                        
                        if (vmhostname[row].getText().contains("available")) {
                        	vmhostname[row].setToolTipText("Check if VM is running and if VM tools are installed");
                        }

                        if ((vmtype[row].getText().contains("UCCE"))) {
                            vm_ports[row] = new Text(compositeESXi, SWT.NONE);
                            vm_ports[row].setText((VMs_array[row].ports[0]) + " and " + (VMs_array[row].ports[1]));
                        } else {
                            vm_ports[row] = new Text(compositeESXi, SWT.NONE);
                            vm_ports[row].setText(VMs_array[row].ports[0]);
                        }
                        vm_ports[row].setEditable(false);

                        vm_status[row] = new Text(compositeESXi, SWT.MULTI | SWT.WRAP);
                        vm_status[row].setText("Unknown yet                                ");
                        vm_status[row].setEditable(false);
                        vm_status[row].setLayoutData(gd_status);

                        vm_cert_expire[row] = new Text(compositeESXi, SWT.MULTI | SWT.WRAP);
                        vm_cert_expire[row].setText("Unknown yet                        ");
                        vm_cert_expire[row].setEditable(false);
                        vm_cert_expire[row].setLayoutData(gd_cert);

                        btnVm_details[row] = new Button(compositeESXi, SWT.TOGGLE);
                        btnVm_details[row].setText("   Details...");
                        btnVm_details[row].setData("key", String.valueOf(row));

                        if ((vmtype[row].getText().contains("Unknown")) || (vmtype[row].getText().contains("Annotation is not"))) {
                            btnVm_details[row].setVisible(false);
                            vm_cert_expire[row].setVisible(false);
                            vm_status[row].setText("Unknown");
                            vm_ports[row].setVisible(false);
                            vmhostname[row].setEditable(false);
                        }

                        btnVm_details[row].addSelectionListener(new SelectionAdapter() {
                            @Override
                            public void widgetSelected(SelectionEvent arg0) {
                                int i = 0;
                                while (i < 50) {
                                    boolean state = btnVm_details[i].getSelection();
                                    if (state) {
                                        Shell dialog = new Shell(shell,
                                            SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
                                        dialog.setLayout(new FillLayout());
                                        final Composite compositeChild = new Composite(dialog, SWT.NONE);
                                        compositeChild.setLayout(new FillLayout());
                                        vm_details[i] = new Text(compositeChild, SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL);
                                        vm_details[i].setText("VM name: " + VMs_array[i].getVMName() + "\n");
                                        vm_details[i].append("Annotations: " + VMs_array[i].getAnnotation() + "\n");
                                        vm_details[i].append("Hostname: " + VMs_array[i].getHostName() + "\n");
                                        if ((Objects.isNull(UploadAll[i]) == false)) {                                   	
                                            vm_details[i].append(UploadAll[i].Details);
                                            vm_details[i].append("\n");
                                        }
                                        compositeChild.requestLayout();
                                        compositeChild.update();
                                        dialog.setSize(450, 480);
                                        dialog.setText("VM " + VMs_array[i].getVMName() + " details:");


                                        dialog.open();
                                        Display display = shell.getDisplay();
                                        while (!dialog.isDisposed()) {
                                            if (!display.readAndDispatch()) display.sleep();
                                        }

                                        btnVm_details[i].setSelection(false);
                                        break;
                                    }
                                    i++;
                                }
                            }
                        });

                        row++;

                    }
                    btnUploadAllVMsCerts.setEnabled(true);
                    btnLoadVMs.setEnabled(false);
                    compositeESXi.requestLayout();
                    row = 0;

                } catch (Exception e) {
                	MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
                    messageBox.setMessage(e.getMessage());
                    messageBox.setText("ESXi error");
                    btnLoadVMs.setEnabled(true);
                    messageBox.open();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                row = 0;

            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////		


        btnUploadAllVMsCerts.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (btnJKS.getSelection()) {
                    storeType = "JKS";
                }
                if (btnJCEKS.getSelection()) {
                    storeType = "JCEKS";
                }
                btnUploadAllVMsCerts.setEnabled(false);
                cacertPassw.setEnabled(false);
                cacertPath.setEnabled(false);
                int ObjRows = vm_status.length;
                int count = 0;
                while (ObjRows > 0) {
                    ObjRows--;
                    if (Objects.isNull(vm_status[ObjRows])) {} else {
                        count = count + 1;
                    }
                }

                ESXi_progressBar.setSelection(0);
                ESXi_progressBar.setMaximum(count);
                //		UploadCert UploadAll[] = new UploadCert[count];
                while (row < count) {
                    System.out.println(vmtype[row].getText());
                    UploadAll[row] = new UploadCert();
                    try {
                        if (vmtype[row].getText().contains("known")) {
                            vm_status[row].setText("N/A");

                        } else if (vmtype[row].getText().contains("UCCE")) {


                            vm_status[row].setText("In progress");
                            compositeESXi.update();
                            vm_status[row].setText(
                                UploadAll[row].doUpload(vmhostname[row].getText(), 443,
                                    cacertPath.getText(), cacertPassw.getText(), storeType));
                            vm_status[row].append("\n");

                            if (vm_status[row].getText().contains("Error")) {
                                vm_status[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
                                vm_status[row].setText("IIS: " +
                                    vm_status[row].getText());
                                vm_cert_expire[row].setText("IIS Cerificate errors");
                                vm_cert_expire[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
                                compositeESXi.update();
                            } else if (vm_status[row].getText().contains("Done")) {
                                vm_status[row].setText("IIS: " + vm_status[row].getText());
                                vm_status[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));

                                vm_cert_expire[row].setText("IIS: " + UploadAll[row].getCertExpiration());
                                if ((UploadAll[row].getCertDateDiff() < 180) && (UploadAll[row].getCertDateDiff() > 30)) {
                                    vm_cert_expire[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
                                } else if (UploadAll[row].getCertDateDiff() < 30) {
                                    vm_cert_expire[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
                                } else if (UploadAll[row].getCertDateDiff() > 180) {
                                    vm_cert_expire[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
                                }
                                compositeESXi.update();
                            }


                            vm_status[row].append("Portico: " +
                                UploadAll[row].doUpload(vmhostname[row].getText(), 7890,
                                    cacertPath.getText(), cacertPassw.getText(), storeType));

                            if (vm_status[row].getText().contains("Portico: Error")) {
                                vm_status[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
                                vm_cert_expire[row].append("\nPortico Cerificate errors");
                                vm_cert_expire[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
                                compositeESXi.update();
                            } else if ((vm_status[row].getText().contains("Done"))) {

                                vm_cert_expire[row].append("\nPortico: " + UploadAll[row].getCertExpiration());
                                if ((UploadAll[row].getCertDateDiff() < 180) && (UploadAll[row].getCertDateDiff() > 30)) {
                                    vm_cert_expire[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
                                } else if (UploadAll[row].getCertDateDiff() < 30) {
                                    vm_cert_expire[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
                                } else if (UploadAll[row].getCertDateDiff() > 180) {
                                    vm_cert_expire[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
                                }
                                compositeESXi.update();
                            }



                        } else {
                            vm_status[row].setText("In progress");
                            compositeESXi.update();
                            vm_status[row].setText(
                                UploadAll[row].doUpload(vmhostname[row].getText(), Integer.valueOf(vm_ports[row].getText()),
                                    cacertPath.getText(), cacertPassw.getText(), storeType));

                            if (vm_status[row].getText().contains("Error")) {
                                vm_status[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
                                compositeESXi.update();
                            } else if (vm_status[row].getText().contains("Done")) {
                                vm_status[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
                                vm_cert_expire[row].setText(UploadAll[row].getCertExpiration());
                                if ((UploadAll[row].getCertDateDiff() < 180) && (UploadAll[row].getCertDateDiff() > 30)) {
                                    vm_cert_expire[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
                                } else if (UploadAll[row].getCertDateDiff() < 30) {
                                    vm_cert_expire[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
                                } else if (UploadAll[row].getCertDateDiff() > 180) {
                                    vm_cert_expire[row].setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
                                }
                                compositeESXi.update();
                            }


                        }


                    } catch (KeyManagementException | NumberFormatException | KeyStoreException |
                        NoSuchAlgorithmException | CertificateException | IOException e) {
                        vm_status[row].setText(e.getMessage());
                        btnLoadVMs.setEnabled(true);
                        e.printStackTrace();
                    }
                    ESXi_progressBar.setSelection(row + 1);

                    row++;
                }
                btnUploadAllVMsCerts.setEnabled(true);
                cacertPassw.setEnabled(true);
                cacertPath.setEnabled(true);
               // btnLoadVMs.setEnabled(true);
                row = 0;
                compositeESXi.requestLayout();

            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////		
        ///////////////////////////////////////////////////////////////////////////////////////////		

        final Button btnFreemode = new Button(grpMode, SWT.RADIO);
        btnFreemode.setSelection(false);
        btnFreemode.setBounds(450, 27, 100, 16);
        btnFreemode.setText("Free");

        btnFreemode.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {
                if (btnFreemode.getSelection()) {
                    db_password.setEnabled(false);
                    db_user.setEnabled(false);
                    btnWindowsAuth.setEnabled(false);
                    awdb_name.setEnabled(false);
                    btnLoadInventory.setEnabled(false);
                    groupUCCEdata.setVisible(false);
                    compositeUCCE.setVisible(false);
                    scrolled_composite_UCCE.setVisible(false);
                    compositeFree.setVisible(true);
                    compositeESXi.setVisible(false);
                    btnUploadAllCerts.setEnabled(false);
                    fd_compositeFree.bottom = new FormAttachment(0, 500);
                    btnJCEKS.setEnabled(true);
                    compositeFree.requestLayout();
                    compositeUCCE.requestLayout();

                } else {

                    if (Objects.isNull(address[0])) {
                        btnLoadInventory.setEnabled(true);
                        db_password.setEnabled(false);
                        db_user.setEnabled(false);
                        awdb_name.setEnabled(true);
                        btnWindowsAuth.setEnabled(true);
                        btnWindowsAuth.setSelection(true);
                        btnUploadAllCerts.setEnabled(false);

                    } else {
                        btnUploadAllCerts.setEnabled(true);
                        btnLoadInventory.setEnabled(false);
                        db_password.setEnabled(false);
                        db_user.setEnabled(false);
                        awdb_name.setEnabled(false);
                    }

                    compositeFree.setVisible(false);
                    compositeESXi.setVisible(false);
                    compositeUCCE.setVisible(true);
                    scrolled_composite_UCCE.setVisible(true);
                    compositeUCCE.requestLayout();


                }
            }

        });

        ///////////////////////////////////////////////////////////////////////////////////////////



        ///////////////////////////////////////////////////////////////////////////////////////////
        final Button btnESXiMode = new Button(grpMode, SWT.RADIO);
        btnESXiMode.setSelection(false);
        btnESXiMode.setBounds(450, 27, 100, 16);
        btnESXiMode.setText("ESXi");

        btnESXiMode.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {
                if (btnESXiMode.getSelection()) {
                    db_password.setEnabled(false);
                    db_user.setEnabled(false);
                    btnWindowsAuth.setEnabled(false);
                    awdb_name.setEnabled(false);
                    btnLoadInventory.setEnabled(false);
                    compositeUCCE.setVisible(false);
                    scrolled_composite_UCCE.setVisible(false);
                    compositeFree.setVisible(false);
                    groupESXIdata.setVisible(true);
                    compositeESXi.setVisible(true);
                    btnUploadAllCerts.setEnabled(false);
                    fd_compositeESXi.bottom = new FormAttachment(0, 550);
                    btnJKS.setSelection(true);
                    btnJCEKS.setEnabled(false);
                    btnJCEKS.setSelection(false);
                    compositeESXi.requestLayout();
                    compositeUCCE.requestLayout();

                } else {

                    if (Objects.isNull(address[0])) {
                        btnLoadInventory.setEnabled(true);
                        db_password.setEnabled(false);
                        db_user.setEnabled(false);
                        awdb_name.setEnabled(true);
                        btnWindowsAuth.setEnabled(true);
                        btnWindowsAuth.setSelection(true);
                        btnUploadAllCerts.setEnabled(false);

                    } else {
                        btnUploadAllCerts.setEnabled(true);
                        btnLoadInventory.setEnabled(false);
                        db_password.setEnabled(false);
                        db_user.setEnabled(false);
                        awdb_name.setEnabled(false);
                    }

                    compositeFree.setVisible(false);
                    compositeUCCE.setVisible(false);
                    scrolled_composite_UCCE.setVisible(false);
                    compositeESXi.requestLayout();


                }
            }

        });

        ///////////////////////////////////////////////////////////////////////////////////////////
        final Button btnUCCEMode = new Button(grpMode, SWT.RADIO);
        btnUCCEMode.setSelection(true);
        btnUCCEMode.setBounds(450, 27, 100, 16);
        btnUCCEMode.setText("UCCE/PCCE");

        btnUCCEMode.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {
                if (btnUCCEMode.getSelection()) {
                    groupUCCEdata.setVisible(true);
                    compositeFree.setVisible(false);
                    compositeESXi.setVisible(false);
                    compositeUCCE.setVisible(true);
                    scrolled_composite_UCCE.setVisible(true);
                    compositeUCCE.requestLayout();
                    btnJKS.setSelection(true);
                    btnJCEKS.setEnabled(false);
                    btnJCEKS.setSelection(false);


                } else {
                    compositeFree.setVisible(false);
                    compositeESXi.setVisible(false);
                    compositeUCCE.setVisible(true);
                    compositeUCCE.requestLayout();
                    groupUCCEdata.setVisible(false);

                }
            }

        });

        grpMode.requestLayout();
        grpStoreType.requestLayout();
        groupJKSdata.requestLayout();
        groupUCCEdata.requestLayout();
        compositeUCCE.setVisible(true);
        groupUCCEversion.requestLayout();
        scrolled_composite_UCCE.setVisible(true);

        ///////////////////////////////////////////////////////////////////////////////////////////
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}