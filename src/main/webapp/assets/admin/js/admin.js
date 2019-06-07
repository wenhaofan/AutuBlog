layui.define([
  'form','jquery',
    'fl' ,'pjaxmain'
], function (exports) {
  $=layui.$;
    const admin = {

        bind: function () {

            form = layui.form;
            layer = layui.layer;
           
            var element = layui.element;
            //监听提交
            form.on('submit(updatePwd)', function (data) {
                fl.ajax({
                    url: "/admin/api/user/editPwd",
                    data: data.field,
                    success: function (data) {
                        fl.alertOk({ title: "密码修改成功！" });
                        layer.close(updatePwdIndex);
                    }
                })
                return false;
            });

            //监听提交
            form.on('submit(updateInfo)', function (data) {

                fl.ajax({
                    url: "/admin/api/user/editInfo",
                    data: data.field,
                    success: function (data) {
                        fl.alertOk({ title: "修改成功！" });
                        layer.close(updatePwdIndex);
                    }
                })
                return false;
            });

            $("#updatePwd").click(function () {
                updatePwd();
            });
            $("#updateInfo").click(function () {
                updateInfo();
            });
            $(".layui-layout-right").click(function () {
                $(this).trigger("mouseover");
            })

            $(".layui-toggle-menu").click(function () {
                if ($(this).find("i").hasClass("layui-icon-spread-left")) {
                    $(this).find("i").removeClass("layui-icon-spread-left");
                    $(this).find("i").addClass("layui-icon-shrink-right");
                    $(".layui-side").show();
                    $(".layui-body").css("left", "200px");
                } else {
                    $(this).find("i").addClass("layui-icon-spread-left");
                    $(this).find("i").removeClass("layui-icon-shrink-right");
                    $(".layui-side").hide();
                    $(".layui-body").css("left", "0");
                }
            })

        }, load: function () {

            $(".menu-tree a").each(function () {
                if ($(this).attr("href") == window.location.pathname) {
                    $(this).addClass("layui-this")
                }
            })
 

            $(window).resize(function () {
                if ($(window).width() <= 768) {
                    $(".layui-toggle-menu i").addClass("layui-icon-spread-left");
                    $(".layui-toggle-menu i").removeClass("layui-icon-spread-right");
                    $(".layui-side").hide();
                    $(".layui-body").css("left", 0);
                }
            })

            if ($(window).width() <= 768) {
                $(".layui-toggle-menu i").addClass("layui-icon-spread-left");
                $(".layui-side").hide();
                $(".layui-body").css("left", 0);
            } else {
                $(".layui-toggle-menu i").addClass("layui-icon-shrink-right");
                $(".layui-side").show();
            }

            const pjaxmain=layui.pjaxmain;

            if(typeof requireLoad != "undefined"){
                requireLoad();
            } 

            pjaxmain.load();

        }, pjaxLoad: function () {

        }, updatePwd: function () {
            updatePwdIndex = layer.open({
                title: "修改密码",
                area: ['340px', '215px'],
                type: 1,
                content: $("#tpl-update-pwd").html(),
                success: function () {
                    form.render();
                }
            });
        },
        updateInfo: function () {
            updatePwdIndex = layer.open({
                title: "修改个人信息",
                area: ['340px', '265px'],
                type: 1,
                content: $("#tpl-update-info").html(),
                success: function () {
                    form.render();
                }
            });
        }


    };

    admin.bind();

    exports("admin",admin);
});












