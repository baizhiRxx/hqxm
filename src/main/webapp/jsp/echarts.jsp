<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="../boot/js/jquery-2.2.1.min.js"></script>
    <script charset="UTF-8" src="../echarts/echarts.min.js"></script>
    <script src="../echarts/china.js" charset="UTF-8"></script>
    <title>echarts</title>
</head>
<body>
    <div id="userByTime" style="width: 600px;height:400px;"></div>
    <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('userByTime'));

        // 指定图表的配置项和数据
        var option = {
            // 对标题(子标题)显示样式设置
            title: {
                text: '持明法洲App用户注册统计'
            },
            tooltip: {},
            legend: {
                data:['男','女']
            },
            xAxis: {
                data: ["当天注册用户","近一周注册用户","近一月注册用户","近一年注册用户"]
            },
            yAxis: {}
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
        $.get("${pageContext.request.contextPath}/user/showUserByTime","json",function (data) {
            myChart.setOption({
                series:[{
                    name:"男",
                    type:"bar",
                    data:data.man
                },{
                    name:"女",
                    type:"bar",
                    data:data.women
                }]
            })
        })
    </script>
</body>
</html>