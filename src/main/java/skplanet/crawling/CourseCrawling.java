package skplanet.crawling;

import java.util.Iterator;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CourseCrawling {

	private int Course_Id;
	private String Course_Title;
	private String Course_Des;
	private String Course_Thumbnail;
	private String Course_Url;
	private int Course_Category_Cnt;
	private int Course_level;
	private int Category_Cd;
	private int User_Type_Cd;
	private Vector<Long> lCategory_Cd  = new Vector<Long>();
	
	private DataBaseManager manager_db;
	
	private HttpClientManager manager;// = HttpClientManager.getInstance();
	private String URL_course;
	private String json_result;
	
	public CourseCrawling(String URL_course){
		this.URL_course = URL_course;
		manager_db = DataBaseManager.getInstance();
	}
	
	public void courseRun(){

		json_result = httpClientRequest_GET(URL_course);  		// Http GET
		jsonParsing(json_result);  		// JSON Parsing
	}
	
	public String httpClientRequest_GET(String url){
		manager = HttpClientManager.getInstance();
		String json_result = manager.get_raw(url);
		return json_result;
	}
	
	public void jsonParsing(String json_result){
		try {
			 
            JSONParser jsonParser = new JSONParser();
            //JSON데이터를 넣어 JSON Object 로 만들어 준다.
            JSONObject jsonObject = (JSONObject) jsonParser.parse(json_result);
            //elements의 배열을 추출
            JSONArray elementsInfoArray = (JSONArray) jsonObject.get("elements");
 
            System.out.println("* elements *");
 
            for(int i=0; i<elementsInfoArray.size(); i++){
            	int nCategories=0;
                System.out.println("=elements"+i+" ===========================================");
                //배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
                JSONObject elementObject = (JSONObject) elementsInfoArray.get(i);
                //JSON name으로 추출
                
                if( elementObject.get("language").toString().equals("en") ){
                	Course course = new Course();// Course Info

                	this.Course_Des = (String) elementObject.get("shortDescription");
	                this.Course_Id = Integer.parseInt(elementObject.get("id").toString());
	                this.Course_Thumbnail = (String) elementObject.get("smallIcon");
	                this.Course_Title = (String) elementObject.get("name");
	                this.Course_Url = "https://www.coursera.org/course/" + (String)elementObject.get("shortName");
	                
	                if(elementObject.get("targetAudience") !=null){
	                	this.Course_level = Integer.parseInt(elementObject.get("targetAudience").toString());
	                }
	                else{
	                	this.Course_level = -1;
	                }
	                
	                System.out.println( Course_Id + ", "+Course_Title+", "+ Course_Des+", " +Course_Thumbnail+ ", "+Course_Url+ ", "+ Course_level);
	                
	                JSONObject elementLink =  (JSONObject) elementObject.get("links");
	                if (elementLink.size()==0){
	                	System.out.println("size is 0");
	                	nCategories = 0;
	                }
	                else{
		                JSONArray elementCategories =  (JSONArray) elementLink.get("categories");
		                Iterator<Long> iterator =  (elementCategories).iterator();
		                Vector<Long> LongVectorCategory_Cd = new Vector<Long>();
		                nCategories=0;
		                
		        		while (iterator.hasNext()) {
		        			nCategories++;
		        			Long LongCategory_Cd = iterator.next(); //LongCategory_Cd를 Course에 저장하기위해서
		        			LongVectorCategory_Cd.add(LongCategory_Cd);
		        			
		        			this.Category_Cd = LongCategory_Cd.intValue();	// DB의 Course_Category에 저장하기위해 Long -> int로변
		        			Course_Category cc = new Course_Category(this.Course_Id, this.Category_Cd);
		        			//  DB Course_Category에 들어가는 것을 막아!
		        			//	manager_db.insert_course_category(cc);	
		        		}
		        		
		        		this.lCategory_Cd = LongVectorCategory_Cd;
	                }
	                this.Course_Category_Cnt = nCategories;

	                // Course Info insert!
	                course.settingCourseInfo(this.Course_Id, this.Course_Title, this.Course_Des, this.Course_Thumbnail, this.Course_Url, this.Course_Category_Cnt, this.Course_level, this.lCategory_Cd);
	                course.checkCourse();  //print information
	                manager_db.insert_course(course);// insert into Course Table // insert to DB
                }
              //  Course course = new Course(this.Course_Id, this.Course_Title, this.Course_Des, this.Course_Thumbnail, this.Course_Url, this.Course_Category_Cnt, this.Course_level, this.lCategory_Cd);
            }
 
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
   
	}
	
	
	
}
