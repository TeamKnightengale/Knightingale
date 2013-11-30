package edu.allegheny.TweetAnalyze.Parser;

import au.com.bytecode.opencsv.*;
import edu.allegheny.TweetAnalyze.Tweet;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import java.text.SimpleDateFormat;
import java.text.ParseException;

//import edu.allegheny.TweetAnalyze.Parser.Visitor.*;
/**
 * @authpr Gabe Kelly
 * @author Hawk Weisman
 * @1.0
 */

public class CSVParser {

	public CSVParser() {
	}

	public ArrayList<Tweet> parseFile(File c) throws IOException, ParseException {

		SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss z");

		CSVReader reader = new CSVReader(new FileReader(c));

		ArrayList<String[]> tweetData = (ArrayList<String[]>) reader.readAll();

		String[] lonetweet = new String[tweetData.size()];

		ArrayList<Tweet> tweetAnalyze = new ArrayList<Tweet>();

		for (int i = 1; i < tweetData.size(); i++)
		{
			lonetweet = tweetData.get(i);

			Long Id = new Long(lonetweet[0]);

			long id = Id.longValue();

			Date timeStamp = timestampFormat.parse(lonetweet[3]);

			String source = lonetweet[4];

			String text = lonetweet[5];

					 if (lonetweet[1].isEmpty() == false)//will replace at somepoint with visitor!
					 {
					 	Long ReplyStatus = new Long(lonetweet[1]);

					 	long replyStatus = ReplyStatus.longValue();

					 	Long ReplyUser = new Long(lonetweet[2]);

					 	long replyUser = ReplyUser.longValue();

					 	if (lonetweet[9].isEmpty() == false)
					 	{
					 		ArrayList<String> url = new ArrayList<String>(Arrays.asList(lonetweet[9].split(",")));

					 		Tweet tweet = new Tweet (id, timeStamp, source, text, replyStatus, replyUser, url);
					 		tweetAnalyze.add(tweet);

					 	}
					 	else
					 	{
					 		Tweet tweet = new Tweet (id, timeStamp, source, text, replyStatus, replyUser);
					 		tweetAnalyze.add(tweet);

					 	}
					 }
					 else{
					 	if (lonetweet[6].isEmpty() == false)
					 	{

					 		Long RetweetId = new Long(lonetweet[6]);
					 		long retweetId = RetweetId.longValue();

					 		Long RetweetUser = new Long(lonetweet[7]);
					 		long retweetUser = RetweetUser.longValue();

					 		Date retweetDate = timestampFormat.parse(lonetweet[8]);


					 		if (lonetweet[9].isEmpty() == false)
					 		{
					 			ArrayList<String> url = new ArrayList<String>(Arrays.asList(lonetweet[9].split(",")));

					 			Tweet tweet = new Tweet (id, timeStamp, source, text, retweetId, retweetUser, retweetDate, url);
					 			tweetAnalyze.add(tweet);

					 		}
					 		else
					 		{
					 			Tweet tweet = new Tweet (id, timeStamp, source, text, retweetId, retweetUser, retweetDate);
					 			tweetAnalyze.add(tweet);

					 		}
					 	}

					 	else{
					 		if (lonetweet[9].isEmpty() == false)
					 		{
					 			ArrayList<String> url = new ArrayList<String>(Arrays.asList(lonetweet[9].split(",")));

					 			Tweet tweet = new Tweet (id, timeStamp, source, text, url);
					 			tweetAnalyze.add(tweet);

					 		}
					 		else
					 		{
					 			Tweet tweet = new Tweet (id, timeStamp, source, text);
					 			tweetAnalyze.add(tweet);

					 		}
					 	}
					 }


					}
					return tweetAnalyze;
				}

			}
