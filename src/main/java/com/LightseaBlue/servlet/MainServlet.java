package com.LightseaBlue.servlet;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.LightseaBlue.Dao.ZkHelp;
import com.LightseaBlue.JosnModel.JsonModel;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: MainServlet Date: 2020/3/5 Time 12:55 Author: LightseaBlue
 * Version: 1.0 <br>
 */
@WebServlet("/main.action")
public class MainServlet extends BaseServlet {
	private static final long serialVersionUID = -7638464140541956401L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			if ("getZk".equals(op)) {
				getZk(req, resp);
			} else if ("showTree".equals(op)) {
				showTree(req, resp);
			} else if ("showSata".equals(op)) {
				showSata(req, resp);
			} else if ("findChild".equals(op)) {
				findChild(req, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查找子节点
	 * 
	 * @param req
	 * @param resp
	 * @throws InterruptedException
	 * @throws KeeperException
	 * @throws IOException
	 */
	private void findChild(HttpServletRequest req, HttpServletResponse resp)
			throws KeeperException, InterruptedException, IOException {
		String path = req.getParameter("path");
		ZooKeeper zk = (ZooKeeper) req.getSession().getAttribute("zk");
		jm = getTree(path, zk);
		toJson(resp, jm);
	}

	/**
	 * 显示节点信息
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void showSata(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		ZooKeeper zk = (ZooKeeper) req.getSession().getAttribute("zk");
		String path = req.getParameter("path");
		JsonModel jm = new JsonModel();
		Stat stat = new Stat();
		try {
			byte[] data = zk.getData(path, false, stat);
			Map<String, String> map = new HashMap<String, String>();
			SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
			String str = new String(data, "utf-8");
			map.put("nodeContent", str);
			map.put("numChildren", stat.getNumChildren() + "");
			map.put("czxid", Long.toHexString(stat.getCzxid()));
			map.put("ctime", df.format(stat.getCtime()));
			map.put("mzxid", Long.toHexString(stat.getMzxid()));
			map.put("pzxid", Long.toHexString(stat.getPzxid()));
			map.put("cversion", stat.getCversion() + "");
			map.put("dataVersion", stat.getVersion() + "");
			map.put("aclVersion", stat.getAversion() + "");
			map.put("mtime", df.format(stat.getMtime()));
			if (stat.getEphemeralOwner() == 0) {
				map.put("nodetype", "持久节点");
			} else {
				map.put("nodetype", "临时节点");
				map.put("clientId", Long.toHexString(stat.getEphemeralOwner()));
			}
			map.put("dataLength", stat.getDataLength() + "");
			map.put("cversion", stat.getCversion() + "");
			jm.setCode(1);
			jm.setObj(map);
		} catch (KeeperException | InterruptedException e) {
//			e.printStackTrace();
			jm.setCode(2);
			jm.setMessage("无权限....");
		} catch (UnsupportedEncodingException e) {
			jm.setCode(2);
			jm.setMessage("无无信息....");
		}
		toJson(resp, jm);
	}

	/**
	 * 展示树
	 *
	 * @param req
	 * @param resp
	 */
	private void showTree(HttpServletRequest req, HttpServletResponse resp)
			throws KeeperException, InterruptedException, IOException {
		ZooKeeper zk = (ZooKeeper) req.getSession().getAttribute("zk");
		String path = req.getParameter("path");
		jm = getTree(path, zk);
		toJson(resp, jm);
	}

	private JsonModel getTree(String path, ZooKeeper zk) throws KeeperException, InterruptedException {
		JsonModel jm = new JsonModel();
		List<String> tree = zk.getChildren(path, null);
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < tree.size(); i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", i + 1);
			map.put("text", tree.get(i));
			map.put("state", "closed");
			String reallyPath = null;
			if ("/".equals(path)) {
				reallyPath = path + tree.get(i);
			} else {
				reallyPath = path + "/" + tree.get(i);
			}
			map.put("attributes", reallyPath);
			list.add(map);
		}
		jm.setCode(1);
		jm.setObj(list);
		return jm;
	}

	/**
	 * 获取zookeeper
	 *
	 * @param req
	 * @param resp
	 */
	private void getZk(HttpServletRequest req, HttpServletResponse resp) throws IOException, InterruptedException {
		int waitTime = Integer.parseInt(req.getParameter("waitTime"));
		String addr = req.getParameter("addr");
		ZkHelp help = new ZkHelp(addr, waitTime);
		ZooKeeper zk = help.getZk();
		req.getSession().setAttribute("zk", zk);
		resp.sendRedirect("back/Main.jsp");
	}

}
