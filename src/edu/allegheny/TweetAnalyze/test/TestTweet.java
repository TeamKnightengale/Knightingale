package edu.allegheny.tweetanalyze.test;

import static org.junit.Assert.*;

import edu.allegheny.tweetanalyze.Tweet;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import  org.junit.*;
import  org.junit.rules.ExpectedException;
import  org.junit.runner.RunWith;
import  org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class TestTweet
{

    private SimpleDateFormat    format;
    private Random              numberGenerator;    


    @Before
    public void setUp () {
        format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss z");
        numberGenerator = new Random();
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * testConstructorBasic()
     * Tests if the basic constructor works
     */
    @Test
    public void testConstructorBasic () throws ParseException{
        Date timestamp = format.parse("2013-10-15 03:01:16 +0000");//values don't matter for constructor just data type
        
        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp , "Gabe", "This is a tweet");

        assertNotNull(actual);
    }
    

    /**
     * testConstructorBasicUrl()
     * Tests if the basic with url constructor work
     */
    @Test
    public void testConstructorBasicUrl() throws ParseException {
        Date totimestamp = format.parse("2013-10-06 06:26:51 +0000");
        ArrayList<String> url = new ArrayList<String>();

        url.add("https://www.google.com/");

        Tweet actual = new Tweet (numberGenerator.nextLong(), totimestamp, "Hawk", "This is a tweet", url);

        assertNotNull(actual);
    }
    
    
    /**
     * testContructorRetweet()
     * Tests if the retweet constructor works
     */
    @Test
    public void testConstructorRetweet() throws ParseException {
        Date timestamp = format.parse("2012-12-05 22:22:59 +0000");

        Date otherTimestamp = format.parse("2012-12-05 22:22:59 +0000");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Jake", "RA", numberGenerator.nextLong(), numberGenerator.nextLong(), otherTimestamp);

        assertNotNull(actual);
    }
    

    /**
     * testContructorRetweetUrl()
     * Tests if the rewtweet with url constructor works
     */
    @Test
    public void testConstructorRetweetUrl() throws ParseException {
        Date timestamp = format.parse("2012-10-23 04:07:58 +0000");
        
        Date otherTimestamp = format.parse("2012-10-04 01:38:40 +0000");

        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Dibyo", "Tweet", numberGenerator.nextLong(), numberGenerator.nextLong(), otherTimestamp, url);

        assertNotNull(actual);
    }
    

    /**
     * testConstructorReply()
     * Tests if the reply constructor works
     */
    @Test
    public void testConstructorReply() throws ParseException {
        Date timestamp = format.parse("2012-12-05 22:22:59 +0000");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Ian", "Database", numberGenerator.nextLong(), numberGenerator.nextLong());

        assertNotNull(actual);
    }
    

    /**
     * testConstructorReplyUrl()
     * Tests if the reply with url constructor works
     */
    @Test
    public void testConstructorReplyUrl() throws ParseException {
        Date timestamp = format.parse("2013-03-08 04:33:40 +0000");

        ArrayList<String> url = new ArrayList<String>();
        url.add("Greetings NSA");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Thing", numberGenerator.nextLong(), numberGenerator.nextLong(), url);

        assertNotNull(actual);
    }
    
    
    /**
     * testIsRetweetFalse()
     * Tests if isRetweet() can return false
     */
    @Test
    public void testIsRetweetFalse() throws ParseException {
        Date timestamp = format.parse("2010-06-20 00:00:00 +0000");
        Date otherTimestamp = format.parse("2012-04-19 00:22:19 +0000");
        Date nullTimestamp = null;

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Things", numberGenerator.nextLong(), numberGenerator.nextLong());

        assertFalse(actual.isRetweet());

        actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Thing", 0, numberGenerator.nextLong(), otherTimestamp);
        assertFalse(actual.isRetweet());

        actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Thing", numberGenerator.nextLong(), 0, otherTimestamp);
        assertFalse(actual.isRetweet());

        actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Thing", numberGenerator.nextLong(), numberGenerator.nextLong(), nullTimestamp);
        assertFalse(actual.isRetweet());
    }
    
    /**
     * testIsRetweetTrue()
     * Tests if isRetweet() can return true
     */
    @Test
    public void testIsRetweetTrue() throws ParseException {
        Date timestamp = format.parse("2012-06-30 21:47:26 +0000");
        
        Date otherTimestamp = format.parse("2012-04-19 00:22:19 +0000");

        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Thing", numberGenerator.nextLong(), numberGenerator.nextLong(), otherTimestamp, url);

        assertTrue(actual.isRetweet());
    }
    
    /**
     * testIsReplyFalse()
     * Tests if isReply() can return false
     */
    @Test
    public void testIsReplyFalse() throws ParseException {
        Date timestamp = format.parse("2012-08-05 18:11:42 +0000");
        
        Date otherTimestamp = format.parse("2012-03-09 18:35:49 +0000");

        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Thing", numberGenerator.nextLong(), numberGenerator.nextLong(), otherTimestamp, url);
        
        assertFalse(actual.isReply());

        actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Thing", numberGenerator.nextLong(), 0);
        assertFalse(actual.isReply());

        actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Thing", 0, numberGenerator.nextLong());
        assertFalse(actual.isReply());
    }

    /**
     * testIsReplyTrue()
     * Tests if isReply() can return true
     */
    @Test
    public void testIsReplyTrue() throws ParseException {
        Date timestamp = format.parse("2011-04-08 04:31:35 +0000");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Things", numberGenerator.nextLong(), numberGenerator.nextLong());

        assertTrue(actual.isReply());
    }
    
    /**
     * testContainsURLsFalse()
     * Tests if containsUrl() can returns false
     */
    @Test
    public void testContainsURLsFalse() throws ParseException {
        Date timestamp = format.parse("2011-12-17 22:37:52 +0000");        
        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp , "Me", "This is a tweet");
        
        assertFalse(actual.containsURLs());
    }

    /**
     * testContainsURLsTrue()
     * Tests if containsUrl() can return true
     */
    @Test
    public void testContainsURLsTrue() throws ParseException {
        Date timestamp = format.parse("2009-12-06 00:00:00 +0000");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Things", numberGenerator.nextLong(), numberGenerator.nextLong(), url);

        assertTrue(actual.containsURLs());
    }

    /**
     * testGetTweetID()
     * Tests if getTweetId works
     */
    @Test
    public void testGetTweetID() throws ParseException {
        Date timestamp = format.parse("2010-06-11 00:00:00 +0000");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        long expected = numberGenerator.nextLong();

        Tweet actual = new Tweet (expected, timestamp, "Name", "Things", numberGenerator.nextLong(), numberGenerator.nextLong(), url);

        assertEquals(expected, actual.getTweetID());
    }

    /**
     * testSetTweetID()
     * Tests if setTweetID works
     */
    @Test
    public void testSetTweetId() throws ParseException {
        Date timestamp = format.parse("2012-07-01 15:54:54 +0000");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Things", numberGenerator.nextLong(), numberGenerator.nextLong(), url);

        long expected = numberGenerator.nextLong();
        actual.setTweetID(expected);

        assertEquals(expected, actual.getTweetID());
    }

    /**
     * testGetTimestamp()
     * Tests if getTimestamp() works
     */
    @Test
    public void testGetTimestamp() throws ParseException {
        Date timestamp = format.parse("2012-04-19 04:22:30 +0000");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet(numberGenerator.nextLong(), timestamp, "Name", "Things", numberGenerator.nextLong(), numberGenerator.nextLong(), url);

        assertEquals(timestamp, actual.getTimestamp());
    }

    /**
     * testSetTimestamp()
     * Tests if setTimestamp() works
     */
    @Test
    public void testSetTimestamp() throws ParseException {
        Date timestamp = format.parse("2013-05-31 17:34:37 +0000");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Things", numberGenerator.nextLong(), numberGenerator.nextLong(), url);

        Date expected = format.parse("2012-12-23 20:02:19 +0000");
        actual.setTimestamp(expected);

        assertEquals(expected, actual.getTimestamp());
    }

    /**
     * testGetText()
     * Tests if getText() works
     */
    @Test
    public void testGetText() throws ParseException {
        Date timestamp = format.parse("2013-05-31 17:34:37 +0000");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Things", numberGenerator.nextLong(), numberGenerator.nextLong(), url);

        String expected = "Things";

        assertEquals(expected, actual.getText());
    }

    /**
     * testSetText()
     * Tests if setText() works
     */
    @Test
    public void testSetText() throws ParseException {
        Date timestamp = format.parse("2012-09-17 02:39:55 +0000");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Things", numberGenerator.nextLong(), numberGenerator.nextLong(), url);

        String expected = "YAGNI";
        actual.setText("YAGNI");

        assertEquals(expected, actual.getText());
    }

    /**
     * testGetSource()
     * Tests if getSource() works
     */
    @Test
    public void testGetSource() throws ParseException {
        Date timestamp = format.parse("2012-06-30 23:15:50 +0000");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Things", numberGenerator.nextLong(), numberGenerator.nextLong(), url);

        String expected = "Name";

        assertEquals(expected, actual.getSource());
    }

    /**
     * testSetSource()
     * Tests if setSource() works
     */
    @Test
    public void testSetSource() throws ParseException {
        Date timestamp = format.parse("2011-12-17 22:37:52 +0000");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Things", numberGenerator.nextLong(), numberGenerator.nextLong(), url);

        String expected = "Team1";
        actual.setSource("Team1");

        assertEquals(expected, actual.getSource());
    }

    /**
     * testGetExpandedURLs()
     * Tests if getExpandedURLs() works
     */
    @Test
    public void testGetExpandedURLs() throws ParseException {
        Date timestamp = format.parse("2012-09-15 17:46:46 +0000");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Things", numberGenerator.nextLong(), numberGenerator.nextLong(), url);

        ArrayList<String> expected = url;

        assertEquals(expected, actual.getExpandedURLs());
    }

    /**
     * testSetExpandedURLs()
     * Tests if setExpandedURLs() works
     */
    @Test
    public void testSetExpandedURLs() throws ParseException {
        Date timestamp = format.parse("2013-03-17 06:54:33 +0000");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Things", numberGenerator.nextLong(), numberGenerator.nextLong(), url);

        ArrayList<String> expected = new ArrayList<String>();
        expected.add("https://www.google.com/search?q=junit+assert&ie=UTF-8&sa=Search&channel=fe&client=browser-ubuntu&hl=en");

        actual.setExpandedURLs(expected);

        assertEquals(expected, actual.getExpandedURLs());
    }


    /**
     * testGetInReplyToStatusID()
     * Tests if getInReplyToStatusID() works
     */
    @Test
    public void testGetInReplyToStatusID() throws ParseException {
        Date timestamp = format.parse("2013-06-26 13:32:06 +0000");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        long expected = numberGenerator.nextLong();

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Things", expected, numberGenerator.nextLong(), url);

        assertEquals(expected, actual.getInReplyToStatusID());
    }

    /**
     * testSetInReplyToStatusID()
     * Tests if setInReplyToStatusID() works
     */
    @Test
    public void testSetInReplyToStatusID() throws ParseException {
        Date timestamp = format.parse("2013-10-06 06:26:51 +0000");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        long expected = numberGenerator.nextLong();

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Things", numberGenerator.nextLong(), numberGenerator.nextLong(), url);

        actual.setInReplyToStatusID(expected);

        assertEquals(expected, actual.getInReplyToStatusID());
    }

    /**
     * testGetInReplyToUserID()
     * Tests if getInReplyToUserID() works
     */
    @Test
    public void testGetInReplyToUserID() throws ParseException {
        Date timestamp = format.parse("2013-02-08 21:03:10 +0000");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        long expected = numberGenerator.nextLong();

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Things", numberGenerator.nextLong(), expected, url);

        assertEquals(expected, actual.getInReplyToUserID());
    }

    /**
     * testSetInReplyToUserID()
     * Tests if setInReplyToUserID() works
     */
    @Test
    public void testSetInReplyToUserID() throws ParseException {
        Date timestamp = format.parse("2013-10-05 12:53:53 +0000");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Things", numberGenerator.nextLong(), numberGenerator.nextLong(), url);

        long expected = numberGenerator.nextLong();
        actual.setInReplyToUserID(expected);

        assertEquals(expected, actual.getInReplyToUserID());
    }

    /**
     * testGetRetweetedUserID
     * Tests if getRetweetedUserID() works
     */
    @Test
    public void testGetRetweetedUserID() throws ParseException {
        Date timestamp = format.parse("2012-06-30 23:20:55 +0000");

        Date otherTimestamp = format.parse("2013-10-05 12:53:53 +0000");

        long expected = numberGenerator.nextLong();

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Object", expected, numberGenerator.nextLong(), otherTimestamp);

        assertEquals(expected, actual.getRetweetedUserID());
    }

    /**
     * testSetRetweetedUserID
     * Tests if setRetweetedUserID() works
     */
    @Test
    public void testSetRetweetedUserID() throws ParseException {
        Date timestamp = format.parse("2012-03-09 18:35:49 +0000");

        Date otherTimestamp = format.parse("2012-07-19 02:19:57 +0000");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Object", numberGenerator.nextLong(), numberGenerator.nextLong(), otherTimestamp);

        long expected = numberGenerator.nextLong();

        actual.setRetweetedUserID(expected);

        assertEquals(expected, actual.getRetweetedUserID());
    }

    /**
     * testGetRetweetedStatusID
     * Tests if getRetweetedStatusID() works
     */
    @Test
    public void testGetRetweetedStatusID() throws ParseException {
        Date timestamp = format.parse("2012-07-19 02:19:57 +0000");

        Date otherTimestamp = format.parse("2012-06-30 21:49:04 +0000");

        long expected = numberGenerator.nextLong();

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Object", numberGenerator.nextLong(), expected, otherTimestamp);

        assertEquals(expected, actual.getRetweetedStatusID());
    }

    /**
     * testSetRetweetedStatusID
     * Tests if setRetweetedStatusID() works
     */
    @Test
    public void testSetRetweetedStatusID() throws ParseException {
        Date timestamp = format.parse("2012-06-30 21:49:04 +0000");

        Date otherTimestamp = format.parse("2012-07-19 02:19:57 +0000");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Object", numberGenerator.nextLong(), numberGenerator.nextLong(), otherTimestamp);

        long expected = numberGenerator.nextLong();
        actual.setRetweetedStatusID(expected);

        assertEquals(expected, actual.getRetweetedStatusID());
    }

    /**
     * testGetRetweetedStatusTimestamp
     * Tests if getRetweetedStatusTimesstamp() works
     */
    @Test
    public void testGetRetweetedStatusTimestamp() throws ParseException {
        Date timestamp = format.parse("2013-05-11 05:31:46 +0000");

        Date otherTimestamp = format.parse("2013-10-05 21:33:13 +0000");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Object", numberGenerator.nextLong(), numberGenerator.nextLong(), otherTimestamp);

        Date expected = otherTimestamp;

        assertEquals(expected, actual.getRetweetedStatusTimestamp());
    }

    /**
     * testSetRetweetedStatusTimestamp
     * Tests if setRetweetedStatusTimestamp()
     */
    @Test
    public void testSetRetweetedStatusTimestamp() throws ParseException {
        Date timestamp = format.parse("2010-06-16 00:00:00 +0000");

        Date otherTimestamp = format.parse("2010-07-21 00:00:00 +0000");

        Tweet actual = new Tweet (numberGenerator.nextLong(), timestamp, "Name", "Object", numberGenerator.nextLong(), numberGenerator.nextLong(), otherTimestamp);

        Date expected = format.parse("2012-06-30 14:17:09 +0000");
        actual.setRetweetedStatusTimestamp(expected);

        assertEquals(expected, actual.getRetweetedStatusTimestamp());
    }




}
