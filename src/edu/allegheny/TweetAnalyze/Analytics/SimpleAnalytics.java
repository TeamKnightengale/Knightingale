package edu.allegheny.TweetAnalyze.Analytics;

import edu.allegheny.TweetAnalyze.*;
import edu.allegheny.TweetAnalyze.Database.DatabaseHelper;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
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

		List<Long> repliedUsers = repliedToUsers();
		for (Long l : repliedUsers)
		{
			System.out.println(l);
		}

		System.out.printf("\n%d%% of your tweets are retweets, and %d%% are replies.\n", percentRetweets(), percentReplies());
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
		double numTweets = 0, numRetweets = 0;

		String numTweetsQuery 	=  "SELECT COUNT(*) FROM tweets";
		String numRetweetsQuery =  "SELECT COUNT(*) FROM tweets WHERE retweeted_status_id IS NOT 0";

		ResultSet numTweetsResultSet = DatabaseHelper.execute(numTweetsQuery);
		ResultSet numRetweetsResultSet = DatabaseHelper.execute(numRetweetsQuery);

		while (numTweetsResultSet.next()) {
			numTweets = (double)numTweetsResultSet.getInt(1);
		}

		while (numRetweetsResultSet.next()) {
			numRetweets = (double)numRetweetsResultSet.getInt(1);
		}
		
		return (int)((numRetweets/numTweets)*100);
	}

	/**
	 * @return a List containing all tweets in the user's database that contain hyperlinks.
	 * @FIXME: throws java.sql.SQLException: [SQLITE_ERROR] SQL error or missing database (near "expanded_urls": syntax error)
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
		double numTweets = 0, numReplies = 0;

		String numTweetsQuery = "SELECT COUNT(*)FROM Tweets";
		String numRepliesQuery = "SELECT COUNT(*)FROM Tweets WHERE in_reply_to_status_id IS NOT 0 AND" + 
									" in_reply_to_user_id IS NOT 0";

		ResultSet numTweetsResultSet = DatabaseHelper.execute(numTweetsQuery);
		ResultSet numRepliesResultSet = DatabaseHelper.execute(numRepliesQuery);

		while (numTweetsResultSet.next()) {
			numTweets = (double)numTweetsResultSet.getInt(1);
		}

		while (numRepliesResultSet.next()) {
			numReplies = (double)numRepliesResultSet.getInt(1);
		}

		return (int)((numReplies/numTweets)*100);
	}

	/**
	 * @return a List containg all users that have been replied to
	 */
	public static List<Long> repliedToUsers () throws SQLException, ParseException {
		List<Long> repliedToUserIDs = new ArrayList<Long>();
		String repliedToUsersQuery = "SELECT DISTINCT in_reply_to_user_id "
									+ "FROM Tweets " 
									+ "WHERE in_reply_to_user_id IS NOT 0";

		ResultSet repliedToUserIDsResultSet = DatabaseHelper.execute(repliedToUsersQuery);	  

		while (repliedToUserIDsResultSet.next())
			repliedToUserIDs.add(new Long(repliedToUserIDsResultSet.getLong(1)));

		return repliedToUserIDs;
	}

	public static int getReplyCount (Long userID) throws SQLException, ParseException {
		int replyCount = 0;
		String replyCountQuery = "SELECT COUNT(*) FROM Tweets WHERE in_reply_to_user_id LIKE '" + userID.toString() +"'";

		ResultSet replyCountResultSet = DatabaseHelper.execute(replyCountQuery);

		while (replyCountResultSet.next())
			replyCount = replyCountResultSet.getInt(1);

		return replyCount;
	}

	/**
	 * @return list of tweets in october
	 * @FIXME: doesn't work
	 */
	public static List<Tweet> tweetsInOctober () throws SQLException, ParseException {
		List<Tweet> tweetsInOctober = new ArrayList<Tweet>();
		String tweetsInOctoberQuery = "SELECT * FROM Tweets WHERE timestamp " + 
									"BETWEEN '2013-09-30 23:59:59 PST' AND '2013-11-01 00:00:01 PST'";

		tweetsInOctober = TweetBuilder.buildTweetFromResultSet(DatabaseHelper.execute(tweetsInOctoberQuery));	 

		return tweetsInOctober; 
	}

	/**
	 * @return list of tweets with hashtags(#)
	 */
	public static List<Tweet> tweetsWithHashtag () throws SQLException, ParseException {
		List<Tweet> tweetsWithHashtag = new ArrayList<Tweet>();
		String tweetsWithHashtagQuery = "SELECT * FROM Tweets WHERE text LIKE '%#%'";

		tweetsWithHashtag = TweetBuilder.buildTweetFromResultSet(DatabaseHelper.execute(tweetsWithHashtagQuery));	

		return tweetsWithHashtag;
	}

}
