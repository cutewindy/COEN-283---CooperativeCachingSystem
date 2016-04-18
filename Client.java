
public class Client {
	private int id;
	private AutonomousSystem belongToAS;
	
	public Client(int id, AutonomousSystem belongToAS) {
		this.id = id;
		this.belongToAS = belongToAS;
	}
	
	public int getId() {
		return id;
	}
	
	public AutonomousSystem belongToAS() {
		return belongToAS;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
