#sql("lastNextArticle")
	SELECT
		*
	FROM
		article
	WHERE
	state = 1
	and id >= (
		(
			SELECT
				MAX(id)
			FROM
				article
			WHERE
				state = 1
			and id !=#para(0)
		) - (
			SELECT
				MIN(id)
			FROM
				article
			WHERE
				state = 1
			and id !=#para(0)
		)
	) * RAND() + (
		SELECT
			MIN(id)
		FROM
			article
		WHERE
			state = 1
		and id !=#para(0)
	)
	and id !=#para(0)
 
	LIMIT 2
#end

#sql("randomArticle")
SELECT
	*
FROM
	article
WHERE
	id >= (
		(
			SELECT
				MAX(id)
			FROM
				article
			WHERE
				state = 1
			#if(notInIds!=null&&!notInIds.isEmpty())
			and 
				id not in (#for(id:notInIds)#(id)#if(!for.last),#end #end)
			#end
		) - (
			SELECT
				MIN(id)
			FROM
				article
			WHERE
				state = 1
			#if(notInIds!=null&&!notInIds.isEmpty())
			and 
				id not in (#for(id:notInIds)#(id)#if(!for.last),#end #end)
			#end
		)
	) * RAND() + (
		SELECT
			MIN(id)
		FROM
			article
		WHERE
			state = 1
		#if(notInIds!=null&&!notInIds.isEmpty())
		and 
			id not in (#for(id:notInIds)#(id)#if(!for.last),#end #end)
		#end
	)
	#if(notInIds!=null&&!notInIds.isEmpty())
	and 
		id not in (#for(id:notInIds)#(id)#if(!for.last),#end #end)
	#end
LIMIT #(limit??1)
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