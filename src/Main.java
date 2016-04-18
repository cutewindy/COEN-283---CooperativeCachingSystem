import java.util.concurrent.TimeUnit;

public class Main {
	
	public static void main(String[] args) {
		
	    long startTime = System.currentTimeMillis();

		GlobalEnvironment g = new GlobalEnvironment();
		//g.show();
		g.run();

	    long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.printf("%d seconds passed.\n", TimeUnit.MILLISECONDS.toSeconds(elapsedTime));

	}
}
