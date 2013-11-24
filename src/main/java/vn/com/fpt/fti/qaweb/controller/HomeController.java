package vn.com.fpt.fti.qaweb.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.codehaus.jackson.JsonToken;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import fpt.qa.qiafc.AFConverter;
import fpt.qa.qiafc.AffirmativeForm;
import vn.hus.nlp.tokenizer.VietTokenizer;
 


@Controller
@RequestMapping("/")
public class HomeController {
	
	
	
	@RequestMapping(value="/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("message", "Maven Web Project + Spring 3 MVC - welcome()");
		return "index";
 	}

	@RequestMapping(value="/test", method = RequestMethod.GET)
	@ResponseBody
	public String testFunction(){
		String result = "This is result of a test function";
		return result;
	}
	
	@RequestMapping(value="/testSendPost", method = RequestMethod.GET)
	@ResponseBody
	public String testSendPost() throws Exception{
		//String result = sendGet();
		String result = "{\"name\":\"mit\",\"age\":21}";
		JSONObject json = new JSONObject(result);
		return json.getString("name");
	}
	
	@RequestMapping(value="/testJSON", method = RequestMethod.GET, produces="application/json; charset=UTF-8")
	@ResponseBody
	public Person testJSON(){
		Person aPerson = new Person();
		aPerson.name = "mit";
		aPerson.age = 21;
		return aPerson;
	}
	
	private static String sendGet() throws Exception {
		 
		String url = "http://localhost:8080/QAWeb/testJSON";
 
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
 
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