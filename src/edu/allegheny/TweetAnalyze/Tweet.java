//     __ __     _      __   __  _                __   
//    / //_/__  (_)__ _/ /  / /_(_)__  ___ ____ _/ /__ 
//   / ,< / _ \/ / _ `/ _ \/ __/ / _ \/ _ `/ _ `/ / -_)
//  /_/|_/_//_/_/\_, /_//_/\__/_/_//_/\_, /\_,_/_/\__/ 
//              /___/                /___/          
//  Open-source Twitter analytics...with style!
 

package	edu.allegheny.tweetanalyze;


import 	java.util.Date;
import	java.util.ArrayList;

/**
 * Tweet.java: Represents a single Tweet.
 * 
 * @author	Hawk Weisman
 * @version	1.5
 */
public class Tweet {

	private long tweetID;

	private long inReplyToStatusID;
	private long inReplyToUserID;

	private	long retweetedStatusID;
	private long retweetedUserID;

	private Date timestamp;

	private Date retweetedStatusTimestamp;

	private String source;
	private String text;
	private ArrayList<String> expandedURLs;

//=============================================================================
// CONSTRUCTORS

	/**
	 * Constructor for a tweet that is not a retweet, not a reply, and contains no expanded urls.
	 * @param	tweetID 	the ID for this tweet
	 * @param 	timestamp 	a java.util.Date containing the date this tweet was posted
	 * @param	source 		a String containing the source identifier for the tweet
	 * @param	text 		the body text of the tweet
	 */
	public Tweet (long tweetID, Date timestamp, String source, String text) {
		this.tweetID = tweetID;
		this.timestamp = timestamp;
		this.source = source;
		this.text = text;
	}

	/**
	 * Constructor for a tweet that is not retweet, not a reply, and contains expanded urls.
	 * @param	tweetID 		the ID for this tweet
	 * @param 	timestamp 		a java.util.Date containing the date this tweet was posted
	 * @param	source 			a String containing the source identifier for the tweet
	 * @param	text 			a String containing the body text of the tweet
	 * @param	expandedURLs 	a ArrayList<String> containing the expanded URLs contained in the tweet
	 */
	public Tweet (long tweetID, Date timestamp, String source, String text, ArrayList<String> expandedURLs) {
		this.tweetID = tweetID;
		this.timestamp = timestamp;
		this.source = source;
		this.text = text;
		this.expandedURLs = expandedURLs;
	}

	/**
	 * Constructor for a retweet that contains no expanded urls.
	 * @param	tweetID 				the ID for this tweet
	 * @param 	timestamp 				a java.util.Date containing the date this tweet was posted
	 * @param	source 					a String containing the source identifier for the tweet
	 * @param	text 					the body text of the tweet
	 * @param	retweetedUserID 		the ID for the user who originally posted this status
	 * @param	retweetedStatusID			the ID of the original status
	 * @param	retweetedStatusTimestamp 	the timestamp of the original status
	 */
	public Tweet (long tweetID, Date timestamp, String source, String text, long retweetedUserID, long retweetedStatusID, Date retweetedStatusTimestamp) {
		this.tweetID = tweetID;
		this.timestamp = timestamp;
		this.source = source;
		this.text = text;
		this.retweetedStatusID = retweetedStatusID;
		this.retweetedUserID = retweetedUserID;
		this.retweetedStatusTimestamp = retweetedStatusTimestamp;
	}

	/**
	 * Constructor for a retweet that contains urls.
	 * @param	tweetID 				the ID for this tweet
	 * @param 	timestamp 				a java.util.Date containing the date this tweet was posted
	 * @param	source 					a String containing the source identifier for the tweet
	 * @param	text 					the body text of the tweet
	 * @param	retweetedUserID 		the ID for the user who originally posted this status
	 * @param	retweetedStatusID		the ID of the original status
	 * @param	retweetedStatusTimestamp 	the timestamp of the original status
	 * @param	expandedURLs 			a String containing the expanded URLs contained in the tweet
	 */
	public Tweet (long tweetID, Date timestamp, String source, String text, long retweetedUserID, long retweetedStatusID, Date retweetedStatusTimestamp, ArrayList<String> expandedURLs) {
		this.tweetID = tweetID;
		this.timestamp = timestamp;
		this.source = source;
		this.text = text;
		this.retweetedStatusID = retweetedStatusID;
		this.retweetedUserID = retweetedUserID;
		this.retweetedStatusTimestamp = retweetedStatusTimestamp;
		this.expandedURLs = expandedURLs;
	}

