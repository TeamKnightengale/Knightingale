package edu.allegheny.TweetAnalyze.test;

import	java.io.Serializable;
import	java.io.File;

import 	org.junit.*;
import 	org.junit.runner.RunWith;
import 	org.junit.runners.JUnit4;

import	java.util.logging.Logger;
import	java.util.logging.Level;
import 	java.util.logging.Handler;
import 	java.util.logging.LogRecord;

/**
 * Provides logging functionality to a JUnit test class.
 */

@RunWith(JUnit4.class)
public class LoggingTest {

	protected Logger testLogger;
	protected TestHandler testHandler;

	@Before
	public void setUpLoggingTest() {
		testLogger = Logger.getLogger("");
   		testHandler = new TestHandler();
    	testHandler.setLevel(Level.ALL);
    	testLogger.setUseParentHandlers(false);
    	testLogger.addHandler(testHandler);
    	testLogger.setLevel(Level.ALL);
        //testLogger.removeHandler(ConsoleHandler);
	}
}

class TestHandler extends Handler {
    Level lastLevel = Level.FINEST;

    public Level  checkLevel() {
        return lastLevel;
    }    

    public void publish(LogRecord record) {
        lastLevel = record.getLevel();
    }

    public void close(){}
    public void flush(){}
}