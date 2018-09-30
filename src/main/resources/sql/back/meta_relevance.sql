#sql("isRelevance")
	select count(id) as count from relevancy where mid=#para(mid) and cid=#para(cid)
#end

#sql("delete")
	delete from relevancy where  
		#if(mid!=null) 
			mid= #para(mid) 
		#elseif(cid!=null) 
		   cid = #para(cid) 
		#end 
#end