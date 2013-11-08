package org.metricminer.infra.hadoop;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDFS {

	public void write(String fileName, String value) {
		try {
			Configuration configuration = new Configuration();
			FileSystem hdfs = FileSystem.get( new URI( "hdfs://localhost:54310" ), configuration );
			Path file = new Path("hdfs://localhost:54310/mm-data/" + fileName);
			
			OutputStream os;
			if ( !hdfs.exists(file)) { 
				os = hdfs.create(file); 
			} else {
				os = hdfs.append(file);
			}
			
			BufferedWriter br = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );
			br.write(value + "\n");
			br.close();
			hdfs.close();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
