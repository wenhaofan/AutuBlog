var dateUtil={
 /**
  * 获取date月的第一天凌晨的时间，无参则默认当前月
  */
  getMonthFirst(date){
	  if(date==undefined) date=new Date();
	  return this.getWeeDate(new Date(date.setDate(1)));
  },
	/**
	 * 获取date时间的 天数*days的凌晨时间  例如date为9日 days为0则获取9日凌晨的时间
	 */
  getWeeDates (date,days) {
  	
  	var resultDate=this.getWeeDate(date);
  	if(days<=0){
  		return resultDate;
  	}
  	for(var i=0;i<days;i++){
  		resultDate=new Date(resultDate.getTime()-24*60*60*1000);
  	}
    return  resultDate;
  },
  /**
   * 获取date时间的凌晨时间
   */
  getWeeDate(date){
  	 return new Date(date.setHours(0, 0, 0, 0));
  },
  /**
   * 获取今天结束的时间
   */
  getTodayEnd () {
 	var todayTime =this.getWeeDate(new Date());
 	todayTime.setTime(todayTime.getTime()+24*60*60*1000);
 	return todayTime; 	
  },format(date,fmt) { //author: meizz 
	  if(fmt==undefined){
		  fmt=this.formatTpl
	  }
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
  },
  formatTpl:"yyyy-MM-dd hh:mm:ss"
}


