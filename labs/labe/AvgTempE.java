package labe;


import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
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

public class AvgTempE extends Configured implements Tool {

	public static class ATMapper extends Mapper<LongWritable, Text, Pair, Text> {

		// private final static IntWritable value = new IntWritable(1);
		private Text year = new Text();
		private Pair key = new Pair();

		@Override
		public void map(LongWritable offset, Text row, Context context) throws IOException, InterruptedException {
			try {
				String station = row.toString().substring(4, 10) + "-" + row.toString().substring(10, 15); // station id
				String syear = row.toString().substring(15, 19); // get year
				String temp = row.toString().substring(87, 92); // get temp*10

				key.setStationId(station);
				key.setTemperature(Integer.parseInt(temp));
				year.set(syear);

				context.write(key, year);
			} catch (IndexOutOfBoundsException e) {
				System.out.println(row.toString());
			} catch (NumberFormatException e) {

			}

		}
	}

	public static class ATReducer extends Reducer<Pair, Text, Pair, Text> {

		@Override
		public void reduce(Pair key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			for (Text val : values) {
				context.write(key, val);
			}

		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		delOutDir(conf, args[1]);

		int res = ToolRunner.run(conf, new AvgTempE(), args);

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

		Job job = new Job(getConf(), "AvgTempE");
		job.setJarByClass(AvgTempE.class);

		job.setMapperClass(ATMapper.class);
		job.setReducerClass(ATReducer.class);

		job.setOutputKeyClass(Pair.class);
		job.setOutputValueClass(Text.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);


		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));


		return job.waitForCompletion(true) ? 0 : 1;
	}

	
	
}
