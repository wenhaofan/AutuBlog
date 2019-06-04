
define([
    
], function( ) {
    const articleList={
        resetLayerIndex:0,
        //渲染文章列表
        renderArticles:function (){
            fl.renderTable({
                page:{count:80,limit:10}
                ,url:'/admin/api/article/list' 
                ,elem: '#articles'
                ,page: true //开启分页
                ,cols: [[ //表头
                    {field:'id', sort: true,title:"ID", width:90}
                ,{field:'title',minWidth:150,title:"标题"}
                ,{field:'pv', width:90,sort: true,title:"阅读量"}
                ,{field:'state',width:90,templet:'#state-tpl',title:"状态"}
                ,{field:'identify',title:"访问路径"}
                ,{templet:'#operation-tpl', title:"操作"}
                ]]
            });
        },
        /**
                * 重置lucene所有索引
         * @returns
         */
        resetIndex:function (){
            resetLayerIndex=layer.msg('重置中', {
                    icon: 16
                    ,shade: 0.01,
                    time: false,
                    success:function(){
                            
                        }
                    }); 
            fl.ajax({
                url:"/admin/api/article/createIndex",
                success:function(data){
                    fl.alertOk({title:"重置成功！"});
                    layer.close(resetLayerIndex);
                },error:function(){
                    layer.close(resetLayerIndex);
                }
            })
        },
        /**
             * 恢复文章
        * @param that
        * @returns
        */
        recoverArticle:function (that){
            var id = $(that).data("id");
            fl.alertConfirm({title:"是否确认恢复？",then:function(){
                fl.ajax({
                    url : "/admin/api/article/recover/"+id,
                    success : function(data) {
                        fl.alertOkAndReload(data.msg)
                    }
                })
            }})
        },
        /** 初始化分类多选 */
        initCategorySelect:function () {
            var initCategorys = {
                type:"category",
                callback : function(categorys) {
                    var html = template("option-tpl", {
                        "categorys" : categorys
                    });
                    $("#category-select").html(html);
                    form.render('select');
                }
            }
            metaUtils.listMeta( initCategorys);
        },
         

        /**
                * 废弃文章
         * @param that
         * @returns
         */
        removeArticle:function (that){
            var id = $(that).data("id");
            fl.alertConfirm({title:"是否确认废弃？",then:function(){
                fl.ajax({
                    url : "/admin/api/article/remove/"+id,
                    success : function(data) {
                        fl.alertOk({title:data.msg});
                        $(that).parent().parent().parent().remove();
                    }
                })
            }})
            return false;
        },

        /**
                 * 删除文章
         * @param that
         * @returns
         */
        deleteArticle:function (that){
            var id = $(that).attr("data-id");
            fl.alertConfirm({title:"是否确认删除？",text:"注意：删除后将不能恢复！",then:function(){
                fl.ajax({
                    url : "/admin/api/article/delete/"+id,
                    success : function(data) {
                        fl.alertOk({title:data.msg});
                        $(that).parent().parent().parent().remove();
                    }
                })
            }})
        },

       /**
               * 调用metaweblog接口同步发送文章
        * @param that
        * @returns
        */
        asyncMetaWeblog:function (that){
            fl.ajax({
                url:"/admin/api/article/asyncMetaWeblog/"+$(that).data("id"),
                success:function(data){
                    fl.alertOk({});
                }
            })
        },
        //layui加载完成的回调
        layuiLoad:function(){
 
            form.on('select(category-select)', function(data) {
                querylist($(".layui-form").serializeJson());
            });
            form.on('select(state-select)', function(data) {
                querylist($(".layui-form").serializeJson())
            });
            
            this.renderArticles();
            this.initCategorySelect();
        },
        //jquery加载完成的回调
        jqueryLoad:function(){
            const that=this;
            $("body").on("click", ".article-remove", function() {
                that.removeArticle(this);
            })
            $("body").on("click", ".article-delete", function() {
               that.deleteArticle(this);
            })
            
            $("body").on("click", ".article-recover", function() {
               that.recoverArticle(this);
            })
            
            $("body").on("click",".article-push",function(){
                that.asyncMetaWeblog(this);
            })
        
            $("body").on("click",".createIndex",function(){
                that.resetIndex();
            })
        }
    };
    return {
        articleList:articleList
    };
});
 




