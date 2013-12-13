package edu.allegheny.tweetanalyze.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import edu.allegheny.tweetanalyze.TweetBuilder;
import edu.allegheny.tweetanalyze.Tweet;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.Date;

import twitter4j.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link edu.allegheny.datagen.ChangePair}
 *
 * @author Hawk Weisman
 */

@RunWith(JUnit4.class)
public class TestTweetBuilder {

	Random generator = new Random();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void instantiationCount() {
		assertNotNull(new TweetBuilder());
	}

	@Test
	public void testTweetFromStatus() {
		long 	expectedID;
		String 	expectedText;
		String	expectedSource;
		Date 	expectedTimestamp;

		Tweet 	tweetUnderTest;

		for (int i = 0; i < 100; i++) {	// do it 100 times with random values in order
			expectedID = generator.nextLong();
			expectedText = generator.nextLong() + " " + generator.nextLong() + " " + generator.nextLong();
			expectedSource = generator.nextLong() + " " + generator.nextLong() + " " + generator.nextLong();
			expectedTimestamp = randomTimestamp(generator);

			Status mockStatus = mock(Status.class); 					// are you mocking me?
			when(mockStatus.isRetweet()).thenReturn(false);				// (ugh, this test is gonna be slllloooooww)
			when(mockStatus.getInReplyToUserId()).thenReturn(new Long(0));
			when(mockStatus.getURLEntities()).thenReturn(new URLEntity[0]);
			when(mockStatus.getCreatedAt()).thenReturn(expectedTimestamp);
			when(mockStatus.getId()).thenReturn(expectedID);
			when(mockStatus.getText()).thenReturn(expectedText);
			when(mockStatus.getSource()).thenReturn(expectedSource); 	// as a matter of fact I am mocking you

			tweetUnderTest = TweetBuilder.buildTweet(mockStatus);

			assertNotNull(tweetUnderTest);
			assertEquals(tweetUnderTest.getTweetID(), expectedID);
			assertSame(tweetUnderTest.getTimestamp(), expectedTimestamp);
			assertFalse("FAIL: TweetBuilder built a reply froma tweet that was not a reply", tweetUnderTest.isReply());
			assertFalse("FAIL: TweetBuilder built a retweet from a tweet that was not a retweet", tweetUnderTest.isRetweet());
		}
	}

	@Test
	public void testTweetFromStatusWithURLs() {
		long 	expectedID;
		String 	expectedText;
		String	expectedSource;
		Date 	expectedTimestamp;
		URLEntity[] expectedURLEntities = new URLEntity[2];

		URLEntity mockURLOne = mock(URLEntity.class);
		URLEntity mockURLTwo = mock(URLEntity.class);
		when(mockURLOne.getExpandedURL()).thenReturn("http://twitter4j.org/javadoc/twitter4j/URLEntity.html");
		when(mockURLTwo.getExpandedURL()).thenReturn("https://code.google.com/p/mockito/");

		expectedURLEntities[0] = mockURLOne;
		expectedURLEntities[1] = mockURLTwo;

		Tweet 	tweetUnderTest;

		for (int i = 0; i < 100; i++) {	// do it 100 times with random values in order
			expectedID = generator.nextLong();
			expectedText = generator.nextLong() + " " + generator.nextLong() + " " + generator.nextLong();
			expectedSource = generator.nextLong() + " " + generator.nextLong() + " " + generator.nextLong();
			expectedTimestamp = randomTimestamp(generator);

			Status mockStatus = mock(Status.class); 					// are you mocking me?
			when(mockStatus.isRetweet()).thenReturn(false);				// (ugh, this test is gonna be slllloooooww)
			when(mockStatus.getInReplyToUserId()).thenReturn(new Long(0));
			when(mockStatus.getURLEntities()).thenReturn(expectedURLEntities);
			when(mockStatus.getCreatedAt()).thenReturn(expectedTimestamp);
			when(mockStatus.getId()).thenReturn(expectedID);
			when(mockStatus.getText()).thenReturn(expectedText);
			when(mockStatus.getSource()).thenReturn(expectedSource); 	// as a matter of fact I am mocking you

			tweetUnderTest = TweetBuilder.buildTweet(mockStatus);

			assertNotNull(tweetUnderTest);
			assertEquals(tweetUnderTest.getTweetID(), expectedID);
			assertSame(tweetUnderTest.getTimestamp(), expectedTimestamp);
			assertEquals(tweetUnderTest.getExpandedURLs(), new ArrayList<String>(Arrays.asList("http://twitter4j.org/javadoc/twitter4j/URLEntity.html","https://code.google.com/p/mockito/")));
			assertFalse("FAIL: TweetBuilder built a reply froma tweet that was not a reply", tweetUnderTest.isReply());
			assertFalse("FAIL: TweetBuilder built a retweet from a tweet that was not a retweet", tweetUnderTest.isRetweet());
		}
	}

