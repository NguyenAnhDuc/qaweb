package vn.com.fpt.fti.qaweb.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.codehaus.jackson.JsonToken;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
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

import vn.com.fpt.fti.qaweb.helper.JSONReader;
import vn.com.fpt.fti.qaweb.helper.ProcessHelper;
import vn.com.fpt.fti.qaweb.helper.SolrHelper;
import vn.com.fpt.fti.qaweb.helper.FTIEncoder;
import vn.com.fpt.fti.qaweb.model.SolrResponse;



@Controller
@RequestMapping("/")
public class HomeController {
	@Value("${AffirmativeURL}") String AffirmativeURL;
	@Value("${TokenURL}") String TokenURL;
	@Value("${ClassifyURL}") String ClassifyURL;
	@Value("${SolrURL}") String SolrURL;
	@Value("${NumResult}") int NumResult;
	
	@RequestMapping(value="/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("message", "Maven Web Project + Spring 3 MVC - welcome()");
		return "index";
 	}

	@RequestMapping(value="/qasearch", method = RequestMethod.GET)
	public String qasearch(ModelMap model) {
		model.addAttribute("message", "Maven Web Project + Spring 3 MVC - welcome()");
		return "qasearch";
 	}
	
	@RequestMapping(value="/test", method = RequestMethod.GET,produces="text/html; charset=UTF-8")
	@ResponseBody
	public String testFunction(){
		String result = "Chủ tịch fpt là ông Trương Gia Bình";
		result = FTIEncoder.encode(result);
		return result;
	}
	
	
	
	@RequestMapping(value="/testSendPost", method = RequestMethod.GET,produces="text/html; charset=UTF-8")
	@ResponseBody
	public String testSendPost() throws Exception{
		String sText = "Chủ tịch fpt là ông Trương Gia Bình với doanh thu hơn 50%";
		//String result = ProcessHelper.sendGet(ClassifyURL,"POST", urlParameter);
		String result = ProcessHelper.sendPost(ClassifyURL,sText);
		
		//JSONObject json = new JSONObject(result);
		//return json.getString("result");
		return result;
	}
	
	
	@RequestMapping(value="/testJSON", method = RequestMethod.POST, produces="application/json; charset=UTF-8")
	@ResponseBody
	public Person testJSON(@RequestParam("sText") String sInput){
		Person aPerson = new Person();
		System.out.println("json string: "+sInput);
		aPerson.name = sInput;
		aPerson.age = 21;
		return aPerson;
	}
	
	
	
	@RequestMapping(value="/testQA", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public SolrResponse testQA(@RequestParam("sText") String inputText){
		System.out.println("inputText"+ inputText);
		SolrResponse json = new SolrResponse();
		String questionWord = "", entityType = "";
		String result = "";
		String solrQuery="";
		try{
			// Affirmative
			String affirmativeResult = ProcessHelper.sendPost(AffirmativeURL, inputText);
			//System.out.println(affirmativeResult);
			JSONObject affirmativeJson = new JSONObject(affirmativeResult);
			if ((affirmativeJson.getString("status")).equals("success")){
				questionWord = affirmativeJson.getString("questionWord");
				entityType = affirmativeJson.getString("entityType");
			}
			inputText = inputText.trim().replaceAll("\\?", "").replaceAll("\\s+", " ").replaceAll(" ", ",");
	        String query = "(question:("+inputText+")^1) OR (answer:("+inputText+")^1)";
			if (entityType == "") entityType = "undefine";
			
			// Solr Url
			solrQuery = SolrURL + "select?q="
					+ URLEncoder.encode(query, "UTF-8")
					+ "&rows=10000&wt=json&indent=true&fl=*,score";
		}
		catch (Exception ex){
			json.status = "fail";
			return json;
		}
		
		// Query Solr 
		ArrayList<String> answers = new ArrayList<String>();
		try {
			answers = SolrHelper.QueryResult(solrQuery, inputText, NumResult);
			json.status = "success";
			json.answers = answers;
		}
		catch (Exception ex){
			json.status = "fail";
			return json;
		}
		
		System.out.println("Classify:");
		String textAnswer = "";
		// Classify
		try {
			for (int i=0;i<json.answers.size();i++){
				textAnswer = json.answers.get(i);
				String classifyResult = ProcessHelper.sendPost(ClassifyURL, textAnswer);
				JSONObject classifyJSON = new JSONObject(classifyResult);
			
				if (classifyJSON.getString("status").equals("success")){
					json.answers.set(i,classifyJSON.getString("result"));
					System.out.println(classifyJSON.getString("result"));
				}
					
			}
		}
		catch (Exception ex){
			json.status = "fail";
			System.out.println("fail here: " + textAnswer);
			return json;
		}
		
		return json;
	}
	
	
}