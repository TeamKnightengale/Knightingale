package edu.allegheny.TweetAnalyze.test;

import	java.io.Serializable;
import	java.io.File;

import 	org.junit.*;
import 	org.junit.runner.RunWith;
import 	org.junit.runners.JUnit4;

/**
 * Provides logging functionality to a JUnit test class.
 */

@RunWith(JUnit4.class)
public class LoggingTest {

	@Before
	public void setUpLoggingTest() {
		System.setProperty("log4j.configuration", new File(new File("").getAbsolutePath() + "/config/logging/test_logging.xml").toString());
	}

	@After
	public void tearDownLoggingTest() {
		System.setProperty("log4j.configuration", new File(new File("").getAbsolutePath() + "/config/logging/log4j.xml").toString());
	}
}