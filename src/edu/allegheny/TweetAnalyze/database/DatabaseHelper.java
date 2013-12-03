package  edu.allegheny.TweetAnalyze.Database;

import java.sql.*;
import java.util.ArrayList;
import edu.allegheny.TweetAnalyze.*;
import java.text.ParseException;


public class DatabaseHelper
{
    static Connection c = null;
    static Statement stmt = null;
    static ResultSet rs = null;

    //setting up table initially
    public static void createTable() 
    {
        try 
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:tweet_db");
            stmt = c.createStatement();
            String sql = "Create Table Tweets(" +
                "tweet_id REAL PRIMARY KEY,"+
                "in_reply_status_id REAL," +
                "in_reply_to_user_id REAL," +
                "timestamp DATETIME," +
                "source VARCHAR," +
                "text VARCHAR," +
                "retweeted_status_id REAL," +
                "retweeted_status_user_id REAL," +
                "retweeted_status_timestamp DATETIME," +
                "expanded_urls VARCHAR);";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch(Exception e) {
            System.out.println("-------Problems Creating Table-------");
            e.printStackTrace();
        }
    }

    public static ResultSet execute(String query){        
        ResultSet rs = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:tweet_db");
            stmt = c.createStatement();
            rs = (ResultSet) stmt.executeQuery(query);
            stmt.close();
            c.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return rs;
    }

    public static void insertTweetsIntoTable(ArrayList<Tweet> tweetInfo) 
    {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:tweet_db.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            for(Tweet tweet : tweetInfo) {
                String sql = "INSERT INTO TWEETS (id, in_replay_status_id," + 
                    " in_reply_to_user_id, timestamp, source, text, retweeted_status_id, retweeted_status_user_id," +
                    " retweeted_status_user_timestamp, expanded_urls)" + "VALUES(" + 
                     tweet.getTweetID()                  + "," +
                     tweet.getInReplyToStatusID()   + "," + 
                     tweet.getInReplyToUserID()     + "," + 
                     tweet.getTimestamp()           + "," + 
                     "'"+ tweet.getSource()   + "'" + "," + 
                     "'"+ tweet.getText()     + "'" + "," +
                     tweet.getRetweetedUserID()     + "," +
                     tweet.getRetweetedStatusTimestamp() + 
                     " ); ";
                stmt.executeUpdate(sql);
            }   
            stmt.close();
            c.commit();
            c.close();
        } catch(Exception e) 
        {
            System.out.println("\n\nProblem Updating Tweet Entries \n");
            e.printStackTrace();
        }
    }

    public static ArrayList<Tweet> getAllTweets() throws ParseException
    {
    	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    	try 
        {
            tweets = TweetBuilder.buildTweetFromResultSet(execute("SELECT * FROM tweets;"));
        } catch(Exception e) {
			System.out.println("\n\nTwitter get all problem!!\n");
			e.printStackTrace();
		}
		return tweets;
	}
}
