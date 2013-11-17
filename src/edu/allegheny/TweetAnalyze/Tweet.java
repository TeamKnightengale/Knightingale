/*
 *  _____                    _     _               _               
 * /__   \__      _____  ___| |_  /_\  _ __   __ _| |_   _ _______ 
 *  / /\/\ \ /\ / / _ \/ _ \ __|//_\\| '_ \ / _` | | | | |_  / _ \
 * / /    \ V  V /  __/  __/ |_/  _  \ | | | (_| | | |_| |/ /  __/
 * \/      \_/\_/ \___|\___|\__\_/ \_/_| |_|\__,_|_|\__, /___\___|
 * Open-source Twitter analytics    		        |___/    
 *
 */

package		edu.allegheny.TweetAnalyze;

import 		java.util.Date;

/**
 * Tweet.java: Represents a single Tweet.
 * 
 * @author	Hawk Weisman
 * @version	1.0
 */
public Tweet () {

	private long tweetID;

	private long inReplyToStatusID;
	private long inReplyToUserID;

	private	long retweetedStatusID;
	private long retweetedUserID;

	private Date timestamp;

	private Date retweetedStatusTimestamp;

	private String source;
	private String text;
	private String expandedURLs;

//=============================================================================
// CONSTRUCTORS

	/**
	 * Constructor for a tweet that is not a retweet, not a reply, and contains no expanded urls.
	 */
	public Tweet (long tweetID, Date timestamp, String source, String text) {
		this.tweetID = tweetID;
		this.timestamp = timestamp;
		this.source = source;
		this.text = text;
	}

	/**
	 * Constructor for a tweet that is not retweet, not a reply, and contains expanded urls.
	 */
	public Tweet (long tweetID, Date timestamp, String source, String text, String expandedURLs) {
		this.tweetID = tweetID;
		this.timestamp = timestamp;
		this.source = source;
		this.text = text;
		this.expandedURLs = expandedURLs;
	}

	/**
	 * Constructor for a retweet that contains no expanded urls.
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
	 */
	public Tweet (long tweetID, Date timestamp, String source, String text, long retweetedUserID, long retweetedStatusID, Date retweetedStatusTimestamp, String expandedURLs) {
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
	 */
	public Tweet (long tweetID, Date timestamp, String source, String text, long inReplyToStatusID, long inReplyToUserID) {
		this.tweetID = tweetID;
		this.timestamp = timestamp;
		this.source = source;
		this.text = text;
		this.expandedURLs = expandedURLs;
		this.inReplyToUserID = inReplyToUserID;
		this.inReplyToStatusID = inReplyToStatusID;
	}


	/**
	 * Constructor for a reply with URLs
	 */
	public Tweet (long tweetID, Date timestamp, String source, String text, long inReplyToStatusID, long inReplyToUserID, String expandedURLs) {
		this.tweetID = tweetID;
		this.timestamp = timestamp;
		this.source = source;
		this.text = text;
		this.expandedURLs = expandedURLs;
		this.inReplyToUserID = inReplyToUserID;
		this.inReplyToStatusID = inReplyToStatusID;
		this.expandedURLs = expandedURLs;
	}
//=============================================================================

//=============================================================================
// BOOLEAN METHODS

	/**
	 * isReply()
	 * @return	true if this Tweet is a reply, false otherwise
	 */
	public boolean isReply() {
		return (inReplyToStatusID != null) && (inReplyToUserID != null);
	}

	/**
	 * isRetweet()
	 * @return	true if this Tweet is a retweet, false otherwise
	 */
	public boolean isReply() {
		return (retweetedUserID != null) && (retweetedStatusID != null) && (retweetedStatusTimestamp != null);
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
	  return property;
	}
	 
	public void setTweetID(long property){
	  this.property = property;
	}

	public long getInReplyToStatusID(){
	   return property;
	}
	  
	public void setInReplyToStatusID(long property){
	   this.property = property;
	}

	public long getInReplyToUserID(){
	    return property;
	}
	   
	public void setInReplyToUserID(long property){
	    this.property = property;
	}

}