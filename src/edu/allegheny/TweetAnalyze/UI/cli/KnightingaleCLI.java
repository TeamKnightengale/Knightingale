package edu.allegheny.tweetanalyze.ui.cli;

import com.beust.jcommander.*;
import com.beust.jcommander.ParameterException;

import edu.allegheny.tweetanalyze.*;
import edu.allegheny.tweetanalyze.database.*;

public class KnightingaleCLI
{
	public static void main(String[] args)
	{
		CommandArgs commandargs = new CommandArgs();
		CommandArgs.SearchCommand search = commandargs.new SearchCommand();
		CommandArgs.RefreshCommand refresh = commandargs.new RefreshCommand();
		
		JCommander cmd = new JCommander();
		
		cmd.addCommand("search", search);
		cmd.addCommand("refresh", refresh);

		KnightingaleCLI cli = new KnightingaleCLI();

		try
		{
			cmd.parse(args);
			switch(cmd.getParsedCommand())
			{
				case "search" : 
				break;
				
				case "refresh" : cli.refresh();
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
}