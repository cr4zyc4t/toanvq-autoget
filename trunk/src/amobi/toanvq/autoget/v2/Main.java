package amobi.toanvq.autoget.v2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Main {
	public static void main(String...args){
		ServerSocket serversk = null;
		
		try {
			serversk = new ServerSocket(65535, 1, InetAddress.getLocalHost());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("IOException");
		}
		
		if (serversk != null) {
			Autoget getter;
			getter = new Autoget((args.length > 0));
			
			getter.start();
		} else {
			System.out.println("Autoget is already running!");
		}
	}
}
