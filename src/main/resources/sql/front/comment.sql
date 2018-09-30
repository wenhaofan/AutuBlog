#sql("page")
	select
		*
	from
		comment
	where
	 	identify=#para(0)
	and
	 	parentId=0
	and
		isAduit=1
		
	 order by gmtCreate desc
#end