package junit_tests;

import static org.junit.Assert.*;

import org.junit.Test;

import model.AlreadyFollowedException;
import model.Channel;
import model.Follower;
import model.FollowerNotFoundException;
import model.Monitor;
import model.Subscriber;
import model.VideoAlreadyReleasedException;
import model.VideoNotRecommendedException;

public class StarterTests {

	/*
	 * Tests related to the Channel class.
	 */ 
	
	@Test
	public void test_channel_01a() {
		/*
		 * Create a channel with the specified name and 
		 * 	maximum 50 followers and maximum 100 videos to release.
		 * 
		 */
		Channel ch = new Channel("Cafe Music BGM", 50, 100);
		assertEquals("Cafe Music BGM released no videos and has no followers.", ch.toString());
	}
	
	@Test
	public void test_channel_01b() {
		Channel ch = new Channel("Cafe Music BGM", 50, 100);
		
		/*
		 * `ch` releases two new videos.
		 */
		try {
			ch.releaseANewVideo("Monday Jazz");
			assertEquals("Cafe Music BGM released <Monday Jazz> and has no followers.", ch.toString());
			
			ch.releaseANewVideo("Tuesday Jazz");
			assertEquals("Cafe Music BGM released <Monday Jazz, Tuesday Jazz> and has no followers.", ch.toString());
		}
		catch (VideoAlreadyReleasedException e) {
			fail("Unexpected exception thrown");
		}
		
	}
	
	/*
	 * Tests related to the Follower classes.
	 */ 
	
	@Test
	public void test_follower_01a() {
		/*
		 * Create a subscriber with the specified name and 
		 * 	maximum 20 channels to follow and maximum 40 videos to be recommended by the channels.
		 * 
		 */
		Follower f = new Subscriber("Alan", 20, 40);
		assertEquals("Subscriber Alan follows no channels and has no recommended videos.", f.toString());
	}
	
	@Test
	public void test_follower_01b() {
		/*
		 * Create a monitor with the specified name and maximum 20 channels to follow.
		 * 
		 */
		Follower f = new Monitor("Stat Sensor A", 20);
		assertEquals("Monitor Stat Sensor A follows no channels.", f.toString());
	}
	
	/*
	 * More tests related to the Channel class.
	 */ 
	
	@Test
	public void test_channel_01c() { 
		/*
		 * Note that the two channels are set with different maximums for
		 * 	the allowed numbers of followers and videos to release. 
		 */
		Channel ch1 = new Channel("Cafe Music BGM", 50, 100);
		Channel ch2 = new Channel("I Love You Venice", 60, 135);
		
		/*
		 * Note that the followers are set with different maximums for the allowed numbers of channels.
		 */
		Follower f1 = new Subscriber("Alan", 20, 40);
		Follower f2 = new Monitor("Stat Sensor A", 60); 
		
		/* 
		 * Let `f1` follow `ch1` (which updates both the context object `ch1` and argument object `f1`). 
		 * 
		 * You can assume that a follower, once added to a channel, will not be added to that channel again. 
		 */
		try {
			ch1.follow(f1);
			assertEquals("Cafe Music BGM released no videos and is followed by [Subscriber Alan].", ch1.toString());
			assertEquals("Subscriber Alan follows [Cafe Music BGM] and has no recommended videos.", f1.toString());
			
			/* 
			 * Let `f2` follow `ch1` (which updates both the context object `ch1` and argument object `f2`). 
			 */
			ch1.follow(f2);
			assertEquals("Cafe Music BGM released no videos and is followed by [Subscriber Alan, Monitor Stat Sensor A].", ch1.toString());
			assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM].", f2.toString());
			
			/* 
			 * Let `f2` follow `ch2` (which updates both the context object `ch2` and argument object `f2`). 
			 */
			ch2.follow(f2);
			assertEquals("I Love You Venice released no videos and is followed by [Monitor Stat Sensor A].", ch2.toString());
			assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM, I Love You Venice].", f2.toString());
			
