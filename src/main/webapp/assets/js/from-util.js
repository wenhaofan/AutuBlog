var formUtil={
	setFormVal:function(data,target){
		var val;
		var $obj;
		for(var key in data){
			if(target==undefined){
				$obj=$('[name="'+key+'"]');	
			}else{
				$obj=target.find('[name="'+key+'"]');
			}
			//如果获取到了元素则设置value
			if($obj.length>=1){
				try{
					$obj.val(data[key]);
				}catch(e){
					console.log(key+"不存在对应元素");
				}
			}
		}
	}
}
