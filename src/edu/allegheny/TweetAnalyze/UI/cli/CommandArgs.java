package edu.allegheny.tweetanalyze.ui.cli;

import com.beust.jcommander.*;
import java.util.List;
import java.io.File;

public class CommandArgs
{
	@Parameters(commandDescription = "Add tweets from your tweet archive")
	public class AddCommand
	{
		@Parameter(description = "Your twitter archive zip file")
		List<String> file;

		public String getFile()
		{
			return file.get(0);
		}
	}

	@Parameters(commandDescription = "Searches for all tweets with a word")
	public class SearchCommand
	{
		@Parameter(description = "words to search for")

		private List<String> word;	

		public String getWord()
		{
			return word.get(0);
		}
	}
	
	@Parameters(commandDescription = "Get new tweets for the user from")
	public class RefreshCommand
	{

	}

	@Parameters(commandDescription = "Deletes all your tweets from the table")
	public class DeleteCommand
	{

	}

	@Parameters(commandDescription = "Perform frequency analysis on your tweets")
	public class FrequencyCommand
	{
		@Parameter(names = "-type", description = "Frequency of replies, hastags or retweets?", required = true)
		private String type;

		public String getType()
		{
			return type;
		}
	}

	@Parameters(commandDescription = "What percent of your tweets are retweets or replies?")
	public class CompositionCommand
	{
	}	

	@Parameters(commandDescription = "World Cloud for Reply, Retweet, or HashTag")
	public class CloudCommand
	{
		@Parameter(names = "-type", description = "Cloud for replies, hastags or retweets?", required = true)
		private String type;

		public String getType()
		{
			return type;
		}
	}
}