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

            $(document).on('pjax:start', function() { NProgress.start(); });
    
            $(document).on('pjax:end',   function() { 
                NProgress.done(); 
                $(".note-popover").remove() 
                if(typeof requireLoad !="undefined"){
                    requireLoad();
                }
            });
            
 
            $(document).pjax('a[pjax]', '.layui-fluid');
 	   }
    };
    exports("pjaxmain",pjaxmain);
});


