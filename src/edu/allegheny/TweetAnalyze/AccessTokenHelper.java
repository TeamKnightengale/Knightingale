/**
 * Use Twitter4J to get new OAuthAccessToken and OAuthAccessTokenSecret. 
 * Tokens are stored in the twitter4j.properties file.
*/
package edu.allegheny.TweetAnalyze;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.Configuration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.FileOutputStream;

import	org.apache.logging.log4j.LogManager;
import	org.apache.logging.log4j.Logger;


public class AccessTokenHelper
{
	private static Logger logger = LogManager.getFormatterLogger(AccessTokenHelper.class.getName());

	public static void main(String argv[]) 
	{
		AccessTokenHelper tokenHelper = new AccessTokenHelper();
		tokenHelper.deleteAccessTokens();
		tokenHelper.getnewTokens();
	}

	public void getnewTokens() 
	{
		deleteAccessTokens(); //First, delete old access tokens;
		Twitter twitter = TwitterFactory.getSingleton();
		AccessToken accessToken = null;
		try
		{
			RequestToken requestToken = twitter.getOAuthRequestToken();
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (null == accessToken) {
				System.out.println("Open the following URL and grant access to your account.\n");
				System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
				System.out.println(requestToken.getAuthorizationURL());
				String pin = br.readLine();
				try{
					if(pin.length() > 0){
						accessToken = twitter.getOAuthAccessToken(requestToken, pin);
					}else{
						accessToken = twitter.getOAuthAccessToken();
					}
				} catch (TwitterException te) {
					if(401 == te.getStatusCode()){
						System.out.println("Unable to get the access token.");
					}else{
						te.printStackTrace();
					}
				}
			}

			setTokens(accessToken);	
		}
		catch(IOException e)
		{
			logger.error("AccessTokenHelper: Exception found in getnewTokens()", e);
		}
		catch(twitter4j.TwitterException te)
		{
			logger.error("AccessTokenHelper: TwitterException found in getnewTokens()", te);	
		}		
	}

	private void setTokens(AccessToken token)
	{
		
		String fileName = "twitter4j.properties";
		BufferedReader br = null;
		BufferedWriter bw = null;
		try
		{
			try 
			{
				File file = new File(fileName);
				FileWriter fileWriter = new FileWriter(file,true);//Open FileWriter for appending
				bw = new BufferedWriter(fileWriter); 
				StringBuilder line = new StringBuilder("oauth.accessToken=");
				line.append(token.getToken());
				line.append("\n");
				line.append("oauth.accessTokenSecret=");
				line.append(token.getTokenSecret());										
				line.append("\n");
				fileWriter.append(line.toString());
			}
			finally {
				if(bw != null)
					bw.close();		
			}
		}
		catch (IOException e) 
		{
			logger.error("AccessTokenHelper: IOException found in setTokens()", e);
		} 
		
	}

	public void deleteAccessTokens()
	{
		String oldFileName = "twitter4j.properties";
		String tmpFileName = "twitter4j.properties.temp";

		BufferedReader br = null;
		BufferedWriter bw = null;
		try
		{
			try 
			{
				br = new BufferedReader(new FileReader(oldFileName));
				bw = new BufferedWriter(new FileWriter(tmpFileName));
				String line;

				while ((line = br.readLine()) != null) 
				{
					if (!(line.contains("oauth.accessToken=") || line.contains("oauth.accessTokenSecret=")))
					{
						bw.write(line+"\n");	
					}
				}
			}
			finally {
				if(br != null)
					br.close();
				if(bw != null)
					bw.close();		
			}	
		}
		catch (IOException e) {
			logger.error("AccessTokenHelper: IOException found in deleteTokens()", e);
		} 
		
      // Once everything is complete, delete old file..
		File oldFile = new File(oldFileName);
		oldFile.delete();

      // And rename tmp file's name to old file name
		File newFile = new File(tmpFileName);
		newFile.renameTo(oldFile); 
	}

	public boolean hasToken()
	{
		String oldFileName = "twitter4j.properties";
		boolean hasAccessToken = false;
		boolean hasAccessTokenSecret = false;
		BufferedReader br = null;
		try
		{
			try 
			{
				br = new BufferedReader(new FileReader(oldFileName));
				String line;

				while ((line = br.readLine()) != null) 
				{
					if (line.contains("oauth.accessToken="))
					{
						hasAccessToken = true;
					}

					if (line.contains("oauth.accessTokenSecret="))
					{
						hasAccessTokenSecret = true;
					}
				}
			}
			finally {
				if(br != null)
					br.close();		
			}	
		}
		catch (IOException e) {
			logger.error("AccessTokenHelper: IOException found in deleteTokens()", e);
		}

		return hasAccessToken && hasAccessTokenSecret; 
	}

}
