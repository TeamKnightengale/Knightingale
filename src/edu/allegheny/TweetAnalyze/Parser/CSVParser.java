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

		ArrayList<String[]> twtData = (ArrayList<String[]>) reader.readAll();

		String[] loneTwt = new String[twtData.size()];

		ArrayList<Tweet> twtAnalyze = new ArrayList<Tweet>();

		for (int i = 1; i < twtData.size(); i++)
		{
			loneTwt = twtData.get(i);

			Long Id = new Long(loneTwt[0]);

			long id = Id.longValue();

			Date timeStamp = timestampFormat.parse(loneTwt[3]);

			String source = loneTwt[4];

			String text = loneTwt[5];

					 if (loneTwt[1].isEmpty() == false)//will replace at somepoint with visitor!
					 {
					 	Long ReplyStatus = new Long(loneTwt[1]);

					 	long replyStatus = ReplyStatus.longValue();

					 	Long ReplyUser = new Long(loneTwt[2]);

					 	long replyUser = ReplyUser.longValue();

					 	if (loneTwt[9] != "")
					 	{
					 		ArrayList<String> url = new ArrayList<String>(Arrays.asList(loneTwt[9].split(",")));

					 		Tweet twt = new Tweet (id, timeStamp, source, text, replyStatus, replyUser, url);
					 		twtAnalyze.add(twt);

					 	}
					 	else
					 	{
					 		Tweet twt = new Tweet (id, timeStamp, source, text, replyStatus, replyUser);
					 		twtAnalyze.add(twt);

					 	}
					 }
					 else{
					 	if (loneTwt[6].isEmpty() == false)
					 	{

					 		Long RetweetId = new Long(loneTwt[6]);
					 		long retweetId = RetweetId.longValue();

					 		Long RetweetUser = new Long(loneTwt[7]);
					 		long retweetUser = RetweetUser.longValue();

					 		Date retweetDate = timestampFormat.parse(loneTwt[8]);


					 		if (loneTwt[9].isEmpty() == false)
					 		{
					 			ArrayList<String> url = new ArrayList<String>(Arrays.asList(loneTwt[9].split(",")));

					 			Tweet twt = new Tweet (id, timeStamp, source, text, retweetId, retweetUser, retweetDate, url);
					 			twtAnalyze.add(twt);

					 		}
					 		else
					 		{
					 			Tweet twt = new Tweet (id, timeStamp, source, text, retweetId, retweetUser, retweetDate);
					 			twtAnalyze.add(twt);

					 		}
					 	}

					 	else{
					 		if (loneTwt[9].isEmpty() == false)
					 		{
					 			ArrayList<String> url = new ArrayList<String>(Arrays.asList(loneTwt[9].split(",")));

					 			Tweet twt = new Tweet (id, timeStamp, source, text, url);
					 			twtAnalyze.add(twt);

					 		}
					 		else
					 		{
					 			Tweet twt = new Tweet (id, timeStamp, source, text);
					 			twtAnalyze.add(twt);

					 		}
					 	}
					 }


					}
					return twtAnalyze;
				}

			}
