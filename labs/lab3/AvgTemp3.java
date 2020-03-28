package lab3;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.FloatWritable;
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

public class AvgTemp3 extends Configured implements Tool
{

	public static class ATMapper extends Mapper<LongWritable, Text, Text, Pair>
	{

		private Text key = new Text();
		private HashMap<String, Pair> map;

		@Override
		protected void setup(Mapper<LongWritable, Text, Text, Pair>.Context context)
				throws IOException, InterruptedException {

			map = new HashMap<>();
		}

		@Override
		public void map(LongWritable offset, Text row, Context context) throws IOException, InterruptedException
		{
			Pair pair;
			try{ 
				String year = row.toString().substring(15,19); //get year
				String temp = row.toString().substring(87,92); //get temp*10

				
				// Pair2 p = new Pair2(Integer.parseInt(temp), 1);
				int iTemp = Integer.parseInt(temp);
				if (map.containsKey(year)){
					pair = map.get(year);
					pair.set(pair.getSum()+iTemp, pair.getCount() + 1);
					
				}else{
					pair = new Pair(iTemp, 1);
				}
				map.put(year, pair);

			}catch(IndexOutOfBoundsException e){
				System.out.println(row.toString());
			}catch(NumberFormatException e){

			}
			
		}

		@Override
		protected void cleanup(Mapper<LongWritable, Text, Text, Pair>.Context context)
				throws IOException, InterruptedException {
					
				for(Map.Entry<String, Pair> entry: map.entrySet()){
					
					key.set(entry.getKey().toString());
					context.write(key, (Pair)entry.getValue());
				};
		}
	}

	public static class ATReducer extends Reducer<Text, Pair, Text, FloatWritable>
	{
		private FloatWritable result = new FloatWritable();

		@Override
		public void reduce(Text key, Iterable<Pair> values, Context context) throws IOException, InterruptedException
		{
			int sum = 0, count = 0;
			for (Pair val : values)
			{
                sum += val.getSum();
                count += val.getCount();
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

		int res = ToolRunner.run(conf, new AvgTemp3(), args);

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

		Job job = new Job(getConf(), "AvgTemp3");
		job.setJarByClass(AvgTemp3.class);

		job.setMapperClass(ATMapper.class);
		job.setReducerClass(ATReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Pair.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));


		return job.waitForCompletion(true) ? 0 : 1;
	}
}
