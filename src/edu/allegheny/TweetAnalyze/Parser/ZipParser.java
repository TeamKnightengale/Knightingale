/*
 *  _____                    _     _               _               
 * /__   \__      _____  ___| |_  /_\  _ __   __ _| |_   _ _______ 
 *  / /\/\ \ /\ / / _ \/ _ \ __|//_\\| '_ \ / _` | | | | |_  / _ \
 * / /    \ V  V /  __/  __/ |_/  _  \ | | | (_| | | |_| |/ /  __/
 * \/      \_/\_/ \___|\___|\__\_/ \_/_| |_|\__,_|_|\__, /___\___|
 * Open-source Twitter analytics    		        |___/    
 *
 */

package edu.allegheny.TweetAnalyze.Parser;

import	java.io.File;
import	java.util.ArrayList;
import	java.io.IOException;

import	edu.allegheny.TweetAnalyze.Tweet;

import	net.lingala.zip4j.core.ZipFile;
import	net.lingala.zip4j.exception.ZipException;

import	org.apache.logging.log4j.LogManager;
import	org.apache.logging.log4j.Logger;

/**
 * Contains methods for expanding zipped Twitter archives, locating Tweet CSV 
 * files, and controlling the Twitter archive import process.
 *
 * @author	Hawk Weisman
 * @version	0.1
 * @see		edu.allegheny.TweetAnalyze.Parser.CSVParser
 */
public class ZipParser {

	private static Logger logger = LogManager.getFormatterLogger(ZipParser.class.getName());

	/**
	 * Constructor: instantiates a new ZipParser
	 */
	public ZipParser () {
	}

	/**
	 * Parse: runs the complete parsing process.
	 * 
	 * @param	target	a java.io.File object containing a path to the target Zip file.
	 * @return	an ArrayList containing the Tweets from the Tweets.csv file in the target Zip.
	 */
	public ArrayList<Tweet> parse (File target) {

		try {
				if (logger.isDebugEnabled()) {
					logger.debug("ZipParser: Target file is %s", target);
				}
			
				File 			temp 	= new File("TweetAnalyzeTemp/tweets.csv");
				ZipFile 		archive = new ZipFile(target);
				CSVParser 		parser  = new CSVParser();
				ArrayList<Tweet>output;	

				// Extract the CSV file from the archive
				extractTweetsCSV(archive, temp);

				if (logger.isDebugEnabled()) {
					logger.debug("ZipParser: Tweets.csv extracted, passing control to CSVParser");
				}
				
				// Return the Tweet array list from the parser ParseFile method
				output = parser.parseFile(temp);

				if (temp.delete() = false) {
					logger.error("ZipParser: failed to delete temporary file %s", temp.getPath());
				} else if (logger.isDebugEnabled()) {
					logger.debug("ZipParser: deleted temporary file %s", temp.getPath());
				}

				return output;
		} 

		// Log any ZipExceptions to error with stack traces.
		catch (ZipException e) {
			logger.error("ZipParser: Caught ZipException", e);
		}

		// Log any IOExceptions to error with stack traces
		catch (IOException e) {
			logger.error("ZipParser: Caught IOException", e);
		}

		// Log any other exceptions with stack traces
		catch (Exception e) {
			logger.error("ZipParser: Caught an unexpected exception", e);
		}
	}

	/**
	 * extractTweetsCSV(): extracts the Tweets.csv file from a tweets.zip archive
	 *
	 * @param	target 	the ZipFile to extract the Tweets.csv from
	 * @param	dest 	the directory into which the file is to be extracted
	 */
	private void extractTweetsCSV (ZipFile target, File dest) throws ZipException {

		if (logger.isDebugEnabled()) {
			logger.debug("ZipParser: extracting Tweets.csv from %s", target);
		}

		target.extractFile("tweets.csv", dest.getPath());
	}
}
