/** 基础js加载类 */


require.config({
    baseUrl: "/assets/admin/js",
    paths: {
    "articleList": "article-list",
    "jquery":"/assets/js/jquery-3.2.1.min",
    "fl":"/assets/js/base",
    "console":"console",
    "NProgress":"/assets/plugins/nprogress/nprogress",
    'echarts':"/assets/echarts/echarts",
    'pjaxmain':'pjaxmain',
    'pjax':'/assets/plugins/jquery-pjax/jquery.pjax',
    'layui':'/assets/frame/layui/layui.all'
    },shim:{
        'layui':{ 
            exports:'layui' 
        }
    }
});
 
require(['pjaxmain','jquery','layui'], function (pjaxMain,$,layui){
    if(typeof requireLoad != "undefined"){
        requireLoad();
    } 
    pjaxMain.load();

    $(".menu-tree a").each(function(){
            if($(this).attr("href")==requestUrl){
                $(this).addClass("layui-this")
            }
        })
    
        
        $(".layui-toggle-menu").click(function(){
            if($(this).find("i").hasClass("layui-icon-spread-left")){
                $(this).find("i").removeClass("layui-icon-spread-left");
                $(this).find("i").addClass("layui-icon-shrink-right");
                $(".layui-side").show();
                $(".layui-body").css("left","200px");
            }else{
                $(this).find("i").addClass("layui-icon-spread-left");
                $(this).find("i").removeClass("layui-icon-shrink-right");
                $(".layui-side").hide();
                $(".layui-body").css("left","0");
            }
        })
        
        $(window).resize(function(){
            if($(window).width()<=768){
                $(".layui-toggle-menu i").addClass("layui-icon-spread-left");
                $(".layui-toggle-menu i").removeClass("layui-icon-spread-right");
                $(".layui-side").hide();
                $(".layui-body").css("left",0);
            }
        })

       if($(window).width()<=768){
            $(".layui-toggle-menu i").addClass("layui-icon-spread-left");
            $(".layui-side").hide();
            $(".layui-body").css("left",0);
        }else{
            $(".layui-toggle-menu i").addClass("layui-icon-shrink-right");
            $(".layui-side").show();
        }

});
 
