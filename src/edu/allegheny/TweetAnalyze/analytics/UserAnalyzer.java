//     __ __     _      __   __  _                __   
//    / //_/__  (_)__ _/ /  / /_(_)__  ___ ____ _/ /__ 
//   / ,< / _ \/ / _ `/ _ \/ __/ / _ \/ _ `/ _ `/ / -_)
//  /_/|_/_//_/_/\_, /_//_/\__/_/_//_/\_, /\_,_/_/\__/ 
//              /___/                /___/          
//  Open-source Twitter analytics...with style!

package edu.allegheny.tweetanalyze.analytics;

import edu.allegheny.tweetanalyze.database.DatabaseHelper;

import java.sql.*;
import java.text.ParseException;

/**
 * Searches the database for users.
 *
 * @author	Hawk Weisman
 * @author  Ian McMillan
 * @author  Dibyo Mukherjee
 * @version	0.1
 * @since	December 11, 2013
 */

public class UserAnalyzer extends Analyzer {

	public UserAnalyzer(DatabaseHelper db){
		super(db);
	}

	/**
	 * @return a ResultSet containing the IDs of all users that have been replied to and the number of times they have been replied to
	 */
	public ResultSet repliedToUsers () throws SQLException, ParseException {
		String repliedToUsersQuery 	= "SELECT DISTINCT in_reply_to_user_id as rid, COUNT (*) "
									+ "FROM Tweets " 
									+ "GROUP BY rid "
									+ "ORDER BY rid DESC";

		ResultSet repliedToUserIDsResultSet = db.execute(repliedToUsersQuery);	  

		return repliedToUserIDsResultSet;
	}

	/**
	 * @return a ResultSet containing the IDs of all users that have been retweeted and the number of times they have been retweeted
	 */
	public ResultSet retweetedUsers () throws SQLException, ParseException {
		String retweetedUsersQuery 	= "SELECT DISTINCT retweeted_status_user_id AS id, COUNT(*) "
									+ "FROM Tweets " 
									+ "GROUP BY retweeted_status_user_id "
									+ "ORDER BY id DESC";
		ResultSet retweetedUserIDsResultSet = db.execute(retweetedUsersQuery);	  

		return retweetedUserIDsResultSet;
	}
}
