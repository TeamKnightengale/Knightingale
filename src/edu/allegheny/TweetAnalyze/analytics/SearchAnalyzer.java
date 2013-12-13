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

import edu.allegheny.tweetanalyze.LogConfigurator; // REMOVE WHEN MAIN METHOD IS REMOVED

/**
 * Searches the Tweets database and returns certain types of tweets.
 *
 * @author	Hawk Weisman
 * @author  Ian McMillan
 * @author  Dibyo Mukherjee
 * @version	2.0
 * @since	December 11, 2013
 */
public class SearchAnalyzer extends Analyzer {
	
	public static Logger logger = Logger.getLogger(SearchAnalyzer.class.getName());

	public SearchAnalyzer(DatabaseHelper db)
	{
		super(db);
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
}
