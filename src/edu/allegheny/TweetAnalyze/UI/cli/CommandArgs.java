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
		private List<String> search;	

		public String getSearch()
		{
			return search.get(0);
		}
	}
	
	@Parameters(commandDescription = "Get new tweets for the user from")
	public class RefreshCommand
	{

	}

}