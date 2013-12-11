package edu.allegheny.TweetAnalyze;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogConfigurator {

  	static private FileHandler textFile;
  	static private SimpleFormatter textFormat;

  	static public void setup() throws IOException {

	    // Get the global logger to configure it
	    Logger logger = Logger.getLogger("");

	  	textFile = new FileHandler("TweetAnalyze.log");

	    textFormat = new SimpleFormatter();
	    textFile.setFormatter(textFormat);
	    textFile.setLevel(Level.ALL);

	    logger.addHandler(textFile);
	    logger.setLevel(Level.ALL);
 	}
}
 