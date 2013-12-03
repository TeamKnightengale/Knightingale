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


	/**
	 * @return the percentage of replied tweets
	 */
	public static int repliedTweets {
		int numTweets, numReplied;
		String numTweetsQuery = "SELECT COUNT(*)
								FROM Tweets";
		String numRepliedQuery = "SELECT COUNT(*)
								FROM Tweets
								WHERE in_reply_to_status_id IS NOT NULL AND
								WHERE in_reply_to_user_id IS NOT NULL";


		return numTweets/numReplied;
	}

	/**
	 * @return a List containg all users that have been replied to
	 */
	public static List<Tweet> repliedToUsers {
		List<Tweet> repliedToUsers;
		String repliedToUsersQuery = "SELECT DISTINCT in_reply_to_user_id
								FROM Tweets";

		return repliedToUsers;
	}

	/**
	 * @return list of tweets in october
	 */
	public static List<Tweet> tweetsInOctober {
		List<Tweet> tweetsInOctober;
		String tweetsInOctoberQuery = "SELECT * 
								FROM Tweets
								WHERE timestamp 
								BETWEEN '2013-09-30 23:59:59 PST' AND '2013-11-01 00:00:01 PST'";

	return tweetsInOctober; 
	}

	/**
	 * @return list of tweets with hashtags(#)
	 */
	public static List<Tweet> tweetsWithHashtag {
		List<Tweet> tweetsWithHashtag;
		String tweetsWithHashtagQuery = "SELECT * 
								FROM Tweets
								WHERE text LIKE '#%'

		return tweetsWithHashtag;

}
