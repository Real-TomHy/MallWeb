<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<!-- 登录 注册 购物车... -->
<div class="container-fluid">
	<div class="col-md-4">
		<img src="img/logo2.png" />
	</div>
	<div class="col-md-5">
		<img src="img/header.png" />
	</div>
	<div class="col-md-3" style="padding-top:20px">
		<ol class="list-inline">
            <c:if test="${empty user}">
                <li><a href="login.jsp">登录</a></li>
                <li><a href="register.jsp">注册</a></li>
            </c:if>
            <c:if test="${!empty user}">
                <li>欢迎您,${user.username}</li>
                <li><a href="#" id="QuitUser" onclick="QuitUser()">退出</a> </li>
            </c:if>
			<li><a href="cart.jsp">购物车</a></li>
			<li><a href="${pageContext.request.contextPath}/myOrder">我的订单</a></li>
		</ol>
	</div>
</div>

<!-- 导航条 -->
<div class="container-fluid">
	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">首页</a>
			</div>

			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav" id="catergoryUl">
<%--					这样就写死了,我们可通过ajax去异步加载这些动态数据--%>
<%--					<c:forEach items="${categoryList }" var="category">--%>
<%--						<li><a href="#">${category.cname }</a></li>--%>
<%--					</c:forEach>--%>

				</ul>
				<form class="navbar-form navbar-right" role="search">
					<div class="form-group" style="position: relative">
						<input id="search" type="text" class="form-control" placeholder="Search" onkeyup="searchWord(this)">
						<div id="showDiv" style="position: absolute;z-index: 500;background: #f0f0f0;width: 179px;display:none;"></div>
					</div>
					<button type="submit" class="btn btn-default">Submit</button>
				</form>
		<%--			完成异步搜索	--%>
				<script type="text/javascript">
					//header.jsp加载完毕后 去服务器中获得category数据
					$(function () {

						$.ajax({
							type: "POST",
							url: "${pageContext.request.contextPath}/categoryList",
							async: true,
							dataType:"json",
							success: function(data){
								var context = "";
								<%--//动态创建 <li><a href="#">${category.cname }</a></li>--%>
								for (var i=0;i<data.length;i++){
									context+="<li><a href='${pageContext.request.contextPath}/productListByCid?cid="+data[i].cid+"'>"+data[i].cname+"</a></li>";
								}
								//将拼接好的li放入ul中
								$("#catergoryUl").html(context);
							}
						});

					});
                    function QuitUser() {
<%--                        <%--%>
<%--                        session.removeAttribute("user");%>//一个bug存在--%>
                        $.ajax({
                            type: "POST",
                            url: "${pageContext.request.contextPath}/QuitUser",
                            async: true,
                            success: function(data){
                                alert("账户已退出");
                            },
                            error:function () {
                                alert("失败");
                            }
                        });

						window.location.href = "index.jsp";

                    }

					function overFn(obj){
						$(obj).css("background","#DBEAF9");
					}
					function outFn(obj){
						$(obj).css("background","#fff");
					}

					function clickFn(obj){
						$("#search").val($(obj).html());
						$("#showDiv").css("display","none");
					}
					function searchWord(obj) {
						// 1.获得输入框内容
						var word=$(obj).val();
						var content = "";
						// 2.去数据库中模糊查询
						$.ajax({
							type: "POST",
							url: "${pageContext.request.contextPath}/searchWord",
							data: {"word":word},
							async: true,
							dataType:"json",
							success: function(data){
								if(data.length>0){
									for(var i=0;i<data.length;i++){
										content+="<div style='padding:5px;cursor:pointer' onclick='clickFn(this)' onmouseover='overFn(this)' onmouseout='outFn(this)'>"+data[i]+"</div>";
									// cursor:"pointer" 设置为小手
									}
									$("#showDiv").html(content);
									$("#showDiv").css("display","block");
								}
							},
							error:function () {
								alert("失败");
							}
						});}
				</script>
			</div>
		</div>
	</nav>
</div>