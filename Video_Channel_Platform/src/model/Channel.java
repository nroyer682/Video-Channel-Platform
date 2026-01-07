package model;

public class Channel {
	private String name;
	private int maxFollowers;
	private int maxVideos;
	private String[] videos;
	private int nov; // number of videos
	private Follower[] followers;
	private int nof; // number of followers

	public Channel(String name, int maxFollowers, int maxVideos) {
		/*
		 * Create a channel with the specified name and 
		 * 	maximum 50 followers and maximum 100 videos to release.
		 * See the lab manual to see what to expect when a preset maximum is exceeded.
		 */
		this.name = name;
		this.maxFollowers = maxFollowers;
		this.maxVideos = maxVideos;
		this.videos = new String[this.maxVideos];
		this.nov = 0;
		this.followers = new Follower[this.maxFollowers];
		this.nof = 0;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getNumVideos() {
		return this.nov;
	}
	
	public String[] getVideos() {
		return this.videos;
	}
	
	public int getNumFollowers() {
		return this.nof;
	}
	
	public Follower[] getFollowers() {
		return this.followers;
	}
	
	public boolean isReleased(String video) {
		boolean found = false;
		for (int i = 0; !found && i < this.nov; i++) {
			if (this.videos[i].equals(video)) {
				found = true;
			}
		}
		return found;
	}

	public void releaseANewVideo(String video) throws VideoAlreadyReleasedException {
		if (this.isReleased(video)) {
			throw new VideoAlreadyReleasedException("Video has already been released");
		}
		else {
			this.videos[this.nov] = video;
			this.nov++;
			
			for (int i = 0; i < this.nof; i++) {
				if (this.followers[i] instanceof Subscriber) {
					((Subscriber) this.followers[i]).addRecommendedVideo(video);
				}
			}
		}
	}
	
	public boolean isFollowed(Follower f) {
		boolean found = false;
		for (int i = 0; !found && i < this.nof; i++) {
			if (this.followers[i] == f) {
				found = true;
			}
		}
		return found;
	}

	public void follow(Follower f) throws AlreadyFollowedException {
		if (this.isFollowed(f)) {
			throw new AlreadyFollowedException("Already followed by this channel");
		}
		else {
			// updates on the channel
			this.followers[this.nof] = f;
			this.nof++;
			
			// updates on the parameter 'f'
			f.addChannel(this);
		}
	}
	
	public void unfollow(Follower f) throws FollowerNotFoundException {
		if (!isFollowed(f)) {
			throw new FollowerNotFoundException("Follow not found");
		}
		else {
			Follower[] nf = new Follower[this.maxFollowers];
			for (int i = 0, j = 0; i < this.nof; i++) {
				if (this.followers[i] != f) {
					nf[j] = this.followers[i];
					j++;
				}
			}
			this.followers = nf;
			this.nof--;
			
			f.removeChannel(this);
		}
	}
	
	public String toString() {
		String result = null;
		
		String videoList = "<";
		for (int i = 0; i < this.nov; i++) {
			videoList += this.videos[i];
			if (i < this.nov - 1) {
				videoList += ", ";
			}
		}
		videoList += ">";
		
		String followerList = "[";
		for (int i = 0; i < this.nof; i++) {
			followerList += String.format("%s %s", this.followers[i].getType(), this.followers[i].getName());
			if (i < this.nof - 1) {
				followerList += ", ";
			}
		}
		followerList += "]";
		if (this.nov == 0 && this.nof == 0) {
			result = String.format("%s released no videos and has no followers.", this.name);
		}
		else if (this.nov != 0 && this.nof == 0) {
			result = String.format("%s released %s and has no followers.", this.name, videoList);
		}
		else if (this.nov != 0 && this.nof != 0) {
			result = String.format("%s released %s and is followed by %s.", this.name, videoList, followerList);
		}
		else {
			result = String.format("%s released no videos and is followed by %s.", this.name, followerList);
		}
		
		return result;
	}

	
	

}
