<%@page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../boot/css/bootstrap.min.css">
    <link rel="stylesheet" href="../boot/css/back.css">
    <link rel="stylesheet" href="../jqgrid/css/trirand/ui.jqgrid-bootstrap.css">
    <link rel="stylesheet" href="../jqgrid/css/jquery-ui.css">
    <script src="../boot/js/jquery-2.2.1.min.js"></script>
    <script src="../boot/js/bootstrap.min.js"></script>
    <script src="../jqgrid/js/trirand/src/jquery.jqGrid.js"></script>
    <script src="../jqgrid/js/trirand/i18n/grid.locale-cn.js"></script>
    <script src="../boot/js/ajaxfileupload.js"></script>
    <script charset="utf-8" src="../kindeditor/kindeditor-all.js"></script>
    <script charset="utf-8" src="../kindeditor/lang/zh-CN.js"></script>
    <script charset="UTF-8" src="../echarts/echarts.min.js"></script>
    <script src="../echarts/china.js" charset="UTF-8"></script>
    <title>持明法洲后台管理系统</title>
    <script type="text/javascript">
        KindEditor.ready(function (K) {
            K.create("#editor_id", {
                uploadJson: "${pageContext.request.contextPath}/article/uploadImg",
                afterBlur: function () {
                    this.sync();
                }
            })
        });

    </script>
</head>
<body>
<!-- 顶部导航栏 -->
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">持明法洲后台管理系统</a>
        </div>
        <div>
            <ul class="nav navbar-nav navbar-right">
                <li><a>欢迎:<shiro:principal></shiro:principal></a></li>
                <li><a href="${pageContext.request.contextPath}/admin/logout">退出登录</a></li>
            </ul>
        </div>
    </div>
</nav>
<!-- 栅格系统-->
<div class="container-fluid">
    <div class="row">
        <!-- 左侧 -->
        <div class="col-xs-2">
            <!-- 手风琴菜单 -->
            <div class="panel-group" id="accordion">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion"
                               href="#collapseOne">
                                <h4>用户管理</h4>
                            </a>
                        </h4>
                    </div>
                    <div id="collapseOne" class="panel-collapse collapse">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li><a href="javascript:$('#centerLay').load()">用户列表</a></li>
                                <li><a href="javascript:$('#centerLay').load('./echarts.jsp')">注册趋势图</a></li>
                                <li><a href="javascript:$('#centerLay').load('./map.jsp')">地理分布图</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion"
                               href="#collapseTwo">
                                <h4>上师管理</h4>
                            </a>
                        </h4>
                    </div>
                    <div id="collapseTwo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li><a>上师列表</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion"
                               href="#collapseThree">
                                <h4>文章管理</h4>
                            </a>
                        </h4>
                    </div>
                    <div id="collapseThree" class="panel-collapse collapse">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li><a href="javascript:$('#centerLay').load('./article.jsp')">文章列表</a></li>
                                <li><a href="javascript:$('#centerLay').load('./articleSearch.jsp')">文章搜索</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion"
                               href="#collapseFour">
                                <h4>专辑管理</h4>
                            </a>
                        </h4>
                    </div>
                    <div id="collapseFour" class="panel-collapse collapse">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li><a href="javascript:$('#centerLay').load('./album.jsp')">专辑列表</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <shiro:hasPermission name="banner:add">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion"
                               href="#collapseFive">
                                <h4>轮播图管理</h4>
                            </a>
                        </h4>
                    </div>
                    <div id="collapseFive" class="panel-collapse collapse">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li><a href="javascript:$('#centerLay').load('./banner.jsp')">轮播图列表</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                </shiro:hasPermission>
            </div>

        </div>
        <!-- 中部大屏 -->
        <div class="col-xs-10" id="centerLay">
            <div class="jumbotron" style="padding-top:5px;padding-bottom:10px">
                <h3>欢迎使用持明法洲后台管理系统！</h3>
            </div>
            <div id="myCarousel" class="carousel slide">
                <!-- 轮播（Carousel）指标 -->
                <ol class="carousel-indicators">
                    <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                    <li data-target="#myCarousel" data-slide-to="1"></li>
                    <li data-target="#myCarousel" data-slide-to="2"></li>
                </ol>
                <!-- 轮播（Carousel）项目 -->
                <div class="carousel-inner">
                    <div class="item active">
                        <img src="../img/3e4d03381f30e924eebbff0d40086e061d95f7b0.jpg" alt="First slide"
                             style="height: 80%;width: 100%">
                    </div>
                    <div class="item">
                        <img src="../img/009e9dfd5266d016d30938279a2bd40735fa3576.jpg" alt="Second slide"
                             style="height: 80%;width: 100%">
                    </div>
                    <div class="item">
                        <img src="../img/098ca7cad1c8a786b4e6a0366609c93d71cf5049.jpg" alt="Third slide"
                             style="height: 80%;width: 100%">
                    </div>
                </div>
                <!-- 轮播（Carousel）导航 -->
                <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
                    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                    <span class="sr-only">Previous</span>
                </a>
                <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
                    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                    <span class="sr-only">Next</span>
                </a>
            </div>
        </div>
    </div>
</div>

<div class="panel-footer" style="height:50px;
    width:100%;
    clear:both;
    margin-top:-50px;">
    <div class="panel-title">
        <h5 style="text-align: center">@百知教育 baizhi@zparkhr.com.cn</h5>
    </div>
</div>

<!-- KindEditor模态框 -->
<div class="modal fade" id="kind" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width: 750px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">文章信息</h4>
            </div>
            <div class="modal-body">
                <form role="form" id="kindfrm" method="post" class="form-horizontal">
                    <div class="form-group">
                        <input type="hidden" name="id" id="id" class="form-control">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-1 control-label">标题</label>
                        <div class="col-sm-5">
                            <input type="text" name="title" id="title" placeholder="请输入标题" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-1 control-label">状态</label>
                        <div class="col-sm-5">
                            <select class="form-control" name="status" id="status">
                                <option value="展示">展示</option>
                                <option value="不展示">不展示</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-12">
                            <textarea id="editor_id" name="content" style="width:700px;height:300px;"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer" id="modal_foot">

            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</body>
</html>