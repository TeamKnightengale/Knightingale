package edu.allegheny.TweetAnalyze.ui.gui;

import edu.allegheny.TweetAnalyze.analytics.ComplexAnalytics;
import edu.allegheny.TweetAnalyze.ui.gui.UserLabel;
import edu.allegheny.TweetAnalyze.ui.FrequencyVisualization;

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

public class UserCloud implements FrequencyVisualization{

	protected Map<User, Integer> contents;
	protected JFrame frame;
	protected JPanel panel;
	protected Cloud cloud;

	public UserCloud (Map<User, Integer> users, String title) {
		contents = users;
		frame = new JFrame(title);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    panel = new JPanel();
	    cloud = new Cloud();

	    for (Map.Entry<User, Integer> entry : contents.entrySet())
	        cloud.addTag(new Tag("@" + entry.getKey().getScreenName(), entry.getValue()));

        for (Tag tag : cloud.tags()) {
	        final UserLabel label = new UserLabel(tag);
	       	panel.add(label);
		}

	    frame.add(panel);
	    frame.setSize(800, 600);
	}

    public void visualize() {
		frame.setVisible(true);
    }

    public static void main(String[] argv) {
    	try {
	   	    FrequencyVisualization a = new UserCloud(ComplexAnalytics.getGlobalRetweetFrequency(), "Demo RetweetCloud");
	   	   	FrequencyVisualization b = new UserCloud(ComplexAnalytics.getGlobalReplyFrequency(), "Demo ReplyCloud");
	    	
	    	a.visualize();
	    	b.visualize();

	    } catch (Exception ex) {
	    	System.out.println ("Something bad happened in a demo method. If you see this message in a production build, "
	    	                    + "find Hawk Weisman and let him know");
	    }
    }
}