	@Test
	public void testRetweetFromStatusWithURLs() {
		long 	expectedID;
		long 	expectedRetweetedUserID;
		long 	expectedRetweetedStatusID;
		String 	expectedText;
		String	expectedSource;
		Date 	expectedTimestamp;
		Date 	expectedRetweetTimestamp;
		Status 	mockRetweetedStatus = mock(Status.class);
		User 	mockRetweetedUser = mock(User.class);
		URLEntity[] expectedURLEntities = new URLEntity[2];

		URLEntity mockURLOne = mock(URLEntity.class);
		URLEntity mockURLTwo = mock(URLEntity.class);
		when(mockURLOne.getExpandedURL()).thenReturn("http://twitter4j.org/javadoc/twitter4j/URLEntity.html");
		when(mockURLTwo.getExpandedURL()).thenReturn("https://code.google.com/p/mockito/");

		expectedURLEntities[0] = mockURLOne;
		expectedURLEntities[1] = mockURLTwo;

		Tweet 	tweetUnderTest;

		for (int i = 0; i < 100; i++) {	// do it 100 times with random values in order
			expectedID = generator.nextLong();
			expectedText = generator.nextLong() + " " + generator.nextLong() + " " + generator.nextLong();
			expectedSource = generator.nextLong() + " " + generator.nextLong() + " " + generator.nextLong();
			expectedTimestamp = randomTimestamp(generator);
			expectedRetweetTimestamp = randomTimestamp(generator);
			expectedRetweetedUserID = generator.nextLong();
			expectedRetweetedStatusID = generator.nextLong();

			Status mockStatus = mock(Status.class); 					// are you mocking me?
			when(mockStatus.isRetweet()).thenReturn(true);				// (ugh, this test is gonna be slllloooooww)
			when(mockStatus.getInReplyToUserId()).thenReturn(new Long(-1));
			when(mockStatus.getURLEntities()).thenReturn(expectedURLEntities);
			when(mockStatus.getCreatedAt()).thenReturn(expectedTimestamp);
			when(mockStatus.getId()).thenReturn(expectedID);
			when(mockStatus.getText()).thenReturn(expectedText);
			when(mockStatus.getRetweetedStatus()).thenReturn(mockRetweetedStatus);
			when(mockRetweetedStatus.getId()).thenReturn(expectedRetweetedStatusID);
			when(mockRetweetedStatus.getCreatedAt()).thenReturn(expectedRetweetTimestamp);
			when(mockRetweetedStatus.getUser()).thenReturn(mockRetweetedUser);
			when(mockRetweetedUser.getId()).thenReturn(expectedRetweetedUserID);
			when(mockStatus.getSource()).thenReturn(expectedSource); 	// as a matter of fact I am mocking you

			tweetUnderTest = TweetBuilder.buildTweet(mockStatus);

			assertNotNull(tweetUnderTest);
			assertEquals(tweetUnderTest.getTweetID(), expectedID);
			assertSame(tweetUnderTest.getTimestamp(), expectedTimestamp);
			assertSame(tweetUnderTest.getRetweetedStatusTimestamp(), expectedRetweetTimestamp);
			assertEquals(tweetUnderTest.getExpandedURLs(), new ArrayList<String>(Arrays.asList("http://twitter4j.org/javadoc/twitter4j/URLEntity.html","https://code.google.com/p/mockito/")));
			assertFalse("FAIL: TweetBuilder built a reply froma tweet that was not a reply", tweetUnderTest.isReply());
			assertTrue("FAIL: TweetBuilder built a non-retweet tweet from a retweet that was not a retweet", tweetUnderTest.isRetweet());
			assertEquals(tweetUnderTest.getRetweetedUserID(), expectedRetweetedUserID);
			assertEquals(tweetUnderTest.getRetweetedStatusID(), expectedRetweetedStatusID);
		}
	}

