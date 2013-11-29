/**
 * Get recent tweets and store them in the db
 * @author Dibyo Mukherjee
 * @version 0.1
 * @since November 29, 2013
 */
package edu.allegheny.TweetAnalyze;

import twitter4j.*;
import java.util.List;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;

public class TweetRefreshClient
{
	private AccessTokenHelper tokenHelper;

	public TweetRefreshClient()
	{
		this.tokenHelper = new AccessTokenHelper();
	}

	public static void main(String argv[]) 
	{
		long since_id = Long.parseLong("386276101074714624");
		TweetRefreshClient client = new TweetRefreshClient();
		client.refreshTweets();
	}

	public void refreshTweets()
	{
		//Make sure you have the right Access Tokens
		if (! tokenHelper.hasToken())
		{
			System.out.println("Invalid Tokens.\n Lets get new ones!!!");
			this.refreshCredentials();
		}
		
		long since_id = Long.parseLong("386276101074714624");
		List<Tweet> tweets = this.getRecentTweets(since_id);
		for(Tweet tweet : tweets)
		{
			System.out.println(tweet.getText());
		}
		
		//Store them in dB
	}

	/**
	* Replace old OAuthAccess Tokens with new ones
	*/
	public void refreshCredentials()
	{
		tokenHelper.deleteAccessTokens();
		tokenHelper.getnewTokens();
	}

	/**
	 * Gets the recent statuses since since_id and converts them to tweets
	 * @param since_id 
	 * @return 	a list of statuses since since_id
	*/
	private List<Tweet> getRecentTweets(long since_id) 
	{
		Twitter twitter = TwitterFactory.getSingleton();
		List<Status> statuses = null;
		List<Tweet> tweets = new ArrayList<Tweet>();
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