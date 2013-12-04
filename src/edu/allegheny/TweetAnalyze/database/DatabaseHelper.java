package  edu.allegheny.TweetAnalyze.Database;

import java.sql.*;
import javax.sql.rowset.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.io.File;

import edu.allegheny.TweetAnalyze.*;
import edu.allegheny.TweetAnalyze.Parser.*;

public class DatabaseHelper
{
    private static Connection c = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;
    private static SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss z");


    public static void main(String[] argv) throws Exception
    {
        DatabaseHelper db = new DatabaseHelper();
        File zipFile = new File("tweets.zip");
        db.dropTweetsTable();
        db.createTweetsTable();
        db.insertTweets((ArrayList<Tweet>) ZipParser.parse(zipFile));
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
                System.out.println("-------Problems Creating Table-------");
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
            //Log 
            se.printStackTrace();
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
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
                    for(Tweet t : tweets)
                    {
                        System.out.println(t.getText());
                        System.out.println(t.getTimestamp());
                    }
        } 
        catch(Exception e) 
        {
           System.out.println("\n\nTwitter get all problem!!\n");
           e.printStackTrace();
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
            se.printStackTrace();
        }
        
        return  latest_tweet;
    }
}
