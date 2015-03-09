package amobi.toanvq.autoget.v2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tool {
	public static String sendGet(String link) throws Exception {
		HttpURLConnection connection;
		URL url = new URL(link);
		
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept-Charset", "UTF-8");
		connection.setRequestProperty("User-Agent", Config.USER_AGENT);
		
		BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream(), "UTF-8") );
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		
		in.close();
		
		return response.toString();
	}
	
	public static String getCurrentDate(String format) {
		if (format == null) {
			format = "yyyyMMdd"; 
		}
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		
		return dateFormat.format(date);
	}
	
	public static String sendPost(String link, String postData) throws Exception {
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length",  String.valueOf(postData.length()));
        connection.setConnectTimeout(10000);
         
        // Write data
        OutputStream os = connection.getOutputStream();
        os.write(postData.getBytes());
         
        // Read response
        StringBuilder responseSB = new StringBuilder();
        InputStream stream = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
          
        String line;
        while ( (line = br.readLine()) != null)
            responseSB.append(line);
                 
        // Close streams
        br.close();
        os.close();
        return responseSB.toString();
    }
	
	public static String readFile(String fileName) throws Exception {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
}
