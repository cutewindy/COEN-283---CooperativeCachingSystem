public class Event {
	private int id;
	private int clientId;
	private int requestObjectId;

	public Event(int id, int clientId, int requestObjectId) {
		this.id = id;
		this.clientId = clientId;
		this.requestObjectId = requestObjectId;
	}
	
	public int getId() {
		return id;
	}

	public int getClientId() {
		return clientId;
	}

	public int getRequestObjectId() {
		return requestObjectId;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}



}
