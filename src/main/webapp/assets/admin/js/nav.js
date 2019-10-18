
layui.define([
    'jquery',
    'fl',
    'form',
    'layer',
    'table'
], function (exports) {

    const nav = {
        bind: function () {
            form = layui.form;

            table = layui.table;
            const that = this;
            const fl = layui.fl;
            //监听提交
            form.on('submit(nav-add)', function (data) {
                that.add(data.field);
                return false;
            });
            form.on('submit(nav-update)', function (data) {
                that.update(data.field);
                return false;
            });
            
        	$("body").on("click",".nav-add",function(){
        	  	var editHtml=template("tpl-nav-edit",{method:"add"});

        	  	layerIndex=layer.open({
        	  		title:"新增导航",
        	  		area: ['420px', '300px'],
        		  	  type: 1, 
        		  	  content: editHtml, //这里content是一个普通的String
        		  	  success:function(){
        		  		  form.render();
        		  	  }
        	  	});
        	})
        	
        	$("body").on("click",".nav-update",function(){
        		var id=$(this).data("id");
        		var sort=$(this).parent().parent().prev().text();
        		var url=$(this).parent().parent().prev().prev().text();
        		var title=$(this).parent().parent().prev().prev().prev().text();
        		
        	  	var editHtml=template("tpl-nav-edit",{method:"update",sort:sort,url:url,title:title,id:id});
        	  	layerIndex=layui.layer.open({
        	  		title:"修改导航",
        	  		area: ['420px', '300px'],
        		  	  type: 1, 
        		  	  content: editHtml, //这里content是一个普通的String
        		  	  success:function(){
        		  		  layui.form.render();
        		  	  }
        	  	});
        	})
        	
        	$("body").on("click",".nav-delete",function(){
				deleteId=$(this).data("id");
				fl=layui.fll
				fl.alertConfirm({title:"确认删除！",then:function(){
					fl.ajax({
						url:"/admin/api/nav/delete",
						data:{toId:deleteId},
						success:function(data){
							fl.alertOkAndReload();	
						}
					})
				}})
			})
         
        }, load: function () {

        	   layui.fl.renderTable({
                   elem: '#navs'
                   , url: '/admin/api/nav/list'
                   , cellMinWidth: 80
                   , cols: [[
                       { field: 'id', sort: true, width: 60, title: "id" }
                       , { field: 'title', title: "标题", width: 130 }
                       , { field: 'url', title: "URL" }
                       , { field: 'sort', title: "排序" }
                       , { templet: '#operation-tpl', title: "操作" }
                   ]]
               });
        	
        }, pjaxLoad: function () {
        	this.load();
        }, add: function (data) {
            const fl = layui.fl;
            $.ajax({
                url: "/admin/api/nav/add",
                type: "post",
                data: data,
                success: function (data) {
                    if (fl.isOk(data)) {
                        fl.alertOkAndReload("添加成功！");
                    }
                }
            })

        },
        update: function (data) {
            const fl = layui.fl;
            fl.ajax({
                url: "/admin/api/nav/update",
                type: "post",
                data: data,
                success: function (data) {
                    fl.alertOkAndReload("修改成功！");
                }
            })
        }
    };

    nav.bind();
    nav.load();
    exports("nav", nav);
});



