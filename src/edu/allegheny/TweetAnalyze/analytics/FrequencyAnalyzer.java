package edu.allegheny.TweetAnalyze.analytics;

import edu.allegheny.TweetAnalyze.database.DatabaseHelper;

import edu.allegheny.TweetAnalyze.analytics.*;
import edu.allegheny.TweetAnalyze.ui.gui.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.ParseException;

import twitter4j.*;

import edu.allegheny.TweetAnalyze.LogConfigurator; // REMOVE WHEN MAIN METHOD IS REMOVED

/**
 * Provides analyses that involve {user | hashtag} frequency in the database.
 *
 * @author	Hawk Weisman
 * @version	1.0
 * @since	December 11, 2013
 */

public class FrequencyAnalyzer {

	public static Logger logger = Logger.getLogger(FrequencyAnalyzer.class.getName());
	private static Twitter twitter = TwitterFactory.getSingleton();
	private SimpleAnalyzer simpleAnalyzer;
	private DatabaseHelper db;
	private static String usernameQuery = "SELECT user_name "
										+ "FROM users "
										+ "WHERE user_id IS ";

	public static void main(String[] argv) {

		try {
			LogConfigurator.setup(); // setup the logger.
			FrequencyAnalyzer analyzer = new FrequencyAnalyzer(new DatabaseHelper());

			Map<String, Integer> globalReplyFrequency = analyzer.globalReplyFrequency();
			Map<String, Integer> globalRetweetFrequency = analyzer.globalRetweetFrequency();

			System.out.println("#-----Printing raw reply data:\n");

			for (Map.Entry<String, Integer> entry : globalReplyFrequency.entrySet())
       			System.out.println(entry.getKey() + " has been replied to " + entry.getValue() + " times.");

       		System.out.println("\n#-----Printing raw reply data:\n");

       		for (Map.Entry<String, Integer> entry : globalRetweetFrequency.entrySet())
       			System.out.println(entry.getKey() + " has been retweeted " + entry.getValue() + " times.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FrequencyAnalyzer(DatabaseHelper db)
	{
		this.simpleAnalyzer = new SimpleAnalyzer(db);
		this.db = db;
	}

	/**
	 * @return a Map <String, Integer> representing the frequency of hashtags in the database.
	 */
	public Map<String, Integer> globalHashtagFrequency () throws ParseException {

	}

	/**
	 * @return a Map<String, Integer> where the values associated with each user represent the number of times that user has been replied to.
	 */
	public Map<String, Integer> globalReplyFrequency () {

		HashMap<String, Integer> frequencyMap = new HashMap<String, Integer>();
		List<Long> repliedIDs = simpleAnalyzer.repliedToUsers();
		ResultSet usernameResult;
		String username = null; // assume we don't know the username.

		for (Long id : repliedIDs) {
			if (id > 0){ // IDs must be nonzero

				try {
					// execute the DB query for each ID
					usernameResult = db.execute(usernameQuery + id.toString());
				
					// unpack result set
					while (usernameResult.next()) {
						username = usernameResult.getString(1);
					}

				} catch (SQLException se) {
					logger.log(Level.SEVERE, "SQLException occured during reply frequency analysis" + se);
				} catch (ParseException pe) {
					logger.log(Level.SEVERE, "ParseException occured during reply frequency analysis" + se);
				}

				if (username == null) { // we haven't gotten a name for this user yet
					try {
						username = twitter.showUser(id).getScreenName(); 			// get a username from twitter
						db.updateUsername(username, id);							// update the db
						logger.finer("Got a username for replied user " + id +"."); // and log to finer
					} catch (TwitterException te) {
						if (te.getStatusCode() == 404) {
							logger.info("Got a bad userID, the user with ID #" + id + " may no longer exist.");
						} else if (te.isCausedByNetworkIssue()) {
							logger.warning("You're having network issues and cannot connect to the Twitter API, try again later..");
						} else if (te.exceededRateLimitation()) {
							logger.warning("You've exceeded the Twitter API rate limit and are being throttled.");
						} else {
							logger.log(Level.SEVERE, "Caught an unexpected TwitterException:" + pe);
							}
						}
					frequencyMap.put(username, new Integer(simpleAnalyzer.getReplyCount(id)));
				}
			}
		}
		return frequencyMap;
	}

	/**
	 * @return a Map<String, Integer> where the Integers associated with each user represents the number of times that user has been retweeted to.
	 */
	public Map<String, Integer> globalRetweetFrequency () throws SQLException, ParseException, TwitterException{

		HashMap<String, Integer> frequencyMap = new HashMap<String, Integer>();
		List<Long> repliedIDs = simpleAnalyzer.retweetedUsers();
		ResultSet usernameResult;
		String username = null; // assume we don't know the username.

		for (Long id : repliedIDs) {
			if (id > 0){ // IDs must be nonzero

				try {
					// execute the DB query for each ID
					usernameResult = db.execute(usernameQuery + id.toString());
				
					// unpack result set
					while (usernameResult.next()) {
						username = usernameResult.getString(1);
					}

				} catch (SQLException se) {
					logger.log(Level.SEVERE, "SQLException occured during retweet frequency analysis" + se);
				} catch (ParseException pe) {
					logger.log(Level.SEVERE, "ParseException occured during retweet frequency analysis" + pe);
				}

				if (username == null) { // we haven't gotten a name for this user yet
					try {s
						username = twitter.showUser(id).getScreenName();
						logger.finer("Got a username for replied user " + id +".");
					} catch (TwitterException te) {
						if (te.getStatusCode() == 404) {
							logger.info("Got a bad userID, the user with ID #" + id + " may no longer exist.");
						} else if (te.isCausedByNetworkIssue()) {
							logger.warning("You're having network issues and cannot connect to the Twitter API, try again later..");
						} else if (te.exceededRateLimitation()) {
							logger.warning("You've exceeded the Twitter API rate limit and are being throttled.");
						} else {
							logger.log(Level.SEVERE, "Caught an unexpected TwitterException:" + te);
							}
						}
					frequencyMap.put(username, new Integer(simpleAnalyzer.getReplyCount(id)));
				}
			}
		}
		return frequencyMap;
	}
}