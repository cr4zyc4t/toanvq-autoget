package amobi.toanvq.autoget.v2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Autoget {
	private String get_news_url;
	private String reset_link_url;
	private String server_report_url;
	private int delay_time;
	private int wait_connection_time;
	private int connection_error_count = 0;
	private int max_multiple_wait = 50;
	
	private String runtime_log_path;
	private String exception_log_path;
	private String config_path;
	
	private boolean debug = false;
	
	public Autoget(boolean debug) {
		super();
		this.debug = debug;
		
//		LOAD DEFAULT CONFIG
		this.get_news_url 		= Config.GET_NEWS_URL;
		this.reset_link_url 	= Config.RESET_LINK_URL;
		this.server_report_url 	= Config.SERVER_REPORT_URL;
		
		this.delay_time 			= Config.DELAY_TIME;
		this.wait_connection_time 	= Config.WAIT_CONNECTION_TIME;
	}

	public void start() {
		config_path = getClass().getResource("/").getPath() + "config.properties";
		runtime_log_path = getClass().getResource("/").getPath() + "runtime_log_" + Tool.getCurrentDate("yyyyMMdd") + ".txt";
		exception_log_path = getClass().getResource("/").getPath() + "last_exception.txt";
		
		//READ CONFIG FILE
		Properties prop = new Properties();
		InputStream input = null;
	 
		try {
			// load a properties file
			prop.load( new FileInputStream(config_path) );
			
			get_news_url = prop.getProperty("get_news_url", get_news_url);
			reset_link_url = prop.getProperty("reset_link_url", reset_link_url);
			server_report_url = prop.getProperty("server_report_url", server_report_url);
			delay_time = Integer.parseInt( prop.getProperty("delay_time", delay_time + "" ) );
			wait_connection_time = Integer.parseInt( prop.getProperty("wait_connection_time", wait_connection_time + "") );
		} catch (IOException ex) {
			System.out.println("KHONG TIM THAY FILE config.properties, CHAY VOI THONG SO MAC DINH");
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		runLog("Start - " + Tool.getCurrentDate("dd/MM/yyyy"));
		runLog("Get news url: " + get_news_url);
		runLog("Reset link url: " + reset_link_url);
		runLog("Server report url: " + server_report_url);
		runLog("Delay repeat time: " + delay_time + "s");
		runLog("Wait connection time: " + wait_connection_time + "s");
		runLog("-------------------------------------");
		
		getNews();
	}

	private void getNews() {
		String reply = makeRequest(get_news_url);
		if ((reply != null) && reply.equals("FINISH")) {
			resetLink();
		} else {
			runLog(reply);
			getNews();
		}
	}

	private void resetLink() {
		String reply = makeRequest(reset_link_url);
		if ((reply != null) && reply.equals("FINISH")) {
			runLog("FINISH " + Tool.getCurrentDate("yyyy-MM-dd H:ms:ss") + " , DELAY " + delay_time + "s \r\n \r\n");
			
			try {
				TimeUnit.SECONDS.sleep(delay_time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			runLog("Start " + Tool.getCurrentDate("yyyy-MM-dd H:mm:ss"));
			getNews();
		} else {
			resetLink();
		}
	}
	
	
	public int getWaitConnectionTime() {
		return wait_connection_time*connection_error_count;
	}
	
	public String makeRequest(String url) {
		String response = null;
		try {
			response = Tool.sendGet(url);
		} catch (Exception e) {
			if (connection_error_count < max_multiple_wait ) {
				connection_error_count++;
			}
			errorLog(e, "CONNECTION ERROR AT " + Tool.getCurrentDate("yyyy-MM-dd H:mm:ss") + ", RETRY IN " + getWaitConnectionTime() + "s");
		}
		
		if (response == null) {
			try {
				TimeUnit.SECONDS.sleep( getWaitConnectionTime() );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			makeRequest(url);
		}
		
		connection_error_count = 0;
		return response;
	}
	
	private void errorLog(Exception e, String info) {
		if (this.debug) {
			System.out.println(info);
			e.printStackTrace();
		} else {
			runLog(info);
			try {
				FileWriter fstream = new FileWriter(exception_log_path);
				BufferedWriter out = new BufferedWriter(fstream);
				PrintWriter pWriter = new PrintWriter(out, true);
				e.printStackTrace(pWriter);
			} catch (Exception ie) {
				ie.printStackTrace();
			}
		}
		
	}
	
	private void runLog(String action) {
		if (this.debug) {
			System.out.println(action);
		} else {
			BufferedWriter writer = null;
			try {
	            File logFile = new File( runtime_log_path );
	            writer = new BufferedWriter(new FileWriter(logFile,true));
	            writer.write(action + "\r\n");
			} catch (Exception ie) {
				ie.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
}
