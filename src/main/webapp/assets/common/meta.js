var metaUtils={

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




