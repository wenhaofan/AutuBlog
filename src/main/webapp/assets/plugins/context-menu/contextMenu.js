var $contextMenu= (function(){
    return {
    	menuItemMap:{},
    	onlyOne:true,
    	on:function(selector,paras){
    	   var that=this;
    	   if(!paras){
    		   throw  new Error( 'paras can not be bull' );
    	   }
    	   //绑定右键点击事件,右键生成目录
		   $('body').on("contextmenu",selector, function (e){
			   if(that.onlyOne){
				   $(".context-menu").hide();
			   }
			   that.gen(this,e,paras);
			   return false;
           });
    	},
    	checkCondition:function(paras){
		 for (var i = 0, l = paras.length; i < l; i++) {
          	var condition={method:paras[i].condition};
          	
          	if(!condition.method){
          		continue;
          	}
          	var isShow=!condition.method();
          	if(isShow) $("[menu-item-id='"+paras[i].id+"']").hide();
          	else  $("[menu-item-id='"+paras[i].id+"']").show();
          	
          }
    	},
        gen: function (element,e,paras) {
        	var that=this;
        	var menuId=$(element).attr("menu-id");
        	var $menu=$("#"+menuId);

        	if($menu.length==0||!menuId){
        		 menuId="context-menu"+this.getRandomStr();
        		 $menu = $('<div class="context-menu" id="'+menuId+'" ><ul></ul></div>');
                 for (var i = 0, l = paras.length; i < l; i++) {
                 	var $li=this.getItem(paras[i]);
                 	$menu.find("ul").append($li);
                 }
                 $(element).attr("menu-id",menuId);
        	}

        	l = ($(document).width() - e.clientX) < $menu.width() ? (e.clientX - $menu.width()) : e.clientX;
            t = ($(document).height() - e.clientY) < $menu.height() ? (e.clientY - $menu.height()) : e.clientY;
          	$menu.css({left: l,top: t}).show().appendTo("body");
          	
          	this.checkCondition(paras);
		},getItem(paras){
			//为每一个目录生成一个随机id
			var id="contextMenuLi"+this.getRandomStr();
			var $li='<li menu-item-id="'+id+'" id="'+paras.id+'" class="'+paras.className+'" ><a href="javascript:void(0)" >'+paras.text+'</a></li>'; 
			paras.id=id;
			
			//遍历绑定事件
			var events=paras.events;
			var that=this;
	    	for (var key in events) {
	    		var method=events[key];
    			$('body').on(key,"[menu-item-id='"+id+"']", function (){
    				var menuId=$(this).parent().parent().attr("id");	
    				method($("[menu-id='"+menuId+"']"),this);
    			});
	    	}
		    return $li;
		},getRandomStr(){
			return Math.random().toString(36).substr(2);
		}
	}})();
//隐藏所有
$('html').on('contextmenu', function (){return false;}).click(function(){
	$('.context-menu').hide();
});
