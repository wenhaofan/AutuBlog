#sql("beforeNum")
	select 
		DATE_FORMAT(a.gmtcreate,'%m-%d') as gmtcreate,ifnull(b.count,0) as count
	from (
	    SELECT 
	    	curdate() as gmtcreate
	    #for(i=1;i<days;i++)
	      union all
	   	  SELECT date_sub(curdate(), interval #(i) day) as gmtcreate
	    #end
	) a left join (
	  select  DATE_FORMAT(gmtCreate,'%Y%m%d') as datetime, count(distinct cookie) as count ,cookie
	  from access_log
	  group by DATE_FORMAT(gmtCreate,'%Y%m%d')
	) b on a.gmtcreate = b.datetime
	order by UNIX_TIMESTAMP(a.gmtcreate)
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
