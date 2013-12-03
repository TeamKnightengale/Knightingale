package edu.allegheny.TweetAnalyze.Analytics;

import edu.allegheny.TweetAnalyze.Tweet;

import java.util.List;

/**
 * Provides simple analytic methods for the TweetAnalyze database.
 *
 * @author	Hawk Weisman
 * @version	0.1
 * @since	December 2, 2013
 */
public class SimpleAnalytics {
	
	/**
	 * @param searchTerm a String
	 * @return a List of tweets containing the specified string.
	 */
	public static List<Tweet> search (String searchTerm) {
		
		List<Tweet> searchResults;

		String searchQuery 	=  "SELECT *
								FROM Tweets
								WHERE something";

		// INSERT DB ACCESS LOGIC HERE

		return searchResults;
	}

	/**
	 * @return the percentage of retweets in the user's database.
	 */
	public static int percentRetweets () {
		int numTweets, numRetweets;
		String numTweetsQuery 	=  "SELECT COUNT(*)
								   	FROM Tweets";
		String numRetweetsQuery =  "SELECT COUNT(*)
								   	FROM Tweets 
								   	WHERE retweets_id IS NOT NULL";

		// some magic happens

		return numTweets/numRetweets;
	}

	/**
	 * @return a List containing all tweets in the user's database that contain hyperlinks.
	 */
	public static List<Tweet> tweetsWithHyperlinks {
		List<Tweet> tweetsWithHyperlinks;
		String hyperlinksQuery =   "SELECT *
									FROM Tweets
									WHERE id IN (SELECT DISTINCT tweet_id FROM expanded_urls";
		// db access layer takes place

		return tweetsWithHyperlinks;
	}

}
