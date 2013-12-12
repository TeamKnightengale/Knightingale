package edu.allegheny.tweetanalyze.ui.cli;

import com.beust.jcommander.*;
import com.beust.jcommander.ParameterException;


public class KnightingaleCLI
{
	public static void main(String[] args)
	{
		CommandArgs commandargs = new CommandArgs();
		CommandArgs.SearchCommand search = commandargs.new SearchCommand();
		JCommander cmd = new JCommander();

		try
		{
			cmd.parse(args);
		}
		catch(ParameterException e)
		{
			System.out.println(e.getMessage());
			cmd.usage();
		}
	}
}