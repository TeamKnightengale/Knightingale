//     __ __     _      __   __  _                __   
//    / //_/__  (_)__ _/ /  / /_(_)__  ___ ____ _/ /__ 
//   / ,< / _ \/ / _ `/ _ \/ __/ / _ \/ _ `/ _ `/ / -_)
//  /_/|_/_//_/_/\_, /_//_/\__/_/_//_/\_, /\_,_/_/\__/ 
//              /___/                /___/          
//  Open-source Twitter analytics...with style!

package edu.allegheny.tweetanalyze.parser;

import au.com.bytecode.opencsv.*;
import edu.allegheny.tweetanalyze.Tweet;
import edu.allegheny.tweetanalyze.TweetBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * @authpr	Gabe Kelly
 * @author	Hawk Weisman
 * @throws	IOException in the event of a problem finding the file to be parsed.
 * @version	2.0
 * @since	November 30, 2013
 */

public class CSVParser {


	public static List<Tweet> parseFile(File c) throws IOException, ParseException {

		CSVReader reader = new CSVReader(new FileReader(c));

		ArrayList<String[]> allLines = (ArrayList<String[]>) reader.readAll();

		ArrayList<Tweet> tweets = new ArrayList<Tweet>();

		for (String[] line : allLines.subList(1, allLines.size())) { 
			tweets.add(TweetBuilder.buildTweet(line));
		}
 
		return tweets;
	}
}