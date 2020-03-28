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
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class AvgTempE2 extends Configured implements Tool {

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

		// private MultipleOutputs<Pair, Text> multipleOutputs;

		@Override
		public void reduce(Pair key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			for (Text val : values) {
				context.write(key, val);
				// multipleOutputs.write(key, val, "StationTempRecord");
				// multipleOutputs.write("text", key, val, "StationTempRecord");
			}

		}


		// @Override
		// public void setup(Context context){
		// 	multipleOutputs = new MultipleOutputs<Pair, Text>(context);
		// }
		
		// @Override
		// public void cleanup(final Context context) throws IOException, InterruptedException{
		// 	multipleOutputs.close();
		// }
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		delOutDir(conf, args[1]);

		int res = ToolRunner.run(conf, new AvgTempE2(), args);

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

		Job job = new Job(getConf(), "AvgTempE2");
		job.setJarByClass(AvgTempE2.class);

		job.setMapperClass(ATMapper.class);
		job.setReducerClass(ATReducer.class);

		job.setOutputKeyClass(Pair.class);
		job.setOutputValueClass(Text.class);

		job.setInputFormatClass(TextInputFormat.class);
		// job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputFormatClass(STextOutputFormat.class);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));


		return job.waitForCompletion(true) ? 0 : 1;
	}

	
	
}
