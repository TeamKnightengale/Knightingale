package edu.allegheny.TweetAnalyze.analytics;

import edu.allegheny.TweetAnalyze.*;
import edu.allegheny.TweetAnalyze.database.DatabaseHelper;

import java.util.Iterator;
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

public class ComplexAnalytics {

	public static void main(String argv[]) {
		
		try {
			
			Map<User, Integer> globalReplyFrequency = getGlobalReplyFrequency();

			for (Map.Entry<User, Integer> entry : globalReplyFrequency.entrySet())
       			System.out.println(entry.getKey().getScreenName() + " has been retweeted " + entry.getValue() + " times.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private static Twitter twitter = TwitterFactory.getSingleton();

	public static HashMap<User, Integer> getGlobalReplyFrequency () throws SQLException, ParseException, TwitterException{

		HashMap<User, Integer> frequencyMap = new HashMap<User, Integer>();

		List<Long> repliedIDs = SimpleAnalytics.repliedToUsers();

		for (Long id : repliedIDs) {
			frequencyMap.put(twitter.showUser(id), new Integer(SimpleAnalytics.getReplyCount(id)));
		}

		return frequencyMap;
	}
}