package edu.allegheny.knightingale.test;

import edu.allegheny.knightingale.analytics.CompositionAnalyzer;
import edu.allegheny.knightingale.analytics.FrequencyAnalyzer;
import edu.allegheny.knightingale.analytics.HashtagAnalyzer;
import edu.allegheny.knightingale.analytics.SearchAnalyzer;
import edu.allegheny.knightingale.analytics.UserAnalyzer;
import edu.allegheny.knightingale.database.DatabaseHelper;
import edu.allegheny.knightingale.Tweet;
import edu.allegheny.knightingale.TweetBuilder;

import  org.junit.*;
import  org.junit.rules.ExpectedException;
import  org.junit.runner.RunWith;
import  org.junit.runners.JUnit4;
import  org.junit.rules.ExpectedException;
import  static org.junit.Assert.*;

import twitter4j.*;

import  static org.mockito.Mockito.*;
import  org.mockito.ArgumentCaptor;
import  org.mockito.stubbing.Answer;
import  org.mockito.invocation.InvocationOnMock;

import  java.util.ArrayList;
import  java.util.Arrays;
import  java.util.Date;
import  java.util.List;
import  java.util.Map;
import  java.util.HashMap;
import  java.util.concurrent.atomic.AtomicInteger;
import  java.text.SimpleDateFormat;
import  java.text.ParseException;
import  java.io.IOException;
import  java.sql.*;

/**
 * Tests for {@link(edu.allegheny.knightingale.analytics.SimpleAnalyzer.java}
 * Mocking now, even though i was against it initially. 
 * Still missing some stuff from tests.
 * @Author Gabe Kelly
 * */
public class TestAnalytics extends LoggingTest
{
    
    private SimpleDateFormat timestampFormat;

    //private FrequencyAnalyzer spyFA;

    //private static Twitter mockTwit;

    private DatabaseHelper mockDH;
    @Rule
  	public ExpectedException exception = ExpectedException.none();
    

    @Before
    public void setUp ()  {
        timestampFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss z");    
        mockDH = mock(DatabaseHelper.class);
        }

//makeResultSet(namesCollumn, idRow);

   private static ResultSet makeResultSet(final List<String> Collumns, final List... rows) throws Exception 