	public void testReplyFromStatusWithURLs() {
		long 	expectedID;
		long 	expectedRepliedUserID;
		long 	expectedRepliedStatusID;
		String 	expectedText;
		String	expectedSource;
		Date 	expectedTimestamp;
		URLEntity[] expectedURLEntities = new URLEntity[2];

		URLEntity mockURLOne = mock(URLEntity.class);
		URLEntity mockURLTwo = mock(URLEntity.class);
		when(mockURLOne.getExpandedURL()).thenReturn("http://twitter4j.org/javadoc/twitter4j/URLEntity.html");
		when(mockURLTwo.getExpandedURL()).thenReturn("https://code.google.com/p/mockito/");

		expectedURLEntities[0] = mockURLOne;
		expectedURLEntities[1] = mockURLTwo;

		Tweet 	tweetUnderTest;

		for (int i = 0; i < 100; i++) {	// do it 100 times with random values in order
			expectedID = generator.nextLong();
			expectedText = generator.nextLong() + " " + generator.nextLong() + " " + generator.nextLong();
			expectedSource = generator.nextLong() + " " + generator.nextLong() + " " + generator.nextLong();
			expectedTimestamp = randomTimestamp(generator);
			expectedRepliedUserID = generator.nextLong();
			expectedRepliedStatusID = generator.nextLong();

			Status mockStatus = mock(Status.class); 					// are you mocking me?
			when(mockStatus.isRetweet()).thenReturn(false);				// (ugh, this test is gonna be slllloooooww)
			when(mockStatus.getInReplyToStatusId()).thenReturn(expectedRepliedStatusID);
			when(mockStatus.getInReplyToUserId()).thenReturn(expectedRepliedUserID);
			when(mockStatus.getURLEntities()).thenReturn(expectedURLEntities);
			when(mockStatus.getCreatedAt()).thenReturn(expectedTimestamp);
			when(mockStatus.getId()).thenReturn(expectedID);
			when(mockStatus.getText()).thenReturn(expectedText);
			when(mockStatus.getSource()).thenReturn(expectedSource); 	// as a matter of fact I am mocking you

			tweetUnderTest = TweetBuilder.buildTweet(mockStatus);

			assertNotNull(tweetUnderTest);
			assertEquals(tweetUnderTest.getTweetID(), expectedID);
			assertSame(tweetUnderTest.getTimestamp(), expectedTimestamp);
			assertEquals(tweetUnderTest.getExpandedURLs(), new ArrayList<String>(Arrays.asList("http://twitter4j.org/javadoc/twitter4j/URLEntity.html","https://code.google.com/p/mockito/")));
			assertTrue("FAIL: TweetBuilder built a reply froma tweet that was not a reply", tweetUnderTest.isReply());
			assertFalse("FAIL: TweetBuilder built a retweet from status that is a retweet", tweetUnderTest.isRetweet());
			assertEquals(tweetUnderTest.getInReplyToUserID(), expectedRepliedUserID);
		}
	}

