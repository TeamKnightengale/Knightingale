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

/**
 * Does analyses relating to hashtags.
 *
 * @author	Hawk Weisman
 * @version	0.1
 * @since	December 11, 2013
 */

public class HashtagAnalyzer extends Analyzer {

	public static Logger logger = Logger.getLogger(SimpleAnalyzer.class.getName());

	public HashtagAnalyzer(DatabaseHelper db) {
		super(db);
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