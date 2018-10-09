package com.autu._admin.statistic;

import java.util.Date;

import com.autu.common.controller.BaseController;
import com.jfinal.aop.Inject;

public class StatisticApi extends BaseController{

	@Inject
	private StatisticAccessLogService statisticAccessLogService;
	
	@Inject
	private StatisticArticleService statisticArticleService;

	@Inject
	private StatisticsService statisticsService;
	
	public void statisticsAccessNumDays() {
		renderJson(statisticAccessLogService.accessNum(getParaToInt()));
	}
	/**
	 * 获取热门文章
	 */
	public void hotArticle() {
		renderJson(statisticArticleService.hotArticle(getParaToInt()));
	}
	/**
	 * 根据时间区间统计文章数
	 */
	public void articleNum() {
		Date gmtStart=getParaToDate("gmtStart");
		Date gmtEnd=getParaToDate("gmtEnd");
		renderJson(statisticsService.countByDate("article", gmtStart, gmtEnd));
	}
	/**
	 * 根据时间区间获取评论数
	 */
	public void commentNum() {
		Date gmtStart=getParaToDate("gmtStart");
		Date gmtEnd=getParaToDate("gmtEnd");
		renderJson(statisticsService.countByDate("comment", gmtStart, gmtEnd));
	}
	/**
	 * 根据时间区间获取附件数
	 */
	public void diskNum() {
		Date gmtStart=getParaToDate("gmtStart");
		Date gmtEnd=getParaToDate("gmtEnd");
		renderJson(statisticsService.countByDate("disk", gmtStart, gmtEnd));
	}
	
}
