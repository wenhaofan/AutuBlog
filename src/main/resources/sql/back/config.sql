#sql("get")
	select
		*
	from
	  config
	order by gmtCreate desc
	limit 1
#end