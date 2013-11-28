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

public class AccessTokenHelper
{
	public static void main(String argv[]) throws Exception
	{

		deleteAccessTokens();
		getnewTokens();
	}

	public static void getnewTokens() throws Exception
	{
		deleteAccessTokens(); //First, delete old access tokens;
		Twitter twitter = TwitterFactory.getSingleton();
		RequestToken requestToken = twitter.getOAuthRequestToken();
		AccessToken accessToken = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (null == accessToken) {
			System.out.println("Open the following URL and grant access to your account:");
			System.out.println(requestToken.getAuthorizationURL());
			System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
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

	private static void setTokens(AccessToken token)
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
		catch (IOException e) {
            	e.printStackTrace();
		} 
		
	}

	public static void deleteAccessTokens()
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
            	e.printStackTrace();
		} 
		
      // Once everything is complete, delete old file..
		File oldFile = new File(oldFileName);
		oldFile.delete();

      // And rename tmp file's name to old file name
		File newFile = new File(tmpFileName);
		newFile.renameTo(oldFile); 
	}

}
