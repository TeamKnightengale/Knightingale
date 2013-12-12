package edu.allegheny.tweetanalyze.analytics;

import edu.allegheny.tweetanalyze.Tweet;
import edu.allegheny.tweetanalyze.database.DatabaseHelper;

//////////////////////////////////////////////////////
// REMOVE BEFORE FLIGHT 							//
import edu.allegheny.tweetanalyze.LogConfigurator;  //
//////////////////////////////////////////////////////

import java.util.Map;
import java.util.List;

public class Analyzer {

	protected DatabaseHelper db;

	public Analyzer(DatabaseHelper db)
	{
		this.db = db;
	}


	//////////////////////////////////////////////////////////////////
	// DEMO MAIN METHOD - SHOULD NOT OCCUR IN PRODUCTION BUILDS 	//
	// Remove before flight, handle with care, so on and so forth.	//
	// If this breaks, I warned you! ~ Hawk 						//
	//////////////////////////////////////////////////////////////////
	public static void main(String[] argv) {

		try {
			LogConfigurator.setup(); // setup the logger.

			FrequencyAnalyzer frequencies = new FrequencyAnalyzer(new DatabaseHelper());
			CompositionAnalyzer composition = new CompositionAnalyzer(new DatabaseHelper());
			UserAnalyzer users = new UserAnalyzer(new DatabaseHelper());
			//SearchAnalyzer search = new SearchAnalyzer(new DatabaseHelper());

			Map<String, Integer> globalReplyFrequency = frequencies.globalReplyFrequency();
			Map<String, Integer> globalRetweetFrequency = frequencies.globalRetweetFrequency();
			Map<String, Integer> globalHashtagFrequency = frequencies.globalHashtagFrequency();
		
			// System.out.println("Tweets having " + argv[0] + " : \n");
			// //List<Tweet> tweets = search.search(argv[0]);

			// for (Tweet t : tweets)
			// 	System.out.println(t.getText());
		
			System.out.printf("\n%d%% of your tweets are retweets, and %d%% are replies.\n", composition.percentRetweets(), composition.percentReplies());

			System.out.println("#-----Printing raw reply data:\n");

			for (Map.Entry<String, Integer> entry : globalReplyFrequency.entrySet())
       			System.out.println(entry.getKey() + " has been replied to " + entry.getValue() + " times.");

       		System.out.println("\n#-----Printing raw retweet data:\n");

       		for (Map.Entry<String, Integer> entry : globalRetweetFrequency.entrySet())
       			System.out.println(entry.getKey() + " has been retweeted " + entry.getValue() + " times.");

       		System.out.println("\n#-----Printing hashtag frequency data:\n");

       		for (Map.Entry<String, Integer> entry : globalHashtagFrequency.entrySet())
       			System.out.println(entry.getKey() + " has occured " + entry.getValue() + " times.");

		} catch (Exception ex) {
			System.err.println ("Something bad happened in a demo method. You should never see this message in production builds. "
	    	                    + "If you do, please find Hawk Weisman and let him know");
	    	ex.printStackTrace(System.err);
		}
	}
	//////////////////////////////////////////////////////////////////
	// END CODE TO BE REMOVED PRIOR TO RELEASE 						//
	//////////////////////////////////////////////////////////////////

}