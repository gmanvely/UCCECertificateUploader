package com.cisco.tac.ucce.utils;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class UploadCert {

	String Host;
	int Port;
	String Keystore;
	char[] Passphrase;
	String storeType = "JKS";
	Date Expired;
	String Result;
	String Details = "";

	private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

	UploadCert() {

//		Host = host;
//		Port = port;
//		Keystore = keystore;
//		Passphrase = passphrase.toCharArray();
	}
	
	String getCertExpiration(){
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
		String formatedDate=sdf.format(this.Expired);
		
		return formatedDate;
	}
	
	int getCertDateDiff() {
		Date current = new Date();
		long difference_In_Days= (((this.Expired.getTime() - current.getTime())/(86400))/1000);
		return (int)difference_In_Days;
	}
	
	String getUploadDetails() {
		
		return Details;
	}
	
	String doUpload(String host, int port, String keystore, String passphrase, String storetype) throws UnknownHostException, KeyStoreException, NoSuchAlgorithmException, CertificateException,
			IOException, KeyManagementException, ConnectException {
		
		Host = host;
		Port = port;
		Keystore = keystore;
		Passphrase = passphrase.toCharArray();
		storeType = storetype;

		File file = new File(Keystore);
		if (file.isFile() == false) {
			System.out.println("Error: keystore file not found");
			return "Error: keystore file not found";
		}

		System.out.println("Info: host : " + Host);
		System.out.println("Info: keystore : " + Keystore);
		InputStream in = new FileInputStream(Keystore);
//		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		KeyStore ks = KeyStore.getInstance(storeType);
		try {
		ks.load(in, Passphrase);
		in.close();
		} catch (IOException e) {
			return ("Error: "+e.getMessage());
		}
		

		SSLContext context = SSLContext.getInstance("TLS");
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(ks);
		X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
		SaveTrustManager tm = new SaveTrustManager(defaultTrustManager);
		context.init(null, new TrustManager[] { tm }, null);
		SSLSocketFactory factory = context.getSocketFactory();

		System.out.println("Info: Opening connection to " + Host + ":" + Port + " ...");

		try {
			SSLSocket socket = (SSLSocket) factory.createSocket(Host, Port);

			System.out.println("Info: Initiating SSL handshake...");
			socket.startHandshake();
			socket.close();
			System.out.println("Info: Certificate is already trusted");
			this.Result = ("Done: Certificate is already trusted");
			this.Details=("Done: Certificate is already trusted"+"\r\n");
		} catch (SSLException | SocketException| UnknownHostException e) {
			e.printStackTrace(System.out);

			if ((e.toString()).contains("java.net.UnknownHostException")) {
				this.Result =  "Error. Unknown Host";
				return Result;
			} 
			else if ((e.toString()).contains("Unsupported or unrecognized SSL message")) {
				this.Result =  ("Error: "+e.getMessage());
				return Result;
			}
			else if ((e.toString()).contains("Connection refused: connect")) {
				this.Result =  ("Error: "+e.getMessage());
				return Result;
			}
			else if ((e.toString()).contains("Connection timed out:")) {
				this.Result =  ("Error: "+e.getMessage());
				return Result;
			}
			else if ((e.toString()).contains("Network is unreachable:")) {
				this.Result = ("Error: "+e.getMessage());
				return Result;
			}

		}

		X509Certificate[] chain = tm.chain;
		if (chain == null) {
			System.out.println("Error: Could not obtain server certificate chain");
			this.Result = "Error: Could not obtain server certificate chain";
			return "Error: Could not obtain server certificate chain";
		}

		System.out.println();
		System.out.println("Info: Server sent " + chain.length + " certificate(s):");
		this.Details=(this.Details+"Server sent " + chain.length + " certificate(s) on port "+Port+"\r\n");
		System.out.println();

		MessageDigest sha1 = MessageDigest.getInstance("SHA1");
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		for (int i = 0; i < chain.length; i++) {
			X509Certificate cert = chain[i];
			sha1.update(cert.getEncoded());
			md5.update(cert.getEncoded());
			this.Details=(this.Details+"====================================================================\r\n");
			this.Details=(this.Details+" Subject  " + cert.getSubjectDN()+"\r\n");
			this.Details=(this.Details+" Issuer   " + cert.getIssuerDN()+"\r\n");
			this.Details=(this.Details+" sha1   " + toHexString(sha1.digest())+"\r\n");
			this.Details=(this.Details+" md5   " + toHexString(md5.digest())+"\r\n");
			this.Details=(this.Details+" Serial number " + cert.getSerialNumber()+"\r\n");
			this.Details=(this.Details+" Valid from " + cert.getNotBefore()+"\r\n");
			this.Details=(this.Details+" Valid to " + cert.getNotAfter()+"\r\n");
			if (i==0) {
				this.Expired=cert.getNotAfter();
			}
		}
		System.out.println(this.Details);
		if (Result == ("Done: Certificate is already trusted")) {
			return Result;
		}
		if (chain.length == 1) {
			X509Certificate cert = chain[0];
			String alias = "Util-" + Host + "-" + String.valueOf(Port);
			ks.setCertificateEntry(alias, cert);

			OutputStream out = new FileOutputStream(Keystore);
			ks.store(out, Passphrase);
			out.close();

			this.Details=(this.Details+"Added certificate to keystore '" + Keystore + "' using alias '" + alias + "'"+"\r\n");
			return ("Done: Added certificate to keystore");

		}

		else {

			X509Certificate cert = chain[chain.length - 1];
			String alias = ("Util-RootCA-for-" + Host);
			ks.setCertificateEntry(alias, cert);

			OutputStream out = new FileOutputStream(Keystore);
			ks.store(out, Passphrase);
			out.close();

			this.Details=(this.Details+"Added certificate to keystore '" + Keystore + "' using alias '" + alias + "'"+"\r\n");

			int x = (chain.length - 1);
			int y = 1;
			while (y <= (chain.length - 2)) {

				X509Certificate certs = chain[y];
				String aliases = ("Util-IntermidiateCA-for-" + Host + String.valueOf(y));
				ks.setCertificateEntry(aliases, certs);

				OutputStream outs = new FileOutputStream(Keystore);
				ks.store(outs, Passphrase);
				outs.close();

				this.Details=(this.Details+"Added certificate to keystore '" + Keystore + "' using alias '" + aliases + "'"+"\r\n");
				
				x = x - 1;
				y++;
			}

			X509Certificate hostcert = chain[0];
			String hostalias = "Util-" + Host + "-" + String.valueOf(Port);
			ks.setCertificateEntry(hostalias, hostcert);

			OutputStream hostouts = new FileOutputStream(Keystore);
			ks.store(hostouts, Passphrase);
			hostouts.close();

			this.Details=(this.Details+"Added certificate to keystore '" + Keystore + "' using alias '" + hostalias + "'"+"\r\n");

			return "Done: Added certificate to keystore";

		}

	}

	private static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 3);
		for (int b : bytes) {
			b &= 0xff;
			sb.append(HEXDIGITS[b >> 4]);
			sb.append(HEXDIGITS[b & 15]);
			sb.append(' ');
		}
		return sb.toString();
	}

	private static class SaveTrustManager implements X509TrustManager {

		private final X509TrustManager tm;
		private X509Certificate[] chain;

		SaveTrustManager(X509TrustManager tm) {
			this.tm = tm;
		}

		public X509Certificate[] getAcceptedIssuers() {
			// This change has been done due to the following resolution advised for Java
			// 1.7+
			// http://infposs.blogspot.kr/2013/06/installcert-and-java-7.html
			return new X509Certificate[0];
			// throw new UnsupportedOperationException();
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			throw new UnsupportedOperationException();
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			this.chain = chain;
			tm.checkServerTrusted(chain, authType);
		}
	}

}
