#sql("page")
	select
		*
	from
		comment
	where
			1=1
	#if(query.state!=null)
		and isAduit=#para(query.state)
	#end
	#if(query.identify!=null&&query.identify.length()>0)
		and  identify=#para(query.identify)
	#end
	order by gmtCreate desc
#end

#sql("countBefore")
	select count(id) from comment where identify= #para(identify) and gmtCreate > #para(gmtCreate)
#end