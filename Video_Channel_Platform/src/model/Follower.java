package model;

public class Follower {
	protected String name;
	protected int maxChannels;
	protected String type;
	protected Channel[] channels;
	protected int noc; // number of channels
	
	public Follower(String name, int maxChannels) {
		this.name = name;
		this.maxChannels = maxChannels;
		
		if (this instanceof Subscriber) {
			this.type = "Subscriber";
		}
		else {
			this.type = "Monitor";
		}
		
		this.channels = new Channel[this.maxChannels];
		this.noc = 0;
	}

	public String getName() {
		return this.name;
	}
	
	public String getType() {
		return this.type;
	}

	public int getMaxChannels() {
		return this.maxChannels;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMaxChannels(int maxChannels) {
		this.maxChannels = maxChannels;
	}
	
	public void addChannel(Channel c) {
		this.channels[this.noc] = c;
		this.noc++;
	}
	
	public void removeChannel(Channel c) {
		Channel[] nc = new Channel[this.maxChannels];
		for (int i = 0, j = 0; i < this.noc; i++) {
			if (this.channels[i] != c) {
				nc[j] = this.channels[i];
				j++;
			}
		}
		this.channels = nc;
		this.noc--;
	}
	

}
