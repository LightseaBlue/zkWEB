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
<div data-options="region:'north',title:'North Title',split:true" style="height:100px;">
    adf
</div>
<div data-options="region:'south',title:'South Title',split:true" style="height:100px;">
    adsf
</div>
<div data-options="region:'east',title:'East',split:true" style="width:100px;">
    sdaf
</div>
<div data-options="region:'west',title:'West',split:true" style="width:100px;">
    adf
</div>
<div data-options="region:'center',title:'center title'" style="padding:5px;background:#eee;">
    afd
</div>
</body>
</html>
