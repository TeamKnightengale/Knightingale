```
/* 
 *   __ __     _      __   __  _                __   
 *  / //_/__  (_)__ _/ /  / /_(_)__  ___ ____ _/ /__ 
 * / ,< / _ \/ / _ `/ _ \/ __/ / _ \/ _ `/ _ `/ / -_)
 */_/|_/_//_/_/\_, /_//_/\__/_/_//_/\_, /\_,_/_/\__/ 
 *          /___/                /___/          
 */Open-source Twitter analytics...with style!
```

To run the nightingale application you must first unpack the nightingale.jar file which holds all source code and test suites so you can test any future additions.

After unpacking the JAR file you will want to call "ant clean" to set up a clean base directory for running the application.  To start working with data you will call "ant compile" then "ant db" to populate the database with the provided ZIP history.  If you want to use your own history for this application please go to: www.twitter.com/settings/account.  If you are not signed-in please do then follow the link. Once there scroll down towards the bottom of the page and "Request your archive" which Twitter will send via email.  Save this zipfile in the base directory so the parser can put this information into a database.

To run analysis you will use the call "ant test" for the results of the test suite, "ant coverage" to see an HTML summary of test coverage without seeing the direct result of running the test suite, and "ant coveragetest" to return the results of the test suite with an HTML summary of coverage percentage.

Extra analysis: "ant ncss" to return the Java non-commented source statement analysis of all source code

"ant refresh" to update the current database through the twitter4j API