	/**
	 * Constructor for a reply that contains no expanded urls.
	 * @param	tweetID 			the ID for this tweet
	 * @param 	timestamp 			a java.util.Date containing the date this tweet was posted
	 * @param	source 				a String containing the source identifier for the tweet
	 * @param	text 				the body text of the tweet
	 * @param	inReplyToStatusID 	the ID of the status to which this is a reply
	 * @param	inReplyToUserID 	the ID of the user who posted the status to which this is a reply to
	 */
	public Tweet (long tweetID, Date timestamp, String source, String text, long inReplyToStatusID, long inReplyToUserID) {
		this.tweetID = tweetID;
		this.timestamp = timestamp;
		this.source = source;
		this.text = text;
		this.inReplyToUserID = inReplyToUserID;
		this.inReplyToStatusID = inReplyToStatusID;
	}


	/**
	 * Constructor for a reply with URLs
	 * @param	tweetID 			the ID for this tweet
	 * @param 	timestamp 			a java.util.Date containing the date this tweet was posted
	 * @param	source 				a String containing the source identifier for the tweet
	 * @param	text 				the body text of the tweet
	 * @param	inReplyToStatusID 	the ID of the status to which this is a reply
	 * @param	inReplyToUserID 	the ID of the user who posted the status to which this is a reply to
	 * @param	expandedURLs 		a String containing the expanded URLs contained in the tweet
	 */
	public Tweet (long tweetID, Date timestamp, String source, String text, long inReplyToStatusID, long inReplyToUserID, ArrayList<String> expandedURLs) {
		this.tweetID = tweetID;
		this.timestamp = timestamp;
		this.source = source;
		this.text = text;
		this.inReplyToUserID = inReplyToUserID;
		this.inReplyToStatusID = inReplyToStatusID;
		this.expandedURLs = expandedURLs;
	}
//=============================================================================

//=============================================================================
// BOOLEAN (analytical) METHODS

	/**
	 * isReply()
	 * @return	true if this Tweet is a reply, false otherwise
	 */
	public boolean isReply() {
		return (inReplyToStatusID != 0) && (inReplyToUserID != 0);
	}

	/**
	 * isRetweet()
	 * @return	true if this Tweet is a retweet, false otherwise
	 */
	public boolean isRetweet() {
		return (retweetedUserID != 0) && (retweetedStatusID != 0) && (retweetedStatusTimestamp != null);
	}

	/**
	 * containsURLs()
	 * @return	true if this Tweet contains URLs, false otherwise
	 */
	public boolean containsURLs() {
		return expandedURLs != null;
	}
//=============================================================================

//=============================================================================
// GETTERS & SETTERS

	public long getTweetID(){
	  return tweetID;
	}
	 
	public void setTweetID(long tweetID){
	  this.tweetID = tweetID;
	}

	public long getInReplyToStatusID(){
	   return inReplyToStatusID;
	}
	  
	public void setInReplyToStatusID(long inReplyToStatusID){
	   this.inReplyToStatusID = inReplyToStatusID;
	}

	public long getInReplyToUserID(){
	    return inReplyToUserID;
	}
	   
	public void setInReplyToUserID(long inReplyToUserID){
	    this.inReplyToUserID = inReplyToUserID;
	}

	public long getRetweetedStatusID(){
	  return retweetedStatusID;
	}
	 
	public void setRetweetedStatusID(long retweetedStatusID){
	  this.retweetedStatusID = retweetedStatusID;
	}

	public long getRetweetedUserID(){
	  return retweetedUserID;
	}

	public void setRetweetedUserID(long retweetedUserID){
	  this.retweetedUserID = retweetedUserID;
	}

	public Date getTimestamp(){
	  return timestamp;
	}
	 
	public void setTimestamp(Date timestamp){
	  this.timestamp = timestamp;
	}

	public Date getRetweetedStatusTimestamp(){
	   return retweetedStatusTimestamp;
	}
	  
	public void setRetweetedStatusTimestamp(Date retweetedStatusTimestamp){
	   this.retweetedStatusTimestamp = retweetedStatusTimestamp;
	}

	public String getSource(){
		return source;
	}
	 
	public void setSource(String source){
		this.source = source;
	}

	public String getText(){
		return text;
	}
	 
	public void setText(String text){
		this.text = text;
	}

	public ArrayList<String> getExpandedURLs(){
		return expandedURLs;
	}
	 
	public void setExpandedURLs(ArrayList<String> expandedURLs){
		this.expandedURLs = expandedURLs;
	}

}