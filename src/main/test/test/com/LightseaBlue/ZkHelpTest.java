package test.com.LightseaBlue;

import com.LightseaBlue.Dao.ZkHelp;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: ZkHelpTest Date: 2020/3/5 Time 13:39 Author: LightseaBlue
 * Version: 1.0 <br>
 */
public class ZkHelpTest {
	static ZkHelp help = new ZkHelp();
	static List<String> list = new ArrayList<String>();
	static ZooKeeper zk;

	static StringBuffer sb = null;

	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
		System.out.println("大理石会计分录");

	}

	private static String getMes(String path, int num) throws ClassNotFoundException, IOException {
		Stat stat = null;
		try {
			stat = zk.exists(path, false);
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
		if (stat == null) {
			System.out.println("无此目录...");
			return sb.toString();
		}

		sb = new StringBuffer();
		for (int i = 0; i < num; i++) {
			sb.append("-");
		}
		sb.append(path);

		List<String> child;
		try {
			child = zk.getChildren(path, false, null);
		} catch (KeeperException | InterruptedException e) {
			sb.append("无权限...");
//				e.printStackTrace();
			return sb.toString();
		}

		for (String str : child) {
			Object obj = null;
			byte[] data;
			num++;
			if ("/".equals(path)) {
				try {

					data = zk.getData(path + str, false, null);
					obj = help.toObject(ZkHelp.toBytes(data));
					sb.append("                          " + obj + "\n");
					getMes(path + str, num);
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				path = path + "/" + str;
				try {
					data = zk.getData(path, false, null);
					obj = help.toObject(ZkHelp.toBytes(data));
					sb.append("                          " + obj + "\n");
					getMes(path, num);
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
			}
//                System.out.println(sb);
		}

		return sb.toString();
	}
}