/** 基础js加载类 */

 
(function(){
    var head= document.getElementsByTagName('head')[0]; 
	var script= document.createElement('script'); 
	script.type= 'text/javascript'; 
 
	script.src= '/assets/echarts/echarts.js'; 
    head.appendChild(script); 
})();


 layui.config({
        base: '/assets/admin/js/'//模块存放的目录
}).extend({ //设定模块别名
     fl: '{/}/assets/js/base',// {/}的意思即代表采用自有路径，即不跟随 base 路径
     NProgress:'{/}/assets/plugins/nprogress/nprogress',
     pjax:'{/}/assets/plugins/jquery-pjax/jquery.pjax',
     ueditor:'{/}/assets/plugins/ueditor/ueditor.all',
});
 
 
layui.use(['fl','table','jquery','pjaxmain','element'], function (fl,table,$,pjaxMain){
 

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


    	form=layui.form;
	layer=layui.layer;
	var element = layui.element;
	  //监听提交
	  form.on('submit(updatePwd)', function(data){
		   fl.ajax({
			   url:"/admin/api/user/editPwd",
			   data:data.field,
			   success:function(data){
					fl.alertOk({title:"密码修改成功！"});
					layer.close(updatePwdIndex);
			   }
		   })
	  	 return false;
	  });
	  
	  //监听提交
	  form.on('submit(updateInfo)', function(data){
		 
		   fl.ajax({
			   url:"/admin/api/user/editInfo",
			   data:data.field,
			   success:function(data){
				    fl.alertOk({title:"修改成功！"});
					layer.close(updatePwdIndex);
			   }
		   })
	  	 return false;
      });
      

      function updatePwd(){
	updatePwdIndex=layer.open({
  		title:"修改密码",
  		area: ['340px', '215px'],
	  	type: 1, 
	  	content: $("#tpl-update-pwd").html(),
	  	success:function(){
	  		form.render();
	  	}
  	});
}
function updateInfo(){
	updatePwdIndex=layer.open({
  		title:"修改个人信息",
  		area: ['340px', '265px'],
	  	type: 1, 
	  	content: $("#tpl-update-info").html(),
	  	success:function(){
	  		form.render();
	  	}
  	});
}


 
	$("#updatePwd").click(function(){
		updatePwd();
	});
	$("#updateInfo").click(function(){
		updateInfo();
	});
	$(".layui-layout-right").click(function(){
		$(this).trigger("mouseover");
	})
 

});
 
