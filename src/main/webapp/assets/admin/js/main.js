/** 基础js加载类 */
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
     editormd:'{/}/assets/plugins/editor/editormd',
     lsCache:'{/}/assets/frame/layui/plugins/lsCache/lsCache',
     clipboard:'{/}/assets/plugins/clipboard/clipboard.min',
     echarts:'{/}/assets/echarts/echarts'
});


const config={
	pjaxContainer:'.layui-fluid'
};

layui.use(['admin','element'], function (admin){
    admin.load();
});
 
function ctrlsEvent(func){

	 document.onkeydown = function(oEvent) {
	    var oEvent = oEvent || window.oEvent; 
	    //获取键盘的keyCode值
	    var nKeyCode = oEvent.keyCode || oEvent.which || oEvent.charCode;
	    //获取ctrl 键对应的事件属性
	    var bCtrlKeyCode = oEvent.ctrlKey || oEvent.metaKey;
	     if( nKeyCode == 83 && bCtrlKeyCode  ) {
	    	 func()
	         //doSomeThing...
	    	 return false;
	     }
	}
}
