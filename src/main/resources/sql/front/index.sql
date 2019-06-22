
#sql("page")
	select 
		* 
	from 
		article 
	where
	 
	1=1
	#if(metaId!=null)
		  and  
			id in(
		 		select
					cid 
				from
					relevancy
				where
					mid=#(metaId)
			)
 	#end
	and
		state=1
	#if(isTop!=null)
		#if(isTop)
			and isTop=#para(isTop)
		#else
			and isTop!=1
		#end
	#end
	order by gmtCreate desc
#end

#sql("listTop")
	select
		*
	from
		article
	where
	 	state=1
	and
		isTop=1
#end