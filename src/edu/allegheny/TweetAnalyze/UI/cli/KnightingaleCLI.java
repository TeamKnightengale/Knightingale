package edu.allegheny.tweetanalyze.ui.cli;

import com.beust.jcommander.*;
import com.beust.jcommander.ParameterException;


public class KnightingaleCLI
{
	public static void main(String[] args)
	{
		KnightingaleCLIParameters params = new KnightingaleCLIParameters();
		JCommander cmd = new JCommander(params);
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