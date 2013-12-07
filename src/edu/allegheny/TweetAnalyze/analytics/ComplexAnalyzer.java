package edu.allegheny.TweetAnalyze.analytics;

import edu.allegheny.TweetAnalyze.database.DatabaseHelper;

import edu.allegheny.TweetAnalyze.analytics.*;
import edu.allegheny.TweetAnalyze.analytics.visualization.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.sql.SQLException;
import java.text.ParseException;

import twitter4j.*;

/**
 * Provides complex analytic methods for the TweetAnalyze database.
 *
 * @author	Hawk Weisman
 * @version	1.0
 * @since	December 4, 2013
 */

public class ComplexAnalyzer {

	private static Twitter twitter = TwitterFactory.getSingleton();
	private SimpleAnalyzer simpleAnalyzer;
	

	public ComplexAnalyzer(DatabaseHelper db)
	{
		this.simpleAnalyzer = new SimpleAnalyzer(db);
	}

	public static void main(String[] argv) {
		try {

			ComplexAnalyzer analyzer = new ComplexAnalyzer(new DatabaseHelper());

			Map<User, Integer> globalReplyFrequency = analyzer.getGlobalReplyFrequency();
			Map<User, Integer> globalRetweetFrequency = analyzer.getGlobalRetweetFrequency();

			System.out.println("#-----Printing raw reply data:\n");

			for (Map.Entry<User, Integer> entry : globalReplyFrequency.entrySet())
       			System.out.println(entry.getKey().getScreenName() + " has been replied to " + entry.getValue() + " times.");

       		System.out.println("\n#-----Printing raw reply data:\n");

       		for (Map.Entry<User, Integer> entry : globalRetweetFrequency.entrySet())
       			System.out.println(entry.getKey().getScreenName() + " has been retweeted " + entry.getValue() + " times.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return a HashMap<User, Integer> where the Integers associated with each user represents the number of times that user has been replied to.
	 */
	public HashMap<User, Integer> getGlobalReplyFrequency () throws SQLException, ParseException, TwitterException{

		HashMap<User, Integer> frequencyMap = new HashMap<User, Integer>();
		List<Long> repliedIDs = simpleAnalyzer.repliedToUsers();

		for (Long id : repliedIDs) {
			frequencyMap.put(twitter.showUser(id), new Integer(simpleAnalyzer.getReplyCount(id)));
		}

		return frequencyMap;
	}

	/**
	 * @return a HashMap<User, Integer> where the Integers associated with each user represents the number of times that user has been retweeted to.
	 */
	public HashMap<User, Integer> getGlobalRetweetFrequency () throws SQLException, ParseException, TwitterException{

		HashMap<User, Integer> frequencyMap = new HashMap<User, Integer>();

		List<Long> retweetedIDs = simpleAnalyzer.retweetedUsers();

		for (Long id : retweetedIDs) {
			frequencyMap.put(twitter.showUser(id), new Integer(simpleAnalyzer.getRetweetCount(id)));
		}

		return frequencyMap;
	}
}