	@Test
	public void testRetweetFromStatus() {
		long 	expectedID;
		long 	expectedRetweetedUserID;
		long 	expectedRetweetedStatusID;
		String 	expectedText;
		String	expectedSource;
		Date 	expectedTimestamp;
		Date 	expectedRetweetTimestamp;
		Status 	mockRetweetedStatus = mock(Status.class);
		User 	mockRetweetedUser = mock(User.class);

		Tweet 	tweetUnderTest;

		for (int i = 0; i < 100; i++) {	// do it 100 times with random values in order
			expectedID = generator.nextLong();
			expectedText = generator.nextLong() + " " + generator.nextLong() + " " + generator.nextLong();
			expectedSource = generator.nextLong() + " " + generator.nextLong() + " " + generator.nextLong();
			expectedTimestamp = randomTimestamp(generator);
			expectedRetweetTimestamp = randomTimestamp(generator);
			expectedRetweetedUserID = generator.nextLong();
			expectedRetweetedStatusID = generator.nextLong();

			Status mockStatus = mock(Status.class); 					// are you mocking me?
			when(mockStatus.isRetweet()).thenReturn(true);				// (ugh, this test is gonna be slllloooooww)
			when(mockStatus.getInReplyToUserId()).thenReturn(new Long(-1));
			when(mockStatus.getCreatedAt()).thenReturn(expectedTimestamp);
			when(mockStatus.getId()).thenReturn(expectedID);
			when(mockStatus.getText()).thenReturn(expectedText);
			when(mockStatus.getRetweetedStatus()).thenReturn(mockRetweetedStatus);
			when(mockStatus.getURLEntities()).thenReturn(new URLEntity[0]);
			when(mockRetweetedStatus.getId()).thenReturn(expectedRetweetedStatusID);
			when(mockRetweetedStatus.getCreatedAt()).thenReturn(expectedRetweetTimestamp);
			when(mockRetweetedStatus.getUser()).thenReturn(mockRetweetedUser);
			when(mockRetweetedUser.getId()).thenReturn(expectedRetweetedUserID);
			when(mockStatus.getSource()).thenReturn(expectedSource); 	// as a matter of fact I am mocking you

			tweetUnderTest = TweetBuilder.buildTweet(mockStatus);

			assertNotNull(tweetUnderTest);
			assertEquals(tweetUnderTest.getTweetID(), expectedID);
			assertSame(tweetUnderTest.getTimestamp(), expectedTimestamp);
			assertSame(tweetUnderTest.getRetweetedStatusTimestamp(), expectedRetweetTimestamp);
			assertFalse("FAIL: TweetBuilder built a reply froma tweet that was not a reply", tweetUnderTest.isReply());
			assertTrue("FAIL: TweetBuilder built a non-retweet tweet from a retweet that was not a retweet", tweetUnderTest.isRetweet());
			assertEquals(tweetUnderTest.getRetweetedUserID(), expectedRetweetedUserID);
			assertEquals(tweetUnderTest.getRetweetedStatusID(), expectedRetweetedStatusID);
		}
	}

	@Test
	public void testReplyFromStatus() {
		long 	expectedID;
		long 	expectedRepliedUserID;
		long 	expectedRepliedStatusID;
		String 	expectedText;
		String	expectedSource;
		Date 	expectedTimestamp;

		Tweet 	tweetUnderTest;

		for (int i = 0; i < 100; i++) {	// do it 100 times with random values in order
			expectedID = generator.nextLong();
			expectedText = generator.nextLong() + " " + generator.nextLong() + " " + generator.nextLong();
			expectedSource = generator.nextLong() + " " + generator.nextLong() + " " + generator.nextLong();
			expectedTimestamp = randomTimestamp(generator);
			expectedRepliedUserID = generator.nextLong();
			expectedRepliedStatusID = generator.nextLong();

			Status mockStatus = mock(Status.class); 					// are you mocking me?
			when(mockStatus.isRetweet()).thenReturn(false);				// (ugh, this test is gonna be slllloooooww)
			when(mockStatus.getInReplyToStatusId()).thenReturn(expectedRepliedStatusID);
			when(mockStatus.getInReplyToUserId()).thenReturn(expectedRepliedUserID);
			when(mockStatus.getCreatedAt()).thenReturn(expectedTimestamp);
			when(mockStatus.getURLEntities()).thenReturn(new URLEntity[0]);
			when(mockStatus.getId()).thenReturn(expectedID);
			when(mockStatus.getText()).thenReturn(expectedText);
			when(mockStatus.getSource()).thenReturn(expectedSource); 	// as a matter of fact I am mocking you

			tweetUnderTest = TweetBuilder.buildTweet(mockStatus);

			assertNotNull(tweetUnderTest);
			assertEquals(tweetUnderTest.getTweetID(), expectedID);
			assertSame(tweetUnderTest.getTimestamp(), expectedTimestamp);
			assertTrue("FAIL: TweetBuilder built a reply froma tweet that was not a reply", tweetUnderTest.isReply());
			assertFalse("FAIL: TweetBuilder built a retweet from status that is a retweet", tweetUnderTest.isRetweet());
			assertEquals(tweetUnderTest.getInReplyToUserID(), expectedRepliedUserID);
		}
	}

	private static Date randomTimestamp(Random generator) {
		long unixtime = (long) (1293861599+generator.nextDouble()*60*60*24*365);
		return new Date (unixtime);
	}
}