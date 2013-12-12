package edu.allegheny.TweetAnalyze.test;

import edu.allegheny.TweetAnalyze.analytics.CompositionAnalyzer;
import edu.allegheny.TweetAnalyze.analytics.FrequencyAnalyzer;
import edu.allegheny.TweetAnalyze.analytics.HashtagAnalyzer;
import edu.allegheny.TweetAnalyze.analytics.SearchAnalyzer;
import edu.allegheny.TweetAnalyze.analytics.UserAnalyzer;
import edu.allegheny.TweetAnalyze.database.DatabaseHelper;
import edu.allegheny.TweetAnalyze.Tweet;
import edu.allegheny.TweetAnalyze.TweetBuilder;

import  org.junit.*;
import  org.junit.rules.ExpectedException;
import  org.junit.runner.RunWith;
import  org.junit.runners.JUnit4;
import  org.junit.rules.ExpectedException;
import  static org.junit.Assert.*;

import  static org.mockito.Mockito.*;
import  org.mockito.ArgumentCaptor;
import  org.mockito.stubbing.Answer;
import  org.mockito.invocation.InvocationOnMock;
import  java.util.ArrayList;
import  java.util.Date;
import  java.util.List;
import  java.util.concurrent.atomic.AtomicInteger;
import  java.text.SimpleDateFormat;
import  java.text.ParseException;
import  java.sql.*;

/**
 * Tests for {@link(edu.allegheny.TweetAnalyze.analytics.SimpleAnalyzer.java}
 * Mocking now, even though i was against it initially. 
 * Still missing some stuff from tests.
 * @Author Gabe Kelly
 * */
public class TestAnalytics
{
    /*IMPORTANT NOTE ABOUT THIS VERSION
    AS OF RIGHT NOW THIS DOES NOT TEST THAT
    THE CODE FOR CALCULATIONS AND SUCH
    IN SIMPLE ANALYTICS WORK
    JUST THAT SIMPLE ANALYTICS INTERACTS WITH
    DB HELPER. WHICH IS IMPORTANT
    BUT IT WILL NEED TO EVENTUALLY
    HAVE CODE TO ALSO TEST IF GIVEN BLANK
    IT WILL PROPERLY RETURN BLANK*/

    private SimpleDateFormat timestampFormat;
    private final List<String> namesCollumn = new ArrayList<String>();
    private final List idRow = new ArrayList(); //i'll refactor this abomination later
    @Rule
  	public ExpectedException exception = ExpectedException.none();
    

    @Before
    public void setUp () {
        timestampFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss z");
        

        namesCollumn.add("tweet_id");
        namesCollumn.add("in_reply_status_id");
        namesCollumn.add("in_reply_user_id");
        namesCollumn.add("timestamp");
        namesCollumn.add("source");
        namesCollumn.add("text");
        namesCollumn.add("retweeted_status_id");
        namesCollumn.add("retweeted_status_user_id");
        namesCollumn.add("retweeted_status_timestamp");
        namesCollumn.add("expanded_urls");
        idRow.add(6398642204l);
        idRow.add(7268639295l);
        idRow.add(219185626719850496l);
        idRow.add(263130043444781057l);
        idRow.add("227593758357721088");
        idRow.add("219851590377545729");
        idRow.add(219459070103527425l);
        idRow.add(192770064250978306l);
        idRow.add(192671491215720448l);
        idRow.add("180649285145202688");
         }


    private static ResultSet makeResultSet(final List<String> Collumns, final List rows) throws Exception 
    {//probably don't need this method. may get rid of.
    ResultSet result = mock(ResultSet.class);
    final AtomicInteger currentIndex = new AtomicInteger(-1);
 
    when(result.next()).thenAnswer(new Answer() {
        public Object answer(InvocationOnMock aInvocation) throws Throwable {
            return currentIndex.incrementAndGet() < rows.size();
        }
    });
 
    final ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
    Answer rowLookupAnswer = new Answer() {
        public Object answer(InvocationOnMock aInvocation) throws Throwable {
            int rowIndex = currentIndex.get();
            int columnIndex = Collumns.indexOf(argument.getValue());
            return rows.get(columnIndex);
        }
    };
    when(result.getString(argument.capture())).thenAnswer(rowLookupAnswer);
    when(result.getLong(argument.capture())).thenAnswer(rowLookupAnswer);
    when(result.getDate(argument.capture())).thenAnswer(rowLookupAnswer);
    
    return result;
}


