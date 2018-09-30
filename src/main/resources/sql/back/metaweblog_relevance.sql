#sql("get")
	select * from metaweblog_relevance where articleId=#para(articleId) and metaweblogId=#para(metaweblogId)
#end