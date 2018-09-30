#sql("list")
	select 
		#@metaColumn(),
	count(r.cid) AS count
	from
	 meta as m left join relevancy as r
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

#sql("listByArticleId")
	select 
		#@metaColumn()
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


#define metaColumn()
	m.mname,
	m.type,
	m.sort,
	m.description,
	m.id
#end