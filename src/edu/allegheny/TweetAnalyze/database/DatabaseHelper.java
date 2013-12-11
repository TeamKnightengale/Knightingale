package  edu.allegheny.TweetAnalyze.database;

import java.sql.*;
import javax.sql.rowset.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.io.File;

import edu.allegheny.TweetAnalyze.*;
import edu.allegheny.TweetAnalyze.parser.*;

import edu.allegheny.TweetAnalyze.LogConfigurator; // REMOVE WHEN MAIN METHOD IS REMOVED

public class DatabaseHelper
{
	private static Connection c = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;
	private static SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss z");
	public static Logger logger = Logger.getLogger(DatabaseHelper.class.getName());

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

	public static void createTweetsTable() 
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
				logger.severe("Problems creating Tweets table:\n" + e.getStackTrace());
			}
	}

	public static void createUsersTable()
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
				logger.severe("Problems creating Users table:\n" + e.getStackTrace());
			}
	 
	}

	public static void dropTweetsTable()
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
			logger.severe("Problem updating table entries:\n" + e.getStackTrace());
		}
	}

	public static ResultSet execute(String query)
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
		{
			logger.severe("SQLException in executing query" + query + ":\n" + e.getStackTrace());
		}
		catch(ClassNotFoundException e)
		{
			logger.severe("ClassNotFoundException in executing query" + query + ":\n" + e.getStackTrace());
		}
		return null;
	}

	public static void insertTweets(ArrayList<Tweet> tweets) 
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
				logger.severe("Problem updating tweets table entries:\n" + e.getStackTrace());
			}
	}

	public static void insertUser()
	{
	try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:tweets.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			stmt.executeUpdate("INSERT INTO users (user_id) " +
				   "SELECT DISTINCT(in_reply_to_user_id, retweeted_status_id) " +
				   "FROM tweets WHERE in_reply_to_user_id AND retweet_status_id" +
				   " IS NOT 0");   
			stmt.close();
			c.commit();
			c.close();
		}catch(Exception e) 
		{
			logger.severe("Problem updating users table entries:\n" + e.getStackTrace());
		}
	}
	public static ArrayList<Tweet> getAllTweets() throws ParseException
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
			logger.severe("Problem getting all tweets:\n" + e.getStackTrace());
		}
		return tweets;
	}

	public static long getLastTweetID()
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
			logger.severe("SQLException while getting last tweet ID:\n" + e.getStackTrace());
		}
		
		return  latest_tweet;
	}
}
