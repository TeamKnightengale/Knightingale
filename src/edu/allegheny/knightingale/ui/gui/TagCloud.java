//     __ __     _      __   __  _                __   
//    / //_/__  (_)__ _/ /  / /_(_)__  ___ ____ _/ /__ 
//   / ,< / _ \/ / _ `/ _ \/ __/ / _ \/ _ `/ _ `/ / -_)
//  /_/|_/_//_/_/\_, /_//_/\__/_/_//_/\_, /\_,_/_/\__/ 
//              /___/                /___/          
//  Open-source Twitter analytics...with style!

package edu.allegheny.knightingale.ui.gui;

import edu.allegheny.knightingale.analytics.FrequencyAnalyzer;
import edu.allegheny.knightingale.ui.gui.HashtagLabel;
import edu.allegheny.knightingale.ui.FrequencyVisualization;

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

/**
 * Swing frequency visualization for hashtags.
 *
 * Please note that, as a GUI element, this class is not a target 
 * for test automation, as it will be tested manually during the 
 * user testing and acceptance testing phases.
 *
 * @author 	Hawk Weisman
 * @version 1.0
 * @since 	December 7th, 2013
 */
public class TagCloud implements FrequencyVisualization{

	protected Map<String, Integer> contents;
	protected JFrame frame;
	protected JPanel panel;
	protected Cloud cloud;

	/**
	 * @param users A frequency map of Strings representing hashtags.
	 * @param title A title for the UserCloud window
	 */
	public TagCloud (Map<String, Integer> hashtags, String title) throws IOException {
		contents = hashtags;
		frame = new JFrame(title);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    panel = new JPanel();
	    cloud = new Cloud();

	    for (Map.Entry<String, Integer> entry : contents.entrySet())
	        cloud.addTag(new Tag(entry.getKey(), entry.getValue()));

        for (Tag tag : cloud.tags()) {
	        final HashtagLabel label = new HashtagLabel(tag);
	       	panel.add(label);
		}

		cloud.setMinWeight(findMinimumWeight()); 

	    frame.add(panel);
	    frame.setSize(800, 600);
	}
	
	/**
	 * Executes the visualization.
	 */
    public void visualize() {
		frame.setVisible(true);
    }

    private double findMinimumWeight() {
    	double lowestWeight = 1.0;

    	for (Tag tag : cloud.tags()) {
    		if (tag.getWeight() < lowestWeight)
    			lowestWeight = tag.getWeight();
    	}
    	return lowestWeight;
    }
}