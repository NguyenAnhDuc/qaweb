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
import vn.com.fpt.fti.qaweb.model.SolrResponse;



@Controller
@RequestMapping("/")
public class HomeController {
	@Value("${AffirmativeURL}") String AffirmativeURL;
	@Value("${TokenURL}") String TokenURL;
	@Value("${ClassifyURL}") String ClassifyURL;
	@Value("${SolrURL}") String SolrURL;
	
	
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
		String urlParameter = "sText="+"\"Chủ tịch fpt là ai?\"";
		String result = ProcessHelper.sendGet(AffirmativeURL,"POST",urlParameter);
		//String result = "{\"name\":\"mit\",\"age\":21}";
		//JSONObject json = new JSONObject(result);
		//return json.getString("name");
		JSONObject json = new JSONObject(result);
		return json.getString("questionWord");
	}
	
	@RequestMapping(value="/testJSON", method = RequestMethod.GET, produces="application/json; charset=UTF-8")
	@ResponseBody
	public Person testJSON(){
		Person aPerson = new Person();
		aPerson.name = "mit";
		aPerson.age = 21;
		return aPerson;
	}
	
	@RequestMapping(value="/testQA", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public SolrResponse testQA(@RequestParam("sText") String inputText){
		SolrResponse json = new SolrResponse();
		String questionWord = "", entityType = "";
		String urlParameter = "sText=" + inputText ;
		String result = "";
		System.out.println(urlParameter);
		String solrQuery="";
		try{
			// Affirmative
			String affirmativeResult = ProcessHelper.sendGet(AffirmativeURL, "POST",urlParameter);
			System.out.println(affirmativeResult);
			JSONObject affirmativeJson = new JSONObject(affirmativeResult);
			if ((affirmativeJson.getString("status")).equals("success")){
				questionWord = affirmativeJson.getString("questionWord");
				entityType = affirmativeJson.getString("entityType");
			}
			inputText = inputText.trim().replaceAll("\\?", "").replaceAll("\\s+", " ").replaceAll(" ", ",");
	        String query = "(question:("+inputText+")^1) OR (answer:("+inputText+")^1)";
	        System.out.println(inputText);
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
			answers = SolrHelper.QueryResult(solrQuery, inputText, 10);
			json.status = "success";
			json.answers = answers;
		}
		catch (Exception ex){
			json.status = "fail";
			return json;
		}
		
		// Classify
		try {
			for (int i=0;i<json.answers.size();i++){
				String textAnswer = json.answers.get(i);
				urlParameter = "sText=" + textAnswer;
				System.out.println(textAnswer);
				String classifyResult = ProcessHelper.sendGet(ClassifyURL, "POST", urlParameter);
				JSONObject classifyJSON = new JSONObject(classifyResult);
				System.out.println(classifyJSON.getString("status"));
				if (classifyJSON.getString("status").equals("success")){
					json.answers.set(i,classifyJSON.getString("result"));
					System.out.println(classifyJSON.getString("result"));
				}
					
			}
		}
		catch (Exception ex){
			json.status = "fail";
			return json;
		}
		
		return json;
	}
	
	
}