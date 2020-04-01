package lab4.AvroStationTempYear;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class AvroMaxStationTempYear extends Configured implements Tool
{

	private static Schema SCHEMA;

	public static class AvroMapper extends Mapper<LongWritable, Text, AvroKey<GenericRecord>, FloatWritable>
	{
		private final NcdcLineReaderUtils utils = new NcdcLineReaderUtils();
		private GenericRecord record;
		AvroKey<GenericRecord> avroKey = new AvroKey<>(record);
		FloatWritable temp = new FloatWritable();

		@Override
		protected void setup(Mapper<LongWritable, Text, AvroKey<GenericRecord>, FloatWritable>.Context context)
				throws IOException, InterruptedException {
			
			super.setup(context);

			Schema SCHEMA = new Schema.Parser().parse(new File("./customSchema"));
			record = new GenericData.Record(SCHEMA);
		}
		@Override
		protected void map(final LongWritable key, final Text value, final Context context)
				throws IOException, InterruptedException {
			utils.parse(value.toString());

			if (utils.isValidTemperature()) {
				record.put("year", utils.getYearInt());
				record.put("stationId", utils.getStationId());
				record.put("maxtemp", utils.getAirTemperature());

				avroKey.datum(record);
				temp.set(utils.getAirTemperature());
				context.write(avroKey, temp);
			}
		}
	}

	public static class AvroReducer
			extends Reducer<AvroKey<GenericRecord>, FloatWritable, AvroKey<GenericRecord>, NullWritable> {

		@Override
		protected void reduce(final AvroKey<GenericRecord> key, final Iterable<FloatWritable> values,
				Context context)
				throws IOException, InterruptedException {
			
			float max = Integer.MIN_VALUE;
			for (final FloatWritable val : values) {
				max = Math.max(max, val.get());
			}

			key.datum().put("maxtemp", max);
			
			context.write(key, NullWritable.get());
		}
	}

	@Override
	public int run(final String[] args) throws Exception {
		if (args.length != 3) {
			System.err.printf("Usage: %s [generic options] <input> <output> <schema-file>\n",
					getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}

		final Job job = Job.getInstance();
		job.setJarByClass(AvroMaxStationTempYear.class);
		job.setJobName("Avro Station-Temp-Year");

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		final String schemaFile = args[2];

		// SCHEMA = new Schema.Parser().parse(new File(schemaFile));
		FileSystem hdfs = FileSystem.get(getConf());
		SCHEMA = new Schema.Parser().parse(hdfs.open(new Path(schemaFile)));
		job.addCacheFile(new URI(schemaFile + "#customSchema"));

		job.setMapperClass(AvroMapper.class);
		job.setReducerClass(AvroReducer.class);

		job.setMapOutputValueClass(FloatWritable.class);
		AvroJob.setMapOutputKeySchema(job, SCHEMA);
		// AvroJob.setMapOutputValueSchema(job, SCHEMA);
		AvroJob.setOutputKeySchema(job, SCHEMA);

		job.setOutputFormatClass(AvroKeyOutputFormat.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(final String[] args) throws Exception {
		final Configuration conf = new Configuration();
		FileSystem.get(conf).delete(new Path(args[1]), true);
		final int res = ToolRunner.run(conf, new AvroMaxStationTempYear(), args);
		System.exit(res);
	}
}