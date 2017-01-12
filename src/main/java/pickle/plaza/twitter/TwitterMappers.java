package pickle.plaza.twitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import pickle.plaza.analyze.AnalyzedTweet;
import pickle.plaza.analyze.TweetAnalyzer;

import java.io.IOException;

public class TwitterMappers
{
    private static final ObjectMapper mapper = new ObjectMapper();

    public static AnalyzedTweet toTweet(String s, TweetAnalyzer analyzer)
    {
        try
        {
            Tweet tweet = mapper.readValue(s, Tweet.class); // map JSON string to Tweet class
            tweet.setWords(); // break tweet body into individual String Array
            return analyzer.analyse(tweet); // Analyze Tweet object and return AnalyzedTweet
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
