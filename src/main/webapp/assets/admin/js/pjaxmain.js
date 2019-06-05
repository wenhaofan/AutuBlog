define([
    'jquery',
    'NProgress',
    'pjax'
], function($, NProgress,pjax) {
    return {
        $:$,
        NProgress:NProgress,
        "load":function(){
            const $=this.$;
            const NProgress=this.NProgress;

            $(document).on('pjax:start', function() { NProgress.start(); });
    
            $(document).on('pjax:end',   function() { 
                NProgress.done(); 
                $(".note-popover").remove() 
                requireLoad();
            });
            
 
            $(document).pjax('a[pjax]', '.layui-fluid');
 	   }
    };
});


