/**
* Build a tweet given a Status
* @author Dibyo Mukherjee
* @version 1.0
* @since November 28, 1013
*/

package edu.allegheny.TweetAnalyze;
import java.util.List;
import twitter4j.*;
public class TweetBuilder
{	
	/**
	* @param status A twitter4j.Status
	* @return a Tweet
	*/
	public Tweet buildTweet(Status status)
	{
		Tweet tweet = null;

		long tweetID = status.getID(); 
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
		else
		{
			if (status.isRetweet())
			{	
				long retweetedUserID = status.getRetweetedStatus().getId();
				long retweetedStatusID = status.getRetweetedStatus().getUser().getId();
				Date retweetedStatusTimestamp = status.getRetweetedStatus().getCreatedAt();
				tweet = new Tweet(tweetID, timestamp, source, text,retweetedUserID, retweetedStatusID, retweetedStatusTimestamp );
			}	
			else if (status.getInReplyToUserID() != -1)
			{
				long inReplyToStatusID = status.getInReplyToUserID();
				long inReplyToUserID = status.getInReplyToUserID();
			}
			else
			{
				tweet = new Tweet(tweetID, timestamp, source, text)
			}
		}
		
		return tweet;

	}
}