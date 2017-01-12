package pickle.plaza.twitter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pickle.plaza.analyze.AnalyzedTweet;
import pickle.plaza.analyze.TweetAnalyzer;
import scala.Tuple2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.List;

public class TweetSparkFacade
{
    private static final Logger LOG = LoggerFactory.getLogger(TweetSparkFacade.class);

    private SparkConf config;
    private JavaSparkContext sc;

    public TweetSparkFacade(String appName)
    {
        config = new SparkConf().setAppName(appName).setMaster("yarn-cluster"); // yarn cluster is for working with emrs deployed in cluster, use yarn if running in client mode
        sc = new JavaSparkContext(config);
    }

    // do actual analyis and write results to output
    public void sentimentAnalysis(JavaRDD<AnalyzedTweet> tweets, String output)
    {
        Configuration conf = new Configuration(); // initialize hadoop config
        FileSystem fileSystem; // create hadoop filesystem to write output to s3
        try
        {
            fileSystem = FileSystem.get(URI.create(output), conf);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        List<Tuple2<String, Double>> results;

        results = tweets
                .mapToPair(c -> new Tuple2<>(c.getUserName(), 1.0)) // map analyzed tweets to tuple objects
                .reduceByKey((a, b) -> a + b) // summation by key (i.e. [(Joe, 1), (Paul, 1), (Joe, 1)] reduced by key to [(Joe, 2), (Paul, 1)]
                .collect(); //collect to a list

        writeTuples(results, fileSystem, new Path(output + "totalTweetsByName.csv")); //write output to csv

        results = tweets
                .mapToPair(c -> new Tuple2<>(c.getUserName(), c.getNormalizedScore()))
                .reduceByKey((a, b) -> a + b)
                .collect();

        writeTuples(results, fileSystem, new Path(output + "sentimentTotalByName.csv"));
    }

    // write to s3 path
    private void writeTuples(List<Tuple2<String, Double>> list, FileSystem fileSystem, Path path)
    {
        try
        {
            FSDataOutputStream fsDataOutputStream = fileSystem.create(path); // create hadoop output stream
            LOG.info("Writing {} tuples to {}", list.size(), fsDataOutputStream);
            PrintWriter outs = new PrintWriter(fsDataOutputStream);
            list.stream()
                    .map(t -> t._1() + ", " + t._2())
                    .forEach(outs::println);

            outs.close();
            fsDataOutputStream.close();
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public JavaRDD<AnalyzedTweet> asAnalyzedTweetStream(String inFile, TweetAnalyzer analyser)
    {
        return sc.textFile(inFile) // take input and put into a string RDD
                .map(s -> TwitterMappers.toTweet(s, analyser)); // take strings and map to Tweet objects, then anaylze those
    }
}
