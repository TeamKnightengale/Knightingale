package edu.allegheny.TweetAnalyze.Parser;

import au.com.bytecode.opencsv.*;
import edu.allegheny.TweetAnalyze.Tweet;
import edu.allegheny.TweetAnalyze.TweetBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import java.text.ParseException;

/**
 * @authpr	Gabe Kelly
 * @author	Hawk Weisman
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