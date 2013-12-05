package edu.allegheny.TweetAnalyze.analytics.visualization;

import edu.allegheny.TweetAnalyze.analytics.ComplexAnalytics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

import java.util.Map;
import java.util.HashMap;

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
	        final JLabel label = new JLabel(tag.getName());
	        label.setOpaque(false);
	        label.setFont(label.getFont().deriveFont((float) tag.getWeight() * 10));
	        panel.add(label);
		}

	    frame.add(panel);
	    frame.setSize(800, 600);
	}

    public void visualize() {
		frame.setVisible(true);
    }
}