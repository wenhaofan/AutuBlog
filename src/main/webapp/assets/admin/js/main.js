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
     contextMenu:'{/}/assets/plugins/context-menu/contextMenu',
     formSelects:'{/}/assets/frame/layui/plugins/formSelectes/formSelects-v4',
     inputTags:'{/}/assets/frame/layui/plugins/inputTags/inputTags',
     editormd:'{/}/assets/plugins/editor/editormd'
});

 
layui.use(['admin','element'], function (admin){
    admin.load();
});
 
