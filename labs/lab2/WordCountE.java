import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class WordCountE extends Configured implements Tool
{

	public static class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable>
	{

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
		{
			for (String token : value.toString().split("\\s+"))
			{
				word.set(token);
				context.write(word, one);
			}
		}
	}

	public static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable>
	{
		private IntWritable result = new IntWritable();
		private int distinctCount = 0;

		@Override
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
		{
			distinctCount++;
			/*int sum = 0;
			for (IntWritable val : values)
			{
				sum += val.get();
			}
			if(sum >= 25) {
				result.set(sum);
				context.write(key, result);
			}
			*/
			//context.write(new Text("numberofdistinctword"), new IntWritable(distinctCount));
		}

		public void cleanup(Context context) throws IOException,                     InterruptedException
		{
			context.write(new Text("numberofdistinctword"), new IntWritable(distinctCount));				
		}
	}

	public static void main(String[] args) throws Exception
	{
		Configuration conf = new Configuration();
		delOutDir(conf, args[1]);

		int res = ToolRunner.run(conf, new WordCountE(), args);

		System.exit(res);
	}
	
	private static void delOutDir(Configuration conf, String path) throws IOException {
                // Remove output folder
                Path outdir = new Path(path);
                FileSystem fs = outdir.getFileSystem(conf);
                fs.delete(outdir, true);

	}

	@Override
	public int run(String[] args) throws Exception
	{

		Job job = new Job(getConf(), "WordCountE");
		job.setJarByClass(WordCountE.class);

		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		//job.setNumReduceTasks(2);

		return job.waitForCompletion(true) ? 0 : 1;
	}
}
