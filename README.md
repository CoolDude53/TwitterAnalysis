## TwitterAnalysis
This is a simple spark program. It shows the ability to take a given input of Tweet JSON files, map them to Tweet objects, analyze those, and then collect information and write to output. This specific implementation is built specifically to work with AWS (S3, EMR). The program takes input and writes output to an S3 bucket(s). It is meant to be run on an EMR, which is discussed in more depth below.
## Installation and Usage
1. Clone this repo
2. (Optional) If edits are desired -> Open maven project in any IDE (Java 8 required!)
3. Build maven project into a jar file (so long as Spark is provided on EMR, you do not need to include the extracted Spark output in the jar)
4. Upload jar to S3 bucket and save path
5. Ensure input files are located in the input path and the output directory is created in the output path, as specified in the application.properties file
6. Deploy an EMR cluster
7. Add step to cluster with specifications
  1. Spark Application
  2. Deploy mode = custer
  3. Spark submit args: --class pickle.plaza.TwitterMain
  4. Spark application location = <path from step 3>
8. Let application run, you can view progress if SSH'd into the EMR and proxied