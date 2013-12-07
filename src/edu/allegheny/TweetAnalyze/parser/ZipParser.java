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

	private final static Logger LOGGER = Logger.getLogger(ZipParser.class.getName());

	/**
	 * Parse: runs the complete parsing process.
	 * 
	 * @param	target	a java.io.File object containing a path to the target Zip file.
	 * @return	an ArrayList containing the Tweets from the Tweets.csv file in the target Zip.
	 */
	public static List<Tweet> parse (File target) {

		List<Tweet> output = null;

		try {
				LOGGER.fine("ZipParser: Target file is " + target+ ".");
			
				File 		tempDir = new File(getRelativePath() + "/TweetAnalyzeTemp/");
				File 		temp = new File (getRelativePath() + "/TweetAnalyzeTemp/tweets.csv");
				ZipFile 	archive = new ZipFile(target);

				// Extract the CSV file from the archive
				extractTweetsCSV(archive, tempDir);

				LOGGER.fine("ZipParser: Tweets.csv extracted, passing control to CSVParser.");
				
				// Return the Tweet array list from the parser ParseFile method
				output = CSVParser.parseFile(temp);

				if (temp.delete() == false) {
					LOGGER.severe("ZipParser: failed to delete temporary file " + temp.getPath() + ".");
				} else {
					LOGGER.fine("ZipParser: deleted temporary file " + temp.getPath() + ".");
				}				return output;
		} 

		// Log any ZipExceptions to error with stack traces.
		catch (ZipException e) {
			LOGGER.severe("ZipParser: Caught ZipException while decompressing tweets.zip\n" + e);
		}

		// Log any IOExceptions to error with stack traces
		catch (IOException e) {
			LOGGER.severe("ZipParser: Caught IOException while opening tweets.csv\n" + e);
		}

		// Log any ParseExceptions to error with stack traces
		catch (ParseException e) {
			LOGGER.severe("ZipParser: Caught a ParseException while parsing dates from tweets.csv\n" + e);
		}

		// Log any other exceptions with stack traces
		catch (Exception e) {
			LOGGER.severe("ZipParser: Caught an unexpected exception\n" + e);
		}

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

		LOGGER.fine("ZipParser: extracting Tweets.csv from " + target. + ".");

		target.extractFile("tweets.csv", dest.getPath());
	}

	private static String getRelativePath () {
		return new File("").getAbsolutePath().toString();
	}
}
