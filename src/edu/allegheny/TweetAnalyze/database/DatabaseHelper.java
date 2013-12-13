package  edu.allegheny.tweetanalyze.database;

import java.sql.*;
import javax.sql.rowset.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.io.File;

import edu.allegheny.tweetanalyze.*;
import edu.allegheny.tweetanalyze.parser.*;

import edu.allegheny.tweetanalyze.LogConfigurator; // REMOVE WHEN MAIN METHOD IS REMOVED

public class DatabaseHelper
{
	private static Connection c = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;
	private static SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss z");
	public static Logger logger = Logger.getLogger(DatabaseHelper.class.getName());
	
	public DatabaseHelper()
	{
	}

	public static void main(String[] argv) throws Exception
	{
		try {
			LogConfigurator.setup(); // setup the logger.
		} catch (Exception e) {
		   e.printStackTrace();
		}
		DatabaseHelper db = new DatabaseHelper();
		File zipFile = new File("tweets.zip");
		db.dropTweetsTable();
		db.createTweetsTable();
		db.createUsersTable();
		db.insertTweets((ArrayList<Tweet>) ZipParser.parse(zipFile));
		db.insertUser();
		db.getAllTweets();
		System.out.println(db.getLastTweetID());
	}

	public void createTweetsTable() 
	{
		try 
		{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:tweets.db");
			stmt = c.createStatement();
			String sql = "Create Table tweets(" +
				"tweet_id INTEGER PRIMARY KEY,"+
				"in_reply_to_status_id INTEGER," +
				"in_reply_to_user_id INTEGER," +
				"timestamp DATETIME," +
				"source VARCHAR," +
				"text VARCHAR," +
				"retweeted_status_id INTEGER," +
				"retweeted_status_user_id INTEGER," +
				"retweeted_status_timestamp DATETIME," +
				"expanded_urls VARCHAR);";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
			} catch(Exception e) {
				logger.log(Level.SEVERE, "Problems creating Tweets table:", e);
			}
	}

	public void createUsersTable()
	{
		try
		{
		Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:tweets.db");
			stmt = c.createStatement();
			String sql = "Create Table users(" +
				"user_id INTEGER PRIMARY KEY,"+
		"user_name VARCHAR);";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
			} catch(Exception e) {
				logger.log(Level.SEVERE,"Problems creating Users table:", e);
			}
	 
	}

	public void dropTweetsTable()
	{
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:tweets.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS tweets");
			stmt.executeUpdate("DROP TABLE IF EXISTS users");   
			stmt.close();
			c.commit();
			c.close();
		}catch(Exception e) 
		{
			logger.log(Level.SEVERE, "Problem updating table entries:", e);
		}
	}

	public ResultSet execute(String query)
	{
		try
		{
			RowSetFactory rowSetFactory = RowSetProvider.newFactory();
			CachedRowSet crs = rowSetFactory.createCachedRowSet();
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:tweets.db");
			stmt = c.createStatement();
			crs.populate(stmt.executeQuery(query));
			stmt.close();
			c.close();
			return crs;
		}
		catch(SQLException se)
		{//ava.lang.IncompatibleClassChangeError: Expected static method
			logger.log(Level.SEVERE, "SQLException in executing query: " + query, se);
		}
		catch(ClassNotFoundException e)
		{
			logger.log(Level.SEVERE, "ClassNotFoundException in executing query: " + query, e);
		}
		return null;
	}

	public void insertTweets(ArrayList<Tweet> tweets) 
	{
		try 
		{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:tweets.db");
			c.setAutoCommit(false);
			PreparedStatement statement = null;
			for(Tweet tweet : tweets) 
			{
				String sql = "INSERT INTO tweets (tweet_id, in_reply_to_status_id," + 
					" in_reply_to_user_id, timestamp, source, text, retweeted_status_id, retweeted_status_user_id," +
					" retweeted_status_timestamp, expanded_urls)" + " VALUES( ?,?,?,?,?,?,?,?,?,?);"; 

				statement = c.prepareStatement(sql);
				
				statement.setLong(1, tweet.getTweetID());
				
				
				statement.setLong(2, tweet.getInReplyToStatusID());
				
				
				statement.setLong(3, tweet.getInReplyToUserID());
				
				
				if(tweet.getTimestamp() != null)
					statement.setTimestamp(4, new java.sql.Timestamp(tweet.getTimestamp().getTime()));         
				else
					statement.setTimestamp(4,null);           

				statement.setString(5,tweet.getSource());                   
				
				
				statement.setString(6,tweet.getText());
				
				
				statement.setLong(7, tweet.getRetweetedStatusID());
				
				
				statement.setLong(8, tweet.getRetweetedUserID());                                     
				
				
				if(tweet.getRetweetedStatusTimestamp() != null)
					statement.setTimestamp(9,  new java.sql.Timestamp(tweet.getRetweetedStatusTimestamp().getTime()));
				else
					statement.setTimestamp(9,null);
				
				statement.setString(10, "Expanded_URLS");

				statement.executeUpdate();
				
			}   
			statement.close();
			c.commit();
			c.close();
			} catch(Exception e) 
			{
				logger.log(Level.SEVERE, "Problem updating tweets table entries:", e);
			}
	}

	public void insertUser()
	{
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:tweets.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			stmt.executeUpdate("INSERT OR IGNORE INTO users (user_id) " +
				   "SELECT in_reply_to_user_id FROM tweets " +
			       "UNION SELECT retweeted_status_user_id FROM tweets");   
			stmt.close();
			c.commit();
			c.close();
			}catch(Exception e) 
			{
				logger.log(Level.SEVERE, "Problem updating users table entries:", e);
			}
	}

	/**
	 * Inserts usernames into the database for the user with the given ID.
	 *
	 * @author 	Hawk Weisman
	 * @param 	username The String to insert at the matching userID
	 * @param 	userID ID of the user at which to insert the username.
	 */
	public void updateUsername(String username, long userID) {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:tweets.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			stmt.executeUpdate("UPDATE users "
			                 + "SET user_name = \"" + username + "\" "
			                 + "WHERE user_id = " + userID + ";");   
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			logger.log(Level.SEVERE,"Problem inserting usernames into database:", e);
		}
	}

	public ArrayList<Tweet> getAllTweets() throws ParseException
	{
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		try 
		{
			tweets = TweetBuilder.buildTweetFromResultSet(execute("SELECT * FROM tweets;"));
					for(Tweet t : tweets)
					{
						System.out.println(t.getText());
						System.out.println(t.getTimestamp());
					}
		} 
		catch(Exception e) 
		{
			logger.log(Level.SEVERE, "Problem getting all tweets: ", e);
		}
		return tweets;
	}

	public long getLastTweetID()
	{
		String query = "select tweet_id from tweets order by tweet_id desc limit 1";
		ResultSet resultset = execute(query);
		long latest_tweet = 0;
		try
		{
			while (resultset.next())
			{
				latest_tweet = resultset.getLong(1);
			}    
		}
		catch(SQLException se)
		{
			logger.log(Level.SEVERE, "SQLException while getting last tweet ID:", se);
		}
		
		return  latest_tweet;
	}
}
