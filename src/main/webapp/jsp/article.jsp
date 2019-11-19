<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript">
    $(function () {
        $("#articleTable").jqGrid(
            {
                url: "${pageContext.request.contextPath}/article/showAllArticles",
                datatype: "json",
                colNames: ['编号','内容', '标题', '状态', '上传时间', '发布时间','操作'],
                colModel: [
                    {name: 'id', align: "center", hidden: true},
                    {name: 'content', align: "center", hidden: true},
                    {name: 'title', align: "center"},
                    {name: 'status', align: "center"},
                    {name: 'publish_date', align: "center"},
                    {name: 'create_date', align: "center"},
                    {name:'option',formatter:function (cellvalue, options, rowObject) {
                            var result = '';
                            result += "<a href='javascript:void(0)' onclick=\"showModel('" + rowObject.id + "')\" class='btn btn-lg' title='查看详情'> <span class='glyphicon glyphicon-th-list'></span></a>";
                            return result;
                        }},
                ],
                rowNum: 5,
                rowList: [5, 10, 20],
                pager: '#articlePager',
                mtype: "post",
                viewrecords: true,
                sortorder: "desc",
                styleUI: "Bootstrap",
                autowidth: true,
                multiselect: true,
                height:"500px",
                editurl:"${pageContext.request.contextPath}/article/editArticle"
            });
        $("#articleTable").jqGrid('navGrid', "#articlePager", {
            add:false,
            edit:false,
            del: true,
            deltext: "删除"
        });
    })
    // 打开模态框
    function addArticle() {
        $("#kindfrm")[0].reset();
        KindEditor.html("#editor_id", "");
        $("#modal_foot").html("<button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">关闭</button>"+
        "<button type=\"button\" class=\"btn btn-primary\" onclick=\"insertArticle()\">提交</button>")
        $("#kind").modal("show");
    }
    // 编辑文章
    function showModel(id) {
        var data = $("#articleTable").jqGrid("getRowData",id);
        $("#title").val(data.title);
        $("#status").val(data.status);
        $("#id").val(data.id);
        KindEditor.html("#editor_id",data.content);
        $("#modal_foot").html("<button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">关闭</button>"+
            "<button type=\"button\" class=\"btn btn-primary\" onclick=\"updateArticle()\">提交</button>")
        $("#kind").modal("show");
    }
    // 添加文章
    function insertArticle() {
        $.ajax({
            url:"${pageContext.request.contextPath}/article/addArticle",
            type:"post",
            datatype:"json",
            data:$("#kindfrm").serialize(),
            success:function (data) {
                $("#kind").modal("hide");
                $("#articleTable").trigger("reloadGrid");
            }
        })
    }
    function updateArticle() {
        $.ajax({
            url:"${pageContext.request.contextPath}/article/updateArticle",
            type:"post",
            datatype:"json",
            data:$("#kindfrm").serialize(),
            success:function (data) {
                $("#kind").modal("hide");
                $("#articleTable").trigger("reloadGrid");
            }
        })
    }
</script>
<div class="page-header">
    <h2><strong>文章管理</strong></h2>
</div>
<ul class="nav nav-tabs">
    <li class="active"><a>文章列表</a></li>
    <li><a onclick="addArticle()">添加文章</a></li>
</ul>
<div class="panel">
    <table id="articleTable"></table>
    <div id="articlePager" style="width: auto;height: 50px"></div>
</div>
