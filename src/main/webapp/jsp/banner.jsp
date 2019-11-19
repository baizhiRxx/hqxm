<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript">
    $(function () {
        $("#bannerTable").jqGrid(
            {
                url: "${pageContext.request.contextPath}/banner/showBanners",
                datatype: "json",
                colNames: ['编号', '标题', '状态', '描述', '创建时间', '图片'],
                colModel: [
                    {name: 'id', align: "center", hidden: true},
                    {name: 'title', align: "center", editable: true, editrules: {required: true}},
                    {
                        name: 'status', align: "center", formatter: function (data) {
                            if (data == 1) return "展示";
                            if (data == 2) return "不展示"
                        },editable:true,edittype:"select",editoptions:{value:"1:展示;2:不展示"}
                    },
                    {name: 'description', align: "center", editable: true, editrules: {required: true}},
                    {name: 'create_date', align: "center"},
                    {
                        name: "url",
                        index: "url",
                        align: "center",
                        editable: true,
                        edittype: "file",
                        editoptions: {enctype: "multipart/form-data"},
                        formatter: function (cellValue) {
                            return "<img style='height: 80px;width: 180px' src='../img/" + cellValue + "'/>"
                        },
                    },
                ],
                rowNum: 2,
                rowList: [2, 5, 10],
                pager: '#bannerPager',
                sortname: 'id',
                mtype: "post",
                viewrecords: true,
                sortorder: "desc",
                styleUI: "Bootstrap",
                autowidth: true,
                multiselect: true,
                rownumbers: true,
                editurl:"${pageContext.request.contextPath}/banner/editBanner"
            });
        $("#bannerTable").jqGrid('navGrid', "#bannerPager", {
                edit: true,
                add: true,
                del: true,
                search: false,
                refresh: false,
                edittext: "编辑",
                addtext: "添加",
                deltext: "删除"
            },
            //默认第一个是edit, add, del
            {
                closeAfterEdit: true,
                beforeShowForm: function (frm) {
                    frm.find('#create_date').attr('readOnly',true);
                    frm.find('#url').attr("disabled",true);
                },
                afterComplete:function (response,postData) {
                    var msg = response.responseJSON.msg;
                    $("#msgShow").html(msg);
                    $("#addBannerMsg").show();
                    setTimeout(function () {
                        $("#addBannerMsg").hide();
                    }, 3000);
                }
            },{
                closeAfterAdd:true,
                afterSubmit:function (response,postData) {
                    var bannerId = response.responseJSON.bannerId;
                    var msg = response.responseJSON.msg;
                    $.ajaxFileUpload({
                        url: "${pageContext.request.contextPath}/banner/uploadBanner",
                        datatype: "JSON",
                        type: "POST",
                        fileElementId: "url",
                        data: {bannerId: bannerId},
                        success: function (data) {
                            $("#bannerTable").trigger("reloadGrid");
                            $("#msgShow").html(msg)
                            $("#addBannerMsg").show();
                            setTimeout(function () {
                                $("#addBannerMsg").hide();
                            }, 3000);
                        }
                    })
                    return postData;
                }
            },{
                closeAfterDel:true,
                afterComplete:function (response,postData) {
                    var msg = response.responseJSON.msg;
                    $("#msgShow").html(msg);
                    $("#addBannerMsg").show();
                    setTimeout(function () {
                        $("#addBannerMsg").hide();
                    }, 3000);
                }
            })

    });
    function showUpload() {
        $("#uploadfrm")[0].reset();
        $("#upload").modal("show");
    }
    function upload() {
        $.ajaxFileUpload({
            url:"${pageContext.request.contextPath}/banner/importBanner",
            datatype:"json",
            type:"post",
            fileElementId:"imgFile",
            success:function (data) {
                $("#upload").modal("close");
                $("#bannerTable").jqGrid("reloadGrid");
            }
        })

    }
</script>


<div class="page-header">
    <h2>轮播图管理</h2>
</div>
<ul class="nav nav-tabs">
    <li class="active"><a>轮播图信息</a></li>
    <li><a href="${pageContext.request.contextPath}/banner/outBanner">导出轮播图信息</a></li>
    <li><a href="${pageContext.request.contextPath}/banner/outBannerModel">导出轮播图模板</a></li>
    <li><a onclick="showUpload()">导入轮播图信息</a></li>
</ul>
<div class="panel">
    <table id="bannerTable"></table>
    <div class="alert alert-success" role="alert" id="addBannerMsg" style="display: none">
        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <strong id="msgShow"></strong>
    </div>
    <div id="bannerPager" style="width: auto;height: 50px"></div>
</div>

<div class="modal fade" id="upload" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width: 750px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">导入轮播图数据</h4>
            </div>
            <form class="form-control" action="${pageContext.request.contextPath}/banner/importBanner" method="post" enctype="multipart/form-data" id="uploadfrm">
                <input type="file" name="upload" id="imgFile">
            </form>
            <div class="modal-footer" id="modal_foot">
                <button type="button" class="btn btn-danger" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="upload()">提交</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
