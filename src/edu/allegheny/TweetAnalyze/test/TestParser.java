package edu.allegheny.tweetanalyze.test;

import 	static org.junit.Assert.fail;
import 	static org.junit.Assert.*;

import 	static org.mockito.Mockito.*;

import 	org.junit.*;
import 	org.junit.rules.ExpectedException;
import 	org.junit.runner.RunWith;
import 	org.junit.runners.JUnit4;

import 	edu.allegheny.tweetanalyze.parser.*;

import	net.lingala.zip4j.core.ZipFile;
import	net.lingala.zip4j.exception.ZipException;

import	java.util.logging.Logger;
import	java.util.logging.Level;

import	java.io.File;
import 	java.io.IOException;

/**
 * Tests for {@link edu.allegheny.tweetanalyze.parser}
 * 
 * @author	Hawk Weisman
 * @version	0.1
 * @see		edu.allegheny.tweetanalyze.parser.CSVParser
 * @see		edu.allegheny.tweetanalyze.parser.ZipParser
 */

@RunWith(JUnit4.class)
public class TestParser extends LoggingTest {

	private String	testTweetPath;
	private ZipFile target;
	private File 	temp;
	private File 	mockUndeletableTemp;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp () throws ZipException {

		// set temp equal to the TweetAnalyzeTemp folder
		temp = new File("TweetAnalyzeTemp/");

		// get the relative path to the target
		testTweetPath = new File("").getAbsolutePath();
		testTweetPath = testTweetPath + "/src/edu/allegheny/TweetAnalyze/test/tweets.zip";
		target = new ZipFile (new File(testTweetPath));

		mockUndeletableTemp = mock(File.class);
		when(mockUndeletableTemp.delete()).thenReturn(false);

		ZipParser.logger = testLogger;
	}

	/**
	 * Generic test to insure correct instantiation of parser classes.
	 * If this fails, you're definitely not going to space today.
	 */
	@Test
	public void testParserInstantiation() {
		assertNotNull("FAIL: new ZipParser was not initialized", new ZipParser());
		assertNotNull("FAIL: new CSVParser was not initialized", new CSVParser());
	}

	/**
	 * Tests the ZipParser extracting CSV files from the tweets zip
	 */
	@Test
	public void testExtraction () throws ZipException {

		// assert that a tweets.csv file exists in the temp directory
		ZipParser.extractTweetsCSV(target,temp);
		assertTrue(temp.exists());
	}

	/**
	 * Test ZipParser correctly creating an ArrayList from the tweets.csv
	 */
	@Test 
	public void testZipParsing () {
		assertNotNull("FAIL: zip parser returned a null ArrayList<Tweet>",  ZipParser.parse(new File (testTweetPath)));
	}

	/**
	 * Tests ZipParser correctly logging exceptions
	 */
	@Test
	public void testExceptionLogging () {

		ZipParser.parse(new File(""));
		assertEquals("FAIL: Did not log IOException to SEVERE", Level.SEVERE, testHandler.checkLevel() );

		ZipParser.cleanUp(mockUndeletableTemp, mockUndeletableTemp);
		assertEquals("FAIL: Did not log failure to clean up", Level.SEVERE, testHandler.checkLevel() );
	}

	/**
	 * Tests ZipParser correctly logging info
	 */
	@Test
	public void testFineLogging () {
		
	}
	@After
	public void tearDown() {

		// delete the temp file
		File temp = new File("TweetAnalyzeTemp/");
		temp.delete();
	}

}
