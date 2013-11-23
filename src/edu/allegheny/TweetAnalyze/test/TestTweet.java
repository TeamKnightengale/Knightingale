package edu.allegheny.TweetAnalyze.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.*;
import edu.allegheny.TweetAnalyze.Tweet;
import java.util.Date;
import java.util.ArrayList;



public class TestTweet
{
    /**
     * testConstructorBasic()
     * Tests if the basic constructor works
     */
    @Test
    public void testConstructorBasic(){
        Date day = new Date("11/20/1994");//values don't matter for constructor just data type
        
        Tweet actual = new Tweet (12031l, day , "Gabe", "This is a tweet");

        assertNotNull(actual);
    }
    

    /**
     * testConstructorBasicUrl()
     * Tests if the basic with url constructor work
     */
    @Test
    public void testConstructorBasicUrl(){
        Date today = new Date("11/23/2013");
        ArrayList<String> url = new ArrayList<String>();

        url.add("https://www.google.com/");

        Tweet actual = new Tweet (12301l, today, "Hawk", "This is a tweet", url);

        assertNotNull(actual);
    }
    
    
    /**
     * testContructorRetweet()
     * Tests if the retweet constructor works
     */
    @Test
    public void testConstructorRetweet(){
        Date day = new Date("11/5/1111");

        Date other = new Date("10/5/1111");

        Tweet actual = new Tweet (1l, day, "Jake", "RA", 2l, 3l, other);

        assertNotNull(actual);
    }
    

    /**
     * testContructorRetweetUrl()
     * Tests if the rewtweet with url constructor works
     */
    @Test
    public void testConstructorRetweetUrl(){
        Date day = new Date("11/5/1111");
        
        Date other = new Date("10/5/1111");

        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Dibyo", "Tweet", 2l, 3l, other, url);

        assertNotNull(actual);
    }
    

    /**
     * testConstructorReply()
     * Tests if the reply constructor works
     */
    @Test
    public void testConstructorReply(){
        Date day = new Date("12/17/2013");

        Tweet actual = new Tweet (1l, day, "Ian", "Database", 2l, 3l);

        assertNotNull(actual);
    }
    

    /**
     * testConstructorReplyUrl()
     * Tests if the reply with url constructor works
     */
    @Test
    public void testConstructorReplyUrl(){
        Date day = new Date("8/29/2013");

        ArrayList<String> url = new ArrayList<String>();
        url.add("Greetings NSA");

        Tweet actual = new Tweet (1l, day, "Name", "Thing", 2l, 3l, url);

        assertNotNull(actual);
    }
    
    
    /**
     * testIsRetweetFalse()
     * Tests if isRetweet() can return false
     */
    @Test
    public void testIsRetweetFalse(){
        Date day = new Date("12/17/2013");

        Tweet actual = new Tweet (1l, day, "Name", "Things", 2l, 3l);

        assertFalse(actual.isRetweet());
    }
    
    /**
     * testIsRetweetTrue()
     * Tests if isRetweet() can return true
     */
    @Test
    public void testIsRetweetTrue(){
        Date day = new Date("11/5/1111");
        
        Date other = new Date("10/5/1111");

        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Name", "Thing", 2l, 3l, other, url);

        assertTrue(actual.isRetweet());
    }
    
    /**
     * testIsReplyFalse()
     * Tests if isReply() can return false
     */
    @Test
    public void testIsReplyFalse(){
        Date day = new Date("11/5/1111");
        
        Date other = new Date("10/5/1111");

        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Name", "Thing", 2l, 3l, other, url);
        
        assertFalse(actual.isReply());
    }

    /**
     * testIsReplyTrue()
     * Tests if isReply() can return true
     */
    @Test
    public void testIsReplyTrue(){
        Date day = new Date("12/17/2013");

        Tweet actual = new Tweet (1l, day, "Name", "Things", 2l, 3l);

        assertTrue(actual.isReply());
    }
    
    /**
     * testContainsURLsFalse()
     * Tests if containsUrl() can returns false
     */
    @Test
    public void testContainsURLsFalse(){
        Date day = new Date("11/20/1994");        
        Tweet actual = new Tweet (12031l, day , "Me", "This is a tweet");
        
        assertFalse(actual.containsURLs());
    }

    /**
     * testContainsURLsTrue()
     * Tests if containsUrl() can return true
     */
    @Test
    public void testContainsURLsTrue(){
        Date day = new Date("12/17/2013");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Name", "Things", 2l, 3l, url);

        assertTrue(actual.containsURLs());
    }

    /**
     * testGetTweetID()
     * Tests if getTweetId works
     */
    @Test
    public void testGetTweetID(){
        Date day = new Date("12/17/2013");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Name", "Things", 2l, 3l, url);

        long expected = 1l;

        assertEquals(expected, actual.getTweetID());
    }

    /**
     * testSetTweetID()
     * Tests if setTweetID works
     */
    @Test
    public void testSetTweetId(){
        Date day = new Date("12/17/2013");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Name", "Things", 2l, 3l, url);

        long expected = 11l;
        actual.setTweetID(11l);

        assertEquals(expected, actual.getTweetID());
    }

    /**
     * testGetTimestamp()
     * Tests if getTimestamp() works
     */
    @Test
    public void testGetTimestamp(){
        Date day = new Date("12/17/2013");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet(1l, day, "Name", "Things", 2l, 3l, url);

        Date expected = new Date("12/17/2013");
        assertEquals(expected, actual.getTimestamp());
    }

    /**
     * testSetTimestamp()
     * Tests if setTimestamp() works
     */
    @Test
    public void testSetTimestamp(){
        Date day = new Date("12/17/2013");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Name", "Things", 2l, 3l, url);

        Date expected = new Date("11/27/2013");
        actual.setTimestamp(expected);

        assertEquals(expected, actual.getTimestamp());
    }

    /**
     * testGetText()
     * Tests if getText works
     */
    @Test
    public void testGetText(){
        Date day = new Date("12/17/2013");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Name", "Things", 2l, 3l, url);

        String expected = "Things";

        assertEquals(expected, actual.getText());
    }

    /**
     * testSetText()
     * Tests if setText works
     */
    @Test
    public void testSetText(){
        Date day = new Date("12/17/2013");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Name", "Things", 2l, 3l, url);

        String expected = "YAGNI";
        actual.setText("YAGNI");

        assertEquals(expected, actual.getText());
    }



}
