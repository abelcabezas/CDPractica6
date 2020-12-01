package main;

import java.io.IOException;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.fs.Path;
import us.car.accidents.core.CarAccident;
import us.car.accidents.utils.CarAccidentParser;

public class Main {

    public static class MediumDistanceFloatMapper extends Mapper<Object, Text, Text, FloatWritable> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            CarAccidentParser parser = new CarAccidentParser();
            CarAccident carAccident = parser.csvLineToCarAccident(line);
            //Create a sintetic index since it will be a sum of all of the values, we do not split by key
            context.write(new Text("Media"), new FloatWritable(carAccident.getDistance()));
        }

    }
    public static class MediumDistanceSumReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {
        // Reduce method
        public void reduce(Text key, Iterable<FloatWritable> values, Context context)
                throws IOException, InterruptedException {
            float sum = 0f;
            int i = 0;

            //We iterate over all of the values of the keys (get the sum of the distance and the
            // elements)
            for (FloatWritable val : values) {
                sum += val.get();
                i++;
            }

            // Get the average and write in the context the key and the average
            context.write(key, new FloatWritable(sum / i));
        }
    }


    public static class MostCommonSeverityIntMapper extends Mapper<Object, Text, Text, IntWritable> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            CarAccidentParser parser = new CarAccidentParser();
            CarAccident carAccident = parser.csvLineToCarAccident(line);
            //Create Add one to the type of severity
            if(carAccident.getSeverity()!=null){
                context.write(new Text("Tipo de severidad "+carAccident.getSeverity()), new IntWritable(1));
            }

        }

    }
    public static class MostCommonSeveritySumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        // Reduce method
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;

            //We iterate over all of the values of the keys (get the sum of the distance and the
            // elements)
            for (IntWritable val : values) {
                sum += val.get();
            }

            // Get the total amount of cases per severity and write in the context the key and the number of occurences
            context.write(key, new IntWritable(sum));
        }
    }

    public static class MostCommonSideIntMapper extends Mapper<Object, Text, Text, IntWritable> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            CarAccidentParser parser = new CarAccidentParser();
            CarAccident carAccident = parser.csvLineToCarAccident(line);
            //Create Add one to the type of severity
            if(carAccident.getSide()!=null){
                if(carAccident.getSide()=="R"){
                    context.write(new Text("Lado derecho: "+carAccident.getSide()), new IntWritable(1));
                }
                else if(carAccident.getSide()=="L"){
                    context.write(new Text("Lado izquierdo: "+carAccident.getSide()), new IntWritable(1));
                }
            }

        }

    }
    public static class MostCommonSideSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        // Reduce method
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;

            //We iterate over all of the values of the keys (get the sum of the distance and the
            // elements)
            for (IntWritable val : values) {
                sum += val.get();
            }

            // Get the total amount of cases per severity and write in the context the key and the number of occurences
            context.write(key, new IntWritable(sum));
        }
    }
    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("user_selection", args[2]);
        Job job = Job.getInstance(conf, "CarAccidents");
        job.setJarByClass(Main.class);
        //TODO set mapper, reducer and combiner depending on the option
        //Switch the option depending on the operation introduced by the user
        if (args[2].equals("1")) {
            job.setMapperClass(MostCommonSeverityIntMapper.class);
            job.setCombinerClass(MostCommonSeveritySumReducer.class);
            job.setReducerClass(MostCommonSeveritySumReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
        }
        if (args[2].equals("2")) {
            job.setMapperClass(MediumDistanceFloatMapper.class);
            job.setCombinerClass(MediumDistanceSumReducer.class);
            job.setReducerClass(MediumDistanceSumReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(FloatWritable.class);
        }
        if (args[2].equals("3")) {
            job.setMapperClass(MostCommonSideIntMapper.class);
            job.setCombinerClass(MostCommonSideSumReducer.class);
            job.setReducerClass(MostCommonSideSumReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
        }

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}