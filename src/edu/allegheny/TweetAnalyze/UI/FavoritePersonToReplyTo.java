/**
 * This is a method class for finding out who you reply to the most
 * @Author Gabe Kelly
 */
package edu.allegheny.TweetAnalyze.UI;//will change once i get the actual package or clear things up

import edu.allegheny.TweetAnalyze.Tweet;
import java.util.ArrayList;


public class FavoritePersonToReplyTo
{
    /**
     * This reads the param and returns the person
     * or persons you replied to the most,
     * and may be subject to improvement but this works. 
     * @param An ArrayList of Tweets
     * @return A String of the id of whom you replied the most to.
     * @Author Gabe Kelly
     */
    public String FavoritePersonToReplyTo(ArrayList<Tweet> tweet)
    {
        ArrayList<Long> replyToList = new ArrayList<Long>();//will hold the list of people who the user replied to
        
        int amountOfTimesReplied = 0;//holds how many times the favorite person was replied too. Need a better name that is not as clunky.
        String favorite = new String();//holds the person you replied to the most
        //This next bit of code will be replaced with method that already does that but since I do not have it right now
        //this is what i got

        for (int i = 0; i < tweet.size(); i++)
        {
            if (tweet.get(i).isReply())
            {
                replyToList.add(tweet.get(i).getInReplyToUserID());//adds the string of the user id to the list
            }
        }

        for (int i = 0; i < replyToList.size(); i++)
        {
            if (replyToList.get(i) != null)//makes sure that it is not null. Will make sense in a bit
            {
                long temporary = replyToList.get(i);//the person the tweet replied too

                int counter = 1;//counter to count how many times this person appears in the list
                
                for (int k = i + 1; k < replyToList.size(); k++)
                    {
                        if (temporary == replyToList.get(k))
                                {
                                    counter++;//increases counter

                                    replyToList.remove(k);//gets rid of a value to null which can help if this arraylist is like 1000 tweets.
                                }
                    }
                if (counter > amountOfTimesReplied)//Checks if you replied to this guy more than the other person
                {
                    amountOfTimesReplied = counter;//sets highest now equal to counter

                    favorite = "" + temporary;//favorite is now temporary
                }
                else if (counter == amountOfTimesReplied)//if they are equal
                {
                    favorite = favorite + "and, " + temporary;//adds the person to the favorite string
                }
            }
        }
    return favorite;
    }
}

