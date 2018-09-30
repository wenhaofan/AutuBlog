#sql("get")
	select  *  from meta where type = #para(type) and mname = #para(mname)
#end

#sql("listByArticleId")
	select 
		*
	from
	 meta as m inner join relevancy as r
	 on 1=1  and r.mid=m.id
	 #if(cid!=null)
		 and r.cid =#para(cid)
	 #end
	
	 where
	  #if(type!=null)
	 	  m.type= #para(type)
	 #end
	 
		GROUP BY
		m.id
	ORDER BY
		count DESC,
		m.sort DESC
#end

 