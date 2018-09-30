#sql("lastNextArticle")
	select 
		*
	from
		article
	where
		1=1
	and 
		state=1
		and id in (
			select 
				articleId
			from
				articleCategory
			where
				categoryId in (
						select
							categoryId 
						from 
							articleCategory
						where 
							articleId=#para(0)
						)
			GROUP BY articleId
				)
	order by readNum desc
	limit 2
#end

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

#sql("about")
	select
		pv,id,identify,title,gmtCreate
	from
		article
	where
		id in(
			select cid from(
		 		select
		 			cid
		 		from
		 		relevancy
		 		where
		 			mid in(
		 				select mid from (
			 				select
								mid 
							from
								relevancy
							where
								cid=#(article.id)
		 				)as mids
		 			)
	 		)as cid
		)
	and state =1
	ORDER BY RAND()
	limit #(size)
#end