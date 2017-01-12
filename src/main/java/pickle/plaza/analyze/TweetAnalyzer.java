package pickle.plaza.analyze;

import pickle.plaza.twitter.Tweet;

import java.io.Serializable;

public class TweetAnalyzer implements Serializable
{
    private WordList list;

    public TweetAnalyzer(WordList list)
    {
        this.list = list;
    }

    // basic analysis, sum positive and negative scores to create single score
    public AnalyzedTweet analyse(Tweet tweet)
    {
        double score = 0.0;

        for (String word : tweet.getWords())
        {
            WordList.Score scoreObj = list.get(word);

            if (scoreObj != null)
            {
                score += scoreObj.getPositive();
                score -= scoreObj.getNegative();
            }
        }

        return new AnalyzedTweet(tweet, score);
    }
}
