package pickle.plaza.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet implements Serializable
{
    public static final long serialVersionUID = 1L;
    public String[] words;

    @JsonProperty("user_id")
    public long userId;
    @JsonProperty("user_screen_name")
    public String userName;
    @JsonProperty("user_verified_aka_vip")
    public boolean verified;
    @JsonProperty("tweet")
    public String tweet;
    @JsonProperty("retweet_count")
    public long retweetCount;

    public String[] getWords()
    {
        return words;
    }

    public void setWords()
    {
        String s = tweet;
        s = s.substring(s.lastIndexOf("|") + 1);
        s = s.toLowerCase();
        s = s.replaceAll("[^a-z ]+", " ");

        this.words = s.split("\\s+");
    }

    @Override
    public String toString()
    {
        return "Tweet{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", verified=" + verified +
                ", tweet='" + tweet + '\'' +
                ", retweetCount=" + retweetCount +
                '}';
    }
}
