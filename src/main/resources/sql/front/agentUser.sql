#sql("findByCookie")
select
	*
from
 agent_user
 where cookie=#para(0)
#end