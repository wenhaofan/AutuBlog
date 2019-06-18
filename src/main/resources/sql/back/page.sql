#sql("page")
	select  *  from page  where 1=1
	#if(query.isDeleted!=null)
		 and isDeleted =#para(query.isDeleted)
	#end
	
#end