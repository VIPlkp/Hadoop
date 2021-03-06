package day12.cleanLog;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class AppLogClean {
	
	public static class MapTask extends Mapper<LongWritable, Text, Text, NullWritable>{
		StringBuilder sb = new StringBuilder();
		Text text = new Text();
		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			JSONObject o1 = JSON.parseObject(line);
			JSONObject ob2 = o1.getJSONObject("header");
			if(StringUtils.isBlank(ob2.getString("release_channel"))||
					StringUtils.isBlank(ob2.getString("device_id")) 
					|| StringUtils.isBlank(ob2.getString("city"))
					|| StringUtils.isBlank(ob2.getString("device_id_type"))
					|| StringUtils.isBlank(ob2.getString("app_ver_name"))
					|| StringUtils.isBlank(ob2.getString("os_name"))
					|| StringUtils.isBlank(ob2.getString("mac"))) {
					return;			
			}
			if(ob2.getString("app_ver_name").equals("android")){
				if(StringUtils.isBlank(ob2.getString("android_id"))){
					return;
				}
			}
			sb.append(ob2.getString("cid_sn")).append(",");
			sb.append(ob2.getString("mobile_data_type")).append(",");
			sb.append(ob2.getString("os_ver")).append(",");
			sb.append(ob2.getString("mac")).append(",");
			sb.append(ob2.getString("resolution")).append(",");
			sb.append(ob2.getString("commit_time")).append(",");
			sb.append(ob2.getString("sdk_ver")).append(",");
			sb.append(ob2.getString("device_id_type")).append(",");
			sb.append(ob2.getString("city")).append(",");
			sb.append(ob2.getString("android_id")).append(",");
			sb.append(ob2.getString("device_model")).append(",");
			sb.append(ob2.getString("carrier")).append(",");
			sb.append(ob2.getString("promotion_channel")).append(",");
			sb.append(ob2.getString("app_ver_name")).append(",");
			sb.append(ob2.getString("imei")).append(",");
			sb.append(ob2.getString("app_ver_code")).append(",");
			sb.append(ob2.getString("pid")).append(",");
			sb.append(ob2.getString("net_type")).append(",");
			sb.append(ob2.getString("device_id")).append(",");
			sb.append(ob2.getString("app_device_id")).append(",");
			sb.append(ob2.getString("release_channel")).append(",");
			sb.append(ob2.getString("country")).append(",");
			sb.append(ob2.getString("time_zone")).append(",");
			sb.append(ob2.getString("os_name")).append(",");
			sb.append(ob2.getString("manufacture")).append(",");
			sb.append(ob2.getString("commit_id")).append(",");
			sb.append(ob2.getString("app_token")).append(",");
			sb.append(ob2.getString("account")).append(",");
			sb.append(ob2.getString("app_id")).append(",");
			sb.append(ob2.getString("build_num")).append(",");
			sb.append(ob2.getString("language")).append(",");
			
			String uid = ob2.getString("mac");
			sb.append(uid);
			text.set(sb.toString());
			context.write(text, NullWritable.get());
			sb.delete(0, sb.length());
			
			
		}
	}
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		job.setMapperClass(MapTask.class);
		job.setJarByClass(AppLogClean.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.setNumReduceTasks(0);
		boolean b = job.waitForCompletion(true);
		System.out.println(b?0:1);
	}

}
