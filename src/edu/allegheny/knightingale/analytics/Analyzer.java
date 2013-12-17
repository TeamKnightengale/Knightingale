package edu.allegheny.knightingale.analytics;

import edu.allegheny.knightingale.Tweet;
import edu.allegheny.knightingale.database.DatabaseHelper;

import java.util.Map;
import java.util.List;

public class Analyzer {

	protected DatabaseHelper db;

	public Analyzer(DatabaseHelper db)
	{
		this.db = db;
	}

}