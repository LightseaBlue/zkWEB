<%--
  Created by IntelliJ IDEA.
  User: LightseaBlue
  Date: 2020/3/5
  Time: 12:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%
        String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    %>
    <base href="<%=path%>">
    <title>ZK节点查看</title>
    <link rel="stylesheet" type="text/css" href="jquery-easyui-1.7.0/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="jquery-easyui-1.7.0/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="jquery-easyui-1.7.0/themes/color.css">
    <script type="text/javascript" src="jquery-easyui-1.7.0/jquery.min.js"></script>
    <script type="text/javascript" src="jquery-easyui-1.7.0/jquery.easyui.min.js"></script>

</head>
<body class="easyui-layout">
<div data-options="region:'west',title:'节点',split:true" style="width:100px;">
    <ul id="tt"></ul>
</div>
<div data-options="region:'center',title:'节点信息'" style="padding:5px;background:#eee;">
    afd
</div>
<script>
    $(function () {
        alert(1);
        $.post("main.action",{
            op:"showTree"
        },function (data) {
           alert(data);
        });
    })
</script>
</body>
</html>
