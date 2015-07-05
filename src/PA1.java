import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
//import org.apache.hadoop.mapred.FileInputFormat;
//import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
//import org.apache.hadoop.mapred.JobClient;
//import org.apache.hadoop.mapred.JobConf;
//import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//import org.apache.hadoop.mapred.Mapper;
//import org.apache.hadoop.mapred.OutputCollector;
//import org.apache.hadoop.mapred.Reducer;
//import org.apache.hadoop.mapred.Reporter;
//import org.apache.hadoop.mapred.TextInputFormat;
//import org.apache.hadoop.mapred.TextOutputFormat;

public class PA1 {
	public static class IntDoubleTupleWritable implements Writable {
		public int intPart;
		public double doublePart;

		public IntDoubleTupleWritable() {
		}

		public IntDoubleTupleWritable(int intPart, double doublePart) {
			this.intPart = intPart;
			this.doublePart = doublePart;

		}

		@Override
		public void write(DataOutput out) throws IOException {
			out.writeInt(intPart);
			out.writeDouble(doublePart);
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			intPart = in.readInt();
			doublePart = in.readDouble();
		}

		@Override
		public String toString() {
			return intPart + "," + doublePart;
		}

	}

	public static class Map extends
			Mapper<LongWritable, Text, Text, IntDoubleTupleWritable> {
		@Override
		public void map(LongWritable arg0, Text arg1, Context arg2)	throws IOException, InterruptedException {
			String line = arg1.toString();
			StringTokenizer tokenizer = new StringTokenizer(line, ",");
			InputSplit split = arg2.getInputSplit();

			String fileName = ((FileSplit) split).getPath().getName();
			String token = tokenizer.nextToken();
			if (!token.equals("Date")) {
				String[] date = token.split("-");
				String mnth = date[0] + date[1];
				tokenizer.nextToken();// open
				tokenizer.nextToken();// high
				tokenizer.nextToken();// low
				tokenizer.nextToken();// close
				tokenizer.nextToken();// volume
				String adjClose = tokenizer.nextToken();
				arg2.write(new Text(fileName + ":" + mnth),
						new IntDoubleTupleWritable(Integer.parseInt(date[2]),
								Double.parseDouble(adjClose)));

			}
		}

	}

	public static class Reduce extends
			Reducer<Text, IntDoubleTupleWritable, Text, IntDoubleTupleWritable> {

		@Override
		public void reduce(Text arg0, Iterable<IntDoubleTupleWritable> arg1,
				Context arg2)
				throws IOException, InterruptedException {
			{
				IntDoubleTupleWritable min = new IntDoubleTupleWritable(32, 0.0);
				IntDoubleTupleWritable max = new IntDoubleTupleWritable(-1, 0.0);
				for (IntDoubleTupleWritable tmp : arg1) {
					if (tmp.intPart > max.intPart) {
						max = new IntDoubleTupleWritable(tmp.intPart,
								tmp.doublePart);
					}
					if (tmp.intPart < min.intPart) {
						min = new IntDoubleTupleWritable(tmp.intPart,
								tmp.doublePart);
					}
				}

				double mnthEnd_p = max.doublePart;
				double mnthStrt_p = min.doublePart;
				double m_ror = (double) (mnthEnd_p - mnthStrt_p) / mnthStrt_p;
				arg2.write(arg0, new IntDoubleTupleWritable(0, m_ror));
			}
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		long start = new Date().getTime();
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf,"PA1");
		job.setJarByClass(PA1.class);
		Job job2 = Job.getInstance(conf,"PA1");
		job2.setJarByClass(PA1xbar.class);
		Job job3 = Job.getInstance();
		job3.setJarByClass(PA1MinMax.class);
		System.out.println("******************PA1 Utsav Start*********************");

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntDoubleTupleWritable.class);

		job2.setMapperClass(PA1xbar.Map2.class);
		job2.setReducerClass(PA1xbar.Reduce2.class);
		job2.setMapOutputKeyClass(Text.class);
		job2.setMapOutputValueClass(DoubleWritable.class);

		job3.setMapperClass(PA1MinMax.Map.class);
		job3.setReducerClass(PA1MinMax.Reduce.class);
		job3.setMapOutputKeyClass(Text.class);
		job3.setMapOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path("Inter_" +args[1]));
		FileInputFormat.addInputPath(job2, new Path("Inter_" + args[1]));
		FileOutputFormat.setOutputPath(job2, new Path("Output_" + args[1]));
		FileInputFormat.addInputPath(job3, new Path("Output_" + args[1]));
		FileOutputFormat.setOutputPath(job3, new Path(args[1]));

		job.waitForCompletion(true);
		job2.waitForCompletion(true);
		job3.waitForCompletion(true);
		
		long end = new Date().getTime();
		System.out.println("\nJob took " + (end-start)/1000 + "seconds\n");
		System.out.println("\n**********PA1-> End**********\n");	
	
	}

}
