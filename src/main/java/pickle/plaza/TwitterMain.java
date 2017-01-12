package pickle.plaza;

import org.apache.spark.api.java.JavaRDD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pickle.plaza.analyze.AnalyzedTweet;
import pickle.plaza.analyze.TweetAnalyzer;
import pickle.plaza.loader.Loader;
import pickle.plaza.twitter.TweetSparkFacade;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TwitterMain
{
    public static final Logger LOG = LoggerFactory.getLogger(TwitterMain.class);
    private Properties config;

    private TwitterMain()
    {
        loadConfig(); // load the application properties
        run();
    }

    private void run()
    {
        TweetSparkFacade facade = new TweetSparkFacade(config.getProperty("appname")); // initialize spark objects
        TweetAnalyzer analyser = createAnalyser(); // load tweet analyzer with precompiled sentiment file
        JavaRDD<AnalyzedTweet> tweets = facade.asAnalyzedTweetStream(config.getProperty("input"), analyser); // pull tweet data, process, and store in RDD
        facade.sentimentAnalysis(tweets, config.getProperty("output")); // compile analysis and outputs
    }


    private TweetAnalyzer createAnalyser()
    {
        try
        {
            return new TweetAnalyzer(Loader.loadWordList(config));
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void loadConfig()
    {
        config = new Properties();
        try (InputStream ins = TwitterMain.class.getResourceAsStream("/application.properties"))
        {
            config.load(ins);
        } catch (IOException e)
        {
            LOG.error("Could not load application properties.");
            throw new RuntimeException(e);
        }
    }

    public static void main(String... argv)
    {
        new TwitterMain();
    }
}
