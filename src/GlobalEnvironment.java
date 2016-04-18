import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GlobalEnvironment {
	private String separator = "=================================================";
	public static final int AS_COUNT = 5;
	public static final int CLIENT_PER_AS = 100;
	public static final int CLIENT_COUNT = AS_COUNT * CLIENT_PER_AS;
	public static final int OBJECT_COUNT = 1000;
	public static final int EVENT_COUNT = 8000;
	public static final double CACHE_SIZE_RATIO = 0.2;
	public static final double UNIT_STORE_COST = 0.005;
	public static final int OBJECT_SIZE = 10;

	public AutonomousSystem autonomousSystems[];
	public Client clients[];
	public RequestObject requestObjects[];
	public Event events[];
	private List<Integer> objectRanks;
	private ArrayList<Integer> objectSizes; // MB
	private double linkCosts[][];
	
	private int totalRequestCount = 0;
	private int totalHitCount = 0;
	private double costWithCache = 0;
	private double costWithoutCache = 0;
	private double storageCostWithCache = 0;
	
	
	private double hitRate;
	private double TSCR;
	
	int directcount = 0;
	
	public GlobalEnvironment() {
		// Initialize AS
		autonomousSystems = new AutonomousSystem[AS_COUNT];
		for (int i = 0; i < AS_COUNT; i++) {
			autonomousSystems[i] = new AutonomousSystem(i);
		}
		
		// Initialize Clients
		clients = new Client[CLIENT_COUNT];	
 		int clientId = 0;
 		for (int i = 0; i < AS_COUNT; i++) {
 			for (int j = 0; j < CLIENT_PER_AS; j++) {
 				clients[clientId] = new Client(clientId, autonomousSystems[i]);
 				autonomousSystems[i].getClients()[j] = clients[clientId];
 				clientId++;
 			}
 		}
		
		// Initialize objectRanks
		objectRanks = new ArrayList<Integer>();
 		for (int i = 0; i < OBJECT_COUNT; i++) {
 			objectRanks.add(i+1);
 		}
		long seed = System.nanoTime();
 		Collections.shuffle(objectRanks, new Random(seed));
 		
 		objectSizes = LongTail.generateFixObjectSizes();  // in fix
 		 		
 		// Initialize RequestObjects
 		requestObjects = new RequestObject[OBJECT_COUNT];
		for (int i = 0; i < OBJECT_COUNT; i++) {
			requestObjects[i] = new RequestObject(i, objectSizes.get(i).intValue(), objectRanks.get(i).intValue());
			
		}
		
		// Initialize linkCosts
		LinkCosts l = new LinkCosts(AS_COUNT);
		linkCosts = l.generateLinkCosts();
		
		// Initialize events
		events = new Event[EVENT_COUNT];
		for (int i = 0; i < EVENT_COUNT; i++) {
			events[i] = new Event(i, getRandomClientId(), getRequestObjectByRank(ZipfDist.generateRandomRank()).getId());
		}
		
		// Initialize AS cacheSize & availableFreeSpace
		for (Event event: events) {
			AutonomousSystem as = clients[event.getClientId()].belongToAS();
			RequestObject object = requestObjects[event.getRequestObjectId()];
			as.setCacheSize(as.getCacheSize() + CACHE_SIZE_RATIO * object.getSize());
		}
		
		double minCacheSize = autonomousSystems[0].getCacheSize();
		for (int i =1; i < AS_COUNT; i++) {
			minCacheSize = Math.min(minCacheSize, autonomousSystems[i].getCacheSize());
		}
		
		// Set all the cache with the minimum cache size
		for (AutonomousSystem as: autonomousSystems) {
			as.setCacheSize(minCacheSize);
		}
		
		for (AutonomousSystem as: autonomousSystems) {
			as.setAvailableFreeSpace(as.getCacheSize());
		}					
		
	}
	
	public int getRandomClientId() {
		Random randomGenerator = new Random();
		int clientId = randomGenerator.nextInt(CLIENT_COUNT);
		return clientId;
	}
	
	public RequestObject getRequestObjectByRank(int rank) {
		for (int i = 0; i < OBJECT_COUNT; i++) {
			if (requestObjects[i].getRank() == rank) {
				return requestObjects[i];
			}
		}
		return null;
	}
	
	// Show the initialization result
	public void show() {
		// Show AS
		System.out.println(separator);
 		System.out.printf("Generated %d AS:\n", AS_COUNT);
 		for (int i = 0; i < AS_COUNT; i++) {
 			System.out.printf("AS%d: cacheSize->%.2f\n", 
 					autonomousSystems[i].getId(),
 					autonomousSystems[i].getCacheSize());
 		} 
 		
 		
 		System.out.printf("Generated %d clients:\n", CLIENT_COUNT);
 		for (int i = 0; i < CLIENT_COUNT; i++) {
 			System.out.printf("client%d: %s, BelongToAS: AS%d\n",
 					clients[i].getId(),
 					clients[i],
 					clients[i].belongToAS().getId());
 		}
 		
		// Show objectRanks
 		for (int i = 0; i < OBJECT_COUNT; i++) {
 			System.out.printf("%d ", objectRanks.get(i));
 		}
 		System.out.println();
 		
 		// Show requestObjects
 		System.out.println(separator);
 		System.out.printf("Generated %d requestObjects:\n", OBJECT_COUNT);
 		for (int i = 0; i < OBJECT_COUNT; i++) {
 			System.out.printf("requestObject%d: size->%s, rank->%s, requestFreq->%.0f\n",
 					requestObjects[i].getId(),
 					requestObjects[i].getSize(),
 					requestObjects[i].getRank(),
 					requestObjects[i].getRequestFreq());
 		}

 		// Show linkCosts
 		System.out.println(separator);
 		System.out.printf("Generated %dX%d linkCosts:\n", AS_COUNT, AS_COUNT);
        for (int i = 0; i < AS_COUNT; i++) {
            for (int j = 0; j < AS_COUNT; j++) 
                System.out.printf("%9.4f ", linkCosts[i][j]);
            System.out.println();
        }
        
        // Show events
 		System.out.println(separator);
 		System.out.printf("Generated %d events:\n", EVENT_COUNT);
 		for (int i = 0; i < EVENT_COUNT; i++) {
 			System.out.printf("event%d: clientId->%d, belongToAS->%d, requestObjectId->%d\n",
 					events[i].getId(),
 					events[i].getClientId(),
 					clients[events[i].getClientId()].belongToAS().getId(),
 					events[i].getRequestObjectId());
 		} 		
	}
	
	public void run() {
		for (Event event: events) {
			AutonomousSystem as = clients[event.getClientId()].belongToAS();
			RequestObject object = requestObjects[event.getRequestObjectId()];
 			System.out.printf("event%d: clientId->%d, belongToAS->%d, requestObjectId->%d\n",
 					event.getId(),
 					event.getClientId(),
 					clients[event.getClientId()].belongToAS().getId(),
 					event.getRequestObjectId());
 			
			cacheUpdate(as, object);
//			cacheUpdateWithStoreCost(as, object);
//			cacheUpdateWithLRU(as, object);
		}
		
		// Calculate hitRate and TSCR
		hitRate = (double)totalHitCount / totalRequestCount;
		TSCR = (double)(costWithoutCache - costWithCache) / costWithoutCache;		
		
		System.out.printf("totalHitCount:     %d\n", totalHitCount);
		System.out.printf("totalRequestCount: %d\n", totalRequestCount);
		System.out.printf("costWithCache:     %f\n", costWithCache);
		System.out.printf("costWithoutCache:  %f\n", costWithoutCache);
		System.out.printf("hitRate:           %.4f%%\n", hitRate * 100);
		System.out.printf("TSCR:              %.4f%%\n", TSCR * 100);
		System.out.println(separator);		
	}
	
	
	
	// After client request object, update the cache
	public void cacheUpdate(AutonomousSystem as, RequestObject requestObject) {
		totalRequestCount++;
		costWithoutCache += requestObject.getSize() * requestObject.getRequestFreq();
		if (as.isCached(requestObject)) {
			totalHitCount++;
			// Cached in local AS already.
			return;
		}
	
		double minLinkCost = 1;
		for (int i = 0; i < AS_COUNT; i++) {
			if (autonomousSystems[i].isCached(requestObject)) {
				minLinkCost = Math.min(minLinkCost, linkCosts[as.getId()][i]);
			}
		}
		costWithCache += minLinkCost * requestObject.getSize() * requestObject.getRequestFreq();

		if (requestObject.getSize() <= as.getAvailableFreeSpace()) {
			// Cache size is enough for request object, just cache it directly
			storageCostWithCache += UNIT_STORE_COST * requestObject.getSize() ;
			as.cacheObject(requestObject);


		} else {
			// Cache size isn't enough for request object. May need to replace.
			// Store all objects in this AS
			List<RequestObject> objects = new ArrayList<RequestObject>();
			// Calculate requested object's  Global Value
			requestObject.setGlobalValue(calculateGlobalValue(as, requestObject));
			objects.add(requestObject);
			// Calculate all cached objects' global value in this AS
			for (RequestObject object : as.getCachedObjects()) {
				object.setGlobalValue(calculateGlobalValue(as, object));
				objects.add(object);
				}

			// Sort all objects according to their global value
			Collections.sort(objects);
			double freeSpace = as.getAvailableFreeSpace();
			// Scan object whose GV smaller that requested object
			for (RequestObject object : objects) {
				if (freeSpace >= requestObject.getSize() || object.equals(requestObject)) {
					break;
				}
				freeSpace += object.getSize();
			}

			if (freeSpace >= requestObject.getSize()) {
				// Free space is enough for requested object
				for (RequestObject object : objects) {
					if (as.getAvailableFreeSpace() >= requestObject.getSize()) {
						break;
					}
					as.uncacheObject(object);
					//storageCostWithCache -= UNIT_STORE_COST*object.getSize();
				}
				as.cacheObject(requestObject);
				storageCostWithCache += UNIT_STORE_COST * requestObject.getSize() ;
				
			} else {
				// Insufficient space to cache the requested object,
				// so don't cache and forward the request.
			}
		}
	}
	
	private double calculateGlobalValue(AutonomousSystem as, RequestObject object) {
		double globalValue = 0;
		if (isCachedInAnyOtherAutonomousSystem(as, object)) {
			double minLinkCost = 1;
			for (int i = 0; i < AS_COUNT; i++) {
				if (i != as.getId() && autonomousSystems[i].isCached(object)) {
					minLinkCost = Math.min(minLinkCost, linkCosts[as.getId()][i]) ;				
				}
			}
			
			globalValue = object.getRequestFreq() * minLinkCost;						
			for (int i = 0; i < AS_COUNT; i++) {
				if (i != as.getId() && !autonomousSystems[i].isCached(object)) {
					minLinkCost = 1;
					for (int j = 0; j < AS_COUNT; j++) {
						if (j != i && autonomousSystems[j].isCached(object)) {
							minLinkCost = Math.min(minLinkCost, linkCosts[i][j]);
	
						}
					}					
					globalValue += (minLinkCost - Math.min(minLinkCost, linkCosts[i][as.getId()])) * object.getRequestFreq();					
				}
			}

		} else {
			globalValue = object.getRequestFreq();
			for (int i = 0; i < AS_COUNT; i++) {
				if (i != as.getId()) {
					globalValue += object.getRequestFreq() * (1 - linkCosts[i][as.getId()]);
				}
			}
		}
		
		return globalValue;
	}
	
	public void cacheUpdateWithStoreCost(AutonomousSystem as, RequestObject requestObject) {
		totalRequestCount++;
		costWithoutCache += requestObject.getSize() * requestObject.getRequestFreq();
		if (as.isCached(requestObject)) {
			// System.out.println("Space :" + as.getAvailableFreeSpace());
			totalHitCount++;
			// Cached in local AS already.
			return;
		}
		
		double minLinkCost = 1;
		for (int i = 0; i < AS_COUNT; i++) {
			if (autonomousSystems[i].isCached(requestObject)) {
				minLinkCost = Math.min(minLinkCost, linkCosts[as.getId()][i]);
			}
		}
		
		costWithCache += minLinkCost * requestObject.getSize() * requestObject.getRequestFreq();
		
		// If GV < 0, don't restore it 
		if(calculateGlobalValueWithStoreCost(as, requestObject) < 0) {			
			return;
		}
		
		if (requestObject.getSize() <= as.getAvailableFreeSpace()) {
			// Cache size is enough for request object, just cache it directly
			storageCostWithCache += UNIT_STORE_COST * requestObject.getSize() ;
			as.cacheObject(requestObject);		

		} else {
			
			// Cache size isn't enough for request object. May need to replace.
			// Store all objects in this AS in a list
			List<RequestObject> objects = new ArrayList<RequestObject>();
			
			// Calculate requested object's  Global Value
			requestObject.setGlobalValue(calculateGlobalValueWithStoreCost(as, requestObject));
			objects.add(requestObject);
			
			// Calculate all cached objects' global value in this AS
			for (RequestObject object : as.getCachedObjects()) {
				object.setGlobalValue(calculateGlobalValueWithStoreCost(as, object));
				objects.add(object);
			}

			// Sort all objects according to their global value
			Collections.sort(objects);
			double freeSpace = as.getAvailableFreeSpace();
	
			// Scan object whose GV smaller that requested object
			for (RequestObject object : objects) {
				if (freeSpace >= requestObject.getSize() || object.equals(requestObject)) {
					break;
				}
				freeSpace += object.getSize();
				
			}

			if (freeSpace >= requestObject.getSize()) {
				// Free space is enough for requested object
				for (RequestObject object : objects) {
					if (as.getAvailableFreeSpace() >= requestObject.getSize()) {
						break;
					}
					as.uncacheObject(object);
					storageCostWithCache -= UNIT_STORE_COST*requestObject.getSize();
			        
				}
				as.cacheObject(requestObject);				
				storageCostWithCache += UNIT_STORE_COST * requestObject.getSize() ;			
				costWithCache += storageCostWithCache;
			} else {
				// Insufficient space to cache the requested object,
				// so don't cache and forward the request.
			}
		}
	}
	
	private double calculateGlobalValueWithStoreCost(AutonomousSystem as, RequestObject object) {
		
		double globalValue = 0;	
		if (isCachedInAnyOtherAutonomousSystem(as, object)) {
			double minLinkCost = 1;
			for (int i = 0; i < AS_COUNT; i++) {
				if (i != as.getId() && autonomousSystems[i].isCached(object)) {
					minLinkCost = Math.min(minLinkCost, linkCosts[as.getId()][i]);
				}
			}
			globalValue = object.getRequestFreq() * minLinkCost;
			for (int i = 0; i < AS_COUNT; i++) {
				if (i != as.getId() && !autonomousSystems[i].isCached(object)) {
					minLinkCost = 1;
					for (int j = 0; j < AS_COUNT; j++) {
						if (j != i && autonomousSystems[j].isCached(object)) {
							minLinkCost = Math.min(minLinkCost, linkCosts[i][j]);
							
						}
					}
					
					globalValue += (minLinkCost - Math.min(minLinkCost, linkCosts[i][as.getId()])) * object.getRequestFreq();
					
				}
			}

		} else {
			globalValue = object.getRequestFreq();
			for (int i = 0; i < AS_COUNT; i++) {
				if (i != as.getId()) {
					globalValue += object.getRequestFreq() * (1 - linkCosts[i][as.getId()]);
				}
			}
		}
		
		globalValue -= UNIT_STORE_COST * object.getSize();
		return globalValue;
	}


	private boolean isCachedInAnyOtherAutonomousSystem(AutonomousSystem as, RequestObject object) {
		for (int i = 0; i < AS_COUNT; i++) {
			if (i != as.getId() && autonomousSystems[i].isCached(object)) {
				return true;
			}
		}
		return false;
	}
	
	public void cacheUpdateWithLRU(AutonomousSystem as, RequestObject requestObject){
		totalRequestCount++;
		costWithoutCache += requestObject.getSize();
		if (as.isLruCached(requestObject)) {
			// Cached in local AS already
			totalHitCount++;
			// Update the file frequency
			int oldCount = as.getCacheCount(requestObject);
			as.updateCacheCount(requestObject, oldCount + 1); 
			return;
		}
		
		double minLinkCost = 1;
		for (int i = 0; i < AS_COUNT; i++) {
			if (autonomousSystems[i].isCached(requestObject)) {
				minLinkCost = Math.min(minLinkCost, linkCosts[as.getId()][i]);
			}
		}
		costWithCache += minLinkCost * requestObject.getSize();
		
		if (requestObject.getSize() <= as.getAvailableFreeSpace()) {
			// Cache size is enough for request object, just cache it directly
			as.cacheLruObject(requestObject);
		} else {
			double freeSpace = as.getAvailableFreeSpace();
			int minCount = Integer.MAX_VALUE;
			RequestObject minKey = requestObject;
			while (requestObject.getSize() > freeSpace) {
				for (Map.Entry<RequestObject, Integer> entry: as.getLruObjects().entrySet()) {
					if (entry.getValue() <= minCount) {
						minCount = entry.getValue();
						minKey = entry.getKey();
					}
				}
				
				as.uncacheLruObject(minKey);
				freeSpace += minKey.getSize();
			}
			as.cacheLruObject(requestObject);
		}
		
	}	
}
	