			/* 
			 * Let `f1` follow `ch2` (which updates both the context object `ch2` and argument object `f1`). 
			 */
			ch2.follow(f1);
			assertEquals("I Love You Venice released no videos and is followed by [Monitor Stat Sensor A, Subscriber Alan].", ch2.toString());
			assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and has no recommended videos.", f1.toString());
		}
		catch (AlreadyFollowedException e) {
			fail("Unexpected exception thrown");
		}
	}
	
	@Test
	public void test_channel_01d() { 
		Channel ch1 = new Channel("Cafe Music BGM", 50, 100);
		Channel ch2 = new Channel("I Love You Venice", 60, 135);
		
		Follower f1 = new Subscriber("Alan", 20, 40);
		Follower f2 = new Monitor("Stat Sensor A", 60);
		
		try {
			ch1.follow(f1);
			ch1.follow(f2);
			ch2.follow(f2);
			ch2.follow(f1);
		}
		catch (AlreadyFollowedException e) {
			fail("Unexpected exception thrown");
		}
		
		assertEquals("Cafe Music BGM released no videos and is followed by [Subscriber Alan, Monitor Stat Sensor A].", ch1.toString());
		assertEquals("I Love You Venice released no videos and is followed by [Monitor Stat Sensor A, Subscriber Alan].", ch2.toString());
		assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and has no recommended videos.", f1.toString());
		assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM, I Love You Venice].", f2.toString());
		
		try {
			/*
			 * Let `f1` stop following `ch1` (which updates both the context object `ch1` and argument object `f1`).
			 */
			ch1.unfollow(f1);
			
			assertEquals("Cafe Music BGM released no videos and is followed by [Monitor Stat Sensor A].", ch1.toString());
			assertEquals("I Love You Venice released no videos and is followed by [Monitor Stat Sensor A, Subscriber Alan].", ch2.toString());
			assertEquals("Subscriber Alan follows [I Love You Venice] and has no recommended videos.", f1.toString());
			assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM, I Love You Venice].", f2.toString());
			
			/*
			 * Let `f1` stop following `ch2` (which updates both the context object `ch2` and argument object `f1`).
			 */
			ch2.unfollow(f1);
			assertEquals("Cafe Music BGM released no videos and is followed by [Monitor Stat Sensor A].", ch1.toString());
			assertEquals("I Love You Venice released no videos and is followed by [Monitor Stat Sensor A].", ch2.toString());
			assertEquals("Subscriber Alan follows no channels and has no recommended videos.", f1.toString());
			assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM, I Love You Venice].", f2.toString());
			
			/*
			 * Let `f2` stop following `ch2` (which updates both the context object `ch2` and argument object `f2`).
			 */
			ch2.unfollow(f2);
			assertEquals("Cafe Music BGM released no videos and is followed by [Monitor Stat Sensor A].", ch1.toString());
			assertEquals("I Love You Venice released no videos and has no followers.", ch2.toString());
			assertEquals("Subscriber Alan follows no channels and has no recommended videos.", f1.toString());
			assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM].", f2.toString());
			
		}
		catch (FollowerNotFoundException e) {
			fail("Unexpected exception thrown");
		}
		
		Follower f3 = new Subscriber("Mark", 20, 45);
		
		try {
			ch2.follow(f3);
			assertEquals("Subscriber Mark follows [I Love You Venice] and has no recommended videos.", f3.toString());
			assertEquals("I Love You Venice released no videos and is followed by [Subscriber Mark].", ch2.toString());
			assertEquals("Cafe Music BGM released no videos and is followed by [Monitor Stat Sensor A].", ch1.toString());
		}
		catch (AlreadyFollowedException e) {
			fail("Unexpected exception thrown");
		}
		
		try {
			ch1.unfollow(f3);
			/*
			 * Since `f3` is not following `ch1`, unfollowing it should have no effect.
			 */
			fail("Expected exception not thrown");
		}
		catch (FollowerNotFoundException e) {
			// Expected
		}
	}
	
	@Test
	public void test_channel_01e() { 
		Channel ch1 = new Channel("Cafe Music BGM", 50, 100);
		Channel ch2 = new Channel("I Love You Venice", 60, 135);
		
		Follower f1 = new Subscriber("Alan", 20, 40);
		Follower f2 = new Monitor("Stat Sensor A", 60);
		Follower f3 = new Subscriber("Jim", 30, 50);
		
		try {
			ch1.follow(f1);
			ch1.follow(f2);
			ch2.follow(f2);
			ch2.follow(f1);
			ch2.follow(f3);
		}
		catch (AlreadyFollowedException e) {
			fail("Unexpected exception thrown");
		}
		
		assertEquals("Cafe Music BGM released no videos and is followed by [Subscriber Alan, Monitor Stat Sensor A].", ch1.toString());
		assertEquals("I Love You Venice released no videos and is followed by [Monitor Stat Sensor A, Subscriber Alan, Subscriber Jim].", ch2.toString());
		assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and has no recommended videos.", f1.toString());
		assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM, I Love You Venice].", f2.toString());
		assertEquals("Subscriber Jim follows [I Love You Venice] and has no recommended videos.", f3.toString());
		
		
		try {
			/*
			 * Let `f1` stop following `ch1` (which updates both the context object `ch1` and argument object `f1`).
			 */
			ch1.unfollow(f1);
			
			assertEquals("Cafe Music BGM released no videos and is followed by [Monitor Stat Sensor A].", ch1.toString());
			assertEquals("I Love You Venice released no videos and is followed by [Monitor Stat Sensor A, Subscriber Alan, Subscriber Jim].", ch2.toString());
			assertEquals("Subscriber Alan follows [I Love You Venice] and has no recommended videos.", f1.toString());
			assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM, I Love You Venice].", f2.toString());
			assertEquals("Subscriber Jim follows [I Love You Venice] and has no recommended videos.", f3.toString());
			
			/*
			 * Let `f1` stop following `ch2` (which updates both the context object `ch2` and argument object `f1`).
			 */
			ch2.unfollow(f1);
			assertEquals("Cafe Music BGM released no videos and is followed by [Monitor Stat Sensor A].", ch1.toString());
			assertEquals("I Love You Venice released no videos and is followed by [Monitor Stat Sensor A, Subscriber Jim].", ch2.toString());
			assertEquals("Subscriber Alan follows no channels and has no recommended videos.", f1.toString());
			assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM, I Love You Venice].", f2.toString());
			assertEquals("Subscriber Jim follows [I Love You Venice] and has no recommended videos.", f3.toString());
			
			/*
			 * Let `f2` stop following `ch2` (which updates both the context object `ch2` and argument object `f2`).
			 */
			ch2.unfollow(f2);
			assertEquals("Cafe Music BGM released no videos and is followed by [Monitor Stat Sensor A].", ch1.toString());
			assertEquals("I Love You Venice released no videos and is followed by [Subscriber Jim].", ch2.toString());
			assertEquals("Subscriber Alan follows no channels and has no recommended videos.", f1.toString());
			assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM].", f2.toString());
			assertEquals("Subscriber Jim follows [I Love You Venice] and has no recommended videos.", f3.toString());
			
			/*
			 * Let `f3` stop following `ch2` (which updates both the context object `ch2` and argument object `f3`).
			 */
			ch2.unfollow(f3);
			assertEquals("Cafe Music BGM released no videos and is followed by [Monitor Stat Sensor A].", ch1.toString());
			assertEquals("I Love You Venice released no videos and has no followers.", ch2.toString());
			assertEquals("Subscriber Alan follows no channels and has no recommended videos.", f1.toString());
			assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM].", f2.toString());
			assertEquals("Subscriber Jim follows no channels and has no recommended videos.", f3.toString());
		}
		catch (FollowerNotFoundException e) {
			fail("Unexpected exception thrown");
		}
		
		
		Follower f4 = new Subscriber("Mark", 20, 45);
		try {
			ch2.follow(f4);
		}
		catch (AlreadyFollowedException e) {
			fail("Unexpected exception thrown");
		}
		assertEquals("Subscriber Mark follows [I Love You Venice] and has no recommended videos.", f4.toString());
		assertEquals("I Love You Venice released no videos and is followed by [Subscriber Mark].", ch2.toString());
		assertEquals("Cafe Music BGM released no videos and is followed by [Monitor Stat Sensor A].", ch1.toString());
		
		try {
			
			/*
			 * Let `f4` stop following `ch2` (which updates both the context object `ch2` and argument object `f4`).
			 */
			ch2.unfollow(f4);
			assertEquals("Subscriber Mark follows no channels and has no recommended videos.", f4.toString());
			assertEquals("I Love You Venice released no videos and has no followers.", ch2.toString());
			assertEquals("Cafe Music BGM released no videos and is followed by [Monitor Stat Sensor A].", ch1.toString());
			
			ch2.unfollow(f4);
			fail("Expected exception not thrown");
			/*
			 * Since `f4` is not following `ch2`, unfollowing it should have no effect.
			 */
			assertEquals("Subscriber Mark follows no channels and has no recommended videos.", f4.toString());
			assertEquals("I Love You Venice released no videos and has no followers.", ch2.toString());
			assertEquals("Cafe Music BGM released no videos and is followed by [Monitor Stat Sensor A].", ch1.toString());
		}
		catch (FollowerNotFoundException e) {
			// Expected
		}
	}
	
	@Test
	public void test_channel_01f() {
		Channel ch1 = new Channel("Cafe Music BGM", 50, 100);
		
		Follower f1 = new Subscriber("Alan", 20, 40);
		
		try {
			ch1.follow(f1);
			ch1.follow(f1);
			fail("Expected exception not thrown");
		}
		catch (AlreadyFollowedException e) {
			// Expected
		}
	}
	
	@Test
	public void test_channel_01g() {
		Channel ch1 = new Channel("Cafe Music BGM", 50, 100);
		Channel ch2 = new Channel("I Love You Venice", 60, 135);
		Channel ch3 = new Channel("Top Pop Hits", 50, 120);
		
		Follower f1 = new Subscriber("Alan", 20, 40);
		Follower f2 = new Monitor("Stat Sensor A", 60);
		Follower f3 = new Subscriber("Jim", 30, 50);
		Follower f4 = new Subscriber("Mark", 20, 45);
		
		try {
			ch1.follow(f1);
			ch1.follow(f4);
			ch2.follow(f4);
			ch2.follow(f3);
			ch3.follow(f3);
			ch3.follow(f2);
		}
		catch (AlreadyFollowedException e) {
			fail("Unexpected exception thrown");
		}
		
		assertEquals("Cafe Music BGM released no videos and is followed by [Subscriber Alan, Subscriber Mark].", ch1.toString());
		assertEquals("I Love You Venice released no videos and is followed by [Subscriber Mark, Subscriber Jim].", ch2.toString());
		assertEquals("Top Pop Hits released no videos and is followed by [Subscriber Jim, Monitor Stat Sensor A].", ch3.toString());
		assertEquals("Subscriber Alan follows [Cafe Music BGM] and has no recommended videos.", f1.toString());
		assertEquals("Monitor Stat Sensor A follows [Top Pop Hits].", f2.toString());
		assertEquals("Subscriber Jim follows [I Love You Venice, Top Pop Hits] and has no recommended videos.", f3.toString());
		assertEquals("Subscriber Mark follows [Cafe Music BGM, I Love You Venice] and has no recommended videos.", f4.toString());
		
		try {
			ch3.unfollow(f3);
			assertEquals("Top Pop Hits released no videos and is followed by [Monitor Stat Sensor A].", ch3.toString());
			assertEquals("Subscriber Jim follows [I Love You Venice] and has no recommended videos.", f3.toString());
			
			ch3.unfollow(f2);
			assertEquals("Top Pop Hits released no videos and has no followers.", ch3.toString());
			assertEquals("Monitor Stat Sensor A follows no channels.", f2.toString());
		}
		catch (FollowerNotFoundException e) {
			fail("Unexpected exception thrown");
		}
	}
	
	@Test
	public void test_channel_01h() {
		Channel ch1 = new Channel("Top Pop Hits", 50, 100);
		
		Follower f1 = new Subscriber("Alan", 20, 40);
		Follower f2 = new Monitor("Stat Sensor A", 60);
		Follower f3 = new Subscriber("Jim", 30, 50);
		Follower f4 = new Subscriber("Mark", 20, 45);
		Follower f5 = new Monitor("Stat Sensor B", 50);
		Follower f6 = new Monitor("Stat Sensor C", 70);
		
		try {
			ch1.follow(f1);
			ch1.follow(f4);
			ch1.follow(f2);
			ch1.follow(f3);
			ch1.follow(f6);
			ch1.follow(f5);
		}
		catch (AlreadyFollowedException e) {
			fail("Unexpected exception thrown");
		}
		
		assertEquals("Top Pop Hits released no videos and is followed by [Subscriber Alan, Subscriber Mark, Monitor Stat Sensor A, Subscriber Jim, Monitor Stat Sensor C, Monitor Stat Sensor B].", ch1.toString());
		assertEquals("Subscriber Alan follows [Top Pop Hits] and has no recommended videos.", f1.toString());
		assertEquals("Monitor Stat Sensor A follows [Top Pop Hits].", f2.toString());
		assertEquals("Subscriber Jim follows [Top Pop Hits] and has no recommended videos.", f3.toString());
		assertEquals("Subscriber Mark follows [Top Pop Hits] and has no recommended videos.", f4.toString());
		assertEquals("Monitor Stat Sensor B follows [Top Pop Hits].", f5.toString());
		assertEquals("Monitor Stat Sensor C follows [Top Pop Hits].", f6.toString());
		
		try {
			ch1.unfollow(f3);
			assertEquals("Top Pop Hits released no videos and is followed by [Subscriber Alan, Subscriber Mark, Monitor Stat Sensor A, Monitor Stat Sensor C, Monitor Stat Sensor B].", ch1.toString());
			assertEquals("Subscriber Jim follows no channels and has no recommended videos.", f3.toString());
			
			ch1.unfollow(f5);
			assertEquals("Top Pop Hits released no videos and is followed by [Subscriber Alan, Subscriber Mark, Monitor Stat Sensor A, Monitor Stat Sensor C].", ch1.toString());
			assertEquals("Subscriber Jim follows no channels and has no recommended videos.", f3.toString());
			assertEquals("Monitor Stat Sensor B follows no channels.", f5.toString());
			
			ch1.unfollow(f1);
			assertEquals("Top Pop Hits released no videos and is followed by [Subscriber Mark, Monitor Stat Sensor A, Monitor Stat Sensor C].", ch1.toString());
			assertEquals("Subscriber Alan follows no channels and has no recommended videos.", f1.toString());
			assertEquals("Subscriber Jim follows no channels and has no recommended videos.", f3.toString());
			assertEquals("Monitor Stat Sensor B follows no channels.", f5.toString());
			
			ch1.unfollow(f4);
			assertEquals("Top Pop Hits released no videos and is followed by [Monitor Stat Sensor A, Monitor Stat Sensor C].", ch1.toString());
			assertEquals("Subscriber Alan follows no channels and has no recommended videos.", f1.toString());
			assertEquals("Subscriber Jim follows no channels and has no recommended videos.", f3.toString());
			assertEquals("Subscriber Mark follows no channels and has no recommended videos.", f4.toString());
			assertEquals("Monitor Stat Sensor B follows no channels.", f5.toString());
			
			
		}
		catch (FollowerNotFoundException e) {
			fail("Unexpected exception thrown");
		}
	}
	
	
	@Test
	public void test_channel_02a() { 
		Channel ch = new Channel("Cafe Music BGM", 50, 100);
		assertEquals("Cafe Music BGM released no videos and has no followers.", ch.toString());
		
		/* 
		 * You can assume that no duplicated video names will be released across all channels. 
		 * That is, names of videos released by all channels are unique.
		 * 
		 * Assume that channel videos, once released, will not be removed.
		 */
		try {
			ch.releaseANewVideo("Jazz Piano Radio");
			assertEquals("Cafe Music BGM released <Jazz Piano Radio> and has no followers.", ch.toString());
			
			ch.releaseANewVideo("Starbucks Music Playlist 2021");
			assertEquals("Cafe Music BGM released <Jazz Piano Radio, Starbucks Music Playlist 2021> and has no followers.", ch.toString());
		}
		catch (VideoAlreadyReleasedException e) {
			fail("Unexpected exception thrown");
		}
		
		Follower f1 = new Subscriber("Alan", 20, 40);
		Follower f2 = new Monitor("Stat Sensor A", 60);
		
		/*
		 * Given that `f1` and `f2` only start following `ch` after it released the two videos,
		 * 	those two videos will not be recommended to `f1` and `f2`.
		 * 
		 * That is, a follower is only recommended videos that are released after they start following a channel.
		 */
		
		try {
			ch.follow(f1);
			assertEquals("Cafe Music BGM released <Jazz Piano Radio, Starbucks Music Playlist 2021> and is followed by [Subscriber Alan].", ch.toString());
			assertEquals("Subscriber Alan follows [Cafe Music BGM] and has no recommended videos.", f1.toString());
			assertEquals("Monitor Stat Sensor A follows no channels.", f2.toString());
			
			ch.follow(f2);
			assertEquals("Cafe Music BGM released <Jazz Piano Radio, Starbucks Music Playlist 2021> and is followed by [Subscriber Alan, Monitor Stat Sensor A].", ch.toString());
			assertEquals("Subscriber Alan follows [Cafe Music BGM] and has no recommended videos.", f1.toString());
			assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM].", f2.toString());
		}
		catch (AlreadyFollowedException e) {
			fail("Unexpected exception thrown");
		}
		
		
	}
	
	@Test
	public void test_channel_02b() { 
		Channel ch1 = new Channel("Cafe Music BGM", 50, 100);
		Channel ch2 = new Channel("I Love You Venice", 60, 135);
		
		Follower f1 = new Subscriber("Alan", 20, 40); 
		Follower f2 = new Monitor("Stat Sensor A", 30);
		Follower f3 = new Subscriber("Mark", 20, 40);
		
		try {
			ch1.follow(f1); 
			ch2.follow(f1);
			ch2.follow(f2);
			ch1.follow(f2);
			ch2.follow(f3);
			ch1.follow(f3);
		}
		catch (AlreadyFollowedException e) {
			fail("Unexpected exception thrown");
		}
		
		
		assertEquals("Cafe Music BGM released no videos and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch1.toString());
		assertEquals("I Love You Venice released no videos and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch2.toString());
		assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and has no recommended videos.", f1.toString());
		assertEquals("Monitor Stat Sensor A follows [I Love You Venice, Cafe Music BGM].", f2.toString());
		assertEquals("Subscriber Mark follows [I Love You Venice, Cafe Music BGM] and has no recommended videos.", f3.toString());
		
		/*
		 * When a video is released by the channel, it is immediately recommended to all its subscribers (not monitors).
		 */ 
		
		try {
			ch1.releaseANewVideo("Jazz Piano Radio");
			/* Update 1: video release updated on `ch1` */
			assertEquals("Cafe Music BGM released <Jazz Piano Radio> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch1.toString());
			/* Update 2: video recommendation updated on all subscribers: `f1` and `f3` */
			assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio>.", f1.toString());
			assertEquals("Subscriber Mark follows [I Love You Venice, Cafe Music BGM] and is recommended <Jazz Piano Radio>.", f3.toString());
			
			/* no changes on the other channel and the monitor */
			assertEquals("I Love You Venice released no videos and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch2.toString());
			assertEquals("Monitor Stat Sensor A follows [I Love You Venice, Cafe Music BGM].", f2.toString());
			
			ch2.releaseANewVideo("Baroque Live Music 24/7");
			/* Update 1: video release updated on `ch2` */
			assertEquals("I Love You Venice released <Baroque Live Music 24/7> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch2.toString());
			/* Update 2: video recommendation updated on all subscribers: `f1` and `f3` */
			assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", f1.toString());
			assertEquals("Subscriber Mark follows [I Love You Venice, Cafe Music BGM] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", f3.toString());
			
			/* no changes on the other channel and the monitor */
			assertEquals("Cafe Music BGM released <Jazz Piano Radio> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch1.toString());
			assertEquals("Monitor Stat Sensor A follows [I Love You Venice, Cafe Music BGM].", f2.toString());
			
		}
		catch (VideoAlreadyReleasedException e) {
			fail("Unexpected exception thrown");
		}
	}
	
	@Test
	public void test_channel_02c() { 
		Channel ch1 = new Channel("Cafe Music BGM", 50, 100);
		Channel ch2 = new Channel("I Love You Venice", 60, 135);
		
		Follower f1 = new Subscriber("Alan", 20, 40); 
		Follower f2 = new Monitor("Stat Sensor A", 30);
		Follower f3 = new Subscriber("Mark", 20, 40);
		
		try {
			ch1.follow(f1); 
			ch2.follow(f1);
			ch2.follow(f2);
			ch1.follow(f2);
			ch2.follow(f3);
			ch1.follow(f3);
		}
		catch (AlreadyFollowedException e) {
			fail("Unexpected exception thrown");
		}
		
		
		assertEquals("Cafe Music BGM released no videos and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch1.toString());
		assertEquals("I Love You Venice released no videos and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch2.toString());
		assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and has no recommended videos.", f1.toString());
		assertEquals("Monitor Stat Sensor A follows [I Love You Venice, Cafe Music BGM].", f2.toString());
		assertEquals("Subscriber Mark follows [I Love You Venice, Cafe Music BGM] and has no recommended videos.", f3.toString());
		
		/*
		 * When a video is released by the channel, it is immediately recommended to all its subscribers (not monitors).
		 */ 
		
		try {
			ch1.releaseANewVideo("Jazz Piano Radio");
			/* Update 1: video release updated on `ch1` */
			assertEquals("Cafe Music BGM released <Jazz Piano Radio> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch1.toString());
			/* Update 2: video recommendation updated on all subscribers: `f1` and `f3` */
			assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio>.", f1.toString());
			assertEquals("Subscriber Mark follows [I Love You Venice, Cafe Music BGM] and is recommended <Jazz Piano Radio>.", f3.toString());
			
			/* no changes on the other channel and the monitor */
			assertEquals("I Love You Venice released no videos and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch2.toString());
			assertEquals("Monitor Stat Sensor A follows [I Love You Venice, Cafe Music BGM].", f2.toString());
			
			ch1.releaseANewVideo("Jazz Piano Radio");
			fail("Expected exception not thrown");
			
			
		}
		catch (VideoAlreadyReleasedException e) {
			// Expected
		}
	}
	
	@Test
	public void test_channel_03a() { 
		Channel ch1 = new Channel("Cafe Music BGM", 50, 100);
		Channel ch2 = new Channel("I Love You Venice", 60, 135);
		
		Subscriber sub1 = new Subscriber("Alan", 20, 40); 
		Subscriber sub2 = new Subscriber("Mark", 20, 40);
		Monitor mon1 = new Monitor("Stat Sensor A", 30);
		
		try {
			ch1.follow(sub1); 
			ch1.follow(mon1);
			ch1.follow(sub2);
			
			ch2.follow(mon1);
			ch2.follow(sub2);
			ch2.follow(sub1);
		}
		catch (AlreadyFollowedException e) {
			fail("Unexpected exception thrown");
		}
		
		
		try {
			ch1.releaseANewVideo("Jazz Piano Radio");
			ch2.releaseANewVideo("Baroque Live Music 24/7");
		}
		catch (VideoAlreadyReleasedException e) {
			fail("Unexpected exception thrown");
		}
		
		assertEquals("Cafe Music BGM released <Jazz Piano Radio> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch1.toString());
		assertEquals("I Love You Venice released <Baroque Live Music 24/7> and is followed by [Monitor Stat Sensor A, Subscriber Mark, Subscriber Alan].", ch2.toString());
		assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub1.toString());
		assertEquals("Subscriber Mark follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub2.toString());
		assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM, I Love You Venice].", mon1.toString()); 
		
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
		try {
			sub1.watch("Jazz Piano Radio", 20);
			/* 
			 * Statistics for the watched video is updated for `mon1`.
			 * For the average watch time, display the value with two digits after the decimal point. 
			 */ 
			assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM {#views: 1, max watch time: 20, avg watch time: 20.00}, I Love You Venice].", mon1.toString());
			/* All other channels and subscribers remain unchanged. */
			assertEquals("Cafe Music BGM released <Jazz Piano Radio> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch1.toString());
			assertEquals("I Love You Venice released <Baroque Live Music 24/7> and is followed by [Monitor Stat Sensor A, Subscriber Mark, Subscriber Alan].", ch2.toString());
			assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub1.toString());
			assertEquals("Subscriber Mark follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub2.toString());
			
			/*
			 * Subscriber `sub2` watched Jazz Piano Radio for 30 minutes. 
			 */
			sub2.watch("Jazz Piano Radio", 30);
			/* 
			 * Statistics for the watched video is updated for `mon1`.
			 */
			assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM {#views: 2, max watch time: 30, avg watch time: 25.00}, I Love You Venice].", mon1.toString());
			/* All other channels and subscribers remain unchanged. */
			assertEquals("Cafe Music BGM released <Jazz Piano Radio> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch1.toString());
			assertEquals("I Love You Venice released <Baroque Live Music 24/7> and is followed by [Monitor Stat Sensor A, Subscriber Mark, Subscriber Alan].", ch2.toString());
			assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub1.toString());
			assertEquals("Subscriber Mark follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub2.toString());
			
			/*
			 * Subscriber `sub1` watched Jazz Piano Radio again for 15 minutes. 
			 */
			sub1.watch("Jazz Piano Radio", 15);
			/* 
			 * Statistics for the watched video is updated for `mon1`.
			 */
			assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM {#views: 3, max watch time: 30, avg watch time: 21.67}, I Love You Venice].", mon1.toString());
			/* All other channels and subscribers remain unchanged. */
			assertEquals("Cafe Music BGM released <Jazz Piano Radio> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch1.toString());
			assertEquals("I Love You Venice released <Baroque Live Music 24/7> and is followed by [Monitor Stat Sensor A, Subscriber Mark, Subscriber Alan].", ch2.toString());
			assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub1.toString());
			assertEquals("Subscriber Mark follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub2.toString());
			
			/*
			 * Subscriber `sub2` watched Baroque Live Music 24/7 for 11 minutes. 
			 */
			sub2.watch("Baroque Live Music 24/7", 11);
			/* 
			 * Statistics for the watched video is updated for `mon1`.
			 */
			assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM {#views: 3, max watch time: 30, avg watch time: 21.67}, I Love You Venice {#views: 1, max watch time: 11, avg watch time: 11.00}].", mon1.toString());
			/* All other channels and subscribers remain unchanged. */
			assertEquals("Cafe Music BGM released <Jazz Piano Radio> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch1.toString());
			assertEquals("I Love You Venice released <Baroque Live Music 24/7> and is followed by [Monitor Stat Sensor A, Subscriber Mark, Subscriber Alan].", ch2.toString());
			assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub1.toString());
			assertEquals("Subscriber Mark follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub2.toString());
			/*
			 * Subscriber `sub1` watched Baroque Live Music 24/7 for 8 minutes. 
			 */
			sub1.watch("Baroque Live Music 24/7", 8);
			/* 
			 * Statistics for the watched video is updated for `mon1`.
			 */
			assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM {#views: 3, max watch time: 30, avg watch time: 21.67}, I Love You Venice {#views: 2, max watch time: 11, avg watch time: 9.50}].", mon1.toString());
			/* All other channels and subscribers remain unchanged. */
			assertEquals("Cafe Music BGM released <Jazz Piano Radio> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch1.toString());
			assertEquals("I Love You Venice released <Baroque Live Music 24/7> and is followed by [Monitor Stat Sensor A, Subscriber Mark, Subscriber Alan].", ch2.toString());
			assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub1.toString());
			assertEquals("Subscriber Mark follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub2.toString());
			
			/*
			 * Subscriber `sub2` watched Baroque Live Music 24/7 again for 18 minutes. 
			 */
			sub2.watch("Baroque Live Music 24/7", 18);
			/* 
			 * Statistics for the watched video is updated for `mon1`.
			 */
			assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM {#views: 3, max watch time: 30, avg watch time: 21.67}, I Love You Venice {#views: 3, max watch time: 18, avg watch time: 12.33}].", mon1.toString());
			/* All other channels and subscribers remain unchanged. */
			assertEquals("Cafe Music BGM released <Jazz Piano Radio> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch1.toString());
			assertEquals("I Love You Venice released <Baroque Live Music 24/7> and is followed by [Monitor Stat Sensor A, Subscriber Mark, Subscriber Alan].", ch2.toString());
			assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub1.toString());
			assertEquals("Subscriber Mark follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub2.toString());
		}
		catch (VideoNotRecommendedException e) {
			fail("Unexpected exception thrown");
		}
		
		
	}
	
	@Test
	public void test_channel_03b() { 
		Channel ch1 = new Channel("Cafe Music BGM", 50, 100);
		Channel ch2 = new Channel("I Love You Venice", 60, 135);
		
		Subscriber sub1 = new Subscriber("Alan", 20, 40); 
		Subscriber sub2 = new Subscriber("Mark", 20, 40);
		Monitor mon1 = new Monitor("Stat Sensor A", 30);
		
		try {
			ch1.follow(sub1); 
			ch1.follow(mon1);
			ch1.follow(sub2);
			
			ch2.follow(mon1);
			ch2.follow(sub2);
			ch2.follow(sub1);
		}
		catch (AlreadyFollowedException e) {
			fail("Unexpected exception thrown");
		}
		
		
		try {
			ch1.releaseANewVideo("Jazz Piano Radio");
			ch2.releaseANewVideo("Baroque Live Music 24/7");
		}
		catch (VideoAlreadyReleasedException e) {
			fail("Unexpected exception thrown");
		}
		
		assertEquals("Cafe Music BGM released <Jazz Piano Radio> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch1.toString());
		assertEquals("I Love You Venice released <Baroque Live Music 24/7> and is followed by [Monitor Stat Sensor A, Subscriber Mark, Subscriber Alan].", ch2.toString());
		assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub1.toString());
		assertEquals("Subscriber Mark follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub2.toString());
		assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM, I Love You Venice].", mon1.toString()); 
		
		/* 
		 * Subscriber `sub1` watched Jazz Piano Radio for 40 minutes. 
		 */
		try {
			sub1.watch("Jazz Piano Radio", 40);
		}
		catch (VideoNotRecommendedException e) {
			fail("Unexpected exception thrown");
		}
		
		
		/* 
		 * Statistics for the watched video is updated for `mon1`.
		 * For the average watch time, display with two digits after the decimal point. 
		 */ 
		assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM {#views: 1, max watch time: 40, avg watch time: 40.00}, I Love You Venice].", mon1.toString());
		/* All other channels and subscribers remain unchanged. */
		assertEquals("Cafe Music BGM released <Jazz Piano Radio> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch1.toString());
		assertEquals("I Love You Venice released <Baroque Live Music 24/7> and is followed by [Monitor Stat Sensor A, Subscriber Mark, Subscriber Alan].", ch2.toString());
		assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub1.toString());
		assertEquals("Subscriber Mark follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub2.toString());
		
		Monitor mon2 = new Monitor("Stat Sensor B", 30);
		assertEquals("Monitor Stat Sensor B follows no channels.", mon2.toString());
		
		/*
		 * Let `mon2` start following `ch1`, meaning that
		 * 	its statistics only covers the watch times happening from now on.
		 */
		try {
			ch1.follow(mon2);
			assertEquals("Monitor Stat Sensor B follows [Cafe Music BGM].", mon2.toString());
			assertEquals("Cafe Music BGM released <Jazz Piano Radio> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark, Monitor Stat Sensor B].", ch1.toString());
		}
		catch (AlreadyFollowedException e) {
			fail("Unexpected exception thrown");
		}
		
		
		/*
		 * Subscriber `sub2` watched Jazz Piano Radio for 30 minutes. 
		 */
		
		try {
			sub2.watch("Jazz Piano Radio", 30);
		}
		catch (VideoNotRecommendedException e) {
			fail("Unexpected exception thrown");
		}
		

		/* 
		 * Statistics for the watched video is updated for `mon1` and `mon2`.
		 */
		assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM {#views: 2, max watch time: 40, avg watch time: 35.00}, I Love You Venice].", mon1.toString());
		assertEquals("Monitor Stat Sensor B follows [Cafe Music BGM {#views: 1, max watch time: 30, avg watch time: 30.00}].", mon2.toString());
		/* All other channels and subscribers remain unchanged. */
		assertEquals("Cafe Music BGM released <Jazz Piano Radio> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark, Monitor Stat Sensor B].", ch1.toString());
		assertEquals("I Love You Venice released <Baroque Live Music 24/7> and is followed by [Monitor Stat Sensor A, Subscriber Mark, Subscriber Alan].", ch2.toString());
		assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub1.toString());
		assertEquals("Subscriber Mark follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub2.toString());
		
		/*
		 * Subscriber `sub1` watched Jazz Piano Radio again for 15 minutes. 
		 */
		try {
			sub2.watch("Jazz Piano Radio", 15);
		}
		catch (VideoNotRecommendedException e) {
			fail("Unexpected exception thrown");
		}
		
		
		/* 
		 * Statistics for the watched video is updated for `mon1` and `mon2`.
		 */
		assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM {#views: 3, max watch time: 40, avg watch time: 28.33}, I Love You Venice].", mon1.toString());
		assertEquals("Monitor Stat Sensor B follows [Cafe Music BGM {#views: 2, max watch time: 30, avg watch time: 22.50}].", mon2.toString());
		/* All other channels and subscribers remain unchanged. */
		assertEquals("Cafe Music BGM released <Jazz Piano Radio> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark, Monitor Stat Sensor B].", ch1.toString());
		assertEquals("I Love You Venice released <Baroque Live Music 24/7> and is followed by [Monitor Stat Sensor A, Subscriber Mark, Subscriber Alan].", ch2.toString());
		assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub1.toString());
		assertEquals("Subscriber Mark follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub2.toString());
	}
	
	@Test
	public void test_channel_03c() { 
		Channel ch1 = new Channel("Cafe Music BGM", 50, 100);
		Channel ch2 = new Channel("I Love You Venice", 60, 135);
		
		Subscriber sub1 = new Subscriber("Alan", 20, 40); 
		Subscriber sub2 = new Subscriber("Mark", 20, 40);
		Monitor mon1 = new Monitor("Stat Sensor A", 30);
		
		try {
			ch1.follow(sub1); 
			ch1.follow(mon1);
			ch1.follow(sub2);
			
			ch2.follow(mon1);
			ch2.follow(sub2);
			ch2.follow(sub1);
		}
		catch (AlreadyFollowedException e) {
			fail("Unexpected exception thrown");
		}
		
		
		try {
			ch1.releaseANewVideo("Jazz Piano Radio");
			ch1.releaseANewVideo("Jazz Piano Radio");
			fail("Expected exception not thrown");
		}
		catch (VideoAlreadyReleasedException e) {
			// Expected
		}
	}
	
	@Test
	public void test_channel_03d() { 
		Channel ch1 = new Channel("Cafe Music BGM", 50, 100);
		Channel ch2 = new Channel("I Love You Venice", 60, 135);
		
		Subscriber sub1 = new Subscriber("Alan", 20, 40); 
		Subscriber sub2 = new Subscriber("Mark", 20, 40);
		Monitor mon1 = new Monitor("Stat Sensor A", 30);
		
		try {
			ch1.follow(sub1); 
			ch1.follow(mon1);
			ch1.follow(sub2);
			
			ch2.follow(mon1);
			ch2.follow(sub2);
			ch2.follow(sub1);
		}
		catch (AlreadyFollowedException e) {
			fail("Unexpected exception thrown");
		}
		
		
		try {
			ch1.releaseANewVideo("Jazz Piano Radio");
			ch2.releaseANewVideo("Baroque Live Music 24/7");
		}
		catch (VideoAlreadyReleasedException e) {
			fail("Unexpected exception thrown");
		}
		
		assertEquals("Cafe Music BGM released <Jazz Piano Radio> and is followed by [Subscriber Alan, Monitor Stat Sensor A, Subscriber Mark].", ch1.toString());
		assertEquals("I Love You Venice released <Baroque Live Music 24/7> and is followed by [Monitor Stat Sensor A, Subscriber Mark, Subscriber Alan].", ch2.toString());
		assertEquals("Subscriber Alan follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub1.toString());
		assertEquals("Subscriber Mark follows [Cafe Music BGM, I Love You Venice] and is recommended <Jazz Piano Radio, Baroque Live Music 24/7>.", sub2.toString());
		assertEquals("Monitor Stat Sensor A follows [Cafe Music BGM, I Love You Venice].", mon1.toString()); 
		
		/* 
		 * Subscriber `sub1` watched Jazz Piano Radio for 40 minutes. 
		 */
		try {
			sub1.watch("Pop Hits 100", 40);
			fail("Expected exception not thrown");
		}
		catch (VideoNotRecommendedException e) {
			// Expected
		}
		
	}
}
