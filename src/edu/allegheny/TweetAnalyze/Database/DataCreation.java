package  twitter;

import java.sql.*;
import java.util.ArrayList;
public class DataCreation {
        static Connection c = null;
        static Statement stmt = null;
        static ResultSet rs = null;

        //setting up table initially
        public static void setTable() {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:Tweets");
                stmt = c.createStatement();
                String sql = "Create Table Tweets("+
                            "Tweet_ID REAL PRIMARY KEY,"+
                            "in_reply_status_id REAL," +
                            "in_reply_to_user_id REAL," +
                            "timestamp DATETIME," +
                            "source VARCHAR," +
			    "text VARCHAR," +
			    "retweeted_status_id REAL," +
    			    "retweeted_status_user_id REAL," +
    			    "retweeted_status_user_timestamp DATETIME," +
    			    "expanded_urls VARCHAR)";
                stmt.executeUpdate(sql);
                stmt.close();
                c.close();
            } catch(Exception e) {
                System.out.println("-------Problems Creating Table-------");
                e.printStackTrace();
            }
        }

        public static boolean updateTable(ArrayList<Tweet> tweetInfo) {
            boolean worked = false;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:DATABASELOC.db");
                c.setAutoCommit(false);
                stmt = c.createStatement();
                for(Tweet tweet : tweetInfo) {
                    String sql = "INSERT INTO TWEETS (Tweet_ID, in_replay_status_id, 		in_reply_to_user_id, timestamp, source, text, retweeted_status_id, retweeted_status_user_id," +
"retweeted_status_user_timestamp, expanded_urls)" +
                                "VALUES("+tweet.getId()+", '"+tweet.getInReplyToStatus+"', "+tweet.getInReplayToUserID+", "+tweet.getTimestamp+", '"+tweet.getSource+"', '"+tweet.getText+"', "+tweet.getRetweetedUserID+", "+tweet.getRetweetStatusTimestamp+");";
                    stmt.executeUpdate(sql);
                }
                stmt.close();
                c.commit();
                c.close();
                worked = true;
            } catch(Exception e) {
                System.out.println("\n\nProblem Updating Tweet Entries \n");
                e.printStackTrace();
            }
            return worked;
        }

		//this gets you everything
		//gotta check to see if i can do this for larger sets
		//like 2000 tweets/day
        public static ArrayList<Tweet> grabAll() {
        	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        	try {
			} catch(Exception e) {
				System.out.println("\n\nTwitter get all problem!!\n");
				e.printStackTrace();
			}
			return tweets;
		}
}
