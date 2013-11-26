package vn.com.fpt.fti.qaweb.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
	// error for special character
	private static String sendGet(String url,String method,String urlParameter) throws Exception {
		 
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
	    DataOutputStream writer = new DataOutputStream(con.getOutputStream());
	    BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(writer, "UTF-8"));
		//urlParameter = FTIEncoder.encode(urlParameter);
		//byte[] data=urlParameter.getBytes("UTF-8");
		System.out.println("Url parameter: " + urlParameter);
		wr.write(urlParameter);
		
	    wr.flush();
	    wr.close();
		int responseCode = con.getResponseCode();
		System.out.println("\nSending " + method + "request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
		System.out.println("Url parameter: " + urlParameter);
		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();
 
		System.out.println("url Parameter: " + urlParameter);
		while ((inputLine = in.readLine()) != null) {
			byte [] b = inputLine.getBytes();  // NOT UTF-8
	        String  resultLine = new String(b, "UTF-8");
			response.append(resultLine);
		}
		in.close();
		//print result
		return response.toString();
	}
	
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public static String sendPost(String url,String inputText) throws Exception{
		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("sText", inputText));
		//params.add(new BasicNameValuePair("param-2", "Hello!"));
		httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		//Execute and get the response.
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
		    InputStream instream = entity.getContent();
		    result = convertStreamToString(instream);
		    try {
		        // do something useful
		    } finally {
		        instream.close();
		    }
		}
		return result;
	}
}
