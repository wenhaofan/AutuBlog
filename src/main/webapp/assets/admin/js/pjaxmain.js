layui.define([
    'jquery',
    'NProgress',
    'pjax'
], function(exports) {
    const $=layui.$, NProgress=layui.NProgress,pjax=layui.pjax;
    const pjaxmain= {
        $:$,
        NProgress:NProgress,
        "load":function(){
            const $=this.$;
            const NProgress=this.NProgress;

            $(document).on('pjax:start', function() { 
            	NProgress.start();
            });
    
            $(document).on('pjax:end',   function() { 
                NProgress.done(); 
                $(".note-popover").remove() 
                if(typeof pjaxLoad !="undefined"){
                    pjaxLoad();
                }
            });
            
 
            $(document).pjax('a[pjax]', '.layui-fluid');
 	   }
    };

    pjaxmain.load();
    
    exports("pjaxmain",pjaxmain);
});


