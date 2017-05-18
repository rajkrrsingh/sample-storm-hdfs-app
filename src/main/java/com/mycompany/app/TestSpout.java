package com.mycompany.app;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class TestSpout implements IRichSpout {

  List<Values> values;
  SpoutOutputCollector _collector;

  @SuppressWarnings("rawtypes")
  @Override
  public void open(Map conf, TopologyContext context,
      SpoutOutputCollector collector) {
    this._collector = collector;

    values = new ArrayList<Values>();
    values.add(new Values("row", "cf1", "f1", "20120816"));
    values.add(new Values("row2", "cf1", "f1", "20120817"));
    values.add(new Values("row3", "cf1", "f1", "20120818"));
    values.add(new Values("row4", "cf1", "f1", "20120819"));
    values.add(new Values("row5", "cf1", "f1", "20120820"));
    values.add(new Values("row6", "cf1", "f120120820", "20120820"));
  }

  @Override
  public void close() {
  }

  @Override
  public void activate() {
  }

  @Override
  public void deactivate() {
  }

  @Override
  public void nextTuple() {
    int rand = (int) (Math.random() * 1000);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    _collector.emit(values.get(rand % values.size()));
  }

  @Override
  public void ack(Object msgId) {
  }

  @Override
  public void fail(Object msgId) {
  }

  @Override
  /*public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("shortid", "url", "user", "date"));
  }*/

  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("row", "cf", "f1", "value"));
  }

  @Override
  public Map<String, Object> getComponentConfiguration() {
    return null;
  }

}
