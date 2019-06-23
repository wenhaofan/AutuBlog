layui.define([
    'jquery',
    'fl'
], function(exports) {
    
    const themes={
        bind:function(){
        	const fl=layui.fl;
            $("body").on("click",'.theme-change',function(){
                var name=$(this).data("name");
                $.ajax({
                    url:"/admin/api/themes/change/"+name,
                    success:function(data){
                        if(fl.isOk(data)){
                            fl.alertOkAndReload("切换成功！");
                        }
                    }
                })
            })
        },load:function(){
        	
        },pjaxLoad:function(){
        	
        }
    };
    
    themes.bind();
    themes.load();
    exports("themes",themes);
});
 