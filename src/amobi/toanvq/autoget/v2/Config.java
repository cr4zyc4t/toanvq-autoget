package amobi.toanvq.autoget.v2;

public class Config {
//	DEFAULT CONFIG
	public static final String BASE_URL = "http://content.amobi.vn/";
	
	public static final String GET_NEWS_URL = BASE_URL + "qplayvn/getnewsapi/getnews";
	public static final String RESET_LINK_URL = BASE_URL + "qplayvn/getnewsapi/resetlink";
	public static final String SERVER_REPORT_URL = BASE_URL + "service/autoget-log";
	public static final int DELAY_TIME = 3600; //seconds
	public static final int WAIT_CONNECTION_TIME = 60; //seconds
	
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.99 Safari/537.36";
}
