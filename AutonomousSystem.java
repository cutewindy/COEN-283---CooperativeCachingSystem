import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AutonomousSystem {
	private int id;
	private double cacheSize;
	private double availableFreeSpace;
	private HashMap<RequestObject, Integer> lruObjects = new HashMap<RequestObject, Integer>();
	private int count;
	private HashSet<RequestObject> cachedObjects = new HashSet<RequestObject>();
	private Client clients[] = new Client[GlobalEnvironment.CLIENT_PER_AS];
	
	public AutonomousSystem(int id) {
		this.id = id;
	}
	
	
	public int getId() {
		return id;
	}
	
	public double getCacheSize() {
		return cacheSize;
	}
	
	public void setCacheSize(double cacheSize) {
		this.cacheSize = cacheSize;
	}	
	
	public double getAvailableFreeSpace() {
		return availableFreeSpace;
	}
	
	public void setAvailableFreeSpace(double availableFreeSpace) {
		this.availableFreeSpace = availableFreeSpace;
	}

	public int getCount() {
		return count;
	}
	
//	public double getRequestFreq(RequestObject object) {
//		return requestFreqs[object.getId()];
//	}
	
	public Client[] getClients() {
		return clients;
	}
	
	public Set<RequestObject> getCachedObjects() {
		return cachedObjects;
	}
	
	public boolean isCached(RequestObject object) {
		return cachedObjects.contains(object);
	}
	
	public void cacheObject(RequestObject object) {
		cachedObjects.add(object);
		availableFreeSpace -= object.getSize();
	}
	
	public void uncacheObject(RequestObject object) {
		cachedObjects.remove(object.getId());
		availableFreeSpace += object.getSize();
	}
	
	public HashMap<RequestObject, Integer> getLruObjects() {
		return lruObjects;
	}
	
	public int getCacheCount(RequestObject object) {
		return lruObjects.get(object);
	}
	
	public void updateCacheCount(RequestObject object, int count) {
		HashMap<RequestObject, Integer> lruObject = getLruObjects();
		lruObject.put(object, count + 1);
	}
	
	public boolean isLruCached(RequestObject object) {
		return lruObjects.containsKey(object);
	}
	
	public void cacheLruObject(RequestObject object) {
		lruObjects.put(object, 1);
		availableFreeSpace -= object.getSize();
	}
	
	public void uncacheLruObject(RequestObject object) {
		lruObjects.remove(object);
		availableFreeSpace += object.getSize();
	}
}
