package com.example.springproject.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


@RestController
@RequestMapping("/repo")
public class RepoRestController {

    /**
     * 读取json文件，返回json串
     * @param fileName
     * @return
     */
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }

            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/{reponame}/show")
    public String show(@PathVariable(value = "reponame") String reponame){
        String index = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head lang=\"en\">\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>repo相关数据展示</title>\n" +
                "    <script src=\"https://s3.pstatp.com/cdn/expire-1-M/jquery/3.2.1/jquery.min.js\" ></script>\n" +
                "    <script src=\"https://cdn.bootcss.com/echarts/4.2.1-rc1/echarts.min.js\"></script>\n" +
                "\n" +
                "    <style type=\"text/css\">\n" +
                "        body{background: gainsboro}\n" +
                "        /* 被style标签包围的是css环境 可以写css代码 */\n" +
                "        /* 标签样式表 */\n" +
                "        p{\n" +
                "            line-height: 30px;\n" +
                "            color: black;\n" +
                "        }\n" +
                "\n" +
                "        .text{\n" +
                "            line-height: 20px;\n" +
                "            font-size: 20px;\n" +
                "        }\n" +
                "        /* 类样式 */\n" +
                "        .reponame{\n" +
                "            font-size: 40px;\n" +
                "        }\n" +
                "\n" +
                "        /* ID样式 */\n" +
                "        .lei{\n" +
                "            margin-bottom: 30px;\n" +
                "            margin-top: 50px;\n" +
                "            font-size: 30px;\n" +
                "            display: inline;\n" +
                "            background-color: yellowgreen;\n" +
                "            font-weight: bolder;\n" +
                "            font-style: italic;\n" +
                "\n" +
                "            /*border-radius: 20px;*/\n" +
                "            /*box-shadow: rgba(0,0,0,.6) 10px 10px 2px;*/\n" +
                "\n" +
                "            /*background: #fff*/\n" +
                "        }\n" +
                "\n" +
                "        .TypicalTime{\n" +
                "            font-size: 25px;\n" +
                "            font-family: Arial;\n" +
                "            font-weight: bolder;\n" +
                "            text-decoration: underline;\n" +
                "        }\n" +
                "\n" +
                "        .TypicalTime2{\n" +
                "            font-size: 25px;\n" +
                "            font-family: Arial;\n" +
                "            font-weight: bolder;\n" +
                "            /*text-decoration: underline;*/\n" +
                "        }\n" +
                "        .TypicalName{\n" +
                "            font-weight: lighter;\n" +
                "            font-size: 20px;\n" +
                "        }\n" +
                "        .data{\n" +
                "            font-size: 30px;\n" +
                "            font-family: Arial;\n" +
                "            font-weight: bolder;\n" +
                "            color: crimson;\n" +
                "            /*text-decoration: underline;*/\n" +
                "        }\n" +
                "\n" +
                "        /*div p{*/\n" +
                "        /*    color: bisque;*/\n" +
                "        /*}*/\n" +
                "\n" +
                "        div .f32{\n" +
                "            color: blueviolet;\n" +
                "        }\n" +
                "\n" +
                "        .box_cont{\n" +
                "            width:150px;\n" +
                "            height: 100px;\n" +
                "            margin-left: 20px;\n" +
                "            margin-right: 20px;\n" +
                "            margin-top: 10px;\n" +
                "            line-height: 43px;\n" +
                "            text-align: center;\n" +
                "            float: left;\n" +
                "            border-radius: 20px;\n" +
                "            box-shadow: rgba(0,0,0,.6) 10px 10px 2px;\n" +
                "            background: #fff;\n" +
                "        }\n" +
                "\n" +
                "        .bigFloat{\n" +
                "            float: left;\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p class=\"reponame\">repo name : <span id=\"1\">var1</span></p>\n" +
                "\n" +
                "<div class=\"bigFloat\">\n" +
                "<p class=\"lei\">developers: </p><br>\n" +
                "<p class=\"text\">共有  <span id=\"2\" class=\"data\">var2</span>  位developers   进行了 <span id=\"3\" class=\"data\">var3</span> 次commit</p><br>\n" +
                "\n" +
                "<canvas id=\"barChart\" height=\"400\" width=\"600\" style=\"margin:50px\" class=\"text\"> 你的浏览器不支持HTML5 canvas </canvas><br>\n" +
                "\n" +
                "<p class=\"lei\">issues: </p><br>\n" +
                "<p class=\"text\">共有  <span id=\"8\" class=\"data\">var8</span>  个issue   其中 <span id=\"9\" class=\"data\">var9</span> 个开放状态issue  <span id=\"10\" class=\"data\">var10</span> 个已经关闭的issue</p><br>\n" +
                "<p class=\"text\">closed issue解决时间的特征值 </p>\n" +
                "\n" +
                "<div style=\"margin-bottom: 50px\">\n" +
                "    <div class=\"box_cont fl\"><span class=\"TypicalName\">average</span><br><span id=\"4\" class=\"TypicalTime\">var4</span><span class=\"TypicalTime2\">d</span></div>\n" +
                "\n" +
                "    <div class=\"box_cont fl\"><span class=\"TypicalName\">median</span><br><span id=\"5\" class=\"TypicalTime\">var5</span><span class=\"TypicalTime2\">d</span></div>\n" +
                "\n" +
                "    <div class=\"box_cont fl\"><span class=\"TypicalName\">variance</span><br><span id=\"6\" class=\"TypicalTime\">var6</span><span class=\"TypicalTime2\">d</span></div>\n" +
                "\n" +
                "    <div class=\"box_cont fl\"><span class=\"TypicalName\">range</span><br><span id=\"7\" class=\"TypicalTime\">var7</span><span class=\"TypicalTime2\">d</span></div>\n" +
                "\n" +
                "    <br>\n" +
                "</div>\n" +
                "<br>    <br>    <br>\n" +
                "    <div id=\"rank\" style=\"width: 800px;height:500px;margin-top: 50px;margin-bottom: 30px\"></div>\n" +
                "\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "<div class=\"bigFloat\">\n" +
                "    <p class=\"lei\">commits: </p><br>\n" +
                "\n" +
                "    <div id=\"main\" style=\"width: 800px;height:500px;margin-top: 40px;margin-bottom: 30px\"></div>\n" +
                "\n" +
                "    <p class=\"lei\">releases: </p><br>\n" +
                "    <p class=\"text\">共有  <span id=\"11\" class=\"data\">var11</span>  个releases</p><br>\n" +
                "    <div id=\"main3\" style=\"width: 600px;height:400px;margin-top: 40px;margin-bottom: 30px\"></div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "<script type=\"text/javascript\">\n" +
                "    function goBarChart(Array){\n" +
                "        // 声明所需变量\n" +
                "        var canvas,ctx;\n" +
                "        // 图表属性\n" +
                "        var cWidth, cHeight, cMargin, cSpace;\n" +
                "        var originX, originY;\n" +
                "        // 柱状图属性\n" +
                "        var bMargin, tobalBars, bWidth, maxValue;\n" +
                "        var totalYNomber;\n" +
                "        var gradient;\n" +
                "\n" +
                "        // 运动相关变量\n" +
                "        var ctr, numctr, speed;\n" +
                "        //鼠标移动\n" +
                "        var mousePosition = {};\n" +
                "\n" +
                "        // 获得canvas上下文\n" +
                "        canvas = document.getElementById(\"barChart\");\n" +
                "        if(canvas && canvas.getContext){\n" +
                "            ctx = canvas.getContext(\"2d\");\n" +
                "        }\n" +
                "        initChart(); // 图表初始化\n" +
                "        drawLineLabelMarkers(); // 绘制图表轴、标签和标记\n" +
                "        drawBarAnimate(); // 绘制柱状图的动画\n" +
                "        //检测鼠标移动\n" +
                "        var mouseTimer = null;\n" +
                "        canvas.addEventListener(\"mousemove\",function(e){\n" +
                "            e = e || window.event;\n" +
                "            if( e.layerX || e.layerX==0 ){\n" +
                "                mousePosition.x = e.layerX;\n" +
                "                mousePosition.y = e.layerY;\n" +
                "            }else if( e.offsetX || e.offsetX==0 ){\n" +
                "                mousePosition.x = e.offsetX;\n" +
                "                mousePosition.y = e.offsetY;\n" +
                "            }\n" +
                "\n" +
                "            clearTimeout(mouseTimer);\n" +
                "            mouseTimer = setTimeout(function(){\n" +
                "                ctx.clearRect(0,0,canvas.width, canvas.height);\n" +
                "                drawLineLabelMarkers();\n" +
                "                drawBarAnimate(true);\n" +
                "            },10);\n" +
                "        });\n" +
                "\n" +
                "        //点击刷新图表\n" +
                "        canvas.onclick = function(){\n" +
                "            initChart(); // 图表初始化\n" +
                "            drawLineLabelMarkers(); // 绘制图表轴、标签和标记\n" +
                "            drawBarAnimate(); // 绘制折线图的动画\n" +
                "        };\n" +
                "\n" +
                "\n" +
                "        // 图表初始化\n" +
                "        function initChart(){\n" +
                "            // 图表信息\n" +
                "            cMargin = 30;\n" +
                "            cSpace = 60;\n" +
                "            cHeight = canvas.height - cMargin*2 - cSpace;\n" +
                "            cWidth = canvas.width - cMargin*2 - cSpace;\n" +
                "            originX = cMargin + cSpace;\n" +
                "            originY = cMargin + cHeight;\n" +
                "\n" +
                "            // 柱状图信息\n" +
                "            bMargin = 20;\n" +
                "            tobalBars = Array.length;\n" +
                "            bWidth = parseInt( cWidth/tobalBars - bMargin );\n" +
                "            maxValue = 0;\n" +
                "            for(var i=0; i<Array.length; i++){\n" +
                "                var barVal = parseInt( Array[i][1] );\n" +
                "                if( barVal > maxValue ){\n" +
                "                    maxValue = barVal;\n" +
                "                }\n" +
                "            }\n" +
                "            maxValue += 50;\n" +
                "            totalYNomber = 10;\n" +
                "            // 运动相关\n" +
                "            ctr = 1;\n" +
                "            numctr = 100;\n" +
                "            speed = 10;\n" +
                "\n" +
                "            //柱状图渐变色\n" +
                "            gradient = ctx.createLinearGradient(0, 0, 0, 300);\n" +
                "            gradient.addColorStop(0, 'blue');\n" +
                "            gradient.addColorStop(1, 'darkblue');\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "        // 绘制图表轴、标签和标记\n" +
                "        function drawLineLabelMarkers(){\n" +
                "            ctx.translate(0.5,0.5);  // 当只绘制1像素的线的时候，坐标点需要偏移，这样才能画出1像素实线\n" +
                "            ctx.font = \"12px Arial\";\n" +
                "            ctx.lineWidth = 1;\n" +
                "            ctx.fillStyle = \"#000\";\n" +
                "            ctx.strokeStyle = \"#000\";\n" +
                "            // y轴\n" +
                "            drawLine(originX, originY, originX, cMargin);\n" +
                "            // x轴\n" +
                "            drawLine(originX, originY, originX+cWidth, originY);\n" +
                "\n" +
                "            // 绘制标记\n" +
                "            drawMarkers();\n" +
                "            ctx.translate(-0.5,-0.5);  // 还原位置\n" +
                "        }\n" +
                "\n" +
                "        // 画线的方法\n" +
                "        function drawLine(x, y, X, Y){\n" +
                "            ctx.beginPath();\n" +
                "            ctx.moveTo(x, y);\n" +
                "            ctx.lineTo(X, Y);\n" +
                "            ctx.stroke();\n" +
                "            ctx.closePath();\n" +
                "        }\n" +
                "\n" +
                "        // 绘制标记\n" +
                "        function drawMarkers(){\n" +
                "            ctx.strokeStyle = \"#E0E0E0\";\n" +
                "            // 绘制 y\n" +
                "            var oneVal = parseInt(maxValue/totalYNomber);\n" +
                "            ctx.textAlign = \"right\";\n" +
                "            for(var i=0; i<=totalYNomber; i++){\n" +
                "                var markerVal =  i*oneVal;\n" +
                "                var xMarker = originX-5;\n" +
                "                var yMarker = parseInt( cHeight*(1-markerVal/maxValue) ) + cMargin;\n" +
                "                //console.log(xMarker, yMarker+3,markerVal/maxValue,originY);\n" +
                "                ctx.fillText(markerVal, xMarker, yMarker+3, cSpace); // 文字\n" +
                "                if(i>0){\n" +
                "                    // drawLine(originX, yMarker, originX+cWidth, yMarker);\n" +
                "                }\n" +
                "            }\n" +
                "            // 绘制 x\n" +
                "            ctx.textAlign = \"center\";\n" +
                "            for(var i=0; i<tobalBars; i++){\n" +
                "                var markerVal = Array[i][0];\n" +
                "                var xMarker = parseInt( originX+cWidth*(i/tobalBars)+bMargin+bWidth/2 );\n" +
                "                var yMarker = originY+15;\n" +
                "                ctx.fillText(markerVal, xMarker, yMarker, cSpace); // 文字\n" +
                "            }\n" +
                "            // 绘制标题 y\n" +
                "            ctx.save();\n" +
                "            ctx.rotate(-Math.PI/2);\n" +
                "            ctx.fillText(\"commit次数\", -canvas.height/2, cSpace-10);\n" +
                "            ctx.restore();\n" +
                "            // 绘制标题 x\n" +
                "            ctx.fillText(\"时间分布\", originX+cWidth/2, originY+cSpace/2+10);\n" +
                "        };\n" +
                "\n" +
                "        //绘制柱形图\n" +
                "        function drawBarAnimate(mouseMove){\n" +
                "            for(var i=0; i<tobalBars; i++){\n" +
                "                var oneVal = parseInt(maxValue/totalYNomber);\n" +
                "                var barVal = parseInt(Array[i][1]);\n" +
                "                var barH = parseInt( cHeight*barVal/maxValue * ctr/numctr );\n" +
                "                var y = originY - barH;\n" +
                "                var x = originX + (bWidth+bMargin)*i + bMargin;\n" +
                "                drawRect( x, y, bWidth, barH, mouseMove );  //高度减一避免盖住x轴\n" +
                "                ctx.fillText(parseInt(barVal*ctr/numctr), x+23, y-8); // 文字\n" +
                "            }\n" +
                "            if(ctr<numctr){\n" +
                "                ctr++;\n" +
                "                setTimeout(function(){\n" +
                "                    ctx.clearRect(0,0,canvas.width, canvas.height);\n" +
                "                    drawLineLabelMarkers();\n" +
                "                    drawBarAnimate();\n" +
                "                }, speed);\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        //绘制方块\n" +
                "        function drawRect( x, y, X, Y, mouseMove ){\n" +
                "\n" +
                "            ctx.beginPath();\n" +
                "            ctx.rect( x, y, X, Y );\n" +
                "            if(mouseMove && ctx.isPointInPath(mousePosition.x, mousePosition.y)){ //如果是鼠标移动的到柱状图上，重新绘制图表\n" +
                "                // ctx.fillStyle = \"blue\";\n" +
                "            }else{\n" +
                "                ctx.fillStyle = gradient;\n" +
                "                ctx.strokeStyle = gradient;\n" +
                "            }\n" +
                "            ctx.fill();\n" +
                "            ctx.closePath();\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "\n" +
                "    }\n" +
                "    var monday;\n" +
                "    var tuesday;\n" +
                "    var wednesday;\n" +
                "    var thursday;\n" +
                "    var friday;\n" +
                "    var saturday;\n" +
                "    var sunday;\n" +
                "    getResources();\n" +
                "\n" +
                "    var obj;\n" +
                "    function getResources(){\n" +
                "        $.ajax({\n" +
                "            url:'http://localhost:8084/repo/" + reponame
                + "',\t//这是后端接口的url\n" +
                "            method:'get',\n" +
                "            success:function (res) {\n" +
                "                // console.log(res);\n" +
                "                obj = JSON.parse(res);\n" +
                "                $(\"#1\").text(obj.repo_name);\n" +
                "                $(\"#2\").text(obj.developers_num);\n" +
                "                $(\"#3\").text(obj.commits_num);\n" +
                "                $(\"#4\").text(obj.releaseSolution_average);\n" +
                "                $(\"#5\").text(obj.releaseSolution_median);\n" +
                "                $(\"#6\").text(obj.releaseSolution_var);\n" +
                "                $(\"#7\").text(obj.releaseSolution_range);\n" +
                "                $(\"#8\").text(obj.openIssues_num + obj.closedIssues_num);\n" +
                "                $(\"#9\").text(obj.openIssues_num);\n" +
                "                $(\"#10\").text(obj.closedIssues_num);\n" +
                "                $(\"#11\").text(obj.releases_num);\n" +
                "\n" +
                "\n" +
                "                var time_location = JSON.parse(JSON.stringify(obj.timeLocation));\n" +
                "                // console.log(time_location.monday);\n" +
                "                monday = time_location.monday;\n" +
                "                tuesday = time_location.tuesday;\n" +
                "                wednesday = time_location.wednesday;\n" +
                "                thursday = time_location.thursday;\n" +
                "                friday = time_location.friday;\n" +
                "                saturday = time_location.saturday;\n" +
                "                sunday = time_location.sunday;\n" +
                "\n" +
                "                var rank = JSON.parse(JSON.stringify(obj.active10Developers));\n" +
                "                var email = JSON.parse(JSON.stringify(obj.activeDevelopersEmail));\n" +
                "                console.log(typeof rank);\n" +
                "\n" +
                "                var dateArray = new Array();\n" +
                "                var nameArray = new Array();\n" +
                "                var emailArray = new Array();\n" +
                "                for(var key in rank){\n" +
                "                    console.log(key + \" \"  + rank[key]);\n" +
                "                    dateArray.push(rank[key]);\n" +
                "                    nameArray.push(key);\n" +
                "                    emailArray.push(email[key]);\n" +
                "                }\n" +
                "\n" +
                "\n" +
                "                var cbr = obj.commit_numBetweenRelease;\n" +
                "                console.log(cbr);\n" +
                "\n" +
                "                var x_zhou = new Array();\n" +
                "                for(var i = 1; i < obj.releases_num; i++){\n" +
                "                    x_zhou.push(i + \"-\" + (i+1));\n" +
                "                }\n" +
                "                console.log(x_zhou);\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "                goBarChart(\n" +
                "                    [[\"星期一\", monday], [\"星期二\", tuesday], [\"星期三\", wednesday], [\"星期四\", thursday], [\"星期五\", friday], [\"星期六\", saturday ], [\"星期日\", sunday]]\n" +
                "                )\n" +
                "                //res便是的数据便是后端拿到的数据\n" +
                "                //这里需要注意：res为局部变量，\n" +
                "                //所以需要在方法外定义一个变量把res赋值给他，才能在方法之外使用。\n" +
                "                barGraph(dateArray, nameArray, emailArray);\n" +
                "                barGraph2(dateArray, nameArray, emailArray);\n" +
                "                setChar3(x_zhou, cbr);\n" +
                "            },\n" +
                "        })\n" +
                "    }\n" +
                "\n" +
                "    function setChar3(x_array, y_array){\n" +
                "        var myChart = echarts.init(document.getElementById('main3'));\n" +
                "\n" +
                "        var option = {\n" +
                "            title: {\n" +
                "                text: \"release间的commit数量\",\n" +
                "                x : \"center\"\n" +
                "            },\n" +
                "            toolbox: {\n" +
                "                feature: {\n" +
                "                    // saveAsImage: {},\n" +
                "                },\n" +
                "            },\n" +
                "            tooltip: {\n" +
                "\n" +
                "            },\n" +
                "            xAxis: {\n" +
                "                // type: 'category',\n" +
                "                data: x_array,\n" +
                "\n" +
                "               axisLabel:{\n" +
                "                   textStyle:{\n" +
                "                       fontSize:10 // 让字体变小\n" +
                "                   },\n" +
                "                   rotate: 30,    // 字体倾斜30度\n" +
                "               },\n" +
                "\n" +
                "            },\n" +
                "            yAxis: { type: 'value' },\n" +
                "            series: [\n" +
                "                {\n" +
                "                    data: y_array,\n" +
                "                    type: 'bar',\n" +
                "                },\n" +
                "            ],\n" +
                "        };\n" +
                "        myChart.setOption(option);\n" +
                "    }\n" +
                "\n" +
                "    function barGraph(dataArray, nameArray, emilArray){\n" +
                "        //初始化图标\n" +
                "        var myChart = echarts.init(document.getElementById('main'));\n" +
                "        //Y轴的数据，和数据值位置一一对应\n" +
                "        var cate = [\n" +
                "            \"1\",\n" +
                "            \"2\",\n" +
                "            \"3\",\n" +
                "            \"4\",\n" +
                "            \"5\",\n" +
                "            \"6\",\n" +
                "            \"7\",\n" +
                "            \"8\",\n" +
                "            \"9\",\n" +
                "            \"10\",\n" +
                "        ];\n" +
                "        //数据值，顺序和Y轴的名字一一对应\n" +
                "        var barData = dataArray;\n" +
                "\n" +
                "        var option = {\n" +
                "            title: {\n" +
                "                text: \"commit排行榜top10\",\n" +
                "            },\n" +
                "            tooltip: {\n" +
                "                trigger: \"axis\",\n" +
                "                axisPointer: {\n" +
                "                    type: \"shadow\",\n" +
                "                },\n" +
                "                formatter: function (params) {\n" +
                "                    var result = ''\n" +
                "                    var dotHtml = '<span style=\"display:inline-block;margin-right:5px;border-radius:10px;width:5px;height:5px;background-color: yellow\"></span>'    // 定义第一个数据前的圆点颜色\n" +
                "                    var dotHtml2 = '<span style=\"display:inline-block;margin-right:5px;border-radius:10px;width:5px;height:5px;background-color: green\"></span>'    // 定义第二个数据前的圆点颜色\n" +
                "                    var dotHtml3 = '<span style=\"display:inline-block;margin-right:5px;border-radius:10px;width:5px;height:5px;background-color: red\"></span>'\n" +
                "                    result += \"No.\" + params[0].axisValue + \"</br>\" + dotHtml + \"name:\"+ nameArray[params[0].axisValue - 1] +\n" +
                "                        \"<br>\" + dotHtml2 + \"commit number:\" + dataArray[params[0].axisValue - 1 ] +\n" +
                "                        \"<br>\" + dotHtml3 + \"email address:\" + emilArray[params[0].axisValue - 1 ]\n" +
                "                    ;\n" +
                "                    return result\n" +
                "                },\n" +
                "            },\n" +
                "            //图表位置\n" +
                "            grid: {\n" +
                "                left: \"3%\",\n" +
                "                right: \"4%\",\n" +
                "                bottom: \"3%\",\n" +
                "                containLabel: true,\n" +
                "            },\n" +
                "            //X轴\n" +
                "            xAxis: {\n" +
                "                type: \"value\",\n" +
                "                axisLine: {\n" +
                "                    show: false,\n" +
                "                },\n" +
                "                axisTick: {\n" +
                "                    show: false,\n" +
                "                },\n" +
                "                //不显示X轴刻度线和数字\n" +
                "                splitLine: { show: false },\n" +
                "                axisLabel: { show: false },\n" +
                "            },\n" +
                "            yAxis: {\n" +
                "                type: \"category\",\n" +
                "                data: cate,\n" +
                "                //升序\n" +
                "                inverse: true,\n" +
                "                splitLine: { show: false },\n" +
                "                axisLine: {\n" +
                "                    show: false,\n" +
                "                },\n" +
                "                axisTick: {\n" +
                "                    show: false,\n" +
                "                },\n" +
                "                //key和图间距\n" +
                "                offset: 10,\n" +
                "                //动画部分\n" +
                "                animationDuration: 300,\n" +
                "                animationDurationUpdate: 300,\n" +
                "                //key文字大小\n" +
                "                nameTextStyle: {\n" +
                "                    fontSize: 5,\n" +
                "                },\n" +
                "            },\n" +
                "            series: [\n" +
                "                {\n" +
                "                    //柱状图自动排序，排序自动让Y轴名字跟着数据动\n" +
                "                    realtimeSort: true,\n" +
                "                    name: \"数量\",\n" +
                "                    type: \"bar\",\n" +
                "                    data: barData,\n" +
                "                    barWidth: 14,\n" +
                "                    barGap: 10,\n" +
                "                    smooth: true,\n" +
                "                    valueAnimation: true,\n" +
                "                    // Y轴数字显示部分\n" +
                "                    label: {\n" +
                "                        normal: {\n" +
                "                            show: true,\n" +
                "                            position: \"right\",\n" +
                "                            valueAnimation: true,\n" +
                "                            offset: [5, -2],\n" +
                "                            textStyle: {\n" +
                "                                color: \"#333\",\n" +
                "                                fontSize: 13,\n" +
                "                            },\n" +
                "                        },\n" +
                "                    },\n" +
                "                    itemStyle: {\n" +
                "                        emphasis: {\n" +
                "                            barBorderRadius: 7,\n" +
                "                        },\n" +
                "                        //颜色样式部分\n" +
                "                        normal: {\n" +
                "                            barBorderRadius: 7,\n" +
                "                            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [\n" +
                "                                { offset: 0, color: \"#3977E6\" },\n" +
                "                                { offset: 1, color: \"#37BBF8\" },\n" +
                "                            ]),\n" +
                "                        },\n" +
                "                    },\n" +
                "                },\n" +
                "            ],\n" +
                "            // 动画部分\n" +
                "\n" +
                "            animationDuration: 0,\n" +
                "            animationDurationUpdate: 3000,\n" +
                "            animationEasing: \"linear\",\n" +
                "            animationEasingUpdate: \"linear\",\n" +
                "        };\n" +
                "\n" +
                "        myChart.setOption(option);\n" +
                "        // 图表大小变动从新渲染，动态自适应\n" +
                "        window.addEventListener(\"resize\", function () {\n" +
                "            myChart.resize();\n" +
                "        });\n" +
                "    }\n" +
                "\n" +
                "    function barGraph2(dataArray, nameArray, emilArray){\n" +
                "        //初始化图标\n" +
                "        var myChart = echarts.init(document.getElementById('rank'));\n" +
                "        //Y轴的数据，和数据值位置一一对应\n" +
                "        var cate = [\n" +
                "            \"1\",\n" +
                "            \"2\",\n" +
                "            \"3\",\n" +
                "            \"4\",\n" +
                "            \"5\",\n" +
                "            \"6\",\n" +
                "            \"7\",\n" +
                "            \"8\",\n" +
                "            \"9\",\n" +
                "            \"10\",\n" +
                "        ];\n" +
                "        //数据值，顺序和Y轴的名字一一对应\n" +
                "        var barData = dataArray;\n" +
                "\n" +
                "        var option = {\n" +
                "            title: {\n" +
                "                text: \"commit排行榜top10\",\n" +
                "            },\n" +
                "            tooltip: {\n" +
                "                trigger: \"axis\",\n" +
                "                axisPointer: {\n" +
                "                    type: \"shadow\",\n" +
                "                },\n" +
                "                formatter: function (params) {\n" +
                "                    var result = ''\n" +
                "                    var dotHtml = '<span style=\"display:inline-block;margin-right:5px;border-radius:10px;width:5px;height:5px;background-color: yellow\"></span>'    // 定义第一个数据前的圆点颜色\n" +
                "                    var dotHtml2 = '<span style=\"display:inline-block;margin-right:5px;border-radius:10px;width:5px;height:5px;background-color: green\"></span>'    // 定义第二个数据前的圆点颜色\n" +
                "                    var dotHtml3 = '<span style=\"display:inline-block;margin-right:5px;border-radius:10px;width:5px;height:5px;background-color: red\"></span>'\n" +
                "                    result += params[0].axisValue + \"</br>\" + dotHtml + \"name:\"+ nameArray[params[0].axisValue - 1] +\n" +
                "                        \"<br>\" + dotHtml2 + \"commit number:\" + dataArray[params[0].axisValue - 1 ] +\n" +
                "                        \"<br>\" + dotHtml3 + \"email address:\" + emilArray[params[0].axisValue - 1 ]\n" +
                "                    ;\n" +
                "                    return result\n" +
                "                },\n" +
                "            },\n" +
                "            //图表位置\n" +
                "            grid: {\n" +
                "                left: \"3%\",\n" +
                "                right: \"4%\",\n" +
                "                bottom: \"3%\",\n" +
                "                containLabel: true,\n" +
                "            },\n" +
                "            //X轴\n" +
                "            xAxis: {\n" +
                "                type: \"value\",\n" +
                "                axisLine: {\n" +
                "                    show: false,\n" +
                "                },\n" +
                "                axisTick: {\n" +
                "                    show: false,\n" +
                "                },\n" +
                "                //不显示X轴刻度线和数字\n" +
                "                splitLine: { show: false },\n" +
                "                axisLabel: { show: false },\n" +
                "            },\n" +
                "            yAxis: {\n" +
                "                type: \"category\",\n" +
                "                data: cate,\n" +
                "                //升序\n" +
                "                inverse: true,\n" +
                "                splitLine: { show: false },\n" +
                "                axisLine: {\n" +
                "                    show: false,\n" +
                "                },\n" +
                "                axisTick: {\n" +
                "                    show: false,\n" +
                "                },\n" +
                "                //key和图间距\n" +
                "                offset: 10,\n" +
                "                //动画部分\n" +
                "                animationDuration: 300,\n" +
                "                animationDurationUpdate: 300,\n" +
                "                //key文字大小\n" +
                "                nameTextStyle: {\n" +
                "                    fontSize: 5,\n" +
                "                },\n" +
                "            },\n" +
                "            series: [\n" +
                "                {\n" +
                "                    //柱状图自动排序，排序自动让Y轴名字跟着数据动\n" +
                "                    realtimeSort: true,\n" +
                "                    name: \"数量\",\n" +
                "                    type: \"bar\",\n" +
                "                    data: barData,\n" +
                "                    barWidth: 14,\n" +
                "                    barGap: 10,\n" +
                "                    smooth: true,\n" +
                "                    valueAnimation: true,\n" +
                "                    // Y轴数字显示部分\n" +
                "                    label: {\n" +
                "                        normal: {\n" +
                "                            show: true,\n" +
                "                            position: \"right\",\n" +
                "                            valueAnimation: true,\n" +
                "                            offset: [5, -2],\n" +
                "                            textStyle: {\n" +
                "                                color: \"#333\",\n" +
                "                                fontSize: 13,\n" +
                "                            },\n" +
                "                        },\n" +
                "                    },\n" +
                "                    itemStyle: {\n" +
                "                        emphasis: {\n" +
                "                            barBorderRadius: 7,\n" +
                "                        },\n" +
                "                        //颜色样式部分\n" +
                "                        normal: {\n" +
                "                            barBorderRadius: 7,\n" +
                "                            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [\n" +
                "                                { offset: 0, color: \"#3977E6\" },\n" +
                "                                { offset: 1, color: \"#37BBF8\" },\n" +
                "                            ]),\n" +
                "                        },\n" +
                "                    },\n" +
                "                },\n" +
                "            ],\n" +
                "            // 动画部分\n" +
                "\n" +
                "            animationDuration: 0,\n" +
                "            animationDurationUpdate: 3000,\n" +
                "            animationEasing: \"linear\",\n" +
                "            animationEasingUpdate: \"linear\",\n" +
                "        };\n" +
                "\n" +
                "        myChart.setOption(option);\n" +
                "        // 图表大小变动从新渲染，动态自适应\n" +
                "        window.addEventListener(\"resize\", function () {\n" +
                "            myChart.resize();\n" +
                "        });\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
        return index;
    }

    @CrossOrigin
    @GetMapping("/getInfo2")
    public ArrayList<String> getInfo_2() throws IOException {

        ArrayList<String> arrayList = new ArrayList<>();

        String json = "";

        try {
            BufferedReader in = new BufferedReader(new FileReader("src/temp.json"));
            String str;
            while ((str = in.readLine()) != null) {
                System.out.println(str);
                json = json.concat(str);
            }
            System.out.println(str);
        } catch (IOException e) {
            System.out.println(e);
        }

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);

        arrayList.add(""+JsonPath.read(document, "$.repo"));
        arrayList.add(""+JsonPath.read(document, "$.developers"));
        arrayList.add(""+JsonPath.read(document, "$.most_active_developer.login"));
        arrayList.add(""+JsonPath.read(document, "$.open_issues"));
        arrayList.add(""+JsonPath.read(document, "$.close_issues"));
        System.out.println(arrayList);

        return arrayList;
    }

    @CrossOrigin
    @GetMapping("/{reponame}")
    public String getRepo(@PathVariable(value = "reponame") String reponame) throws IOException {

        StringBuilder response = new StringBuilder();
        BufferedReader in = new BufferedReader(
                new FileReader("src/" + reponame + ".json"));

        String line;

        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        return response.toString();
    }

    @CrossOrigin
    @GetMapping("/{reponame}/issue")
    public String getIssue(@PathVariable(value = "reponame") String reponame) throws IOException {

        StringBuilder response = new StringBuilder();
        BufferedReader in = new BufferedReader(
                new FileReader("src/" + reponame + ".json"));

        String line;

        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

//        JSONArray arr = JSON.parseArray(response.toString());
        JSONObject a = JSON.parseObject(response.toString());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("issue_num", (int)a.get("closedIssues_num") + (int)a.get("openIssues_num"));
        jsonObject.put("open_issue_num", a.get("openIssues_num"));
        jsonObject.put("closed_issue_num", a.get("closedIssues_num"));
        return jsonObject.toJSONString();
    }

    @CrossOrigin
    @GetMapping("/{reponame}/most_active_developer")
    public String getMostActiveDevelopers(@PathVariable(value = "reponame") String reponame) throws IOException {

        StringBuilder response = new StringBuilder();
        BufferedReader in = new BufferedReader(
                new FileReader("src/" + reponame + ".json"));

        String line;

        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

//        JSONArray arr = JSON.parseArray(response.toString());
//        JSONObject respondeBodyJson = JSONObject.parseObject(response.toString(), Feature.OrderedField);
        JSONObject a = JSON.parseObject(response.toString(), Feature.OrderedField).getJSONObject("activeDevelopersEmail");
        JSONObject jsonObject = new JSONObject(true);
        Iterator<Map.Entry<String, Object>> iter = a.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = iter.next();
            jsonObject.put(entry.getKey().toString(), entry.getValue().toString());
        }

        return jsonObject.toJSONString();
    }

    @CrossOrigin
    @GetMapping("/{reponame}/commit_time_distribution")
    public String getCommitTimeDistribution(@PathVariable(value = "reponame") String reponame) throws IOException {

        StringBuilder response = new StringBuilder();
        BufferedReader in = new BufferedReader(
                new FileReader("src/" + reponame + ".json"));

        String line;

        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

//        JSONArray arr = JSON.parseArray(response.toString());
        JSONObject a = JSON.parseObject(response.toString()).getJSONObject("timeLocation");
        JSONObject jsonObject = new JSONObject(true);
        Iterator<Map.Entry<String, Object>> iter = a.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = iter.next();
            jsonObject.put(entry.getKey().toString(), entry.getValue().toString());
        }

        return jsonObject.toJSONString();
    }
}
