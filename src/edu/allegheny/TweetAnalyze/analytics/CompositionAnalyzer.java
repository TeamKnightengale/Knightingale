//     __ __     _      __   __  _                __   
//    / //_/__  (_)__ _/ /  / /_(_)__  ___ ____ _/ /__ 
//   / ,< / _ \/ / _ `/ _ \/ __/ / _ \/ _ `/ _ `/ / -_)
//  /_/|_/_//_/_/\_, /_//_/\__/_/_//_/\_, /\_,_/_/\__/ 
//              /___/                /___/          
//  Open-source Twitter analytics...with style!

package edu.allegheny.tweetanalyze.analytics;

import edu.allegheny.tweetanalyze.Tweet;
import edu.allegheny.tweetanalyze.TweetBuilder;
import edu.allegheny.tweetanalyze.database.DatabaseHelper;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.sql.*;
import java.text.ParseException;

/**
 * Performs analyses relating to the composition of the user's
 * Twitter database as a whole.
 *
 * @author	Hawk Weisman
 * @author  Ian McMillan
 * @author  Dibyo Mukherjee
 * @version	0.1
 * @since	December 11, 2013
 */

public class CompositionAnalyzer extends Analyzer {

	public CompositionAnalyzer (DatabaseHelper db) {
		super(db);
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
}
