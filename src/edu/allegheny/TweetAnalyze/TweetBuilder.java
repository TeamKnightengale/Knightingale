package edu.allegheny.TweetAnalyze;

import java.util.List;
import java.util.Date;
import java.util.Arrays;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import twitter4j.*;
import java.sql.*;

/**
* Builds Tweets
*
* @author	Dibyo Mukherjee
* @author	Hawk Weisman
* @version	2.0
* @since	December 2, 2013
*/

public class TweetBuilder
{	

	private static SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss z");

	/**
	* @param status A twitter4j.Status
	* @return a Tweet
	* @author Dibyo Mukherjee
	*/
	public static Tweet buildTweet(Status status)
	{
		Tweet tweet = null;

		long tweetID = status.getId(); 
		Date timestamp = status.getCreatedAt();
		String source = status.getSource();
		String text = status.getText();

		//Does this tweet have expanded URLs
		if(status.getURLEntities().length > 0)
		{
			ArrayList<String> expandedURLs = new ArrayList<String>();
			for(URLEntity urlEntity : status.getURLEntities())
			{
				expandedURLs.add(urlEntity.getExpandedURL());
			}

			if (status.isRetweet())
			{	
				long retweetedUserID = status.getRetweetedStatus().getId();
				long retweetedStatusID = status.getRetweetedStatus().getUser().getId();
				Date retweetedStatusTimestamp = status.getRetweetedStatus().getCreatedAt();
				tweet = new Tweet(tweetID, timestamp, source, text,retweetedUserID, retweetedStatusID, retweetedStatusTimestamp, expandedURLs);
			}
			//Is it a reply?	
			else if (status.getInReplyToUserId() != -1)
			{
				long inReplyToStatusID = status.getInReplyToUserId();
				long inReplyToUserID = status.getInReplyToUserId();
				tweet = new Tweet(tweetID, timestamp, source, text, inReplyToStatusID, inReplyToUserID, expandedURLs);
			}
			//Tweet that is not reply, not a retweet, and has expandedURLs
			else
			{
				tweet = new Tweet(tweetID, timestamp, source, text, expandedURLs);
			}
		}
		//Does not have expanded URLs
		else
		{
			if (status.isRetweet())
			{	
				long retweetedUserID = status.getRetweetedStatus().getId();
				long retweetedStatusID = status.getRetweetedStatus().getUser().getId();
				Date retweetedStatusTimestamp = status.getRetweetedStatus().getCreatedAt();
				tweet = new Tweet(tweetID, timestamp, source, text,retweetedUserID, retweetedStatusID, retweetedStatusTimestamp );
			}
			//Is it a reply?	
			else if (status.getInReplyToUserId() != -1)
			{
				long inReplyToStatusID = status.getInReplyToUserId();
				long inReplyToUserID = status.getInReplyToUserId();
				tweet = new Tweet(tweetID, timestamp, source, text, inReplyToStatusID, inReplyToUserID);
			}
			//Tweet that is not reply, not a retweet, and has no expandedURLs
			else
			{
				tweet = new Tweet(tweetID, timestamp, source, text);
			}
		}
		
		return tweet;
	}

	/**
	* @param 	line An array containing the components of a tweet in the order they are given in a tweets.csv file
	* @throws	ParseException in the event of a problem parsing timestamps
	* @return 	a Tweet
	* @author 	Hawk Weisman
	*/
	public static Tweet buildTweet(String[] line) throws ParseException
	{
		Tweet tweet = null;
		long tweetID = Long.parseLong(line[0]);
		Date timestamp = timestampFormat.parse(line[3]);
		String source = line[4];
		String text = line[5];

		// does this tweet have extended URLs?
		if (line[9].isEmpty() == false)
		{
			ArrayList<String> expandedURLs;
			expandedURLs = new ArrayList<String>(Arrays.asList(line[9].split(",")));

			// is the tweet a retweet?
			if (line[6].isEmpty() == false)
			{
				long retweetedUserID = Long.parseLong(line[7]);
				long retweetedStatusID = Long.parseLong(line[6]);
				Date retweetedStatusTimestamp = timestampFormat.parse(line[8]);

				tweet = new Tweet(tweetID, timestamp, source, text,retweetedUserID, retweetedStatusID, retweetedStatusTimestamp, expandedURLs);
			}

			// ...is it a reply, then?
			else if (line[1].isEmpty() == false) 
			{
				long inReplyToStatusID = Long.parseLong(line[1]);
				long inReplyToUserID = Long.parseLong(line[2]);

				tweet = new Tweet(tweetID, timestamp, source, text, inReplyToStatusID, inReplyToUserID, expandedURLs);
			}

			// well, it must be a regular status, then.
			else {
				tweet = new Tweet(tweetID, timestamp, source, text, expandedURLs);
			}
		}

		else {
			// is the tweet a retweet?
			if (line[6].isEmpty() == false)
			{
				long retweetedUserID = Long.parseLong(line[7]);
				long retweetedStatusID = Long.parseLong(line[6]);
				Date retweetedStatusTimestamp = timestampFormat.parse(line[8]);

				tweet = new Tweet(tweetID, timestamp, source, text,retweetedUserID, retweetedStatusID, retweetedStatusTimestamp);
			}

			// ...is it a reply, then?
			else if (line[1].isEmpty() == false) 
			{
				long inReplyToStatusID = Long.parseLong(line[1]);
				long inReplyToUserID = Long.parseLong(line[2]);

				tweet = new Tweet(tweetID, timestamp, source, text, inReplyToStatusID, inReplyToUserID);
			}

			// well, it must be a regular status, then.
			else {
				tweet = new Tweet(tweetID, timestamp, source, text);
			}
		}

		return tweet;
	}

	/**
	* @param 	line An array containing the components of a tweet in the order they are given in a tweets.csv file
	* @throws	ParseException in the event of a problem parsing timestamps
	* @return 	an ArrayList of Tweets
	* @author 	Dibyo Mukherjee
	*/
	
	public static ArrayList<Tweet> buildTweetFromResultSet(ResultSet rs) throws ParseException, SQLException
	{
		String[] line = new String[10];
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(); 
		while (rs.next()) 
		{	
			
			line[0] = new Long(rs.getLong("tweet_id")).toString();
			line[1] = new Long(rs.getLong("in_reply_to_status_id")).toString();
			line[2] = new Long(rs.getLong("in_reply_to_user_id")).toString();
			line[3] = timestampFormat.format(new Date(rs.getLong("timestamp")));
			line[4] = rs.getString("source");
			line[5] = rs.getString("text");
			line[6] = new Long(rs.getLong("retweeted_status_id")).toString();
			line[7] = new Long(rs.getLong("retweeted_status_user_id")).toString();
			line[8] = timestampFormat.format(new Date(rs.getLong("retweeted_status_timestamp")));
			line[9] = rs.getString("expanded_urls");
			System.out.println(line[0] + line[1] + line[2]);
			tweets.add(buildTweet(line));			
		}
		return tweets;
	}
	
	}