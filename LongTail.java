import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class LongTail {
	private static int objectNums = GlobalEnvironment.OBJECT_COUNT;	
	private static int objectSize = GlobalEnvironment.OBJECT_SIZE;

	private static int[] objectSizes = {5, 55, 550, 5500}; // MB, the size set is [(1,10),(10,100),(100,1000),(1000,10000)]
	private static ArrayList<Integer> generatedObjectSizes = new ArrayList<Integer>(); // MB
	
	public static ArrayList<Integer> generateRandomObjectSizes() {
		// Calculate the portion of each file size 
		double[] portions = new double[objectSizes.length];
		double portionsSum = 0;
		for (int i = 0; i < portions.length; i++) {
			portions[i] = 1 / Math.pow(objectSizes[i], 0.5);
			portionsSum += portions[i];
		}
		
		// Calculate the number of each file size
		double[] objectSizesDistribution = new double[objectSizes.length];		
		for (int i = 0; i < objectSizes.length; i++) {
			objectSizesDistribution[i] = (portions[i] / portionsSum) * objectNums;
		}
				
		// Generate object size in random by portions
		Random rand = new Random();
		int low = 1;
		int high = 10;
		for (int n = 0; n < objectSizes.length; n++) {
			for (int i = 0; i < objectSizesDistribution[n]; i++) {
				generatedObjectSizes.add(rand.nextInt(high - low) + low);
			}
			low = low * 10;
			high = high * 10;			
		}
		// In case the object number is reduced due to double type, like 0.45.
		while (generatedObjectSizes.size() < objectNums) {
			low = 1;
			high = 10;
			generatedObjectSizes.add(rand.nextInt(high - low) + low);
		}
		
		long seed = System.nanoTime();
 		Collections.shuffle(generatedObjectSizes, new Random(seed));	
 		
		return generatedObjectSizes;
	}
	
	
	public static ArrayList<Integer> generateFixObjectSizes() {
		for (int i = 0; i < objectNums; i++) {
			generatedObjectSizes.add(objectSize);
		}		
		return generatedObjectSizes;
	
	}
	
	
	
//	public static int getObjectSizeByFileId(int fileId) {
//		generatedObjectSizes = generateObjectSizes();
//		// create and init object size
//		
//		return generatedObjectSizes.get(fileId);
//	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		System.out.println(LongTail.generateRandomObjectSizes());
		System.out.println(LongTail.generateFixObjectSizes());

	}

}