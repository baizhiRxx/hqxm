<%@page pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<style>
    img{
        max-width: 300px;
        max-height: 200px;
    }
    tr{
        text-align: center;
    }
    td{
        text-align: center;
    }
</style>
<script>
    $(function () {
        //搜索按钮事件
        $("#queryBtn").click(function(){
            //获取数据
            var content = $("#inputId").val();
            $.ajax({
                url:"${path}/article/showArticleByEs",
                type:"post",
                datatype:"json",
                data:{"str":content},
                success:function(data){

                    /*$.each(data,function(index,article){
                        $("#queryTable").append("<tr><td></td></tr>");

                    });*/
                    $("#queryTable").html("");
                    $.each(data,function(index,article){
                        $("#queryTable").append("<tr>" +
                            "<td>"+article.title+"</td>" +
                            "<td>"+article.content+"</td>" +
                            "<td>"+article.create_date+"</td>" +
                            "</tr>");
                    });
                }
            });
        });
    });

</script>

<%--搜索框--%>
<div align="center">
    <div class="input-group" style="width: 300px">
        <input id="inputId" type="text" class="form-control" placeholder="请输入搜索内容" aria-describedby="basic-addon2">
        <span class="input-group-btn" id="basic-addon2">
            <button type="button" class="btn btn-info" id="queryBtn">点击搜索</button>
        </span>
    </div><br>
</div>

<%--搜索框内容--%>
<div class="panel panel-default">
    <!-- 标题 -->
    <div class="panel-heading">搜索结果</div>

    <!-- 表格 -->
    <table class="table" id="queryTable"/>

</div>