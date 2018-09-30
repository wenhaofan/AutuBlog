$(function(){
	$(".theme-change").click(function(){
		var name=$(this).data("name");
		$.ajax({
			url:"/admin/api/themes/change/"+name,
			success:function(data){
				if(fl.isOk(data)){
					fl.alertOkAndReload("切换成功！");
				}
			}
		})
	})
})