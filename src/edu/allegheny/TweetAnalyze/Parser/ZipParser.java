/*
 *  _____                    _     _               _               
 * /__   \__      _____  ___| |_  /_\  _ __   __ _| |_   _ _______ 
 *  / /\/\ \ /\ / / _ \/ _ \ __|//_\\| '_ \ / _` | | | | |_  / _ \
 * / /    \ V  V /  __/  __/ |_/  _  \ | | | (_| | | |_| |/ /  __/
 * \/      \_/\_/ \___|\___|\__\_/ \_/_| |_|\__,_|_|\__, /___\___|
 * Open-source Twitter analytics    		        |___/    
 *
 */

package .edu.allegheny.TweetAnalyze.Parser;

import	java.io.File;
import 	java.nio.File;
import	java.util.ArrayList;

import edu.allegheny.TweetAnalyze.Tweet;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Contains methods for expanding zipped Twitter archives, locating Tweet CSV files, and controlling the Twitter archive import process.
 *
 * @author	Hawk Weisman
 * @version	0.1
 * @see		edu.allegheny.TweetAnalyze.Parser.CSVParser
 */
public class ZipParser {

	private static Logger logger = LogManager.getFormatterLogger();

	
	public ZipParser () {

	}

	/**
	 * Parse: runs the complete parsing process.
	 * 
	 * @param	target	a java.io.File object containing a path to the target Zip file.
	 * @return	an ArrayList containing the Tweets from the Tweets.csv file in the target Zip.
	 */
	public ArrayList<Tweet> parse (File target) throws ZipException {

		try {
				if (logger.isDebugEnabled()) {
					logger.debug("ZipParser: Target file is %s", target);
				}
		
				ZipFile archive = new ZipFile(target);
		
				extractTweetsCSV(archive);

				if (logger.isDebugEnabled()) {
					logger.debug("ZipParser: Tweets.csv extracted, passing control to CSVParser");
				}
		
				return CSVParser.parseTweets();

		} catch (ZipException e) {

		}
	}

	private void extractTweetsCSV (ZipFile target) throws ZipException {

		if (logger.isDebugEnabled()) {
			logger.debug("ZipParser: extracting Tweets.csv from %s", target);
		}

		target.extractFile("tweets.csv")
	}
}
