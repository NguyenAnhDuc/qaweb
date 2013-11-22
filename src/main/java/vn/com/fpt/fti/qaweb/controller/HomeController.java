package vn.com.fpt.fti.qaweb.controller;

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
import vn.com.fpt.fti.qaweb.model.AffirmativeResponse;
import vn.com.fpt.fti.qaweb.model.Person;
import vn.hus.nlp.tokenizer.VietTokenizer;
 


@Controller
@RequestMapping("/")
public class HomeController {
	public VietTokenizer vietTokenizer;
	public AbstractSequenceClassifier classifier;
	public static String vietTokenPropertyFile = "D:\\Works\\QAData\\tokenizer\\tokenizer.properties";
	public static String dataPath = "D:\\Works\\QAdata\\data\\";
	public static String  model = "D:\\Works\\QAdata\\FPTCO-normal-5-label.ser.gz";
	
	public HomeController(){
		this.vietTokenizer = new VietTokenizer(vietTokenPropertyFile);
		try{
			this.classifier = CRFClassifier.getClassifier(model);
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		System.out.println("HomeController Init");
	}
	
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
	
	@RequestMapping(value="/testParam/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String testParam(@PathVariable String id){
		String result = "This is result of a test function";
		return id;
	}
	
	@RequestMapping(value="/testToken", method = RequestMethod.POST, produces="text/html; charset=UTF-8")
	@ResponseBody
	public String testToken(@RequestParam("sText") String sInput){
		String result = "This is result of a test function";
		//String sInput = "Chủ tịch công ty cổ phần FPT là ông Trương Gia Bình";
		try{
			
			result = this.vietTokenizer.tokenize(sInput)[0];
		}
		catch (Exception ex){
			return ex.getMessage();
		}
		System.out.println("Success");
		return result;
	}
	
	@RequestMapping(value="/testJSON", method = RequestMethod.GET)
	@ResponseBody
	public Person testJSON(){
		Person person = new Person();
		person.name = "Duc";
		person.age = 23;
		return person;
	}
	
	@RequestMapping(value="/testAffirmative", method = RequestMethod.POST, produces="application/json; charset=UTF-8")
	@ResponseBody
	public AffirmativeResponse testAffirmative(@RequestParam("sQuestion") String sQuestion){
		String result = "This is result of a test function";
		//String sInput = "Chủ tịch công ty cổ phần FPT là ông Trương Gia Bình";
		AffirmativeResponse  affirmativeResponse = new AffirmativeResponse();
		try{
			AffirmativeForm affirm = AFConverter.process(sQuestion, this.dataPath);
			affirmativeResponse.questionWord = affirm.getQuestionWord();
			affirmativeResponse.entityType = affirm.getNamedEntityType();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		System.out.println("Success");
		return affirmativeResponse;
	}
	
	@RequestMapping(value="/testClassify", method = RequestMethod.POST, produces="application/json; charset=UTF-8")
	@ResponseBody
	public String testClassify(@RequestParam("sText") String sText){
		String result = "";
		//String sInput = "Chủ tịch công ty cổ phần FPT là ông Trương Gia Bình";
		result = this.classifier.classifyWithInlineXML(sText);
		System.out.println("Success");
		return result;
	}
	
	
	
	
}