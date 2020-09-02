package com.LightseaBlue.Dao;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;


/**
 * Description: ZkHelp
 * Date: 2020/3/5
 * Time 13:17
 * Author: LightseaBlue
 * Version: 1.0 <br>
 */
public class ZkHelp {
    private String connectString = "node1:2181,node2:2181,node3:2181";
    private int sessionTimeout = 2000;
    private Logger log=Logger.getLogger(ZkHelp.class);
    private ZooKeeper zk;

    public ZkHelp(String connectString, int sessionTimeout) {
        this.connectString = connectString;
        this.sessionTimeout = sessionTimeout;
    }

    public ZkHelp() {
    }

    public ZooKeeper getZk() throws InterruptedException, IOException {
        CountDownLatch cdl=new CountDownLatch(1);
        zk=new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if(event.getState()== Event.KeeperState.SyncConnected){
                    log.info("Zk启动...");
                    cdl.countDown();
                }
            }
        });
        cdl.await();
        return zk;
    }

    public static byte[] toBytes(Object x) {
		byte[] b = null;
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream objectOut = null;
		try {
			objectOut = new ObjectOutputStream(byteOut);
			objectOut.writeObject(x);
			objectOut.flush();
			b = byteOut.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (byteOut != null) {
					byteOut.close();
				}
				if (objectOut != null) {
					objectOut.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return b;
	}
    
   public Object toObject(byte[] data)  {
		ByteArrayInputStream bytein=new ByteArrayInputStream(data);
		ObjectInputStream objectin;

		Object objdata = null;
		try {
			objectin = new ObjectInputStream(bytein);
			objdata = objectin.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return objdata;
	}
   
   public  String printZnodeInfo(Stat stat) {
	    SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		StringBuffer sb = new StringBuffer();
		sb.append("\n*******************************\n");
		sb.append("创建znode的事务id czxid:" + stat.getCzxid() + "\n");
		sb.append("创建znode的时间 ctime:" + df.format(stat.getCtime()) + "\n");
		sb.append("更新znode的事务id mzxid:" + stat.getMzxid() + "\n");
		sb.append("更新znode的时间 mtime:" + df.format(stat.getMtime()) + "\n");
		sb.append("更新或删除本节点或子节点的事务id pzxid:" + stat.getPzxid() + "\n");
		sb.append("子节点数据更新次数 cversion:" + stat.getCversion() + "\n");
		sb.append("本节点数据更新次数 dataVersion:" + stat.getVersion() + "\n");
		sb.append("节点ACL(授权信息)的更新次数 aclVersion:" + stat.getAversion() + "\n");
		if (stat.getEphemeralOwner() == 0) {
			sb.append("本节点为持久节点\n");
		} else {
			sb.append("本节点为临时节点,创建客户端id为:" + stat.getEphemeralOwner() + "\n");
		}
		sb.append("数据长度为:" + stat.getDataLength() + "字节\n");
		sb.append("子节点个数:" + stat.getNumChildren() + "\n");
		sb.append("\n*******************************\n");
		return sb.toString();
	}

    public void closeZk() throws InterruptedException {
        if(zk!=null&&zk.getState()== ZooKeeper.States.CONNECTED){
            log.info("Zk关闭...");
            zk.close();
        }
    }
}
