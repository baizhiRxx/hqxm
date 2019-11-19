<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript">
    $(function () {
        $("#albumTable").jqGrid(
            {
                url: "${pageContext.request.contextPath}/album/showAllAlbum",
                datatype: "json",
                height: 500,
                colNames: ['编号', '标题', '分数', '作者', '播音员', '专辑简介', '章节数', '状态', '发行时间', '上传时间', '插图'],
                colModel: [
                    {name: 'id', hidden: true},
                    {name: 'title', align: "center", editable: true, editrules: {required: true}},
                    {
                        name: 'score',
                        align: "center",
                        editable: true,
                        editrules: {required: true, number: true, minValue: 1, maxValue: 10}
                    },
                    {name: 'author', align: "center", editable: true, editrules: {required: true}},
                    {name: 'broadcast', align: "center", editable: true, editrules: {required: true}},
                    {name: 'brief', align: "center", editable: true, editrules: {required: true}},
                    {
                        name: 'count',
                        align: "center",
                        editable: true,
                        editrules: {required: true, number: true, minValue: 1}
                    },
                    {
                        name: 'status',
                        align: "center",
                        editable: true,
                        edittype: "select",
                        editoptions: {value: "展示:展示;不展示:不展示"}
                    },
                    {
                        name: 'publish_date', align: "center", editable: true, edittype: "date",
                        editrules: {required: true}
                    },
                    {
                        name: 'create_date', align: "center", editable: true, edittype: "date",
                        editrules: {required: true}
                    },
                    {
                        name: 'cover', align: "center", formatter: function (data) {
                            return "<img style='height: 80px;width:180px ' src='../img/" + data + "'/>";
                        }, editable: true, edittype: "file",
                        editoptions: {
                            enctype: "multipart/form-data"
                        },
                    },
                ],
                rowNum: 5,
                rowList: [5, 10, 20],
                pager: '#albumPager',
                sortname: 'id',
                viewrecords: true,
                sortorder: "desc",
                multiselect: true,
                styleUI: "Bootstrap",
                autowidth: true,
                editurl: "${pageContext.request.contextPath}/album/editAlbum",
                //开启子表格支持
                subGrid: true,
                //子表格的id；当子表格展开的时候，在主表格中会创建一个div元素用来容纳子表格，subgrid_id就是这个div的id
                subGridRowExpanded: function (subgrid_id, row_id) {
                    addSubGrid(subgrid_id, row_id);
                },
                subGridRowColapsed: function (subgrid_id, row_id) {

                }
            });
        $("#albumTable").jqGrid('navGrid', '#albumPager', {
            add: true,
            edit: true,
            del: true,
            addtext: "添加",
            edittext: "编辑",
            deltext: "删除"
        }, {
            closeAfterEdit: true,
            beforeShowForm: function (frm) {
                frm.find("#publish_date").attr("readOnly", true);
                frm.find("#create_date").attr("readOnly", true);
                frm.find("#cover").attr("disabled", true);
            }
        }, {
            closeAfterAdd: true,
            afterSubmit: function (response, postData) {
                var albumId = response.responseJSON.albumId;

                $.ajaxFileUpload({
                    url: "${pageContext.request.contextPath}/album/uploadAlbum",
                    datatype: "json",
                    type: "post",
                    data: {id: albumId},
                    fileElementId: "cover",
                    success: function (data) {
                        $("#albumTable").trigger("reloadGrid");
                    }
                })
            }
        });
    });

    function addSubGrid(subGridId, albumId) {
        //根据subgrid_id定义对应的子表格的table的id
        subGridTableId = subGridId + "table";
        //根据subgrid_id定义对应的子表格的pager的id
        subGridPagerId = subGridId + "pager";
        //创建表单元素及尾部分页div
        $("#" + subGridId).html("<table id='" + subGridTableId + "'></table><div id='" + subGridPagerId + "'></div>");
        $("#" + subGridTableId).jqGrid({
            url: "${pageContext.request.contextPath}/chapter/showAllChapters?id=" + albumId,
            datatype: "json",
            colNames: ['编号', '标题', '大小', '时长', '上传时间', '音频', '操作'],
            colModel: [
                {name: "id", align: "center", hidden: true},
                {name: "title", align: "center", editable: true,editrules:{required:true}},
                {name: "size", align: "center"},
                {name: "duration", align: "center"},
                {name: "create_date", align: "center", editable: true, edittype: "date"},
                {name: "url", align: "center",editable:true,edittype:"file",editoptions:{enctype:"multpart/form-data"}},
                {
                    name: "url", align: "center", formatter(data) {
                        var result = "";
                        result += "<a href='javascript:void(0)' onclick=\"playAudio('" + data + "')\" class='btn btn-lg' title='播放'><span class='glyphicon glyphicon-play-circle'></span></a>";
                        result += "<a href='javascript:void(0)' onclick=\"downloadAudio('" + data + "')\" class='btn btn-lg' title='下载'><span class='glyphicon glyphicon-download'></span></a>";
                        return result;
                    }
                },
            ],
            rowNum: 5,
            pager: '#' + subGridPagerId,
            sortname: 'num',
            sortorder: "asc",
            height: '100%',
            styleUI: "Bootstrap",
            autowidth: true,
            editurl:"${pageContext.request.contextPath}/chapter/editChapter?albumId="+albumId,
        })
        $("#"+subGridTableId).jqGrid('navGrid', '#'+subGridPagerId, {
            add: true,
            edit: true,
            del: true,
            addtext: "添加",
            edittext: "编辑",
            deltext: "删除"
        },{
            closeAfterEdit:true,
            beforeShowForm:function (frm) {
                frm.find("#create_date").attr("readOnly",true);
                frm.find("#url").attr("disabled",true);
            }
        },{
            closeAfterAdd:true,
            afterSubmit:function (response,postData) {
                var chapterId = response.responseJSON.id;
                $.ajaxFileUpload({
                    url:"${pageContext.request.contextPath}/chapter/uploadChapter",
                    type:"post",
                    datatype:"json",
                    data:{id:chapterId},
                    fileElementId:"url",
                    success:function (data) {
                        $("#albumTable").trigger("reloadGrid");
                    }
                })
                return postData;
            }
        })
    }

    function playAudio(data) {
        $("#musicDiv").modal("show");
        $("#playAudio").attr("src", "../music/" + data);
    }

    function downloadAudio(data) {
        location.href = "${pageContext.request.contextPath}/chapter/downloadChapter?url=" + data;
    }
</script>


<div class="page-header">
    <h2>专辑章节管理</h2>
</div>
<ul class="nav nav-tabs">
    <li class="active"><a>专辑章节信息</a></li>
</ul>
<div class="panel">
    <table id="albumTable"></table>
    <div id="albumPager" style="width: auto;height: 50px"></div>
</div>
<div class="modal fade" id="musicDiv" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <audio id="playAudio" src="" controls></audio>
    </div>
</div>
