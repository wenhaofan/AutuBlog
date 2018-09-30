#sql("page")
	select
		*
	from
		sys_log
	where
		1=1
	#if(query.level!=null)
		and level=#para(query.level)
	#end
	
	 order by gmtCreate desc
#end

#sql("listRecent")
	select * from sys_log  order by gmtCreate desc
#end

