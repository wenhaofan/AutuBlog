#sql("page")
	select 
		* 
	from 
		login_record
	order by gmtCreate desc
#end

#sql("listRecent")
	select 
		*
	from
		login_record
	order by 
		gmtCreate 
	desc  limit #para(0)
#end