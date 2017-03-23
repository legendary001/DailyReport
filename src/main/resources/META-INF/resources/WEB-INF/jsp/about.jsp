<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setHeader("Expires","0");

%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>

<c:set var="ctx" value="${pageContext['request'].contextPath}"/>

<html>
<head>
    <meta charset="utf-8">
    <!-- 引入 echarts.js -->

    <link rel="stylesheet" href="/css/all.css" />
    <link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon">
</head>
<body>
    <script src="/js/canvas-nest.min.js" count="200" zindex="-2" opacity="0.5" color="47,135,193" type="text/javascript"></script>


    <!-----------------内页  begin------------------>
    <div class="contentBk nyBk">
        <div class="box mb25">
            <p> </p>
        </div>
        <div class="box mb25">
            当前位置：  <a href=/about>关于我们</a>
        </div>

       <div class="box" style="width: 100%;height:5px;"></div>

        <div class="box" style="width: 100%;height:540px;">
           <div class="box mb25">
               <div class="box" style="width: 2%;float: left;height:540px;"></div>
               <div class="box" style="width: 96%;float: left;height:540px;">

               </div>
              <div class="box" style="width: 2%;float: left;height:540px;"></div>
           </div>
       </div>

      <div class="box" style="width: 100%;height:30px;"></div>

       </div>
    </div>
    <!-----------------内页  end------------------>

    <!----------------底部信息 begin---------------->
    <div class="bot">
        <div class="box cf">
            <div class="contactUs">
                <h2 class="contactIcon bg cf"></h2>
                <span class="phoneEmail phoneEmailL">
                    <b class="telIcon cz bg"></b>
                    <div class="telNum">
                        <p>mute88</span></p>
                    </div>
                </span>
            </div>
        </div>
    </div>
  <!----------------底部信息 end---------------->



</body>
</html>