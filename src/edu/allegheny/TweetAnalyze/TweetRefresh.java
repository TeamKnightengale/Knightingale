package edu.allegheny.TweetAnalyze;

import twitter4j.*;
import java.util.List;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TweetRefresh
{
	public static void main(String argv[]) throws Exception
	{
		long since_id = Long.parseLong("386276101074714624");
		getRecentTweets(since_id);
	}

	public static void refresh()
	{

	}

	/**
	 * @param since_id 
	 * @return 	a list of statuses since since_id
	*/
	private static List<Tweet> getRecentTweets(long since_id) 
	{
		Twitter twitter = TwitterFactory.getSingleton();
		List<Status> statuses = null;
		List<Tweet> tweets = null;
		TweetBuilder tweetBuilder = new TweetBuilder();
		try
		{
			String user = twitter.verifyCredentials().getScreenName();
			statuses = twitter.getUserTimeline(new Paging(since_id));			
			for (Status status : statuses)
			{
				Tweet tweet = tweetBuilder.buildTweet(status);
				tweets.add(tweet);
			}
			
		} 
		catch (TwitterException te) 
		{
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
			System.exit(-1);
		}

		return tweets;
	}


}