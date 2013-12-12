package edu.allegheny.tweetanalyze.ui.cli;

import com.beust.jcommander.*;
import com.beust.jcommander.ParameterException;

import edu.allegheny.tweetanalyze.*;
import edu.allegheny.tweetanalyze.database.*;
import edu.allegheny.tweetanalyze.parser.*;


import java.io.File;
import java.util.ArrayList;

public class KnightingaleCLI
{
	public static void main(String[] args)
	{
		KnightingaleCLI cli = new KnightingaleCLI();

		CommandArgs commandargs = new CommandArgs();
		CommandArgs.SearchCommand search = commandargs.new SearchCommand();
		CommandArgs.RefreshCommand refresh = commandargs.new RefreshCommand();
		CommandArgs.AddCommand add = commandargs.new AddCommand();
		JCommander cmd = new JCommander(commandargs);
		
		cmd.addCommand("search", search);
		cmd.addCommand("refresh", refresh);
		cmd.addCommand("add", add);

		try
		{
			cmd.parse(args);
			switch(cmd.getParsedCommand())
			{
				case "search" : 
				break;
				
				case "refresh" : cli.refresh();
				break;

				case "add" : cli.add(add.getFile());
				break;

			}
		}
		catch(ParameterException e)
		{
			System.out.println(e.getMessage());
			cmd.usage();
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
		DatabaseHelper db = new DatabaseHelper();
		File zipFile = new File(file);
		db.dropTweetsTable();
		db.createTweetsTable();
		db.createUsersTable();
		db.insertTweets((ArrayList<Tweet>) ZipParser.parse(zipFile));
		db.insertUser();
		System.out.println("Tweets Inserted");
	}
}