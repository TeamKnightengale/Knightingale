/**
* Build a tweet given a Status
* @author Dibyo Mukherjee
* @version 1.0
* @since November 28, 1013
*/

package edu.allegheny.TweetAnalyze;
import java.util.List;
import twitter4j.*;
import java.util.Date;

public class TweetBuilder
{	
	/**
	* @param status A twitter4j.Status
	* @return a Tweet
	*/
	public Tweet buildTweet(Status status)
	{
		Tweet tweet = null;

		long tweetID = status.getId(); 
		Date timestamp = status.getCreatedAt();
		String source = status.getSource();
		String text = status.getText();

		//Does this tweet have expanded URLs
		if(status.getURLEntities().length > 0)
		{
			for(URLEntity urlEntity : status.getURLEntities())
			{
				System.out.println(urlEntity.getExpandedURL());
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
			}
			//Tweet that is not reply, not a retweet, and has no expandedURLs
			else
			{
				tweet = new Tweet(tweetID, timestamp, source, text);
			}
		}
		
		return tweet;
	}
}