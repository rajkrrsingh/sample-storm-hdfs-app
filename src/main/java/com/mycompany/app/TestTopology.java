package com.mycompany.app;

import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.hdfs.bolt.HdfsBolt;
import org.apache.storm.hdfs.bolt.format.DefaultFileNameFormat;
import org.apache.storm.hdfs.bolt.format.DelimitedRecordFormat;
import org.apache.storm.hdfs.bolt.format.FileNameFormat;
import org.apache.storm.hdfs.bolt.format.RecordFormat;
import org.apache.storm.hdfs.bolt.rotation.FileRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy;
import org.apache.storm.hdfs.bolt.sync.CountSyncPolicy;
import org.apache.storm.hdfs.bolt.sync.SyncPolicy;
import org.apache.storm.topology.TopologyBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestTopology

{

  final static Logger logger = Logger.getLogger(TestTopology.class);

  
  public static HdfsBolt configureHDFSBolt(TopologyBuilder builder)
  {

    RecordFormat format = new DelimitedRecordFormat().withFieldDelimiter("|");
    
    SyncPolicy syncPolicy = new CountSyncPolicy(1000);
    
    FileRotationPolicy rotationPolicy = new FileSizeRotationPolicy(5, FileSizeRotationPolicy.Units.MB);
    
    FileNameFormat fileNameFormat = new DefaultFileNameFormat().withPath("/tmp").withPrefix("part-");
    
    HdfsBolt hdfsBolt = new HdfsBolt().withConfigKey("hdfs.config").withFsUrl("hdfs://r253secure.local:8020").withFileNameFormat(fileNameFormat).withRecordFormat(format).withRotationPolicy(rotationPolicy).withSyncPolicy(syncPolicy);

    return hdfsBolt;
  }
  

  
  public void buildAndSubmit(String submissionName)
    throws Exception
  {

    Config conf = new Config();

    
    Map<String, Object> hdfsConf = new HashMap();
    conf.put("hdfs.config", hdfsConf);
    List<String> auto_tgts = new ArrayList<String>();
    auto_tgts.add("org.apache.storm.hdfs.common.security.AutoHDFS");
    conf.put(Config.TOPOLOGY_AUTO_CREDENTIALS, auto_tgts);
    
    TopologyBuilder builder = new TopologyBuilder();

    builder.setSpout("spout", new TestSpout(), 1);

    HdfsBolt hdfsBolt = configureHDFSBolt(builder);
    builder.setBolt("sample-hdfs-topology", hdfsBolt, 1).shuffleGrouping("spout");
    logger.info("################### about to submit topology : ##############################");
    StormSubmitter.submitTopology(submissionName, conf, builder.createTopology());
  }
  
  public static void main(String[] args)
    throws Exception
  {
    if (args.length != 1)
    {
      System.out.println("Please provide a topology instance name as an argument.");
      return;
    }
    String submissionName = args[0];

    TestTopology testTopology = new TestTopology();
    
    testTopology.buildAndSubmit(submissionName);
  }
}
