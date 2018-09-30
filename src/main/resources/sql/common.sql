#define page()
		limit #para((pageSize-1)*pageNum),#para(pageNum)
#end

文章分类通用组件
#define queryMeta()
	#if(mid!=null)
		and id in
					(
						select 
							cid 
						from	
							(select 
									cid
							 from
									relevancy
							 where
									mid=#para(mid)
							) as cids
					)
	#end
#end


#define valid(tableName)

#end