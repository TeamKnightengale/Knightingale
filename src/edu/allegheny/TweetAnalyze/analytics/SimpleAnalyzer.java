//     __ __     _      __   __  _                __   
//    / //_/__  (_)__ _/ /  / /_(_)__  ___ ____ _/ /__ 
//   / ,< / _ \/ / _ `/ _ \/ __/ / _ \/ _ `/ _ `/ / -_)
//  /_/|_/_//_/_/\_, /_//_/\__/_/_//_/\_, /\_,_/_/\__/ 
//              /___/                /___/          
//  Open-source Twitter analytics...with style!

package edu.allegheny.TweetAnalyze.analytics;

import edu.allegheny.TweetAnalyze.Tweet;
import edu.allegheny.TweetAnalyze.TweetBuilder;
import edu.allegheny.TweetAnalyze.database.DatabaseHelper;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.*;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.sql.*;
import java.text.ParseException;

import edu.allegheny.TweetAnalyze.LogConfigurator; // REMOVE WHEN MAIN METHOD IS REMOVED

/**
 * Provides simple analytic methods for the TweetAnalyze database.
 *
 * @author	Hawk Weisman
 * @author  Ian McMillan
 * @author  Dibyo Mukherjee
 * @version	1.0
 * @since	December 11, 2013
 */
public class SimpleAnalyzer {
	
	private DatabaseHelper db;
	public static Logger logger = Logger.getLogger(SimpleAnalyzer.class.getName());

	public SimpleAnalyzer(DatabaseHelper db)
	{
		this.db = db;
	}

	//////////////////////////////////////////////////////////////////
	// DEMO MAIN METHOD - SHOULD NOT OCCUR IN PRODUCTION BUILDS 	//
	// Remove before flight, handle with care, so on and so forth.	//
	// If this breaks, I warned you! ~ Hawk 						//
	//////////////////////////////////////////////////////////////////
	public static void main(String argv[]) throws Exception
	{
		LogConfigurator.setup(); // setup the logger.

		SimpleAnalyzer analyzer = new SimpleAnalyzer(new DatabaseHelper());
		
		System.out.println("Tweets having " + argv[0] + " : \n");
		List<Tweet> tweets = analyzer.search(argv[0]);

		for (Tweet t : tweets)
		{
			System.out.println(t.getText());
		}
		
		System.out.printf("\n%d%% of your tweets are retweets, and %d%% are replies.\n", analyzer.percentRetweets(), analyzer.percentReplies());

		System.out.println("\n\nHashtags you've used are: \n");

		List<String> hashtags = analyzer.extractHashtags();

		for(String hashtag : hashtags)
		{
			System.out.println(hashtag);
		}

		System.out.println("\n\nList of people you reply to: \n");
		
		List<Long> repliedUsers = analyzer.repliedToUsers();
		for (Long l : repliedUsers)
		{
			System.out.println(l);
		}

		System.out.println("\n\nList of people you've retweeted: \n");
		
		List<Long> retweetedUsers = analyzer.retweetedUsers();
		for (Long l : retweetedUsers)
		{
			System.out.println(l);
		}

	}

	/**
	 * @param searchTerm a String
	 * @return a List of tweets containing the specified string.
	 */
	public List<Tweet> search (String searchTerm) throws SQLException, ParseException, ClassNotFoundException
	{
		
		List<Tweet> searchResults = new ArrayList<Tweet>();
		String searchQuery 	=  "SELECT * FROM tweets WHERE text LIKE '%" + searchTerm + "%'";
		searchResults = TweetBuilder.buildTweetFromResultSet(db.execute(searchQuery));
		return searchResults;
	}

