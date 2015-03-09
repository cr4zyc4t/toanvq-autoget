package amobi.toanvq.autoget.v2;

public class Main {
	public static void main(String...args){
		Autoget getter;
		getter = new Autoget((args.length > 0));
		
		getter.start();
	}
}
