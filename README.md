### sample-storm-hdfs-app

#### configure Storm AutoHDFS plugin as follows
```
https://gist.github.com/rajkrrsingh/aeb4a4f10aaf2d6a6ca9a575c1f9572f
```

#### modify TestTopology.java, add your hdfs uri, replace core-site,hdfs-site with your cluster site-xml

#### build using maven
```
mvn clean package
```

### upload to storm cluster and run topology using 

```
storm jar sample-strom-hdfs-1.0-SNAPSHOT.jar com.mycompany.app.TestTopology TestTopology

```


