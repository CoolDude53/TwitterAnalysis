package pickle.plaza.analyze;

import pickle.plaza.twitter.Tweet;

import java.io.Serializable;

public class AnalyzedTweet implements Serializable
{
    public static final long serialVersionUID = 1L;
    private static final double SENTIMENT_CUTOFF = 0.1;
    private long userId;
    private String userName;
    private boolean verified;
    private String tweet;
    private long retweetCount;
    private double score;
    private double normalizedScore;

    public AnalyzedTweet(Tweet tweet, double score)
    {
        this.userId = tweet.userId;
        this.userName = tweet.userName;
        this.verified = tweet.verified;
        this.tweet = tweet.tweet;
        this.retweetCount = tweet.retweetCount;
        this.score = score;
        normalizedScore = score / (double) tweet.getWords().length; // take summed score for tweet and divide by number of words in tweet
    }

    public double getScore()
    {
        return score;
    }

    public double getNormalizedScore()
    {
        return normalizedScore;
    }

    public boolean isPositive()
    {
        return normalizedScore > SENTIMENT_CUTOFF;
    }

    public boolean isNegative()
    {
        return normalizedScore < -SENTIMENT_CUTOFF;
    }

    public boolean isNeutral()
    {
        return normalizedScore >= -SENTIMENT_CUTOFF && normalizedScore <= SENTIMENT_CUTOFF;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public boolean isVerified()
    {
        return verified;
    }

    public void setVerified(boolean verified)
    {
        this.verified = verified;
    }

    public String getTweet()
    {
        return tweet;
    }

    public void setTweet(String tweet)
    {
        this.tweet = tweet;
    }

    public long getRetweetCount()
    {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount)
    {
        this.retweetCount = retweetCount;
    }

    @Override
    public String toString()
    {
        return "AnalyzedTweet{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", verified=" + verified +
                ", retweetCount=" + retweetCount +
                ", score=" + score +
                ", normalizedScore=" + normalizedScore +
                '}';
    }
}
