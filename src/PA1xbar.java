import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapred.MapReduceBase;
//import org.apache.hadoop.mapred.Mapper;
//import org.apache.hadoop.mapred.OutputCollector;
//import org.apache.hadoop.mapred.Reducer;
//import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.*;

public class PA1xbar {
	public static class Map2 extends Mapper<LongWritable,Text,Text,DoubleWritable>{

		@Override
		public void map(LongWritable arg0, Text arg1,
				Context arg2)
				throws IOException, InterruptedException {
			String line = arg1.toString();
			String name = line.split("\\.")[0];
			String xi = line.split(",")[1];
			arg2.write(new Text(name), new DoubleWritable(Double.parseDouble(xi)));
		}
	}
	
	public static class Reduce2 extends Reducer<Text,DoubleWritable,Text,DoubleWritable>{
		@Override
		public void reduce(Text arg0, Iterable<DoubleWritable> arg1,
				Context arg2)
				throws IOException, InterruptedException {
			int count = 0;
			double xbar = 0;
			double xbar_sum = 0;
			List<Double> xis = new ArrayList<Double>();
			for(DoubleWritable tmp : arg1){
				xis.add(tmp.get());
				xbar_sum +=tmp.get();
			}
			count = xis.size();
			xbar = (double)xbar_sum/count;
			double volatility_sum = 0;
			for(int i = 0; i<xis.size(); i++){
				volatility_sum+= Math.pow((xis.get(i)-xbar_sum),2);
			}
			
			double volatility = Math.sqrt(((double)volatility_sum/count));
			arg2.write(arg0, new DoubleWritable(volatility));
		} 
	}
}
