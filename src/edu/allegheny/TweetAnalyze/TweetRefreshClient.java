
//     __ __     _      __   __  _                __   
//    / //_/__  (_)__ _/ /  / /_(_)__  ___ ____ _/ /__ 
//   / ,< / _ \/ / _ `/ _ \/ __/ / _ \/ _ `/ _ `/ / -_)
//  /_/|_/_//_/_/\_, /_//_/\__/_/_//_/\_, /\_,_/_/\__/ 
//              /___/                /___/          
//  Open-source Twitter analytics...with style!

package edu.allegheny.tweetanalyze;


import twitter4j.*;
import java.util.List;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;

import edu.allegheny.tweetanalyze.database.DatabaseHelper;

/**
 * Get recent tweets and store them in the db
 * @author Dibyo Mukherjee
 * @version 0.1
 * @since November 29, 2013
 */
public class TweetRefreshClient
{
	private AccessTokenHelper tokenHelper;
	private DatabaseHelper db;

	public TweetRefreshClient(DatabaseHelper db)
	{
		this.tokenHelper = new AccessTokenHelper();
		this.db = db;
	}

	public static void main(String argv[]) 
	{
		TweetRefreshClient client = new TweetRefreshClient(new DatabaseHelper());
		int numberOfNewTweets = client.refreshTweets();
		System.out.println("\n" + numberOfNewTweets + " new tweets\n");
	}

	public int refreshTweets()
	{
		//Make sure you have the right Access Tokens
		if (! tokenHelper.hasToken())
		{
			System.out.println("Invalid Tokens.\n Lets get new ones!!!");
			this.refreshCredentials();
		}
		
		//Get the last tweet id
		long since_id = db.getLastTweetID();

		//Get the tweets
		ArrayList<Tweet> newTweets = (ArrayList<Tweet>) this.getRecentTweets(since_id);
		
		//Save it to db
		if(newTweets.size() > 0)
		{
			db.insertTweets(newTweets);	
			db.insertUser();	// check to see if there are new users
		}
		
		return newTweets.size();
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