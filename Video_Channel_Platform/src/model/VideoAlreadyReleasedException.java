package model;

@SuppressWarnings("serial")
public class VideoAlreadyReleasedException extends Exception {
	public VideoAlreadyReleasedException(String s) {
		super(s);
	}
}
