#sql("beforeNum")
	SELECT
		DATE_FORMAT( a.gmtcreate, '%m-%d' ) AS gmtcreate,
		count( DISTINCT cookie ) AS count
	FROM(
	    SELECT 
	    	curdate() as gmtcreate
	    #for(i=1;i<days;i++)
	      union all
	   	  SELECT date_sub(curdate(), interval #(i) day) as gmtcreate
	    #end
	) a 
		LEFT JOIN (
		SELECT
			DATE_FORMAT( gmtCreate, '%Y%m%d' ) AS datetime,
			cookie
		FROM
			access_log 
		) b ON a.gmtcreate = b.datetime 
	group BY
		UNIX_TIMESTAMP(a.gmtcreate)
#end

#sql("hotArticle")
	select
		title,
		pv,
		id
	from
		article
		order by pv desc limit 0,#(size)
#end

#sql("countByDate")
	select
		count(id)
	from
		#(tableName)
		
	#if(gmtEnd!=null)
		where
			gmtCreate >= '#date(gmtStart,"yyyy-MM-dd HH:mm:ss")'
		and
			gmtCreate <= '#date(gmtEnd,"yyyy-MM-dd HH:mm:ss")'
	#end
#end
