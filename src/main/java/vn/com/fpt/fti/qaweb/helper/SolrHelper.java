package vn.com.fpt.fti.qaweb.helper;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class SolrHelper {

	
	public static ArrayList<String> QueryResult(String solrUrl,String inputText,int numResult) throws  Exception{
		JSONObject job;
		System.out.println(solrUrl);
		job = JSONReader.readJsonFromUrl(solrUrl);
		JSONObject jresponse = job.getJSONObject("response");
		JSONArray docs = jresponse.getJSONArray("docs");
		System.out.println("SOLR RESPONSES: " + docs.length());
		double maxScore = jresponse.getDouble("maxScore");
		//out.println("MAX SCORE: " + maxScore + "<br>");
		
		ArrayList<String> anstexts = new ArrayList<String>();
		//get Answers
		int found = 0;
		for (int i = 0; i < Math.min(docs.length(),numResult); i++) {
			int stt = found / 10 + 1;
			//resultText += "<p class=\"selection page-" + stt + "\">";
			JSONObject qa = docs.getJSONObject(i);
			JSONArray ans = qa.getJSONArray("answer");
			String anstext = "";
			//resultText += "<br>" + (found + 1) + ". "+ qa.getDouble("score");
			for (int j = 0; j < ans.length(); j++) {
				anstext +=  ans.getString(j);
			}
			anstexts.add(anstext);
			found++;
		}
		
		return anstexts;
		
	}
}
