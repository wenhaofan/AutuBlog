template.defaults.imports.formatMillieSecond = function(millieSecond, format){
	return dateKit.formatMillieSecond(millieSecond,format);
};

var dateKit={
		formatMillieSecond:function(millieSecond,fmt){
			var date=new Date(millieSecond);
			return this.format(date,fmt);
		},
		formatTodayOrBefor:	function (dateStr){
			var agoDate=new Date(dateStr);
			var time = agoDate.getTime() / 1000;  
		    var now = new Date().getTime() / 1000;  
		    var ago = now - time; 
		    if(ago<24*60*60){
		    	return  this.format(agoDate,"hh:mm:ss");
		    }
		    return this.format(agoDate,"MM-dd HH:mm");
		},
		format:function  (date,fmt) { //author: meizz 
		    var o = {
		            "M+": date.getMonth() + 1, //月份 
		            "d+": date.getDate(), //日 
		            "h+": date.getHours(), //小时 
		            "m+": date.getMinutes(), //分 
		            "s+": date.getSeconds(), //秒 
		            "q+": Math.floor((date.getMonth() + 3) / 3), //季度 
		            "S": date.getMilliseconds() //毫秒 
		        };
		        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
		        for (var k in o)
		        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		        return fmt;
	}
}