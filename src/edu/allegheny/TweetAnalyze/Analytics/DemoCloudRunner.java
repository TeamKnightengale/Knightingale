package edu.allegheny.TweetAnalytics.analytics;

import edu.allegheny.analytics.*;

/**
 * @author Hawk Weisman
 * DEMO CLASS WILL BE REMOVED FROM PROJECT WHEN FUCTIONALITY IS IMPLEMENTED ELSEWHERE
 */

// DEMO MAIN METHOD PLEASE DELETE
public class DemoCloudRunner {
	public static void main(String[] argv) {
	    try {
	   	    	IFrequencyCloud a = new RetweetCloud(ComplexAnalytics.getGlobalRetweetFrequency());
	   	    	IFrequencyCloud b = new ReplyCloud(ComplexAnalytics.getGlobalReplyFrequency());
	    	
	    	    a.open();
	    	    b.open();

	    } catch (Exception e) {
	   		e.printStackTrace();
	   	}
	}
}