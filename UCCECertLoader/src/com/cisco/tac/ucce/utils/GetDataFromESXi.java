package com.cisco.tac.ucce.utils;
import com.vmware.vim25.*;

import java.util.*;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.ws.BindingProvider;
     
public class GetDataFromESXi {
     
  private static RetrieveResult collectProperties(VimPortType methods,
                                        ServiceContent sContent) throws Exception {
	  
    ManagedObjectReference viewMgrRef = sContent.getViewManager();
    ManagedObjectReference propColl = sContent.getPropertyCollector();
     
    // use a container view for virtual machines to define the traversal
    // - invoke the VimPortType method createContainerView (corresponds
    //   to the ViewManager method) - pass the ViewManager MOR and
    //   the other parameters required for the method invocation
    //   (use a List<String> for the type parameter's string[])
    List<String> vmList = new ArrayList<String>();
    vmList.add("VirtualMachine");
     
    ManagedObjectReference cViewRef = methods.createContainerView(viewMgrRef, sContent.getRootFolder(), vmList, true);
     
    // create an object spec to define the beginning of the traversal;
    // container view is the root object for this traversal
    ObjectSpec oSpec = new ObjectSpec();
    oSpec.setObj(cViewRef);
    oSpec.setSkip(true);
     
    // create a traversal spec to select all objects in the view
    TraversalSpec tSpec = new TraversalSpec();
    tSpec.setName("traverseEntities");
    tSpec.setPath("view");
    tSpec.setSkip(false);
    tSpec.setType("ContainerView");
     
    // add the traversal spec to the object spec;
    oSpec.getSelectSet().add(tSpec);

     
    // specify the properties for retrieval
    PropertySpec pSpec = new PropertySpec();
    pSpec.setType("VirtualMachine");
    pSpec.getPathSet().add("guest.hostName");
    pSpec.getPathSet().add("config.annotation");
    pSpec.getPathSet().add("name");
     
    // create a PropertyFilterSpec and add the object and
    // property specs to it;
    PropertyFilterSpec fSpec = new PropertyFilterSpec();
    fSpec.getObjectSet().add(oSpec);
    fSpec.getPropSet().add(pSpec);
     
    // Create a list for the filters and add the spec to it
    List<PropertyFilterSpec> fSpecList = new ArrayList<PropertyFilterSpec>();
    fSpecList.add(fSpec);
     
    // get the data from the server
    RetrieveOptions ro = new RetrieveOptions();
    RetrieveResult props = methods.retrievePropertiesEx(propColl,fSpecList,ro);
     
    return props;
  }
  
  //end collectProperties()

  // Authentication is handled by using a TrustManager and supplying
  // a host name verifier method. (The host name verifier is declared
  // in the main function.)
  //
  // For the purposes of this example, this TrustManager implementation
  // will accept all certificates. This is only appropriate for
  // a development environment. Production code should implement certificate support.
  
  
  private static class TrustAllTrustManager
      implements javax.net.ssl.TrustManager,
                 javax.net.ssl.X509TrustManager {

    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
      return null;
    }

//    public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
//      return true;
//    }
//
//    public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
//      return true;
//    }

    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
        throws java.security.cert.CertificateException {
      return;
    }

    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
        throws java.security.cert.CertificateException {
      return;
    }
  }

  public VirtualMachine[] getVMs(String serverName, String userName, String password) throws Exception {

    // arglist variables
//	String serverName = "esxi.cc.lab";
//    String userName = "root";
//    String password = "C1scoIPCC123";
    String url = "https://"+serverName+"/sdk/vimService";

    // Variables of the following types  for access to the API methods
    // and to the vSphere inventory.
    // -- ManagedObjectReference for the ServiceInstance on the Server
    // -- VimService for access to the vSphere Web service
    // -- VimPortType for access to methods
    // -- ServiceContent for access to managed object services
    ManagedObjectReference SVC_INST_REF = new ManagedObjectReference();
    VimService vimService;
    VimPortType vimPort;
    ServiceContent serviceContent;

    // Declare a host name verifier that will automatically enable
    // the connection. The host name verifier is invoked during
    // the SSL handshake.
    HostnameVerifier hv = new HostnameVerifier() {
      public boolean verify(String urlHostName, SSLSession session) {
        return true;
      }
    };

    // Create the trust manager.
    javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
    javax.net.ssl.TrustManager tm = new TrustAllTrustManager();
    trustAllCerts[0] = tm;

    // Create the SSL context
    javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");

    // Create the session context
    javax.net.ssl.SSLSessionContext sslsc = sc.getServerSessionContext();

    // Initialize the contexts; the session context takes the trust manager.
    sslsc.setSessionTimeout(0);
    sc.init(null, trustAllCerts, null);

    // Use the default socket factory to create the socket for the secure connection
    javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    // Set the default host name verifier to enable the connection.
    HttpsURLConnection.setDefaultHostnameVerifier(hv);

    // Set up the manufactured managed object reference for the ServiceInstance
    SVC_INST_REF.setType("ServiceInstance");
    SVC_INST_REF.setValue("ServiceInstance");

    // Create a VimService object to obtain a VimPort binding provider.
    // The BindingProvider provides access to the protocol fields
    // in request/response messages. Retrieve the request context
    // which will be used for processing message requests.
    vimService = new VimService();
    vimPort = vimService.getVimPort();
    Map<String, Object> ctxt = ((BindingProvider) vimPort).getRequestContext();

    // Store the Server URL in the request context and specify true
    // to maintain the connection between the client and server.
    // The client API will include the Server's HTTP cookie in its
    // requests to maintain the session. If you do not set this to true,
    // the Server will start a new session with each request.
    ctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
    ctxt.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);
    // Retrieve the ServiceContent object and login
    serviceContent = vimPort.retrieveServiceContent(SVC_INST_REF);
    vimPort.login(serviceContent.getSessionManager(),
                  userName,
                  password,
                  null);

    // retrieve data
    
   
    RetrieveResult props = new RetrieveResult();
    props = collectProperties( vimPort, serviceContent );
    int vms_count = props.getObjects().size();
    VirtualMachine[] VMs_array = new VirtualMachine[vms_count];
    // go through the returned list and print out the data
    if (props != null) {
    	int k=0;
    	
    	for (ObjectContent oc : props.getObjects()) {
    	VMs_array[k] = new VirtualMachine();
    	System.out.println(" ======= ");
        String value = null;
        String path = null;
        List<DynamicProperty> dps = oc.getPropSet();
        if (dps != null) {
          for (DynamicProperty dp : dps) {
            path = dp.getName();
            value = (String) dp.getVal();
            System.out.println(path + " = " + value);
            if (path.equals("guest.hostName")) {
            	value = (String)dp.getVal();
            	VMs_array[k].setHostName(value);
            }
            else if (path.equals("name")) {
            	value = (String)dp.getVal();
            	VMs_array[k].setVMName(value);
            }
            else if (path.equals("config.annotation")) {
            	VMs_array[k].setAnnotation(value);
            }
          }
        }
        k++;
    	}
    }
      

    // close the connection
    vimPort.logout(serviceContent.getSessionManager());
    return VMs_array;

  }
}