/*
 *  _____                    _     _               _               
 * /__   \__      _____  ___| |_  /_\  _ __   __ _| |_   _ _______ 
 *  / /\/\ \ /\ / / _ \/ _ \ __|//_\\| '_ \ / _` | | | | |_  / _ \
 * / /    \ V  V /  __/  __/ |_/  _  \ | | | (_| | | |_| |/ /  __/
 * \/      \_/\_/ \___|\___|\__\_/ \_/_| |_|\__,_|_|\__, /___\___|
 * Open-source Twitter analytics    		        |___/    
 *
 */

package edu.allegheny.TweetAnalyze.parser;

import	java.io.File;
import	java.io.IOException;
import	java.util.List;
import	java.util.ArrayList;
import	java.util.Formatter;
import	java.text.ParseException;

import	edu.allegheny.TweetAnalyze.Tweet;

import	net.lingala.zip4j.core.ZipFile;
import	net.lingala.zip4j.exception.ZipException;

import	java.util.logging.Logger;

/**
 * Contains methods for expanding zipped Twitter archives, locating Tweet CSV 
 * files, and controlling the Twitter archive import process.
 *
 * @author	Hawk Weisman
 * @version	1.0
 * @see		edu.allegheny.TweetAnalyze.Parser.CSVParser
 */
public class ZipParser {

	public static Logger logger = Logger.getLogger(ZipParser.class.getName());
	private static File tempDir, temp;

	/**
	 * Parse: runs the complete parsing process.
	 * 
	 * @param	target	a java.io.File object containing a path to the target Zip file.
	 * @return	an ArrayList containing the Tweets from the Tweets.csv file in the target Zip.
	 */
	public static List<Tweet> parse (File target) {

		List<Tweet> output = null;

		try {
				logger.fine("ZipParser: Target file is " + target + ".");
			
				tempDir = new File(getRelativePath() + "/TweetAnalyzeTemp/");
				temp = new File (getRelativePath() + "/TweetAnalyzeTemp/tweets.csv");
				ZipFile archive = new ZipFile(target);

				// Extract the CSV file from the archive
				extractTweetsCSV(archive, tempDir);

				logger.fine("ZipParser: Tweets.csv extracted, passing control to CSVParser.");
				
				// Return the Tweet array list from the parser ParseFile method
				output = CSVParser.parseFile(temp);

		} 

		// Log exceptions
		catch (Exception e) {
			logger.severe("ZipParser: Caught an unexpected exception\n" + e);
		}

		cleanUp(tempDir, temp);

		return output;
	}

	/**
	 * extractTweetsCSV(): extracts the Tweets.csv file from a tweets.zip archive
	 *
	 * @param	target 	the ZipFile to extract the Tweets.csv from
	 * @param	dest 	the directory into which the file is to be extracted
	 * @throws	ZipException in the event of a problem unzipping the target
	 */
	public static void extractTweetsCSV (ZipFile target, File dest) throws ZipException {

		logger.fine("ZipParser: extracting Tweets.csv from " + target + ".");

		target.extractFile("tweets.csv", dest.getPath());
	}

	public static void cleanUp (File tempDir, File temp){
		if (temp.delete() == false) 
			logger.severe("ZipParser: failed to delete temporary file " + temp.getPath() + ".");
		else 
			logger.fine("ZipParser: deleted temporary file " + temp.getPath() + ".");
		

		if (tempDir.delete() == false) 
			logger.severe("ZipParser: failed to delete temporary directory" + tempDir.getPath() + ".");
		else 
			logger.fine("ZipParser: deleted temporary directory " + tempDir.getPath() + ".");
	}

	private static String getRelativePath () {
		return new File("").getAbsolutePath().toString();
	}
}