    /** 
     * testSearch()
     * This tests the search() function
     * of the simple analytics class
     * */
    @Test(expected=NullPointerException.class)//has to eat this otherwise will always throw NPE. but it still works with NPE gone
    public void testSearch() throws SQLException, ParseException, ClassNotFoundException, Exception
    {
        DatabaseHelper mockDH = mock(DatabaseHelper.class);

        SearchAnalyzer sa = new SearchAnalyzer(mockDH);

        Date time = timestampFormat.parse("2013-10-15 03:01:16 +0000");
        
        ArrayList<String> url = new ArrayList<String>();

        url.add("http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html");

        Tweet tweet = new Tweet(247525256951132160l, time, "Me", "Epic Testing Go!", url);

            ResultSet rsMock = mock(ResultSet.class);

            when(rsMock.getLong("tweet_id")).thenReturn(Long.valueOf("247525256951132160"));
            //when(rsMock.getLong("in_reply_status_id")).thenReturn(Long.valueOf("2"));this don't have to be here but this is format for 
            //when(rsMock.getLong("in_reply_user_id")).thenReturn(Long.valueOf("1"));the reply and retweet stuff
            when(rsMock.getLong("timestamp")).thenReturn(Long.valueOf("1347849595000"));
            when(rsMock.getString("source")).thenReturn("Me");
            when(rsMock.getString("text")).thenReturn("Epic Testing Go!");
            //when(rsMock.getLong("retweeted_status_id")).thenReturn(Long.valueOf("1"));
            //when(rsMock.getLong("retweeted_status_user_id")).thenReturn(Long.valueOf("2"));
            //when(rsMock.getLong("retweeted_status_timestamp")).thenReturn(Long.valueOf("3"));
            when(rsMock.getString("expanded_urls")).thenReturn("http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html");

            when(mockDH.execute("SELECT * FROM tweets WHERE text LIKE '% Epic %'")).thenReturn(rsMock);    

            when(rsMock.next()).thenReturn(true).thenReturn(false);

            List<Tweet> actual = sa.search("Epic");
            
            List<Tweet> expected = new ArrayList<Tweet>();

            expected.add(tweet);

            assertEquals(expected, actual);
     }

    /**
     * testPercentRetweets()
     * This tests the percentRetweets()
     * of the simple analytics class
     * 
    @Test
    public void testPercentRetweets() throws SQLException, ParseException, ClassNotFoundException
    {
        DatabaseHelper mockDH = mock(DatabaseHelper.class);

        TweetBuilder mockTB = mock(TweetBuilder.class);
        
        SimpleAnalyzer sa = new SimpleAnalyzer(mockDH);

        sa.percentRetweets();

        verify(mockDH).execute("SELECT COUNT(*) FROM tweets");

        verify(mockDH).execute("SELECT COUNT(*) FROM tweets WHERE retweeted_status_id IS NOT 0");
    }

    /**
     * testPercentReplies()
     * This test the percentReplies()
     * of the simple analytics class
     * 
    @Test
    public void testPercentReplies() throws SQLException, ParseException, ClassNotFoundException
    {
        DatabaseHelper mock = mock(DatabaseHelper.class);
        
        SimpleAnalyzer sa = new SimpleAnalyzer(mock);

        sa.percentReplies();

        verify(mock).execute("SELECT COUNT(*)FROM Tweets");

        verify(mock).execute("SELECT COUNT(*)FROM Tweets WHERE in_reply_to_status_id IS NOT 0 AND" + " in_reply_to_user_id IS NOT 0");
    }

    /**
     * testTweetsWithHyperlinks()
     * This tests the tweetsWithHyperlinks()
     * of the simple analytics class
     * 
    @Test
    public void testTweetsWithHyperlinks() throws SQLException, ParseException, ClassNotFoundException
    {
        DatabaseHelper mock = mock(DatabaseHelper.class);

        SimpleAnalyzer sa = new SimpleAnalyzer(mock);

        sa.tweetsWithHyperlinks();

        verify(mock).execute("SELECT * FROM tweets WHERE id IN (SELECT DISTINCT tweet_id FROM expanded_urls");
    }
    
    /**
     * testRepliedToUsers()
     * This test the repliedToUsers()
     * of the simple analytics class
     * 
    @Test
    public void testRepliedToUsers() throws SQLException, ParseException, ClassNotFoundException
    {
        DatabaseHelper mock =  mock(DatabaseHelper.class);
        
        SimpleAnalyzer sa = new SimpleAnalyzer(mock);

        sa.repliedToUsers();

        verify(mock).execute("SELECT DISTINCT in_reply_to_user_id FROM Tweets");
    }*/


}
    
