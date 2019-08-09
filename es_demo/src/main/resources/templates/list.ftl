<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>just for search</title>
    <script type="text/javascript" src="../static/js/jquery-1.12.4.min.js" ></script>
    <script type="text/javascript" src="../static/a.js" ></script>
    <link href="https://cdn.bootcss.com/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <script type="text/javascript" src="https://cdn.bootcss.com/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
</head>

<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <blockquote class="pull-right" style="width: 100%">
                <p>
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante.
                </p>
                <small>Someone famous <cite>Source Title</cite></small>
            </blockquote>
            <div class="form">
                <input type="text" id="userName" class="form-control" placeholder="请输入搜索内容"
                       style="display: inline-block; width: 200px"/>
                <button type="submit" id="submitName" class="btn btn-default">Submit</button>
            </div>
            <table class="table table-hover table-striped">
                <thead>
                <tr>
                    <th>编号</th>
                    <th>姓名</th>
                    <th>年龄</th>
                    <th>创建时间</th>
                </tr>
                </thead>
                <tbody id="user_table">
<#--                    <#list userList as user>-->
<#--                        <tr>-->
<#--                            <td>${user.id}</td>-->
<#--                            <td>${user.name}</td>-->
<#--                            <td>${user.age}</td>-->
<#--                            <td>${user.createTime}</td>-->
<#--                        </tr>-->
<#--                    </#list>-->
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>

<script>
    var APP_PATH = "http://localhost:8080/es_demo";
    var flag = true;

    $(function() {
       //去首页
       to_index();
    });

    function to_index() {
        $.ajax({
            url: APP_PATH + "/user/getAllUser",
            data: "",
            type: "GET",
            success: function(result) {
                console.log(result);
                //解析并显示用户信息
                build_user_table(result);
            }
        });
    }

    function build_user_table(result) {
        var count = 1;
        var users;
        //清空数据
        $("#user_table").empty();
        if (!flag) {
            users = result;
        } else {
            users = result.content;
        }

        $.each(users, function (index, item) {
            console.log(item.id);
            var id = $("<td></td>").append(count);
            var name = $("<td></td>").append(item.name);
            var age = $("<td></td>").append(item.age);
            var time = $("<td></td>").append(item.create_time);

            $("<tr></tr>").append(id)
                .append(name)
                .append(age)
                .append(time).appendTo("#user_table");
            count = count + 1;
        });

    }

    $("#submitName").click(function() {
        var reg = /\s+/g;
        var username = $("#userName").val();
        if (username.length == 0 || reg.test(username)) {
            alert("请输入您要搜索的内容。。。");
        }
        console.log("user name is " + username);
        //点击提交按钮更新视图
        findUserByName(username);
    });

    function findUserByName(userName) {
        $.ajax({
            url: APP_PATH + "/user/find/" + userName,
            type: "GET",
            success: function(result) {
                console.log(result);
                flag = false;
                build_user_table(result);

            }
        });
    }
</script>
</html>