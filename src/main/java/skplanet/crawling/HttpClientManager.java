package skplanet.crawling;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by urstory on 2015. 3. 16..
 */
//HttpClientManager manager = HttpClientManager.getInstance();
////where=nexearch&query=apple&sm=top_hty&fbm=1&ie=utf8
//final String result = manager.post("http://10.203.226.72:8080/androidtest/login", 
//									new String[]{"id", "passwd"},
//									new String[]{"urstory", "1234"}, 
//									"utf-8");


//HttpClientManager manager = HttpClientManager.getInstance();
//where=nexearch&query=apple&sm=top_hty&fbm=1&ie=utf8
//final String result = manager.get("http://10.203.226.72:8080/androidtest/getpost", 
//								   new String[]{"value"},
//								   new String[]{"haha"}, 
//								   "utf-8");


public class HttpClientManager {
    private static HttpClientManager instance = null;
    private HttpClient http = null;

    private HttpClientManager() {
        http = new DefaultHttpClient();
    }

    public synchronized static HttpClientManager getInstance(){
        if(instance == null){
            instance = new HttpClientManager();
        }
        return instance;
    }

    
    public String get_raw(String url){
        String result = "";
        try {
                        
            System.out.println("httpClientManager url:"+ url);
            HttpGet httpGet = new HttpGet(url);

            HttpResponse responsePost = http.execute(httpGet);
            HttpEntity resEntity = responsePost.getEntity();

            result = EntityUtils.toString(resEntity);
        }catch(Exception e){
            StackTraceElement[] ste = e.getStackTrace();
            for(int i = 0; i < ste.length; i++) {
                System.out.println("httpClientManager:"+ ste[i].toString());
            }
        }
        return result;
    }
    
    public String get(String url, String[] paramNames, String[] paramValues, String encoding){
        String result = "";
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            for(int i = 0; i < paramNames.length; i++){
                nameValuePairs.add(new BasicNameValuePair(paramNames[i], paramValues[i]));
            }

            HttpParams params = http.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);

            url = url + "?" + URLEncodedUtils.format(nameValuePairs, encoding);
            
            System.out.println("httpClientManager url:"+ url);
            HttpGet httpGet = new HttpGet(url);

            HttpResponse responsePost = http.execute(httpGet);
            HttpEntity resEntity = responsePost.getEntity();

            result = EntityUtils.toString(resEntity);
        }catch(Exception e){
            StackTraceElement[] ste = e.getStackTrace();
            for(int i = 0; i < ste.length; i++) {
                System.out.println("httpClientManager:"+ ste[i].toString());
            }
        }
        return result;
    }

    public String post(String url, String[] paramNames, String[] paramValues, String encoding){
        String result = "";
        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            for(int i = 0; i < paramNames.length; i++){
                nameValuePairs.add(new BasicNameValuePair(paramNames[i], paramValues[i]));
            }
            HttpParams params = http.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);

            HttpPost httpPost = new HttpPost(url);
            UrlEncodedFormEntity entityRequest =
                    new UrlEncodedFormEntity(nameValuePairs, encoding);

            httpPost.setEntity(entityRequest);

            HttpResponse responsePost = http.execute(httpPost);
            HttpEntity resEntity = responsePost.getEntity();

            result = EntityUtils.toString(resEntity);
        }catch(Exception e){

            System.out.println("httpClientManager "+ e.toString());
        }
        return result;
    }

}
