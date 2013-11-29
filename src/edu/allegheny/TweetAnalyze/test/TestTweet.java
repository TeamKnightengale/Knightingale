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
     * Tests if getText() works
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
     * Tests if setText() works
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

    /**
     * testGetSource()
     * Tests if getSource() works
     */
    @Test
    public void testGetSource(){
        Date day = new Date("12/17/2013");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Name", "Things", 2l, 3l, url);

        String expected = "Name";

        assertEquals(expected, actual.getSource());
    }

    /**
     * testSetSource()
     * Tests if setSource() works
     */
    @Test
    public void testSetSource(){
        Date day = new Date("12/17/2013");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Name", "Things", 2l, 3l, url);

        String expected = "Team1";
        actual.setSource("Team1");

        assertEquals(expected, actual.getSource());
    }

    /**
     * testGetExpandedURLs()
     * Tests if getExpandedURLs() works
     */
    @Test
    public void testGetExpandedURLs(){
        Date day = new Date("12/17/2013");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Name", "Things", 2l, 3l, url);

        ArrayList<String> expected = url;

        assertEquals(expected, actual.getExpandedURLs());
    }

    /**
     * testSetExpandedURLs()
     * Tests if setExpandedURLs() works
     */
    @Test
    public void testSetExpandedURLs(){
        Date day = new Date("12/17/2013");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Name", "Things", 2l, 3l, url);

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
    public void testGetInReplyToStatusID(){
        Date day = new Date("12/17/2013");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Name", "Things", 2l, 3l, url);

        long expected = 2l;

        assertEquals(expected, actual.getInReplyToStatusID());
    }

    /**
     * testSetInReplyToStatusID()
     * Tests if setInReplyToStatusID() works
     */
    @Test
    public void testSetInReplyToStatusID(){
        Date day = new Date("12/17/2013");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Name", "Things", 2l, 3l, url);

        long expected = 55l;
        actual.setInReplyToStatusID(55l);

        assertEquals(expected, actual.getInReplyToStatusID());
    }

    /**
     * testGetInReplyToUserID()
     * Tests if getInReplyToUserID() works
     */
    @Test
    public void testGetInReplyToUserID(){
        Date day = new Date("12/17/2013");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Name", "Things", 2l, 3l, url);

        long expected = 3l;

        assertEquals(expected, actual.getInReplyToUserID());
    }

    /**
     * testSetInReplyToUserID()
     * Tests if setInReplyToUserID() works
     */
    @Test
    public void testSetInReplyToUserID(){
        Date day = new Date("12/17/2013");
        ArrayList<String> url = new ArrayList<String>();
        url.add("http://junit.sourceforge.net/javadoc/org/junit/Assert.html");

        Tweet actual = new Tweet (1l, day, "Name", "Things", 2l, 3l, url);

        long expected = 67l;
        actual.setInReplyToUserID(67l);

        assertEquals(expected, actual.getInReplyToUserID());
    }

    /**
     * testGetRetweetedUserID
     * Tests if getRetweetedUserID() works
     */
    @Test
    public void testGetRetweetedUserID(){
        Date day = new Date("11/5/1111");

        Date other = new Date("10/5/1111");

        Tweet actual = new Tweet (1l, day, "Name", "Object", 2l, 3l, other);

        long expected = 2l;

        assertEquals(expected, actual.getRetweetedUserID());
    }

    /**
     * testSetRetweetedUserID
     * Tests if setRetweetedUserID() works
     */
    @Test
    public void testSetRetweetedUserID(){
        Date day = new Date("11/5/1111");

        Date other = new Date("10/5/1111");

        Tweet actual = new Tweet (1l, day, "Name", "Object", 2l, 3l, other);

        long expected = 42l;
        actual.setRetweetedUserID(42l);

        assertEquals(expected, actual.getRetweetedUserID());
    }

    /**
     * testGetRetweetedStatusID
     * Tests if getRetweetedStatusID() works
     */
    @Test
    public void testGetRetweetedStatusID(){
        Date day = new Date("11/5/1111");

        Date other = new Date("10/5/1111");

        Tweet actual = new Tweet (1l, day, "Name", "Object", 2l, 3l, other);

        long expected = 3l;

        assertEquals(expected, actual.getRetweetedStatusID());
    }

    /**
     * testSetRetweetedStatusID
     * Tests if setRetweetedStatusID() works
     */
    @Test
    public void testSetRetweetedStatusID(){
        Date day = new Date("11/5/1111");

        Date other = new Date("10/5/1111");

        Tweet actual = new Tweet (1l, day, "Name", "Object", 2l, 3l, other);

        long expected = 20l;
        actual.setRetweetedStatusID(20l);

        assertEquals(expected, actual.getRetweetedStatusID());
    }

        /**
     * testGetRetweetedStatusTimestamp
     * Tests if getRetweetedStatusTimesstamp() works
     */
    @Test
    public void testGetRetweetedStatusTimestamp(){
        Date day = new Date("11/5/1111");

        Date other = new Date("10/5/1111");

        Tweet actual = new Tweet (1l, day, "Name", "Object", 2l, 3l, other);

        Date expected = other;

        assertEquals(expected, actual.getRetweetedStatusTimestamp());
    }

    /**
     * testSetRetweetedStatusTimestamp
     * Tests if setRetweetedStatusTimestamp()
     */
    @Test
    public void testSetRetweetedStatusTimestamp(){
        Date day = new Date("11/5/1111");

        Date other = new Date("10/5/1111");

        Tweet actual = new Tweet (1l, day, "Name", "Object", 2l, 3l, other);

        Date expected = new Date("11/24/2013");
        actual.setRetweetedStatusTimestamp(expected);

        assertEquals(expected, actual.getRetweetedStatusTimestamp());
    }




}
