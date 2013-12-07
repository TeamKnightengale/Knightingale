package edu.allegheny.TweetAnalyze.test;

import org.apache.logging.log4j.core.*;

import java.util.ArrayList;
import java.io.Serializable;

public class TestAppender implements Appender {
    private ArrayList<LogEvent> log = new ArrayList<LogEvent>();
 
  	public void append(LogEvent event) {
        log.add(event);
    }

    public void start() {
    }

    public void stop(){
    }

    public String getName() {
    	return "TestAppender";
    }

    public Layout<? extends Serializable> getLayout() {
    	return null;
    }

    public boolean ignoreExceptions() {
    	return false;
    }

    public boolean isStarted() {
    	return true;
    }

    public void setHandler (ErrorHandler handler) {

    }

    public ErrorHandler getHandler() {
    	return null;
    }

    public ArrayList<LogEvent> getLog() {
        return log;
    }
}

