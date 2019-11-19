<%@page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="boot/css/bootstrap.min.css">
    <link rel="stylesheet" href="boot/css/back.css">
    <link rel="stylesheet" href="jqgrid/css/trirand/ui.jqgrid-bootstrap.css">
    <link rel="stylesheet" href="jqgrid/css/jquery-ui.css">
    <script src="boot/js/jquery-2.2.1.min.js"></script>
    <script src="boot/js/bootstrap.min.js"></script>
    <script src="jqgrid/js/trirand/src/jquery.jqGrid.js"></script>
    <script src="jqgrid/js/trirand/i18n/grid.locale-cn.js"></script>
    <script src="boot/js/ajaxfileupload.js"></script>
    <script charset="utf-8" src="kindeditor/kindeditor-all.js"></script>
    <script charset="utf-8" src="kindeditor/lang/zh-CN.js"></script>
    <script>
        KindEditor.ready(function(K) {
            window.editor = K.create('#editor_id',{
                width:"800px",
                uploadJson:"${pageContext.request.contextPath}/article/uploadImg",
                afterBlur: function () {//提交必须要加上  要不拿不到content的内容
                    this.sync();
                }
            });
        });
    </script>
    <title>HelloWorld</title>
    <script>
        function ss() {
            $.ajax({
                url:"",
                data:$("#f").serialize(),
                datatype:"json",
                type:"post",
                success:function (data) {
                    
                }
            })
        }
    </script>
</head>
<body>
<div>
    <form action="" method="post" id="f">
        <textarea id="editor_id" name="content" style="width:700px;height:300px;">
&lt;strong&gt;HTML内容&lt;/strong&gt;
</textarea>
        <input type="button" id="sss" onclick="ss()">
    </form>

</div>
</body>
</html>