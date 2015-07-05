import java.io.IOException;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;


public class PA1MinMax {
	
	public static class Map extends Mapper<Object,Text,Text,Text>{
		TreeMap<Double,String> min = new TreeMap<Double,String>(new Comparator<Double>() {

			@Override
			public int compare(Double p1, Double p2) {
				if (p1 > p2) return -1;
		        if (p1 < p2) return 1;
		        return 0;
			}
		});
		TreeMap<Double,String> max = new TreeMap<Double,String>();
		
		@Override
		public void map(Object key, Text value,
				Context ctx)
				throws IOException {
			StringTokenizer tokenizer = new StringTokenizer(value.toString());
			String name = tokenizer.nextToken();
			double volatility = Double.parseDouble(tokenizer.nextToken());
			min.put(volatility,name);
			max.put(volatility, name);
			if (min.size() > 10) {
				min.remove(min.firstKey());
			}
			
			if (max.size() > 10) {
				max.remove(max.firstKey());
			}
			
		
			
		}
		
		@Override
		protected void cleanup(Context ctx) throws IOException, InterruptedException{
			for(String value : min.values()){
				ctx.write(new Text("min"), new Text(value));
			}
			ctx.write(new Text("max"), new Text(""));
			for(String value : max.values()){
				ctx.write(new Text("max"), new Text(value));
			}
		}

	}
	
	static class Reduce extends Reducer<Text,Text,Text, Text>{
		@Override
		public void reduce(Text key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {
			for(Text t:values){
				context.write(key, t);
			}
		}
	}

}
