package model;

@SuppressWarnings("serial")
public class AlreadyFollowedException extends Exception {
	public AlreadyFollowedException(String s) {
		super(s);
	}
}
