package src.edu.allegheny.TweetAnalyze.Parser;

import au.com.bytecode.opencsv.*;
import java.io.*;
import java.util.ArrayList;
import edu.allegheny.TweetAnalyze.Tweet;
import edu.allegheny.TweetAnalyze.Parser.Visitor.*;


public class CSVParser {

    private File csv;

    public CSVParser(File c)
   {
       csv = c;
   }

   public ArrayList<Tweet> parseFile(File c) throws IOException
   {
       CSVReader reader = new CSVReader(new FileReader(c.getString()));

       ArrayList<String[]> twtData = (ArrayList<String[]>) reader.readAll();

       String[] loneTwt = new String[twtData.size()];

       ArrayList<Tweet> twtAnalyze = new ArrayList<Tweet>();

       for (int i = 1; i < twtData.size(); i++)
       {
           loneTwt = twtData.get(i);

           Long Id = new Long(loneTwt[0]);

           long id = Id.longValue();

           Date timeStamp = new Date(loneTwt[4]);

           String source = loneTwt[4];

           String text = loneTwt[5];

           if (loneTwt[1] != null)
           {
                Long ReplyStatus = new Long(loneTwt[1]);

                long replyStatus = ReplyStatus.longValue();
                
                Long ReplyUser = new Long(loneTwt[2]);

                long replyUser = ReplyUser.longValue();

               if (loneTwt[9] != null)
               {
                   ArrayList<String> url = new ArrayList<String>(Arrays.asList(loneTwt[9].split(",")));

                   Tweet twt = new Tweet (id, timeStamp, source, text, replyStatus, replyUser, url);
               }
               else
               {
                   Tweet twt = new Tweet (id, timeStamp, source, text, replyStatus, replyUser);
               }
           }
           else{
            if (loneTwt[6] != null)
            {
                Long RetweetId = new long(loneTwt[6]);
                long retweetId = RetweetId.longValue();

                Long RetweetUser = new long (loneTwt[7]);
                long retweetUser = RetweetUser.longValue();
                
                Date retweetDate = new Date(loneTwt[8]);

                if (loneTwt[9] != null)
              
                Long RetweetUser = new long (loneTwt[7]);
                long retweetUser = RetweetUser.longValue();
                
                Date retweetDate = new Date(loneTwt[8]);

                if (loneTwt[9] != null)
                {
                    ArrayList<String> url = new ArrayList<String>(Arrays.asList(loneTwt[9].split(",")));

                    Tweet twt = new Tweet (id, timeStamp, source, text, retweetId, retweetUser, retweetDate, url);
                }
                else
                {
                    Tweet twt = new Tweet (id, timeStamp, source, text, retweetId, retweetUser, retweetDate);
                }
            }
         
        else{
            if (loneTwt[9] != null)
            {
                ArrayList<String> url = new ArrayList<String>(Arrays.asList(loneTwt[9].split(",")));
        
                Tweet twt = new Tweet (id, timeStamp, source, text, url);
            }
            else
            {
                Tweet twt = new Tweet (id, timeStamp, source, text)
            }
            }
           }
        twtAnalyze.add(twt);
        }

       return twtAnalyze;
   }



	
}
