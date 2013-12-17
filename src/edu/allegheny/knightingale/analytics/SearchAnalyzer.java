//     __ __     _      __   __  _                __   
//    / //_/__  (_)__ _/ /  / /_(_)__  ___ ____ _/ /__ 
//   / ,< / _ \/ / _ `/ _ \/ __/ / _ \/ _ `/ _ `/ / -_)
//  /_/|_/_//_/_/\_, /_//_/\__/_/_//_/\_, /\_,_/_/\__/ 
//              /___/                /___/          
//  Open-source Twitter analytics...with style!

package edu.allegheny.knightingale.analytics;

import edu.allegheny.knightingale.Tweet;
import edu.allegheny.knightingale.TweetBuilder;
import edu.allegheny.knightingale.database.DatabaseHelper;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.sql.*;
import java.text.ParseException;

import edu.allegheny.knightingale.LogConfigurator; // REMOVE WHEN MAIN METHOD IS REMOVED

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

}
