var metaUtils={
		/**根据类型查询文章元数据,为空则查询所有*/
		listMeta:function (paras){
			$.ajax({
				url:"/api/meta/list/"+paras.type,
				dataType:"json",
				success:function(data){
					if(fl.isOk(data)){
						paras.callback(data.metas);
					}
				}
			})
		},
		/**查询文章下指定类型的元数据*/
		listMetaByArticleId:function (paras){
			var categorys;
			$.ajax({
				url:"/api/meta/article/"+paras.type+"-"+paras.articleId,
				dataType:"json",
				success:function(data){
					if(fl.isOk(data)){
						paras.callback(data.metas);
					}
				}
			})
		}
}