    {//probably don't need this method. may get rid of.
    ResultSet result = mock(ResultSet.class);
    final AtomicInteger currentIndex = new AtomicInteger(-1);
 
    when(result.next()).thenAnswer(new Answer() {
        public Object answer(InvocationOnMock aInvocation) throws Throwable {
            return currentIndex.incrementAndGet() < rows.length;
        }
    });
 
    final ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
    Answer rowLookupAnswer = new Answer() {
        public Object answer(InvocationOnMock aInvocation) throws Throwable {
            int rowIndex = currentIndex.get();
            int columnIndex = Collumns.indexOf(argument.getValue());
            return rows[currentIndex.get()].get(columnIndex);
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
     * of the SearchAnalytics class
     * */
     @Test//(expected=NullPointerException.class)
     public void testSearch() throws SQLException, ParseException, ClassNotFoundException, Exception
    {
        //DatabaseHelper mockDH = mock(DatabaseHelper.class);
        
        SearchAnalyzer sa = new SearchAnalyzer(mockDH);

        Date time = timestampFormat.parse("2012-09-17 02:39:55 +0000");
        
        ArrayList<String> url = new ArrayList<String>();

        url.add("http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html");

        Tweet tweet = new Tweet(247525256951132160l, time, "Me", "Epic Testing Go!", url);

        ResultSet rsMock = makeResultSet( 
                Arrays.asList("tweet_id", "in_reply_to_status_id", "in_reply_to_user_id", "timestamp", "source", "text", "retweeted_status_id", "retweeted_status_user_id",
                    "retweeted_status_timestamp", "expanded_urls"),
                Arrays.asList(247525256951132160l, null, null, 1347849595000l, "Me", "Epic Testing Go!", null, null, null, "http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html")
                );
 
        when(mockDH.execute("SELECT * FROM tweets WHERE text LIKE '%Epic%'")).thenReturn(rsMock);    

        when(rsMock.next()).thenReturn(true).thenReturn(false);

        List<Tweet> actual = sa.search("Epic");
            
        List<Tweet> expected = new ArrayList<Tweet>();

        expected.add(tweet);

        assertEquals(expected.get(0).getText(), actual.get(0).getText());
     }


    /**
     * testPercentRetweets()
     * This tests the percentRetweets()
     * of the CompositionAnalyzer.class
     * */
    @Test//this fails find a way to make it pass.But runs without eating an NPE????
    public void testPercentRetweets() throws SQLException, ParseException, ClassNotFoundException, Exception
    {
        //DatabaseHelper mockDH = mock(DatabaseHelper.class);
        
        CompositionAnalyzer ca = new CompositionAnalyzer(mockDH);

        ResultSet rsMock = makeResultSet(
                Arrays.asList("tweet_id", "in_reply_to_status_id", "in_reply_to_user_id", "timestamp", "source", "text", "retweeted_status_id", "retweeted_status_user_id",
                    "retweeted_status_timestamp", "expanded_urls"),
                Arrays.asList(23l, null, null, 1347894595001l, "me", "BLARG", null, null, null, null),
                Arrays.asList(33l, 21l, 1l, 1347894595001l, "you", "COOKIES!", null, null, null, "file:///home/k/kellyg/cs290/lab6/cs290F2013-lab6-team1/nerpa/build.xml"),
                Arrays.asList(11l, null, null, 1347894595001l, "us", "survive", 23l, 45l, 1347894595001l, null)
                );

        ResultSet rsMock2 = makeResultSet(
                Arrays.asList("tweet_id", "in_reply_to_status_id", "in_reply_to_user_id", "timestamp", "source", "text", "retweeted_status_id", "retweeted_status_user_id",
                    "retweeted_status_timestamp", "expanded_urls"),
                Arrays.asList(11l, null, null, 1347894595001l, "us", "survive", 23l, 45l, 1347894595001l, null)
                );
      
        when(mockDH.execute("SELECT COUNT(*) FROM tweets")).thenReturn(rsMock);
            
        when(mockDH.execute("SELECT COUNT(*) FROM tweets WHERE retweeted_status_id IS NOT 0")).thenReturn(rsMock2);
       
        when(rsMock.getInt(1)).thenReturn(3);

        when(rsMock2.getInt(1)).thenReturn(1);

        when(rsMock.next()).thenReturn(true).thenReturn(false);

        when(rsMock2.next()).thenReturn(true).thenReturn(false);

        int expected = 33;

        int actual = ca.percentRetweets();

        assertEquals(expected, actual);
    }
    /**
     * testPercentReplies()
     * This test the percentReplies()
     * of the simple analytics class
     * */
    @Test
    public void testPercentReplies() throws SQLException, ParseException, ClassNotFoundException, Exception
    {
       // DatabaseHelper mockDH = mock(DatabaseHelper.class);
        
        CompositionAnalyzer ca = new CompositionAnalyzer(mockDH);

        ResultSet rsMock = makeResultSet(
                Arrays.asList("tweet_id", "in_reply_to_status_id", "in_reply_to_user_id", "timestamp", "source", "text", "retweeted_status_id", "retweeted_status_user_id",
                    "retweeted_status_timestamp", "expanded_urls"),
                Arrays.asList(23l, null, null, 1347894595001l, "me", "BLARG", null, null, null, null),
                Arrays.asList(33l, 21l, 1l, 1347894595001l, "you", "COOKIES!", null, null, null, "file:///home/k/kellyg/cs290/lab6/cs290F2013-lab6-team1/nerpa/build.xml"),
                Arrays.asList(11l, null, null, 1347894595001l, "us", "survive", 23l, 45l, 1347894595001l, null)
                );

        ResultSet rsMock2 = makeResultSet(
                Arrays.asList("tweet_id", "in_reply_status_id", "in_reply_user_id", "timestamp", "source", "text", "retweeted_status_id", "retweeted_status_user_id",
                    "retweeted_status_timestamp", "expanded_urls"),
                Arrays.asList(11l, 4l, 2l, 1347894595001l, "us", "survive", null, null, null, null)
                );
      
        when(mockDH.execute("SELECT COUNT(*)FROM Tweets")).thenReturn(rsMock);    
        when(mockDH.execute("SELECT COUNT(*)FROM Tweets WHERE in_reply_to_status_id IS NOT 0 AND in_reply_to_user_id IS NOT 0")).thenReturn(rsMock2);
        
        when(rsMock.getInt(1)).thenReturn(3);

        when(rsMock2.getInt(1)).thenReturn(1);



        when(rsMock.next()).thenReturn(true).thenReturn(false);

        when(rsMock2.next()).thenReturn(true).thenReturn(false);

        int expected = 33;

        int actual = ca.percentReplies();

        assertEquals(expected, actual);




    }

    
    /**
     * testTweetsWithHyperlinks()
     * This tests the tweetsWithHyperlinks()
     * of the search analytics class
     * */
     /*
    @Test//(expected=NullPointerException.class)
    public void testTweetsWithHyperlinks() throws SQLException, ParseException, ClassNotFoundException, Exception
    {
       // DatabaseHelper mockDH = mock(DatabaseHelper.class);

        SearchAnalyzer sa = new SearchAnalyzer(mockDH);

        Date time = timestampFormat.parse("2012-09-17 02:39:55 +0000");
        
        ArrayList<String> url = new ArrayList<String>();

        url.add("http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html");

        Tweet tweet = new Tweet(247525256951132160l, time, "Me", "#CS210Final FTW", url);

        ResultSet rsMock = makeResultSet( 
                Arrays.asList("tweet_id", "in_reply_to_status_id", "in_reply_to_user_id", "timestamp", "source", "text", "retweeted_status_id", "retweeted_status_user_id",
                    "retweeted_status_timestamp", "expanded_urls"),
                Arrays.asList(247525256951132160l, null, null, 1347849595000l, "Me", "#CS210Final FTW", null, null, null, "http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html")
                );
                
        when(mockDH.execute("SELECT * FROM tweets WHERE expanded_urls IS NOT 0")).thenReturn(rsMock);

        when(rsMock.next()).thenReturn(true).thenReturn(false);

        List<Tweet> actual = sa.tweetsWithHyperlinks();
        
        List<Tweet> expected = new ArrayList<Tweet>();

        expected.add(tweet);

        assertEquals(expected.get(0).getTweetID(), actual.get(0).getTweetID());
        assertEquals(expected.get(0).getTimestamp(), actual.get(0).getTimestamp());
        assertEquals(expected.get(0).getSource(), actual.get(0).getSource());
        assertEquals(expected.get(0).getText(), actual.get(0).getText());
        assertEquals(expected.get(0).getExpandedURLs(), actual.get(0).getExpandedURLs());
        } */
    
    /**
     * testRepliedToUsers()
     * This test the repliedToUsers()
     * of the user analytics class
     * */
   @Test//(expected=NullPointerException.class)
    public void testRepliedToUsers() throws SQLException, ParseException, ClassNotFoundException, Exception
    {
        //DatabaseHelper mockDH = mock(DatabaseHelper.class);

        UserAnalyzer ua = new UserAnalyzer(mockDH);

        Date time = timestampFormat.parse("2012-09-17 02:39:55 +0000");
        
        ArrayList<String> url = new ArrayList<String>();

        url.add("http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html");

        Tweet one = new Tweet(33l, time, "you", "COOKIES!", 21l, 1l);

        String repliedToUsersQuery 	= "SELECT DISTINCT in_reply_to_user_id as rid, COUNT (*) " + "FROM Tweets " + 
                                "GROUP BY rid " + "ORDER BY rid DESC"; 

        ResultSet rsMock = makeResultSet(
                Arrays.asList("tweet_id", "in_reply_to_status_id", "in_reply_to_user_id", "timestamp", "source", "text", "retweeted_status_id", "retweeted_status_user_id",
                    "retweeted_status_timestamp", "expanded_urls"),
                Arrays.asList(33l, 21l, 1l, 1347849595000l, "you", "COOKIES!", null, null, null, null)
                );
        
        when(mockDH.execute(repliedToUsersQuery)).thenReturn(rsMock);
        
        when(rsMock.next()).thenReturn(true).thenReturn(false);

        ResultSet actual = ua.repliedToUsers();

        List<Tweet> expected = new ArrayList<Tweet>();

        expected.add(one);
        //expected.add(two);

        assertEquals(expected.get(0).getTweetID(), actual.getLong("tweet_id"));
        assertEquals(expected.get(0).getInReplyToStatusID(), actual.getLong("in_reply_to_status_id"));
        assertEquals(expected.get(0).getInReplyToUserID(), actual.getLong("in_reply_to_user_id"));
        assertEquals(expected.get(0).getSource(), actual.getString("source"));
        assertEquals(expected.get(0).getText(), actual.getString("text"));
        
    }

 /**
     * testRetweetedUsers()
     * This test the retweetedUsers()
     * of the user analytics class
     * */
   @Test//(expected=NullPointerException.class)
    public void testRetweetedUsers() throws SQLException, ParseException, ClassNotFoundException, Exception
    {
        //DatabaseHelper mockDH = mock(DatabaseHelper.class);

        UserAnalyzer ua = new UserAnalyzer(mockDH);

        Date time = timestampFormat.parse("2012-09-17 02:39:55 +0000");
        
        ArrayList<String> url = new ArrayList<String>();

        url.add("http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html");

        Tweet one = new Tweet(33l, time, "you", "COOKIES!", 1l, 21l, time);

        //Tweet two = new Tweet(300000000000000000l, time, "Me", "Estamos fudidos", 4l, 5l, time, url);

        String repliedToUsersQuery 	= "SELECT DISTINCT retweeted_status_user_id AS id, COUNT(*) " + "FROM Tweets " 
									+ "GROUP BY retweeted_status_user_id " + "ORDER BY id DESC";
        ResultSet rsMock = makeResultSet(
                Arrays.asList("tweet_id", "in_reply_to_status_id", "in_reply_to_user_id", "timestamp", "source", "text", "retweeted_status_id", "retweeted_status_user_id",
                    "retweeted_status_timestamp", "expanded_urls"),
                Arrays.asList(33l, null, null, 1347849595000l, "you", "COOKIES!", 21l, 1l, 1347849595000l, null)
                );
        
        when(mockDH.execute(repliedToUsersQuery)).thenReturn(rsMock);
        
        when(rsMock.next()).thenReturn(true).thenReturn(false);

        ResultSet actual = ua.retweetedUsers();

        List<Tweet> expected = new ArrayList<Tweet>();

        expected.add(one);
        //expected.add(two);

        assertEquals(expected.get(0).getTweetID(), actual.getLong("tweet_id"));
        assertEquals(expected.get(0).getSource(), actual.getString("source"));
        assertEquals(expected.get(0).getText(), actual.getString("text"));
        assertEquals(expected.get(0).getRetweetedStatusID(), actual.getLong("retweeted_status_id"));
        assertEquals(expected.get(0).getRetweetedUserID(), actual.getLong("retweeted_status_user_id"));

    }


/**
 * testTweetsWithHashtag()
 * This test the tweetsWithHashtag()
 * of the hastag analytics class
 * */
@Test
public void testTweetsWithHashtag() throws SQLException, ParseException, ClassNotFoundException, Exception
{

        //DatabaseHelper mockDH = mock(DatabaseHelper.class);

        HashtagAnalyzer ha = new HashtagAnalyzer(mockDH);

        Date time = timestampFormat.parse("2012-09-17 02:39:55 +0000");
        
        ArrayList<String> url = new ArrayList<String>();

        url.add("http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html");

        Tweet tweet = new Tweet(247525256951132160l, time, "Me", "#CS210Final FTW", url);

        ResultSet rsMock = makeResultSet( 
                Arrays.asList("tweet_id", "in_reply_to_status_id", "in_reply_to_user_id", "timestamp", "source", "text", "retweeted_status_id", "retweeted_status_user_id",
                    "retweeted_status_timestamp", "expanded_urls"),
                Arrays.asList(247525256951132160l, null, null, 1347849595000l, "Me", "#CS210Final FTW", null, null, null, "http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html")
                );
                
        when(mockDH.execute("SELECT * FROM Tweets WHERE text LIKE '%#%'")).thenReturn(rsMock);

        when(rsMock.next()).thenReturn(true).thenReturn(false);

        List<Tweet> actual = ha.tweetsWithHashtag();
        
        List<Tweet> expected = new ArrayList<Tweet>();

        expected.add(tweet);

        assertEquals(expected.get(0).getTweetID(), actual.get(0).getTweetID());
        assertEquals(expected.get(0).getTimestamp(), actual.get(0).getTimestamp());
        assertEquals(expected.get(0).getSource(), actual.get(0).getSource());
        assertEquals(expected.get(0).getText(), actual.get(0).getText());
        assertEquals(expected.get(0).getExpandedURLs(), actual.get(0).getExpandedURLs());
    }
/**
 * testExtractHashtag()
 * This test the extractHashtags()
 * of the hastag analytics class
 * */
@Test
public void testExtractHashtags() throws SQLException, ParseException, ClassNotFoundException, Exception
    {

        //DatabaseHelper mockDH = mock(DatabaseHelper.class);

        HashtagAnalyzer ha = new HashtagAnalyzer(mockDH);

        ResultSet rsMock = makeResultSet( 
                Arrays.asList("tweet_id", "in_reply_to_status_id", "in_reply_to_user_id", "timestamp", "source", "text", "retweeted_status_id", "retweeted_status_user_id",
                    "retweeted_status_timestamp", "expanded_urls"),
                 Arrays.asList(247525256951132160l, null, null, 1347849595000l, "Me", "#CS210Final FTW", null, null, null, "http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html"),
                 Arrays.asList(247525256951132160l, 4l, 3l, 1347849595000l, "HOORAY", "CS290Final as well", null, null, null, null)
                );
                
        when(mockDH.execute("SELECT * FROM Tweets WHERE text LIKE '%#%'")).thenReturn(rsMock);

        when(rsMock.next()).thenReturn(true).thenReturn(false);

        List<String> actual = ha.extractHashtags();

        List<String> expected = new ArrayList<String>();

        expected.add("#CS210Final");
        
        assertEquals(expected.get(0), actual.get(0));
        assertFalse(actual.size() == 2);
    }

/**
 * testHashtagCount()
 * This test the hashtagCount()
 * of the hastag analytics class
 * */
@Test
public void testHashtagCount() throws SQLException, ParseException, ClassNotFoundException, Exception
{

        //DatabaseHelper mockDH = mock(DatabaseHelper.class);

        HashtagAnalyzer ha = new HashtagAnalyzer(mockDH);

        Date time = timestampFormat.parse("2012-09-17 02:39:55 +0000");
        
        ArrayList<String> url = new ArrayList<String>();

        url.add("http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html");

        Tweet tweet = new Tweet(247525256951132160l, time, "Me", "#CS210Final FTW", url);

        ResultSet rsMock = makeResultSet( 
                Arrays.asList("tweet_id", "in_reply_to_status_id", "in_reply_to_user_id", "timestamp", "source", "text", "retweeted_status_id", "retweeted_status_user_id",
                    "retweeted_status_timestamp", "expanded_urls"),
                Arrays.asList(247525256951132160l, 4l, 3l, 1347849595000l, "HOORAY", "#CS290Final as well", null, null, null, null)
                );
                
        when(mockDH.execute("SELECT COUNT(*) FROM Tweets WHERE text LIKE '%#CS290Final%'")).thenReturn(rsMock);

        when(rsMock.next()).thenReturn(true).thenReturn(false);

        when(rsMock.getInt(1)).thenReturn(1);

        Integer actual = ha.hashtagCount("#CS290Final");

        Integer expected = 1;

        assertEquals(expected, actual);
    }
/**
 * testGlobalHashtagFrequency()
 * This tests globalHashtagFrequency()
 * of the frequency analyzer
 * */
@Test
public void testGlobalHashtagFrequency() throws SQLException, ParseException, ClassNotFoundException, Exception
{
    //DatabaseHelper mockDH = mock(DatabaseHelper.class);


    FrequencyAnalyzer fa = new FrequencyAnalyzer(mockDH);

    HashtagAnalyzer haMOCK = mock(HashtagAnalyzer.class);

    List<String> extract = new ArrayList<String>();

    extract.add("#BLOBBIRD");

    Integer count = 1;

    ResultSet rsMock = makeResultSet( 
                Arrays.asList("tweet_id", "in_reply_to_status_id", "in_reply_to_user_id", "timestamp", "source", "text", "retweeted_status_id", "retweeted_status_user_id",
                    "retweeted_status_timestamp", "expanded_urls"),
                Arrays.asList(247525256951132160l, 4l, 3l, 1347849595000l, "HOORAY", "#BLOBBIRD IS BETTER THAN FISH", null, null, null, "http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html")
                );
                
   ResultSet rsMock2 = makeResultSet( 
                Arrays.asList("tweet_id", "in_reply_to_status_id", "in_reply_to_user_id", "timestamp", "source", "text", "retweeted_status_id", "retweeted_status_user_id",
                    "retweeted_status_timestamp", "expanded_urls"),
                Arrays.asList(247525256951132160l, 4l, 3l, 1347849595000l, "HOORAY", "#BLOBBIRD IS BETTER THAN FISH", null, null, null, null)
                );
                
    when(mockDH.execute("SELECT COUNT(*) FROM Tweets WHERE text LIKE '%#BLOBBIRD%'")).thenReturn(rsMock2);

    when(rsMock2.next()).thenReturn(true).thenReturn(false);

    when(rsMock2.getInt(1)).thenReturn(1);

    when(haMOCK.hashtagCount("#BLOBBIRD")).thenReturn(1);
    when(rsMock.next()).thenReturn(true).thenReturn(false);

    when(mockDH.execute("SELECT * FROM Tweets WHERE text LIKE '%#%'")).thenReturn(rsMock);

    Map<String, Integer> actual = new HashMap<String, Integer>();
    actual = fa.globalHashtagFrequency();

    Map<String, Integer> expected = new HashMap<String, Integer>();

    expected.put(extract.get(0), count);
    
    //verify(rsMock).getInt(1);
    assertEquals(expected, actual);
}

/**
 * testGlobalReplyFrequencyNOTNULL
 * tests the globalReplyFrequency()
 * of the frequency analyzer when
 * id is not zero
 * */
@Test
public void testGlobalReplyFrequencyNOTNULL() throws SQLException, ParseException, ClassNotFoundException, Exception
{
   // DatabaseHelper mockDH = mock(DatabaseHelper.class);

    FrequencyAnalyzer fa = new FrequencyAnalyzer(mockDH);

    List<String> id = new ArrayList<String>();

    id.add("Santa Claus");

    Integer count = 1;

    ResultSet rsMock = makeResultSet( 
                Arrays.asList("tweet_id", "in_reply_to_status_id", "in_reply_to_user_id", "timestamp", "source", "text", "retweeted_status_id", "retweeted_status_user_id",
                    "retweeted_status_timestamp", "expanded_urls"),
                Arrays.asList(247525256951132160l, 4l, 3l, 1347849595000l, "HOORAY", "#BLOBBIRD IS BETTER THAN FISH", null, null, null, "http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html")
                );
    ResultSet rsMock2 = makeResultSet(
            Arrays.asList("tweet_id"),
            Arrays.asList(247525256951132160l)
    );
                
    when(mockDH.execute("SELECT DISTINCT in_reply_to_user_id as rid, COUNT (*) " + "FROM Tweets " + "GROUP BY rid " + "ORDER BY rid DESC")).thenReturn(rsMock);

    when(mockDH.execute("SELECT user_name " + "FROM users " + "WHERE user_id IS " + 247525256951132160l)).thenReturn(rsMock2);

    when(rsMock.next()).thenReturn(true).thenReturn(false);

    when(rsMock2.next()).thenReturn(true).thenReturn(false);

    when(rsMock.getLong(1)).thenReturn(247525256951132160l);

    when(rsMock.getInt(2)).thenReturn(1);

    when(rsMock2.getString(1)).thenReturn("Santa Claus");

    Map<String, Integer> actual = new HashMap<String, Integer>();
    actual = fa.globalReplyFrequency();

    Map<String, Integer> expected = new HashMap<String, Integer>();

    expected.put(id.get(0), count);

    assertEquals(expected, actual);
}

/**
 * testGlobalReplyFrequencyNULL
 * tests the globalReplyFrequency()
 * of the frequency analyzer when
 * id is zero
 * *INCOMPLETE* CANNOT SOLVE STATIC MOCKING.
 * 
//@PrepareForTest(Twitter.class)
@Test
public void testGlobalReplyFrequencyNULL() throws SQLException, ParseException, ClassNotFoundException, Exception
{
   // DatabaseHelper mockDH = mock(DatabaseHelper.class);

    FrequencyAnalyzer fa = new FrequencyAnalyzer(mockDH);

    List<String> id = new ArrayList<String>();

    Twitter mockTwit = mock(Twitter.class);
    //PowerMockito.whenNew(Twitter.class).withNoArguments().thenReturn(mockTwit);
    //spyFA = spy(new FrequencyAnalyzer(mockDH));

    id.add("Santa Claus");

    Integer count = 1;

    ResultSet rsMock = makeResultSet( 
                Arrays.asList("tweet_id", "in_reply_to_status_id", "in_reply_to_user_id", "timestamp", "source", "text", "retweeted_status_id", "retweeted_status_user_id",
                    "retweeted_status_timestamp", "expanded_urls"),
                Arrays.asList(247525256951132160l, 4l, 3l, 1347849595000l, "HOORAY", "#BLOBBIRD IS BETTER THAN FISH", null, null, null, "http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html")
                );
    ResultSet rsMock2 = makeResultSet(
            Arrays.asList("tweet_id"),
            Arrays.asList(1l)
            );
                  
    when(mockDH.execute("SELECT DISTINCT in_reply_to_user_id as rid, COUNT (*) " + "FROM Tweets " + "GROUP BY rid " + "ORDER BY rid DESC")).thenReturn(rsMock);

    when(mockDH.execute("SELECT user_name " + "FROM users " + "WHERE user_id IS " + 247525256951132160l)).thenReturn(rsMock2);

    when(rsMock.next()).thenReturn(true).thenReturn(false);
    
    when(rsMock.getLong(1)).thenReturn(247525256951132160l);

    when(rsMock.getInt(2)).thenReturn(1);

    when(rsMock2.getString(1)).thenReturn(null);

    when(mockTwit.getScreenName()).thenReturn("Santa Claus");

    Map<String, Integer> actual = new HashMap<String, Integer>();
    actual = fa.globalReplyFrequency();

    Map<String, Integer> expected = new HashMap<String, Integer>();

    expected.put(id.get(0), count);
    
    verify(mockTwit).getScreenName();
    //assertEquals(expected, actual);
}*/

/**
 * testGlobalRetweetedFrequencyNOTNULL
 * tests the globalReplyFrequency()
 * of the frequency analyzer when
 * id is not zero
 * */
@Test
public void testGlobalRetweetedFrequencyNOTNULL() throws SQLException, ParseException, ClassNotFoundException, Exception
{
   // DatabaseHelper mockDH = mock(DatabaseHelper.class);

    FrequencyAnalyzer fa = new FrequencyAnalyzer(mockDH);

    List<String> id = new ArrayList<String>();

    id.add("Dr. Kapfhammer");

    Integer count = 1;

    ResultSet rsMock = makeResultSet( 
                Arrays.asList("tweet_id", "in_reply_to_status_id", "in_reply_to_user_id", "timestamp", "source", "text", "retweeted_status_id", "retweeted_status_user_id",
                    "retweeted_status_timestamp", "expanded_urls"),
                Arrays.asList(247525256951132160l, null, null, 1347849595000l, "HOORAY", "#BLOBBIRD IS BETTER THAN FISH", 34l, 23l, 42l, "http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html")
                );
    ResultSet rsMock2 = makeResultSet(
            Arrays.asList("tweet_id"),
            Arrays.asList(247525256951132160l)
    );
                
    when(mockDH.execute("SELECT DISTINCT retweeted_status_user_id AS id, COUNT(*) " + "FROM Tweets " + "GROUP BY retweeted_status_user_id " + "ORDER BY id DESC")).thenReturn(rsMock);

    when(mockDH.execute("SELECT user_name " + "FROM users " + "WHERE user_id IS " + 247525256951132160l)).thenReturn(rsMock2);

    when(rsMock.next()).thenReturn(true).thenReturn(false);

    when(rsMock2.next()).thenReturn(true).thenReturn(false);

    when(rsMock.getLong(1)).thenReturn(247525256951132160l);

    when(rsMock.getInt(2)).thenReturn(1);

    when(rsMock2.getString(1)).thenReturn("Dr. Kapfhammer");

    Map<String, Integer> actual = new HashMap<String, Integer>();

    actual = fa.globalRetweetFrequency();

    Map<String, Integer> expected = new HashMap<String, Integer>();

    expected.put(id.get(0), count);

    assertEquals(expected, actual);
}



}
    
