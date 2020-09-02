package com.LightseaBlue.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.LightseaBlue.JosnModel.JsonModel;
import com.google.gson.*;

/**
 * Description: BaseServlet Date: 2020/3/5 Time 12:55 Author: LightseaBlue
 * Version: 1.0 <br>
 */
public abstract class BaseServlet extends HttpServlet {

	private static final long serialVersionUID = 3929147019319436036L;

	protected String op;
	protected int pages = 1;
	protected int pagesize = 2;
	protected HttpSession session;
	protected JsonModel jm = new JsonModel();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse arg1) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8"); // 这样所有的servlet的编码都设置好了.
		arg1.setCharacterEncoding("utf-8");
		session = req.getSession();
		op = req.getParameter("op");
		if (req.getParameter("pages") != null) {
			pages = Integer.parseInt(req.getParameter("pages"));
		}
		if (req.getParameter("pagesize") != null) {
			pagesize = Integer.parseInt(req.getParameter("pagesize"));
		}
		super.service(req, arg1); // 判断请求方式，如果是get,调用doGet(),..
	}

	// 将一个对象以json字符串的格式输出客户端
	protected void toJson(HttpServletResponse response, Object obj) throws IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		String s = gson.toJson(obj);
		out.println(s);
		out.flush();
		out.close();
	}

	/**
	 * 获取传来的参数.....................request.getp
	 * 
	 * @param <T>
	 * @param request
	 * @param c
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NumberFormatException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	protected <T> T parseMap(HttpServletRequest request, Class<T> c) throws InstantiationException,
			IllegalAccessException, NumberFormatException, IllegalArgumentException, InvocationTargetException {
		T t = c.newInstance();// 调用c的无参数的构造方法相当于 new一个对象
		// 1.从request取出Map
		Map<String, String[]> map = request.getParameterMap();
		// entry条目的set集合
		Set<Entry<String, String[]>> set = map.entrySet();
		Iterator<Entry<String, String[]>> ite = set.iterator();
		// 取出c的方法
		Method[] md = c.getMethods();

		// 2.利用反射来创建
		while (ite.hasNext()) {
			Entry<String, String[]> entry = ite.next();
			String key = entry.getKey();
			String[] values = entry.getValue();
			String value = null;

			if (values.length != 1) {
				continue;
			}
			value = values[0];
			// System.out.println("传递参数"+key+"---值："+value);
			Method m = findMethod(md, key);
			// System.out.println(m+"111111111111");
			if (m == null) {
				continue;
			}
			// System.out.println("找到的激活的方法："+m.getName());
			String typeName = m.getParameterTypes()[0].getName();
//			System.out.println(typeName);
			if ("java.lang.Integer".equals(typeName) || "int".equals(typeName)) {
				m.invoke(t, Integer.parseInt(value));
			} else if ("java.lang.Double".equals(typeName) || "double".equals(typeName)) {
				m.invoke(t, Double.parseDouble(value));
			} else if ("java.lang.Float".equals(typeName) || "float".equals(typeName)) {
				m.invoke(t, Float.parseFloat(value));
			} else if ("java.lang.Long".equals(typeName) || "long".equals(typeName)) {
				m.invoke(t, Long.parseLong(value));
			} else {
				m.invoke(t, value);
			}
		}
		return t;
	}

	private Method findMethod(Method[] md, String key) {
		for (Method m : md) {
			// System.out.println(m.getName());
			String key1 = "set" + key;
			// System.out.println(key+"----------");
			if (key1.equalsIgnoreCase(m.getName())) {
				return m;
			}
		}
		return null;
	}

}
