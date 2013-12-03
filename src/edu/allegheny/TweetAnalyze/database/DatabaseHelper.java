package  edu.allegheny.TweetAnalyze.Database;

import java.sql.*;
import java.util.ArrayList;
import java.text.ParseException;
import java.io.File;
import edu.allegheny.TweetAnalyze.*;
import edu.allegheny.TweetAnalyze.Parser.*;

public class DatabaseHelper
{
    static Connection c = null;
    static Statement stmt = null;
    static ResultSet rs = null;
    public static Logger logger = LogManager.getFormatterLogger(DatabaseHelper.class.getName());

    
    public static void main(String[] argv) throws Exception
    {
        File zipFile = new File("tweets.zip");
        //createTweetsTable();
        insertTweets((ArrayList<Tweet>) ZipParser.parse(zipFile));
    }

    public static void createTweetsTable() 
    {
        try 
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:tweets.db");
            stmt = c.createStatement();
            String sql = "Create Table tweets(" +
                "tweet_id REAL PRIMARY KEY,"+
                "in_reply_status_id REAL," +
                "in_reply_to_user_id REAL," +
                "timestamp DATETIME," +
                "source VARCHAR," +
                "text VARCHAR," +
                "retweeted_status_id REAL," +
                "retweeted_status_user_id REAL," +
                "retweeted_status_user_timestamp DATETIME," +
                "expanded_urls VARCHAR);";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch(Exception e) {
            System.out.println("-------Problems Creating Table-------");
            e.printStackTrace();
        }
    }

    public static ResultSet execute(String query)
    {
        ResultSet rs = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:tweets.db");
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

    public static void insertTweets(ArrayList<Tweet> tweetInfo) 
    {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:tweets.db");
            c.setAutoCommit(false);
            PreparedStatement statement = null;
            //System.out.println("Created Statement");
            for(Tweet tweet : tweetInfo) {
                String sql = "INSERT INTO tweets (tweet_id, in_reply_status_id," + 
                    " in_reply_to_user_id, timestamp, source, text, retweeted_status_id, retweeted_status_user_id," +
                    " retweeted_status_user_timestamp, expanded_urls)" + " VALUES( ?,?,?,?,?,?,?,?,?,?);"; 
                     statement = c.prepareStatement(sql);
                     statement.setLong(1, tweet.getTweetID());
                     //System.out.println("Passesd" + tweet.getTweetID());
                     statement.setLong(2, tweet.getInReplyToStatusID());
                     //System.out.println("Passesd" + tweet.getInReplyToStatusID());
                     statement.setLong(3, tweet.getInReplyToUserID());
                     //System.out.println("Passesd" + tweet.getInReplyToUserID());
                     if(tweet.getTimestamp() != null)
                        statement.setDate(4, new java.sql.Date(tweet.getTimestamp().getTime()));         
                    else
                        statement.setDate(4,null);
                     //System.out.println("Passesd" + tweet.getTimestamp());
                     statement.setString(5,tweet.getSource());
                     //System.out.println("Passesd" + tweet.getSource());
                     statement.setString(6,tweet.getText());
                     //System.out.println("Passesd" + tweet.getText());
                     statement.setLong(7, tweet.getRetweetedStatusID());
                     //System.out.println("Passesd" + tweet.getRetweetedStatusID());
                     statement.setLong(8, tweet.getRetweetedUserID());
                     //System.out.println("Passesd" + tweet.getRetweetedUserID());
                     if(tweet.getRetweetedStatusTimestamp() != null)
                        statement.setDate(9,  new java.sql.Date(tweet.getRetweetedStatusTimestamp().getTime()));
                    else
                        statement.setDate(9,null);
                     //System.out.println("Passesd" + tweet.getRetweetedStatusTimestamp());
                     statement.setString(10, "Expanded_URLS");
                     
                //System.out.println("About to execute statement");
                System.out.println(sql);
                int number = statement.executeUpdate();
                //System.out.println("Just executed statement");
                System.out.println(number);
            }   
            statement.close();
            c.commit();
            c.close();
        } catch(Exception e) 
        {
            System.out.println("\n\nProblem Updating Tweet Entries \n");
            e.printStackTrace();
        }
    }

    public static void dropTweetsTable()
    {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:tweets.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS Tweets");   
            stmt.close();
            c.commit();
            c.close();
        }catch(Exception e) 
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
