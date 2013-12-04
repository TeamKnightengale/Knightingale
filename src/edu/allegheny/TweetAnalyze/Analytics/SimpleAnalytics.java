package edu.allegheny.TweetAnalyze.Analytics;

import edu.allegheny.TweetAnalyze.*;
import edu.allegheny.TweetAnalyze.Database.DatabaseHelper;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Provides simple analytic methods for the TweetAnalyze database.
 *
 * @author	Hawk Weisman
 * @author  Ian McMillan
 * @author  Dibyo Mukherjee
 * @version	0.1
 * @since	December 2, 2013
 */
public class SimpleAnalytics {
	
	public static void main(String argv[]) throws Exception
	{
		List<Tweet> tweets = search("HackMIT");
		for (Tweet t : tweets)
		{
			System.out.println(t.getText());
		}
	}

	/**
	 * @param searchTerm a String
	 * @return a List of tweets containing the specified string.
	 */
	public static List<Tweet> search (String searchTerm) throws SQLException, ParseException, ClassNotFoundException
	{
		
		List<Tweet> searchResults = new ArrayList<Tweet>();
		String searchQuery 	=  "SELECT * FROM tweets WHERE text LIKE '%" + searchTerm + "%'";
		searchResults = TweetBuilder.buildTweetFromResultSet(DatabaseHelper.execute(searchQuery));
		return searchResults;
	}

	/**
	 * @return the percentage of retweets in the user's database.
	 */
	public static int percentRetweets () {
		int numTweets = 0; 
		int numRetweets = 0;
		String numTweetsQuery 	=  "SELECT COUNT(*) FROM tweets";
		String numRetweetsQuery =  "SELECT COUNT(*) FROM tweets WHERE retweets_id IS NOT NULL";

		// some magic happens

		return numTweets/numRetweets;
	}

	/**
	 * @return a List containing all tweets in the user's database that contain hyperlinks.
	 */
	public static List<Tweet> tweetsWithHyperlinks () {
		List<Tweet> tweetsWithHyperlinks = new ArrayList<Tweet>();;
		String hyperlinksQuery =   "SELECT * FROM tweets WHERE id IN (SELECT DISTINCT tweet_id FROM expanded_urls";
		// db access layer takes place

		return tweetsWithHyperlinks;
	}

	/**
	 * @return the percentage of replied tweets
	 */
	public static int repliedTweets () {
		int numTweets = 0 ;
		int numReplied = 0;
		String numTweetsQuery = "SELECT COUNT(*)FROM Tweets";
		String numRepliedQuery = "SELECT COUNT(*)FROM Tweets WHERE in_reply_to_status_id IS NOT NULL AND" + 
									"in_reply_to_user_id IS NOT NULL";


		return numTweets/numReplied;
	}

	/**
	 * @return a List containg all users that have been replied to
	 */
	public static List<Tweet> repliedToUsers () {
		List<Tweet> repliedToUsers = new ArrayList<Tweet>();;
		String repliedToUsersQuery = "SELECT DISTINCT in_reply_to_user_id FROM Tweets";

		return repliedToUsers;
	}

	/**
	 * @return list of tweets in october
	 */
	public static List<Tweet> tweetsInOctober (){
		List<Tweet> tweetsInOctober = new ArrayList<Tweet>();;
		String tweetsInOctoberQuery = "SELECT * FROM Tweets WHERE timestamp " + 
									"BETWEEN '2013-09-30 23:59:59 PST' AND '2013-11-01 00:00:01 PST'";

	return tweetsInOctober; 
	}

	/**
	 * @return list of tweets with hashtags(#)
	 */
	public static List<Tweet> tweetsWithHashtag (){
		List<Tweet> tweetsWithHashtag = new ArrayList<Tweet>();;
		String tweetsWithHashtagQuery = "SELECT * FROM Tweets WHERE text LIKE '#%'";

		return tweetsWithHashtag;
	}

}
