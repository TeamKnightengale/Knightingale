package edu.allegheny.tweetanalyze.ui.cli;

import com.beust.jcommander.*;

public class CommandArgs
{

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
	
	@Parameters(commandDescription = "Get new tweets for the user from")
	public class RefreshCommand
	{

	}

	public class TokenCommand
	{

	}
}