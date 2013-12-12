package edu.allegheny.tweetanalyze.ui.cli;

import com.beust.jcommander.*;

public class CommandArgs
{
	public class DatabaseCommands
	{

	} 

	public class AnalysisCommands
	{

	}

	@Parameters(commandDescription = "Searches for all tweets with a word")
	public class SearchCommand
	{
		@Parameter(description = "words to search for")
		private String search;	

		public String getSearch()
		{
			return search;
		}
	}
	
	public class RefreshCommand
	{

	}

	public class TokenCommand
	{

	}
}