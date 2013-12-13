//     __ __     _      __   __  _                __   
//    / //_/__  (_)__ _/ /  / /_(_)__  ___ ____ _/ /__ 
//   / ,< / _ \/ / _ `/ _ \/ __/ / _ \/ _ `/ _ `/ / -_)
//  /_/|_/_//_/_/\_, /_//_/\__/_/_//_/\_, /\_,_/_/\__/ 
//              /___/                /___/          
//  Open-source Twitter analytics...with style!

package edu.allegheny.tweetanalyze.analytics;

import edu.allegheny.tweetanalyze.database.DatabaseHelper;

import edu.allegheny.tweetanalyze.analytics.*;
import edu.allegheny.tweetanalyze.ui.gui.*;

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

import edu.allegheny.tweetanalyze.LogConfigurator; // REMOVE WHEN MAIN METHOD IS REMOVED

/**
 * Provides analyses that involve the frequency of typed elements in the database.
 *
 * @author	Hawk Weisman
 * @version	1.0
 * @since	December 11, 2013
 */

public class FrequencyAnalyzer extends Analyzer {

	public static Logger logger = Logger.getLogger(FrequencyAnalyzer.class.getName());
	protected static Twitter twitter = TwitterFactory.getSingleton();
	protected UserAnalyzer userAnalyzer;
	protected HashtagAnalyzer hashtagAnalyzer;
	private static String usernameQuery = "SELECT user_name "
										+ "FROM users "
										+ "WHERE user_id IS ";

	public FrequencyAnalyzer(DatabaseHelper db) {
		super(db);
		this.userAnalyzer = new UserAnalyzer(db);
		this.hashtagAnalyzer = new HashtagAnalyzer(db);
	}

	/**
	 * @return a Map <String, Integer> representing the frequency of hashtags in the database.
	 */
	public Map<String, Integer> globalHashtagFrequency () {

		HashMap<String, Integer> frequencyMap = new HashMap<String,Integer>();
		List<String> hashtags = hashtagAnalyzer.extractHashtags();

		for (String hashtag : hashtags) {
			try {
				frequencyMap.put(hashtag, new Integer(hashtagAnalyzer.hashtagCount(hashtag)));
			} catch (SQLException se) {
				logger.log(Level.SEVERE, "SQLException took place during hashtag frequency analysis", se);
			} catch (ParseException pe) {
				logger.log(Level.SEVERE, "ParseException took place during hashtag frequency analysis", pe);
			}
		}
		return frequencyMap;
	}

	/**
	 * @return a Map<String, Integer> where the values associated with each user represent the number of times that user has been replied to.
	 */
	public Map<String, Integer> globalReplyFrequency () {

		HashMap<String, Integer> frequencyMap = new HashMap<String, Integer>();
		long id; 
		int count;
		String username = null; // assume we don't know the username.
		ResultSet repliedIDs;
		ResultSet usernameResult;

		try {
			repliedIDs = userAnalyzer.repliedToUsers();
			
			while (repliedIDs.next()) {
				id = repliedIDs.getLong(1);
				count = repliedIDs.getInt(2);

				if (id > 0){ // IDs must be nonzero
					// execute the DB query for each ID
					usernameResult = db.execute(usernameQuery + id);
						
					// unpack result set
					while (usernameResult.next()) 
						username = usernameResult.getString(1);

					if (username == null) { // we haven't gotten a name for this user yet
						try {
							username = twitter.showUser(id).getScreenName(); 			// get a username from twitter
							db.updateUsername(username, id);							// update the db
								logger.finer("Got a username for replied user " + id +"."); // and log to finer
							} catch (TwitterException te) {
								if (te.getStatusCode() == 404) {
									logger.warning("Error getting username from ID number: the user with ID #" + id + " may no longer exist.");
								} else if (te.isCausedByNetworkIssue()) {
									logger.warning("You're having network issues and cannot connect to the Twitter API, try again later.");
								} else if (te.exceededRateLimitation()) {
									logger.warning("You've exceeded the Twitter API rate limit and are being throttled.");
								} else {
									logger.log(Level.SEVERE, "Caught an unexpected TwitterException:" + te);
								}
							}
						}
					frequencyMap.put(username, new Integer(count));
				}
			}

		} catch (SQLException se) {
			logger.log(Level.WARNING, "SQLException occured during reply frequency analysis" + se);
		} catch (ParseException pe) {
			logger.log(Level.WARNING, "ParseException occured during reply frequency analysis" + pe);
		}
		return frequencyMap;
	}

	/**
	 * @return a Map<String, Integer> where the Integers associated with each user represents the number of times that user has been retweeted to.
	 */
	public Map<String, Integer> globalRetweetFrequency () {

		HashMap<String, Integer> frequencyMap = new HashMap<String, Integer>();
		long id; 
		int count;
		String username = null; // assume we don't know the username.
		ResultSet retweetIDs;
		ResultSet usernameResult;

		try {
			retweetIDs = userAnalyzer.retweetedUsers();
			
			while (retweetIDs.next()) {
				id = retweetIDs.getLong(1);
				count = retweetIDs.getInt(2);

				if (id > 0){ // IDs must be nonzero
					// execute the DB query for each ID
					usernameResult = db.execute(usernameQuery + id);
						
					// unpack result set
					while (usernameResult.next()) 
						username = usernameResult.getString(1);

					if (username == null) { // we haven't gotten a name for this user yet
						try {
							username = twitter.showUser(id).getScreenName(); 			// get a username from twitter
							db.updateUsername(username, id);							// update the db
								logger.finer("Got a username for replied user " + id +"."); // and log to finer
							} catch (TwitterException te) {
								if (te.getStatusCode() == 404) {
									logger.warning("Error getting username from ID number: the user with ID #" + id + " may no longer exist.");
								} else if (te.isCausedByNetworkIssue()) {
									logger.warning("You're having network issues and cannot connect to the Twitter API, try again later.");
								} else if (te.exceededRateLimitation()) {
									logger.warning("You've exceeded the Twitter API rate limit and are being throttled.");
								} else {
									logger.log(Level.SEVERE, "Caught an unexpected TwitterException:" + te);
								}
							}
						}
					frequencyMap.put(username, new Integer(count));
				}
			}
		} catch (SQLException se) {
			logger.log(Level.WARNING, "SQLException occured during reply frequency analysis" + se);
		} catch (ParseException pe) {
			logger.log(Level.WARNING, "ParseException occured during reply frequency analysis" + pe);
		}
		return frequencyMap;
	}
}
