package com.example.isochannelproject;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;




@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DistributedSequence {

    private String ip="35.223.9.119";

    private String port="2181";
    private static ZooKeeper zookeeper;

    private String path="/stan";   //Latch is used to make the main thread wait until the zookeeper       client connects with the ensemble.
    private CountDownLatch latch = new CountDownLatch(0);
    private static final Logger logger = LoggerFactory.getLogger(DistributedSequence.class);
    //static List<String> zlist= new ArrayList<String>();
    @PostConstruct
    public void DistributedSequence1() {
        try {
            zookeeper= this.connect(ip,Integer.parseInt(port));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public String getStan(){
        try{
            if(zookeeper==null) {
                zookeeper= this.connect(ip,Integer.parseInt(port));
            }
            String out = zookeeper.create(path,"test".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            String seq = out.replace(path,"");
            return seq.substring(seq.length()-6);
        }catch(Exception e)
          {
            try {
                this.close();
                this.connect(ip,Integer.parseInt(port));
                String out = zookeeper.create(path,"test".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);String seq=   out.replace(path,"");
                return seq.substring(seq.length()-6);
            } catch (NumberFormatException | IOException | InterruptedException | KeeperException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
          //  logger.info("failed in stan generation ,reason="+ExceptionUtils.getStackTrace(e));
            return null;
        }
    }
    public static void update(String path,byte[] data) throws KeeperException, InterruptedException {
        zookeeper.setData(path, data, zookeeper.exists(path, true).getVersion());
    }
//              public static byte[] read() throws KeeperException, InterruptedException {
//                      return zookeeper.getData(path,true, zookeeper.exists("localhost", true));
//               }
     public void delete(String path) throws InterruptedException, KeeperException {zookeeper.delete(path,zookeeper.exists(path, true).getVersion());
    }
    public ZooKeeper connect(String host,int port) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper(host, port, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
            }
        });
        latch.await();
        return zookeeper;
    }    @PreDestroy
    public void close(){
        try {
            zookeeper.close();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
                       }
               }
       }