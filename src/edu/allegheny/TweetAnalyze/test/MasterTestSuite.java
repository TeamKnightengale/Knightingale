package edu.allegheny.TweetAnalyze.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for TweetAnalyze, invokes all test cases
 *
 * @author 		Hawk Weisman
 * @version 	0.1
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestTweet.class,
	TestParser.class,
	TestTweetBuilder.class,
	TestAnalytics.class,
})

public class MasterTestSuite {
}
