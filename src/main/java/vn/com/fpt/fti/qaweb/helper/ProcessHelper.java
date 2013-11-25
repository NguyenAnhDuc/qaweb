package vn.com.fpt.fti.qaweb.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProcessHelper {
	public String marktext(String input, String type){
	        String markedtext=input;
	        markedtext = markedtext.replaceAll("<"+type+">", "<"+type+"M>");
	        markedtext = markedtext.replaceAll("<ORG>","<span class='ORG'>");
	        markedtext = markedtext.replaceAll("</ORG>","</span>");
	        markedtext = markedtext.replaceAll("<HUM>","<span class='HUM'>");
	        markedtext = markedtext.replaceAll("</HUM>","</span>");
	        markedtext = markedtext.replaceAll("<LOC>","<span class='LOC'>");
	        markedtext = markedtext.replaceAll("</LOC>","</span>");
	        markedtext = markedtext.replaceAll("<DTIME>","<span class='DTIME'>");
	        markedtext = markedtext.replaceAll("</DTIME>","</span>");
	        markedtext = markedtext.replaceAll("<NUM>","<span class='NUM'>");
	        markedtext = markedtext.replaceAll("</NUM>","</span>");
	        
	        markedtext = markedtext.replaceAll("<ORGM>","<span class='ORG' id='M'>");
	        markedtext = markedtext.replaceAll("</ORG>","</span>");
	        markedtext = markedtext.replaceAll("<HUMM>","<span class='HUM' id='M'>");
	        markedtext = markedtext.replaceAll("</HUM>","</span>");
	        markedtext = markedtext.replaceAll("<LOCM>","<span class='LOC' id='M'>");
	        markedtext = markedtext.replaceAll("</LOC>","</span>");
	        markedtext = markedtext.replaceAll("<DTIMEM>","<span class='DTIME' id='M'>");
	        markedtext = markedtext.replaceAll("</DTIME>","</span>");
	        markedtext = markedtext.replaceAll("<NUMM>","<span class='NUM' id='M'>");
	        markedtext = markedtext.replaceAll("</NUM>","</span>");
	        return markedtext;
	    }
	
	public static String sendGet(String url,String method,String urlParameter) throws Exception {
		 
		//String url = "http://localhost:8080/QAWeb/testJSON";
 
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod(method);
 
		//add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + "UTF-8");
		
		con.setDoInput(true);
	    con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream (con.getOutputStream ());
	    wr.writeBytes(urlParameter);
	    wr.flush();
	    wr.close();
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		return response.toString();
	}
}
