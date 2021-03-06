package main;

import java.io.IOException;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
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
            //Create a sintetic key for summing all the values
            context.write(new Text("Media"), new FloatWritable(carAccident.getDistance()));
        }

    }

    public static class MediumDistanceSumReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {
        // Reduce method
        public void reduce(Text key, Iterable<FloatWritable> values, Context context)
                throws IOException, InterruptedException {
            float sum = 0;
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
            //Add one to the type of severity
            if (carAccident.getSeverity() != null) {
                context.write(new Text("Tipo de severidad " + carAccident.getSeverity()), new IntWritable(1));
            }

        }

    }

    public static class MostCommonSeveritySumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        // Reduce method
        int max = 0;
        Text keyWithMax = new Text("");

        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            //We iterate over all of the keys (get the sum of the distance and the
            // elements)
            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new IntWritable(sum));
            if (sum > max) {
                max = sum;
                keyWithMax = new Text(key.toString());
            }

        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
                context.write(new Text("La severidad mas comun es: " + keyWithMax + " con un numero de ocurrencias igual a: "), new IntWritable(max));
        }
    }

    public static class MostCommonSideIntMapper extends Mapper<Object, Text, Text, IntWritable> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            CarAccidentParser parser = new CarAccidentParser();
            CarAccident carAccident = parser.csvLineToCarAccident(line);
            //Add one to the side key
            if (carAccident.getSide() != null && !carAccident.getSide().isEmpty()) {
                context.write(new Text(carAccident.getSide()), new IntWritable(1));
            }

        }

    }

    public static class MostCommonSideSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        int max = 0;
        Text keyWithMax = new Text("");
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new IntWritable(sum));
            if (sum > max) {
                max = sum;
                keyWithMax = new Text(key.toString());
            }

        }
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
                context.write(new Text("El lado mas comun de la calle es: " + keyWithMax + " con un numero de ocurrencias igual a: "), new IntWritable(max));
        }
    }

    public static class MostCommonConditionMapper extends Mapper<Object, Text, Text, IntWritable> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            CarAccidentParser parser = new CarAccidentParser();
            CarAccident carAccident = parser.csvLineToCarAccident(line);
            if (carAccident.getW_condition() != null && !carAccident.getW_condition().isEmpty()) {
                context.write(new Text(carAccident.getW_condition()), new IntWritable(1));
            }

        }

    }

    public static class MostCommonConditionReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        int max = 0;
        Text keyWithMax = new Text("");

        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new IntWritable(sum));
            if (sum > max) {
                max = sum;
                keyWithMax = new Text(key.toString());
            }

        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
                context.write(new Text("La condicion climatologica maxima es: " + keyWithMax), new IntWritable(max));
        }
    }

    public static class AccidentsUnderVisibilityThresholdMapper extends Mapper<Object, Text, Text, IntWritable> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            Configuration conf = context.getConfiguration();
            float threshold = Integer.parseInt(conf.get("umbral"));
            CarAccidentParser parser = new CarAccidentParser();
            CarAccident carAccident = parser.csvLineToCarAccident(line);
            //Create Add one to the type of severity
            if (carAccident.getVisibility() <= threshold) {
                context.write(new Text("Numero de accidentes bajo el umbral (" + threshold + ") :"), new IntWritable(1));
            }

        }

    }

    public static class AccidentsUnderVisibilityThresholdReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        if (args.length > 3) {
            conf.set("umbral", args[3]);
        }
        Job job = Job.getInstance(conf, "CarAccidents_" + args[2]);
        job.setJarByClass(Main.class);

        //Switch the option depending on the operation introduced by the user
        if (args[2].equals("1")) {
            job.setMapperClass(MostCommonSeverityIntMapper.class);
            job.setReducerClass(MostCommonSeveritySumReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
        }
        if (args[2].equals("2")) {
            job.setMapperClass(MediumDistanceFloatMapper.class);
            job.setReducerClass(MediumDistanceSumReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(FloatWritable.class);
        }
        if (args[2].equals("3")) {
            job.setMapperClass(MostCommonSideIntMapper.class);
            job.setReducerClass(MostCommonSideSumReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
        }
        if (args[2].equals("4")) {
            job.setMapperClass(MostCommonConditionMapper.class);
            job.setReducerClass(MostCommonConditionReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
        }
        if (args[2].equals("5")) {
            job.setMapperClass(AccidentsUnderVisibilityThresholdMapper.class);
            job.setReducerClass(AccidentsUnderVisibilityThresholdReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
        }
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}