#sql("get")
	select 
		k,v,type,id
	from 
		kv
	where 
		1=1
	#if(kv.id!=null)
		and id=#para(kv.id)
	#end
	#if(kv.k!=null)
		and k=#para(kv.k)
	#end
	#if(kv.v!=null)
		and v=#para(kv.v)
	#end
#end