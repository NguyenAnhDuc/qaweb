package vn.com.fpt.fti.qaweb.helper;


import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.json.*;

/**
 *
 * @author Hai
 */
public class JSONReader {

    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws Exception {
        
        URL solrurl = new URL(url);
        
        InputStream is = solrurl.openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        }catch(Exception ex){
            System.out.println("JSON ERROR: "+ex.getMessage());
            throw ex;
        } finally {
            is.close();
        }
    }
}
