package edu.allegheny.tweetanalyze.ui.cli;

import com.beust.jcommander.*;
import com.beust.jcommander.ParameterException;

import edu.allegheny.tweetanalyze.*;
import edu.allegheny.tweetanalyze.database.*;
import edu.allegheny.tweetanalyze.parser.*;
import edu.allegheny.tweetanalyze.analytics.*;
import edu.allegheny.tweetanalyze.ui.*;
import edu.allegheny.tweetanalyze.ui.gui.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.IOException;


public class KnightingaleCLI
{
	DatabaseHelper db = new DatabaseHelper();

	public static void main(String[] args)
	{
		KnightingaleCLI cli = new KnightingaleCLI();

		CommandArgs commandargs = new CommandArgs();
		CommandArgs.SearchCommand search = commandargs.new SearchCommand();
		CommandArgs.RefreshCommand refresh = commandargs.new RefreshCommand();
		CommandArgs.AddCommand add = commandargs.new AddCommand();
		CommandArgs.DeleteCommand delete = commandargs.new DeleteCommand();
		CommandArgs.FrequencyCommand frequency = commandargs.new FrequencyCommand();
		CommandArgs.CompositionCommand composition = commandargs.new CompositionCommand();
		CommandArgs.CloudCommand cloud = commandargs.new CloudCommand();
		JCommander cmd = new JCommander(commandargs);
		
		cmd.addCommand("search", search);
		cmd.addCommand("refresh", refresh);
		cmd.addCommand("add", add);
		cmd.addCommand("delete", delete);
		cmd.addCommand("frequency", frequency);
		cmd.addCommand("composition", composition);
		cmd.addCommand("cloud", cloud);

		try
		{
			cmd.parse(args);
			switch(cmd.getParsedCommand())
			{
				case "search" : 
				cli.search(search.getWord());
				break;
				
				case "refresh" : cli.refresh();
				break;

				case "add" : cli.add(add.getFile());
				break;

				case "delete" : cli.delete();
				break;

				case "frequency" : cli.frequency(frequency.getType());
				break;

				case "composition" : cli.composition();
				break;

				case "cloud" :cli.cloud(cloud.getType());
				break;
				
				default : cmd.usage();
			}
		}
		catch(ParameterException e)
		{
			System.out.println(e.getMessage());
			cmd.usage();
		}

		catch(SQLException e)
		{
			e.printStackTrace();	
		}

		catch(ParseException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void refresh()
	{
		TweetRefreshClient client = new TweetRefreshClient(new DatabaseHelper());
		int numberOfNewTweets =  client.refreshTweets();
		System.out.println("\n" + numberOfNewTweets + " new tweets\n");
	}

	public void add(String file)
	{

		File zipFile = new File(file);
		//@TODO : Check to see if table exists; if it does insert it, else create it
		db.dropTweetsTable();
		db.createTweetsTable();
		db.createUsersTable();
		db.insertTweets((ArrayList<Tweet>) ZipParser.parse(zipFile));
		db.insertUser();
		System.out.println("Tweets Inserted");
	}

	public void delete()
	{
		db.dropTweetsTable();
		System.out.println("Table deleted");
		System.out.println("Re creating empty tables");
		db.createTweetsTable();
		db.createUsersTable();
	}

	public void frequency(String type)
	{
		FrequencyAnalyzer frequencies = new FrequencyAnalyzer(db);
		Map<String, Integer> frequency = null;	
		switch(type)
		{
				case "reply" :
				frequency = frequencies.globalReplyFrequency();
				for (Map.Entry<String, Integer> entry : frequency.entrySet())
					System.out.println(entry.getKey() + " has been replied to " + entry.getValue() + " times.");
				break;
				
				case "retweet" :
				frequency = frequencies.globalRetweetFrequency();
				for (Map.Entry<String, Integer> entry : frequency.entrySet())
					System.out.println(entry.getKey() + " has been retweeted " + entry.getValue() + " times.");

				break;
				
				case "hashtag" :
				frequency = frequencies.globalHashtagFrequency();
				for (Map.Entry<String, Integer> entry : frequency.entrySet())
					System.out.println(entry.getKey() + " has occured " + entry.getValue() + " times.");
				break;

				default :
					System.out.println("Valid options are : retweet, reply, and hashtags");
				break;
		}	
	}

	public void search(String word)
	{
		try
		{
			SearchAnalyzer search = new SearchAnalyzer(new DatabaseHelper());
			List<Tweet> tweets = search.search(word);
			System.out.println("Tweets having " + word + " : \n");
			for (Tweet t : tweets)
				System.out.println(t.getText());
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}

	}

	public void composition() throws SQLException, ParseException
	{
		CompositionAnalyzer composition = new CompositionAnalyzer(db);
		System.out.printf("\n%d%% of your tweets are retweets, and %d%% are replies.\n", composition.percentRetweets(), composition.percentReplies());
	}

	public void cloud(String type) throws IOException
	{
		FrequencyAnalyzer analyzer = new FrequencyAnalyzer(db);
		FrequencyVisualization visualization = null;
		switch(type)
			{
				case "reply" :
					visualization = new UserCloud(analyzer.globalReplyFrequency(), "ReplyCloud");
					visualization.visualize();
				break;
				
				case "retweet" :
					visualization = new UserCloud(analyzer.globalRetweetFrequency(), "RetweetCloud");
					visualization.visualize();
				break;
				
				case "hashtag" :
					visualization = new TagCloud(analyzer.globalHashtagFrequency(), "HashtagCloud");
					visualization.visualize();
				break;

				default :
					System.out.println("Valid options are : retweet, reply, and hashtags");
				break;
			}
	}
}