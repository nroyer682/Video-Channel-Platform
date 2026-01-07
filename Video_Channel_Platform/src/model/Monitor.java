package model;

public class Monitor extends Follower {
	private int[] views;
	private int[] totalWatchTime;
	private int[] maxWatchTime;
	private double[] avgWatchTime;
	private boolean[] hasStats;

	public Monitor(String name, int maxChannels) {
		super(name, maxChannels);
		this.views = new int[this.maxChannels];
		this.totalWatchTime = new int[this.maxChannels];
		this.maxWatchTime = new int[this.maxChannels];
		this.avgWatchTime = new double[this.maxChannels];
		this.hasStats = new boolean[this.maxChannels];
	}
	
	public int indexOf(Channel c) {
		int index = -1;
		boolean found = false;
		for (int i = 0; !found && i < this.noc; i++) {
			if (this.channels[i] == c) {
				index = i;
				found = true;
			}
		}
		return index;
	}
	
	public void setStats(Channel c, int watchTime) {
		int index = this.indexOf(c);
		this.hasStats[index] = true;
		this.views[index]++;
		this.totalWatchTime[index] += watchTime;
		if (watchTime > this.maxWatchTime[index]) {
			this.maxWatchTime[index] = watchTime;
		}
		this.avgWatchTime[index] = this.totalWatchTime[index] / (double) this.views[index];
	}
	
	public String toString() {
		String result = null;
		
		String channelList = "[";
		for (int i = 0; i < this.noc; i++) {
			channelList += this.channels[i].getName();
			if (this.hasStats[i]) {
				channelList += String.format(" {#views: %d, max watch time: %d, avg watch time: %.2f}", this.views[i], this.maxWatchTime[i], this.avgWatchTime[i]);
			}
			if (i < this.noc - 1) {
				channelList += ", ";
			}
		}
		channelList += "]";
		
		if (this.noc == 0) {
			result = String.format("Monitor %s follows no channels.", this.name);
		}
		else {
			result = String.format("Monitor %s follows %s.", this.name, channelList);
		}
		return result;
	}

}
