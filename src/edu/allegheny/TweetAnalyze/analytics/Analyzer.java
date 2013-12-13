package edu.allegheny.tweetanalyze.analytics;

import edu.allegheny.tweetanalyze.Tweet;
import edu.allegheny.tweetanalyze.database.DatabaseHelper;

import java.util.Map;
import java.util.List;

public class Analyzer {

	protected DatabaseHelper db;

	public Analyzer(DatabaseHelper db)
	{
		this.db = db;
	}

}