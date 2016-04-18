
public class RequestObject implements Comparable<RequestObject> {
	private int id;
	private int size;
	private int rank;
	private double requestFreq;
	
	
	private double globalValue; // Changes over time.
	
	public RequestObject(int id, int size, int rank) {
		this.id = id;
		this.size = size;
		this.rank = rank;
		this.requestFreq = ZipfDist.getFrequency(this.rank);
	}

	public int getId() {
		return id;
	}

	public int getSize() {
		return size;
	}
	
	public double getRequestFreq() {
		return requestFreq;
	}
	
	public int getRank() {
		return rank;
	}
	
	public double getGlobalValue() {
		return globalValue;
	}
	
	public void setGlobalValue(double globalValue) {
		this.globalValue = globalValue;
	}
	
	@Override
	public boolean equals(Object obj) {
		RequestObject other = (RequestObject) obj;
		return this.id == other.id;
	}
	
	@Override
	public int compareTo(RequestObject other) {
		return globalValue < other.globalValue ? -1 : 1;
	}
}
