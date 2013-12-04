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
 * @version	1.0
 * @since	December 4, 2013
 */
public class SimpleAnalytics {
	
	public static void main(String argv[]) throws Exception
	{
		List<Tweet> tweets = search(argv[0]);
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
	public static int percentRetweets() throws SQLException, ParseException {
		int numTweets = 0; 
		int numRetweets = 0;
		String numTweetsQuery 	=  "SELECT COUNT(*) FROM tweets";
		String numRetweetsQuery =  "SELECT COUNT(*) FROM tweets WHERE retweets_id IS NOT NULL";

		numTweets = DatabaseHelper.execute(numTweetsQuery).getInt(1);
		numRetweets = DatabaseHelper.execute(numRetweetsQuery).getInt(1);

		return numTweets/numRetweets * 100;
	}

	/**
	 * @return a List containing all tweets in the user's database that contain hyperlinks.
	 */
	public static List<Tweet> tweetsWithHyperlinks () throws SQLException, ParseException {

		List<Tweet> tweetsWithHyperlinks = new ArrayList<Tweet>();;
		String hyperlinksQuery = "SELECT * FROM tweets WHERE id IN (SELECT DISTINCT tweet_id FROM expanded_urls";

		tweetsWithHyperlinks = TweetBuilder.buildTweetFromResultSet(DatabaseHelper.execute(hyperlinksQuery));	                                      

		return tweetsWithHyperlinks;
	}

	/**
	 * @return the percentage of replied tweets
	 */
	public static int percentReplies () throws SQLException, ParseException {
		int numTweets = 0 ;
		int numReplied = 0;
		String numTweetsQuery = "SELECT COUNT(*)FROM Tweets";
		String numRepliedQuery = "SELECT COUNT(*)FROM Tweets WHERE in_reply_to_status_id IS NOT NULL AND" + 
									"in_reply_to_user_id IS NOT NULL";

		numTweets = DatabaseHelper.execute(numTweetsQuery).getInt(1);
		numReplied = DatabaseHelper.execute(numRepliedQuery).getInt(1);

		return numTweets/numReplied * 100;
	}

	/**
	 * @return a List containg all users that have been replied to
	 */
	public static List<Tweet> repliedToUsers () throws SQLException, ParseException {
		List<Tweet> repliedToUsers = new ArrayList<Tweet>();;
		String repliedToUsersQuery = "SELECT DISTINCT in_reply_to_user_id FROM Tweets";

		repliedToUsers = TweetBuilder.buildTweetFromResultSet(DatabaseHelper.execute(repliedToUsersQuery));	  

		return repliedToUsers;
	}

	/**
	 * @return list of tweets in october
	 */
	public static List<Tweet> tweetsInOctober () throws SQLException, ParseException {
		List<Tweet> tweetsInOctober = new ArrayList<Tweet>();;
		String tweetsInOctoberQuery = "SELECT * FROM Tweets WHERE timestamp " + 
									"BETWEEN '2013-09-30 23:59:59 PST' AND '2013-11-01 00:00:01 PST'";

		tweetsInOctober = TweetBuilder.buildTweetFromResultSet(DatabaseHelper.execute(tweetsInOctoberQuery));	 

		return tweetsInOctober; 
	}

	/**
	 * @return list of tweets with hashtags(#)
	 */
	public static List<Tweet> tweetsWithHashtag () throws SQLException, ParseException {
		List<Tweet> tweetsWithHashtag = new ArrayList<Tweet>();;
		String tweetsWithHashtagQuery = "SELECT * FROM Tweets WHERE text LIKE '#%'";

		tweetsWithHashtag = TweetBuilder.buildTweetFromResultSet(DatabaseHelper.execute(tweetsWithHashtagQuery));	

		return tweetsWithHashtag;
	}

}
