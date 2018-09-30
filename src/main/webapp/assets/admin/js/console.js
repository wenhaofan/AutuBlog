
$(function(){

	var head= document.getElementsByTagName('head')[0]; 
	var script= document.createElement('script'); 
	script.type= 'text/javascript'; 
	script.onreadystatechange= function () { 
	if (this.readyState == 'complete') 
		lazyRender();
	} 
	script.onload= function(){ 
		lazyRender();
	} 
	script.src= '/assets/echarts/echarts.js'; 
	head.appendChild(script); 
	
})

function lazyRender(){
	renderAccessReport();
	renderArticleReport();
}

 function renderArticleReport(){
	var myChart = echarts.init(document.getElementById('bar'));

	fl.ajax({
		url:"/admin/api/statistic/hotArticle/10",
		success:function(data){
			// 指定图表的配置项和数据
			var option = {
			    title: {
			        text: '热门文章'
			    },
			    tooltip: {},   toolbox: {
			        feature: {
			            dataView: {},
			            saveAsImage: {
			                pixelRatio: 2
			            },
			            restore: {}
			        }
			    },
			    legend: {
			        data:['阅读量']
			    },
			    grid:{
			    	bottom:"30%"
			    },
			    color:'#66baf0',
			    xAxis: {
			        data: data.titleList,
			        axisLabel:{
	                    interval:0,
	                    rotate:45,
	                    margin:2,
	                     formatter:function(value)
	                    {
	                    	if(value.length>7){
	                    		return value.substring(0,7)+"...";
	                    	}
	                        return value;
	                    }}
			    },
			    yAxis: {},
			    series: [{
			        type: 'bar',
			        data:data.pvList,
			        barMaxWidth:40
			    }]
			};
			
			// 使用刚指定的配置项和数据显示图表。
			myChart.setOption(option);
	
			} 
	})
}
function renderAccessReport(){
	fl.ajax({
		url:"/admin/api/statistic/statisticsAccessNumDays/7",
		success:function(data){
				echarts.init(document.getElementById('line')).setOption({
				    title: {text: '7天访问量'},
				    tooltip: {},
				    toolbox: {
				        feature: {
				            dataView: {},
				            saveAsImage: {
				                pixelRatio: 2
				            },
				            restore: {}
				        }
				    }, color:'#66baf0',
				    xAxis: { 
				    	type: 'category',
				        data: data.dayList
				    }, grid:{
				    	bottom:"30%"
				    },
				    yAxis: {
				    	type:'value'
				    },
				    series: [{
				        type: 'line',
				        smooth: true,
				        data: data.numList
				    }]
				});
			}
	})
}


$(function(){
	
	fl.ajax({
		url:"/admin/api/article/listHot",
		success:function(data){
			$.each(data.list,function(index,item){
				var $dd=$(template("tpl-list-article",item));
				$("#article-list").append($dd);
			})
		}
	})
	
	fl.ajax({
		url:"/admin/api/comment/listRecent",
		success:function(data){
			$.each(data.list,function(index,item){
				var $dd=$(template("tpl-list-comment",item));
				$("#comment-list").append($dd);
			})
		}
	})
	fl.ajax({
		url:"/admin/api/sysLog/listRecent",
		success:function(data){
			$.each(data.logPage.list,function(index,item){
				 
					var $dd=$(template("tpl-list-sysLog",item));
					$("#sysLog-list").append($dd);
				 
			})
			
		}
	})
	window.statistic={
		countByDate:function(paras){
			$.ajax({
				url:"/admin/api/statistic/"+paras.methodName+"Num/",
				type:"post",
				data:paras.data,
				success:function(data){
					if(fl.isOk(data)){
						paras.success(data);
					}
				}
			})
		}
	}

	statistic.countByDate({
		methodName:"article",
		data:{},
		success:function(data){
			$(".article-total-num").text(data.count);
		}
	})
	statistic.countByDate({
		methodName:"comment",
		data:{},
		success:function(data){
			$(".comment-total-num").text(data.count);
		}
	})
	
	statistic.countByDate({
		methodName:"disk",
		data:{},
		success:function(data){
			$(".disk-total-num").text(data.count);
		}
	})
	
	statistic.countByDate({
		methodName:"comment",
		data:{gmtStart:dateUtil.format(dateUtil.getMonthFirst()),gmtEnd:dateUtil.format(dateUtil.getTodayEnd())},
		success:function(data){
			$(".comment-month-num").text(data.count);
		}
	})
	statistic.countByDate({
		methodName:"article",
		data:{gmtStart:dateUtil.format(dateUtil.getMonthFirst()),gmtEnd:dateUtil.format(dateUtil.getTodayEnd())},
		success:function(data){
			$(".article-month-num").text(data.count);
		}
	})

	statistic.countByDate({
		methodName:"disk",
		data:{gmtStart:dateUtil.format(dateUtil.getMonthFirst()),gmtEnd:dateUtil.format(dateUtil.getTodayEnd())},
		success:function(data){
			$(".disk-month-num").text(data.count);
		}
	})

});