#sql("list")

	select
		*
	from
		disk
 
	where
		1=1
		#if(query.state!=null)
			and
				state=#para(query.state)
		#else
			and state =0
		#end
		
		#if(query.parentId!=null)
			and
				parentId=#para(query.parentId)
		#else
			and
				parentId=0
		#end
		
		#if(query.order!=null)
			order by #para(query.order) #para(query.orderType) 
		#else if(query.isSizeOrder())
			order by size #para(query.orderType) 
		#end
#end

#sql("listCheckFile")
	select 
		* 
	from
		disk 
	where 
		left(disk.name, #(fileName.length()))=#para(fileName)
	and
		parentId=#para(parentId==null?0:parentId)
#end

#sql("listCheckFolder")
	select 
		* 
	from
		disk 
	where 
		left(disk.name, #(name.length()))=#para(name)
	and
		parentId=#para(parentId)
#end