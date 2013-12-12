// /**
//  * @Author Gabe Kelly
//  * A Class that handles the JCommander Aspect of this project.
//  * Accepts multiple different kinds of command line inputs and parses
//  * them together. Could use more help in how exactly this will fit 
//  * with the other ui systems and into the project as a whole.*/
// package edu.allegheny.TweetAnalyze.UI;

// import com.beust.jcommander.Parameter;
// import com.beust.jcommander.JCommander;
// import java.io.File;

// public class TwitterAnalysisUI
// {

//     public static class JCommanderParams{

//     @Parameter(names = "-Type", description = "Chooses what aspect of the program to run (zip parser, refresh tweet etc.)", required = true)
//         private String command;//as far as i can see, there is no need to have multiple parameters for -f, -r, etc.

//     @Parameter(names = "-file", description = "The file you read in, into the program", converter = FileConverter.class)
//         File file;//reads in a file if -f is called. Or whatever it will be
    
//     @Parameter(names = "-Search", description = "The Tweet you are searching for")
//         private String tweetToLookFor;//will look for tweet by string if -s or whatever it will be is called.

//     @Parameter(names = "-Analysis", description = "What kind of analysis are you running")
//         private String analysis;//This defines what kind of analysis to do.

//     }

    
// }
