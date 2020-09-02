<%--
  Created by IntelliJ IDEA.
  User: LightseaBlue
  Date: 2020/3/5
  Time: 12:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<%
	if (session.getAttribute("zk") == null) {
		response.sendRedirect("../index.jsp");
	}
	String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<base href="<%=path%>">
<title>ZK节点查看</title>
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.7.0/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.7.0/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.7.0/themes/color.css">
<script type="text/javascript" src="jquery-easyui-1.7.0/jquery.min.js"></script>
<script type="text/javascript"
	src="jquery-easyui-1.7.0/jquery.easyui.min.js"></script>



<style type="text/css">
table {
	border-collapse: collapse;
	margin: 0 auto;
	text-align: center;
}

table td, table th {
	border: 1px solid #cad9ea;
	color: #666;
	height: 30px;
}

table thead th {
	background-color: #CCE8EB;
	width: 100px;
}

table tr:nth-child(odd) {
	background: #fff;
}

table tr:nth-child(even) {
	background: #F5FAFA;
}
</style>

</head>
<body class="easyui-layout">
	<div data-options="region:'west',title:'节点',split:true"
		style="width: 189px;">
		<ul id="tt"></ul>
	</div>

	<div data-options="region:'center',title:'节点信息'"
		style="padding: 5px; background: #eee;">
		当前路径为:<span id="currentPath"></span> <br /> 节点内容: <span
			id="nodeContent"></span>
		<hr />
		<table border="1" padding="0px" margin="0px" width="90%">
			<tr>
				<td>子节点数</td>
				<td><span id="numChildren"></span></td>
				<td>数据长度(字节)</td>
				<td><span id="dataLength"></span></td>
			</tr>
			<tr>
				<td>创建znode的事务id czxid</td>
				<td><span id="czxid"></span></td>
				<td>创建znode的时间 ctime</td>
				<td><span id="ctime"></span></td>
			</tr>
			<tr>
				<td>更新znode的事务id mzxid</td>
				<td><span id="mzxid"></span></td>
				<td>更新znode的时间 mtime</td>
				<td><span id="mtime"></span></td>
			</tr>
			<tr>
				<td>更新或删除本节点或子节点的事务id pzxid</td>
				<td><span id="pzxid"></span></td>
				<td>子节点数据更新次数 cversion</td>
				<td><span id="cversion"></span></td>
			</tr>
			<tr>
				<td>本节点数据更新次数 dataVersion</td>
				<td><span id="dataVersion"></span></td>
				<td>节点ACL(授权信息)的更新次数 aclVersion</td>
				<td><span id="aclVersion"></span></td>
			</tr>
			<tr>
				<td>节点类型</td>
				<td><span id="nodetype"></span></td>
				<td>创建客户端id</td>
				<td><span id="clientId"></span></td>
			</tr>
		</table>
	</div>


	<script>
		$(function() {
			$.post("main.action", {
				op : "showTree",
				path:"/"
			}, function(data1) {
				$('#tt').tree({
					data : data1.obj
				});
				$('#currentPath').html('/');
				showStat('/');
			});
			
		})

		$('#tt').tree({
			onClick : function(node) {
				var path=node.attributes;
				$('#currentPath').html(path);
				showStat(path);
			}
		});

		$('#tt').tree({
			onDblClick : function(node) {
				var path=node.attributes;
				var target=node.target;
				$.post("main.action",{
					op:"findChild",
					path:path
				},function(data){
					var child=$('#tt').tree('getChildren',node.target);
					for(var i=0;i<child.length;i++){
						$('#tt').tree('remove',child[i]);
					}
					$('#tt').tree('reload',node.target); 
					
					$('#tt').tree('append', {
							parent:target,
							data:data.obj
					});
				});
			}
		});
		
		
		function showStat(path){
			$.post("main.action",{
				op:"showSata",
				path:path
			},function(data){
				if(data.message==2){
					alert("此节点无权限...");
					return;
				}
				clearAll();
				showData(data.obj);
			});
		}

		function clearAll() {
			$("#nodeContent").html("");
			$("#numChildren").html("");
			$("#czxid").html("");
			$("#ctime").html("");
			$("#mzxid").html("");
			$("#mtime").html("");
			$("#pzxid").html("");
			$("#cversion").html("");
			$("#dataVersion").html("");
			$("#aclVersion").html("");
			$("#nodetype").html("");
			$("#clientId").html("");
			$("#dataLength").html("");
			$("#clientId").html("");
		}

		function showData(obj) {
			if (obj.nodeContent != undefined) {
				$("#nodeContent").html(obj.nodeContent);
			}
			$("#numChildren").html(obj.numChildren);
			$("#czxid").html(obj.czxid);
			$("#ctime").html(obj.ctime);
			$("#mzxid").html(obj.mzxid);
			$("#mtime").html(obj.mtime);
			$("#pzxid").html(obj.pzxid);
			$("#cversion").html(obj.cversion);
			$("#dataVersion").html(obj.dataVersion);
			$("#aclVersion").html(obj.aclVersion);
			$("#nodetype").html(obj.nodetype);
			$("#clientId").html(obj.clientId);
			$("#dataLength").html(obj.dataLength);
			$("#clientId").html(obj.clientId);
		}
	</script>
</body>
</html>
