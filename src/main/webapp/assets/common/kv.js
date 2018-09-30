/**添加一个标签或者分类*/
function saveKv(data){
	$.ajax({
		url:"/admin/api/kv/save",
		type:"post",
		data:data,
		dataType:"json",
		success:function(data){
			if(fl.isOk(data)){
				fl.alertOkAndReload("操作成功");
			}
		}
	})
}

function listKv(type,cal){
	$.ajax({
		url:"/admin/api/basic",
		success:function(data){
			if(fl.isOk(data)){
				cal.callback(data.kvs);
			}
		}
	})
}