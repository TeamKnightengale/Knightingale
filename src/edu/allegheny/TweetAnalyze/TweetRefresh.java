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

	/**
	 * @param since_id 
	 * @return 	a list of statuses since since_id
	*/
	public static List<Status> getRecentTweets(long since_id) 
	{
		Twitter twitter = TwitterFactory.getSingleton();
		try{
			List<Status> statuses;
			String user = twitter.verifyCredentials().getScreenName();
			statuses = twitter.getUserTimeline(new Paging(since_id));
			System.out.println("Showing @" + user + "'s user timeline.");
			for (Status status : statuses) {
				Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//String timestamp = formatter.format();
				System.out.println(status.getId() + "\t" +
					status.getInReplyToStatusId() + "\t" +
					status.getInReplyToUserId() + "\t" +
					formatter.format(status.getCreatedAt()) + "\t" +
					status.getSource() + 
					status.getText() + "\t"
				);

				if (status.isRetweet())
				{	
					System.out.println("\n");
					System.out.println(status.getRetweetedStatus().getId() + "\t" +
					status.getRetweetedStatus().getUser().getId() + "\t" +
					status.getRetweetedStatus().getCreatedAt() + "\t"
					);
				}	//status.getURLEntities() 

				if(status.getURLEntities().length > 0)
				{
					for(URLEntity urlEntity : status.getURLEntities())
					{
						System.out.println(urlEntity.getExpandedURL());
					}
				}
					
			}
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
			System.exit(-1);
		}
	}
}