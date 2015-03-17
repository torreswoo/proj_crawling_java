package skplanet.crawling;

//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.methods.PostMethod;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class App {
	public static void main(String args[]) {
	
		HttpClientManager manager = HttpClientManager.getInstance();
		
		/* MOOC API GET URL request
		 * course : includes=categories
		 * course : fields=name,shortDescription,shortName,language,targetAudience,smallIcon
		 */
		String url_courseInfo = "https://api.coursera.org/api/catalog.v1/courses?includes=categories&fields=name,shortDescription,shortName,language,targetAudience,smallIcon";
		CourseCrawling course = new CourseCrawling(url_courseInfo);// course Info crawling
		course.courseRun();  // 초기 정보수집
		
		//2. K means 알고리즘!
		Kmeans kmeans = new Kmeans();
		kmeans.start();
				

	}
}



