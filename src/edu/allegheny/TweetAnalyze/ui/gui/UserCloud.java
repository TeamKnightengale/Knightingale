package edu.allegheny.TweetAnalyze.ui.gui;

import edu.allegheny.TweetAnalyze.analytics.FrequencyAnalyzer;

import edu.allegheny.TweetAnalyze.ui.gui.UserLabel;
import edu.allegheny.TweetAnalyze.ui.FrequencyVisualization;
import edu.allegheny.TweetAnalyze.database.DatabaseHelper;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

import java.util.Map;
import java.util.HashMap;

import java.io.IOException;

import twitter4j.*;

import edu.allegheny.TweetAnalyze.LogConfigurator; 	// REMOVE WHEN MAIN METHOD IS REMOVED
												   	// REMOVE BEFORE FLIGHT
/**
 * Swing frequency visualization for Twitter users.
 *
 * Please note that, as a GUI element, this class is not a target 
 * for test automation, as it will be tested manually during the 
 * user testing and acceptance testing phases.
 *
 * @author 	Hawk Weisman
 * @version 1.0
 * @since 	December 11th, 2013
 */
public class UserCloud implements FrequencyVisualization{

	protected Map<String, Integer> contents;
	protected JFrame frame;
	protected JPanel panel;
	protected Cloud cloud;

	/**
	 * @param users A frequency map of Twitter usernames (as Strings)
	 * @param title A title for the UserCloud window
	 */
	public UserCloud (Map<String, Integer> users, String title) {
		contents = users;
		frame = new JFrame(title);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    panel = new JPanel();
	    cloud = new Cloud();

	    for (Map.Entry<String, Integer> entry : contents.entrySet())
	        cloud.addTag(new Tag("@" + entry.getKey(), entry.getValue()));

        for (Tag tag : cloud.tags()) {
	        final UserLabel label = new UserLabel(tag);
	       	panel.add(label);
		}

	    frame.add(panel);
	    frame.setSize(800, 600);
	}

	/**
	 * Executes the visualization.
	 */
    public void visualize() {
		frame.setVisible(true);
    }

    /**
     * USER-TESTING MAIN METHOD FOR RUNNING DEMOS FROM ANT TARGETS - PLEASE IGNORE
     * This method should not be called in production builds. It may be retained in alphas for testing purposes.
     */
    public static void main(String[] argv) {

    	try {
    		FrequencyAnalyzer analyzer = new FrequencyAnalyzer(new DatabaseHelper());
    		LogConfigurator.setup(); // setup the logger.
	   	    FrequencyVisualization a = new UserCloud(analyzer.getGlobalRetweetFrequency(), "Demo RetweetCloud");
	   	   	FrequencyVisualization b = new UserCloud(analyzer.getGlobalReplyFrequency(), "Demo ReplyCloud");
	    	
	    	a.visualize();
	    	b.visualize();

	    } catch (Exception ex) {
	    	System.err.println ("Something bad happened in a demo method. You should never see this message in production builds. "
	    	                    + "If you do, please find Hawk Weisman and let him know");
	    	ex.printStackTrace(System.err);
	    }
    }
}