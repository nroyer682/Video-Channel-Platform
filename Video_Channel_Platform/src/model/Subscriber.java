package model;

public class Subscriber extends Follower {
	private int maxRecommendedVideos;
	private int nov;
	private String[] recommendedVideos;

	public Subscriber(String name, int maxChannels, int maxRecommendedVideos) {
		/*
		 * Create a subscriber with the specified name and 
		 * 	maximum 20 channels to follow and maximum 40 videos to be recommended by the channels.
		 * See the lab manual to see what to expect when a preset maximum is exceeded.
		 */
		super(name, maxChannels);
		this.maxRecommendedVideos = maxRecommendedVideos;
		this.nov = 0;
		this.recommendedVideos = new String[this.maxRecommendedVideos];
	}
	
	public int getMaxRecommendedVideos() {
		return this.maxRecommendedVideos;
	}

	public void addRecommendedVideo(String video) {
		this.recommendedVideos[this.nov] = video;
		this.nov++;
	}
	
	public boolean isRecommended(String video) {
		boolean found = false;
		for (int i = 0; !found && i < this.nov; i++) {
			if (this.recommendedVideos[i].equals(video)) {
				found = true;
			}
		}
		return found;
	}
	
	public void watch(String video, int watchTime) throws VideoNotRecommendedException {
		/* 
		 * Subscriber `sub1` watched Jazz Piano Radio for 20 minutes. 
		 * 
		 * After a subscriber watched a recommended video of a channel, 
		 * 	the watch time is immediately used to update the statistics of all that channel's monitors (not subscribers).
		 * 
		 * Assume that the second argument of method `watch` is always an integer specifying the watch time in terms of minutes. 
		 * 
		 * Since video names across all channels are assumed to be unique, 
		 * 	the `watch` method should be able to figure out to which channel the specified video name belongs.
		 */
		if (!this.isRecommended(video)) {
			throw new VideoNotRecommendedException("Video is not recommended");
		}
		else {
			boolean found = false;
			for (int i = 0; !found && i < this.noc; i++) {
				Channel c = this.channels[i];
				for (int j = 0; !found && j < c.getNumVideos(); j++) {
					if (c.getVideos()[j].equals(video)) {
						found = true;
						for (int k = 0; k < c.getNumFollowers(); k++) {
							Follower f = c.getFollowers()[k];
							if (f instanceof Monitor) {
								((Monitor) f).setStats(c, watchTime);
							}
						}
					}
				}
			}
		}
		
	}

	public String toString() {
		String result = null;
		
		String channelList = "[";
		for (int i = 0; i < this.noc; i++) {
			channelList += this.channels[i].getName();
			if (i < this.noc - 1) {
				channelList += ", ";
			}
		}
		channelList += "]";
		
		String videoList = "<";
		for (int i = 0; i < this.nov; i++) {
			videoList += this.recommendedVideos[i];
			if (i < this.nov - 1) {
				videoList += ", ";
			}
		}
		videoList += ">";
		
		if (this.noc == 0 && this.nov == 0) {
			result = String.format("Subscriber %s follows no channels and has no recommended videos.", this.name);
		}
		else if (this.noc != 0 && this.nov == 0) {
			result = String.format("Subscriber %s follows %s and has no recommended videos.", this.name, channelList);
		}
		else if (this.noc != 0 && this.nov != 0) {
			result = String.format("Subscriber %s follows %s and is recommended %s.", this.name, channelList, videoList);
		}
		else {
			result = String.format("Subscriber %s follows no channels and is recommended %s.", this.name, videoList);
		}
		return result;
	}

	

}
