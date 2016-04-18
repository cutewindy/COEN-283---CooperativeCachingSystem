import java.util.Random;
 
public class ZipfDist {	
	private static Random rnd = new Random(System.currentTimeMillis());
	/* total file number, skew, q, and base number used for freqCalc can be changed freely according to our algorithm*/
	private static int fileNums = GlobalEnvironment.OBJECT_COUNT;
	private static double skew = 0.7;
	private static double q = 20;
//	private static double[] skew_ary = {0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1, 1.1, 1.2};
	//private static int skew_cnt = 0;
	private static int freqBase = 10000;//assume 10000 events already happened for frequency calculation
	private static double bottom = 0;
	 
	// the generateRandomRank() method returns an random rank id.
	// The frequency of the generated rank ids follow M-Zipf distribution.
	public static int generateRandomRank() {		 
		int rank;
	    double friquency = 0;
	    double dice;
	 
	    rank = rnd.nextInt(fileNums+1);
	    friquency = (1.0d / Math.pow((rank+q), skew)) / bottom;
	    dice = friquency * rnd.nextDouble();	 
	    //System.out.println("frequency is " + frequency + " and dice is " + dice);
	    while(!(dice < friquency) || (rank == 0)) {	    	 
	    	rank = rnd.nextInt(fileNums+1);
	    	friquency = (1.0d / Math.pow((rank+q), skew)) / bottom;
	    	dice = friquency * rnd.nextDouble();
	    }
	    return rank;
	}
	
	 // This method returns a probability that the given rank occurs.
	 public static double getFrequency(int rank) {		 
		 for(int i=1;i < fileNums; i++) {			 
			 bottom += (1/Math.pow((i+q), skew));
		 }
	     return freqBase * (1.0d / Math.pow((rank+q), skew)) / bottom;
	 }
	 
	 public static void main(String[] args) {
		// zipfCalculation(rankInput);
	 }
}
