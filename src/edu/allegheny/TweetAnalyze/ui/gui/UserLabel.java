package edu.allegheny.TweetAnalyze.ui.gui;

import java.net.URI;
import java.net.URISyntaxException;

import java.awt.Desktop;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.mcavallo.opencloud.Tag;

public class UserLabel extends JPanel implements MouseListener {

	protected Desktop desktop;
	int nbOver = 0, nbClick = 0;
	JLabel label;
	Tag tag;
	
	public UserLabel(Tag tag) {
		super();
	    label = new JLabel(tag.getName());
	    this.tag = tag;
	    label.addMouseListener(this);
	    add(label);
		desktop = Desktop.getDesktop();

		label.setOpaque(false);
	    label.setFont(label.getFont().deriveFont((float) tag.getWeight() * 10));
	}

	@Override
	public void mouseEntered(MouseEvent e){
		label.setForeground(Color.BLUE);
	}

	@Override
	public void mouseExited(MouseEvent e){
		label.setForeground(Color.BLACK);
	}

    @Override
	public void mouseClicked(MouseEvent e) {    
	   	try {       
	        desktop.browse(new URI("http://twitter.com/"+label.getText().substring(1)));
	    } catch (Exception ex) {
	    	ex.printStackTrace(); // log this later
	    }
	}

	@Override
	public void mouseReleased(MouseEvent e) {
    }   

	@Override
   	public void mousePressed(MouseEvent e) {
   	}  
}
