package edu.allegheny.tweetanalyze.test;

import edu.allegheny.tweetanalyze.TweetBuilder;
import edu.allegheny.tweetanalyze.Tweet;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.Date;

import twitter4j.*;

//import static org.Mockito.*;

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
/* COMMENTED OUT BECAUSE MOCKITO IS BROKEN

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
			when(mockStatus.getInReplyToUserId()).thenReturn(-1);
			when(mockStatus.getExpandedURLs()).thenReturn(new ArrayList());
			when(mockStatus.getCreatedAt()).thenReturn(expectedTimestamp);
			when(mockStatus.getId()).thenReturn(expectedID);
			when(mockStatus.getText()).thenReturn(expectedText);
			when(mockStatus.getSource()).then($spReturn(expectedSource)); 	// as a matter of fact I am mocking you

			tweetUnderTest = TweetBuilder.buildTweet(mockStatus);

			assertNotNull(tweetUnderTest);
			assertEqual(tweetUnderTest.getTweetID(), expectedID);
			assertEqual(tweetUnderTest.getTimestamp(), expectedTimestamp);
		}
	}

	private static Date randomTimestamp(Random generator) {
		long unixtime = (long) (1293861599+generator.nextDouble()*60*60*24*365);
		return new Date (unixtime);
	}
	*/
}