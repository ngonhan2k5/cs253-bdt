package lab3;

import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class AvgTemp2 extends Configured implements Tool
{
	
	final static class Pair2 {
		private int count = 0;
		private int sum = 0;
		Pair2(int s, int c){
			count = s;
			sum = c;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getSum() {
			return sum;
		}

		public void setSum(int sum) {
			this.sum = sum;
		}

		
	}

	public static class ATMapper extends Mapper<LongWritable, Text, Text, ObjectWritable>
	{

		// private final static IntWritable value = new IntWritable(1);
		private Text key = new Text();
		private ObjectWritable valueObj = new ObjectWritable();

		@Override
		public void map(LongWritable offset, Text row, Context context) throws IOException, InterruptedException
		{
			try{ 
				String date = row.toString().substring(15,19); //get year
				String temp = row.toString().substring(87,92); //get temp*10

				key.set(date);
				Pair2 p = new Pair2(Integer.parseInt(temp), 1);
				valueObj.set(p);

				context.write(key, valueObj);
			}catch(IndexOutOfBoundsException e){
				System.out.println(row.toString());
			}catch(NumberFormatException e){

			}
			
		}
	}

	public static class ATCombiner extends Reducer<Text, ObjectWritable, Text, ObjectWritable>
	{
		private ObjectWritable result = new ObjectWritable();

		@Override
		public void reduce(Text key, Iterable<ObjectWritable> values, Context context) throws IOException, InterruptedException
		{
			int sum = 0, count = 0;
			for (ObjectWritable val : values) {
				Pair2 p = (Pair2) val.get();
                sum += p.getSum();
                count += p.getCount();
            }
            
            if (count>0){
                result.set(new Pair2(sum, count));
                context.write(key, result);
            }
		}
	}

	public static class ATReducer extends Reducer<Text, ObjectWritable, Text, FloatWritable>
	{
		private FloatWritable result = new FloatWritable();

		@Override
		public void reduce(Text key, Iterable<ObjectWritable> values, Context context) throws IOException, InterruptedException
		{
			int sum = 0, count = 0;
			for (ObjectWritable val : values)
			{
				Pair2 p = (Pair2) val.get();
                sum += p.sum;
                count += p.count;
            }
            
            if (count>0){
                result.set(sum/count);
                context.write(key, result);
            }
		}
	}

	public static void main(String[] args) throws Exception
	{
		Configuration conf = new Configuration();
		delOutDir(conf, args[1]);

		int res = ToolRunner.run(conf, new AvgTemp1(), args);

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

		Job job = new Job(getConf(), "AvgTemp2");
		job.setJarByClass(AvgTemp2.class);

		job.setMapperClass(ATMapper.class);
		job.setCombinerClass(ATCombiner.class);
		job.setReducerClass(ATReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));


		return job.waitForCompletion(true) ? 0 : 1;
	}
}