	/**
	 * @return the percentage of retweets in the user's database.
	 */
	public int percentRetweets() throws SQLException, ParseException {
		double numTweets = 0, numRetweets = 0;

		String numTweetsQuery 	=  "SELECT COUNT(*) FROM tweets";
		String numRetweetsQuery =  "SELECT COUNT(*) FROM tweets WHERE retweeted_status_id IS NOT 0";

		ResultSet numTweetsResultSet = db.execute(numTweetsQuery);
		ResultSet numRetweetsResultSet = db.execute(numRetweetsQuery);

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
	public List<Tweet> tweetsWithHyperlinks () throws SQLException, ParseException {

		List<Tweet> tweetsWithHyperlinks = new ArrayList<Tweet>();;
		String hyperlinksQuery = "SELECT * FROM tweets WHERE expanded_urls IS NOT 0";

		tweetsWithHyperlinks = TweetBuilder.buildTweetFromResultSet(db.execute(hyperlinksQuery));	                                      

		return tweetsWithHyperlinks;
	}

	/**
	 * @return the percentage of replied tweets
	 */
	public int percentReplies () throws SQLException, ParseException {
		double numTweets = 0, numReplies = 0;

		String numTweetsQuery 	= "SELECT COUNT(*)FROM Tweets";
		String numRepliesQuery 	= "SELECT COUNT(*)FROM Tweets WHERE in_reply_to_status_id IS NOT 0 AND" 
								+ " in_reply_to_user_id IS NOT 0";

		ResultSet numTweetsResultSet = db.execute(numTweetsQuery);
		ResultSet numRepliesResultSet = db.execute(numRepliesQuery);

		while (numTweetsResultSet.next()) {
			numTweets = (double)numTweetsResultSet.getInt(1);
		}

		while (numRepliesResultSet.next()) {
			numReplies = (double)numRepliesResultSet.getInt(1);
		}

		return (int)((numReplies/numTweets)*100);
	}

	/**
	 * @return a List containg all users that have been replied to with count
	 * of times replied to
	 */
	public List<Long> repliedToUsers () throws SQLException, ParseException {
		List<Long> repliedToUserIDs = new ArrayList<Long>();
		String repliedToUsersQuery 	= "SELECT DISTINCT in_reply_to_user_id as rid, COUNT (*) "
									+ "FROM Tweets " 
									+ "GROUP BY in_reply_to_user_id "
									+ "ORDER BY rid DESC";

		ResultSet repliedToUserIDsResultSet = db.execute(repliedToUsersQuery);	  

		while (repliedToUserIDsResultSet.next())
			repliedToUserIDs.add(new Long(repliedToUserIDsResultSet.getLong(1)));

		return repliedToUserIDs;
	}

	/** 
	 * @param userID the ID of the user whose replies to count
	 * @return the number of times that user has been replied to (as an int)
	 */
	public int getReplyCount (Long userID) throws SQLException, ParseException {
		int replyCount = 0;
		String replyCountQuery = "SELECT COUNT(*) FROM Tweets WHERE in_reply_to_user_id LIKE '" + userID.toString() +"'";

		ResultSet replyCountResultSet = db.execute(replyCountQuery);

		while (replyCountResultSet.next())
			replyCount = replyCountResultSet.getInt(1);

		return replyCount;
	}

	/**
	 * @return a List containing the IDs of all users that have been retweeted
	 */
	public List<Long> retweetedUsers () throws SQLException, ParseException {
		List<Long> retweetedUserIDs = new ArrayList<Long>();
		String retweetedUsersQuery 	= "SELECT DISTINCT retweeted_status_user_id AS id, COUNT(*) "
									+ "FROM Tweets " 
									+ "GROUP BY retweeted_status_user_id "
									+ "ORDER BY id DESC";
		ResultSet retweetedUserIDsResultSet = db.execute(retweetedUsersQuery);	  

		while (retweetedUserIDsResultSet.next())
			retweetedUserIDs.add(new Long(retweetedUserIDsResultSet.getLong(1)));

		return retweetedUserIDs;
	}

	/** 
	 * @param userID the ID of the user whose retweeted statuses to count
	 * @return the number of times that user has been retweeted (as an int)
	 */
	public int getRetweetCount (Long userID) throws SQLException, ParseException {
		int retweetCount = 0;
		String retweetCountQuery = "SELECT COUNT(*) FROM Tweets WHERE retweeted_status_user_id LIKE '" + userID.toString() +"'";

		ResultSet retweetCountResultSet = db.execute(retweetCountQuery);

		while (retweetCountResultSet.next())
			retweetCount = retweetCountResultSet.getInt(1);

		return retweetCount;
	}

	/**
	 * @return list of tweets in october
	 * @FIXME: doesn't work
	 * @TODO: Change from tweetsInOctober to tweetsAsMonth(String Month)?
	 */
	public List<Tweet> tweetsInOctober () throws SQLException, ParseException {
		List<Tweet> tweetsInOctober = new ArrayList<Tweet>();
		String tweetsInOctoberQuery = "SELECT * FROM Tweets WHERE timestamp " 
									+ "BETWEEN '2013-09-30 23:59:59' AND '2013-11-01 00:00:01'";

		tweetsInOctober = TweetBuilder.buildTweetFromResultSet(db.execute(tweetsInOctoberQuery));	 

		return tweetsInOctober; 
	}

	/**
	 * @return list of tweets with hashtags(#)
	 */
	public List<Tweet> tweetsWithHashtag () throws SQLException, ParseException {
		List<Tweet> tweetsWithHashtag = new ArrayList<Tweet>();
		String tweetsWithHashtagQuery = "SELECT * FROM Tweets WHERE text LIKE '%#%'";

		tweetsWithHashtag = TweetBuilder.buildTweetFromResultSet(db.execute(tweetsWithHashtagQuery));	

		return tweetsWithHashtag;
	}

	/**
	 * @return a list of the hashtags that there are
	 */
	public List<String> extractHashtags () {
		List<Tweet> taggedTweets = new ArrayList<Tweet>(); 
		List<String> hashtags = new ArrayList<String>();
		Pattern tagExtractor = Pattern.compile("(#[A-Za-z0-9_]+)");

		try {
			taggedTweets = tweetsWithHashtag();
		} catch (SQLException se) {
			logger.log(Level.SEVERE, "SQLException took place while extracting hashtags", se);
		} catch (ParseException pe) {
			logger.log(Level.SEVERE, "ParseException took place while extracting hashtags", pe);
		}

		for (Tweet tweet : taggedTweets) {
			Matcher lineMatcher = tagExtractor.matcher(tweet.getText());

			// pattern-matching taketh place
			while (lineMatcher.find()) 
				hashtags.add(lineMatcher.group()); 

		}
		return hashtags;
	}

	/**
	 * @param hashtag A String containing a hashtag
	 * @return the number of occurences of the hashtag given as a parameter
	 */
	public Integer hashtagCount (String hashtag) throws SQLException, ParseException {
		Integer count = 0;
		String hashtagCountQuery = "SELECT COUNT(*) FROM Tweets WHERE text LIKE '%" + hashtag + "%'";
		ResultSet countResultSet = db.execute(hashtagCountQuery);

		while (countResultSet.next())
			count = countResultSet.getInt(1);

		return count;
	}

}
