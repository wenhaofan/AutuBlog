#sql("list")
	SELECT
	title,
	intro,
	identify,
	gmtCreate,
	Date_FORMAT( gmtCreate, '%Y年%m月%d日' ) AS TimelineGmtCreate 
FROM
	article 
WHERE
	Date_FORMAT( gmtCreate, '%Y年%m月%d日' ) IN (
	SELECT
		datestr 
	FROM
		(
		SELECT
			Date_FORMAT( gmtCreate, '%Y年%m月%d日' ) AS datestr 
		FROM
			article 
		where
			state=1
		GROUP BY
			Date_FORMAT( gmtCreate, '%Y年%m月%d日' ) 
		ORDER BY
			gmtCreate DESC 
			 limit #((pageNum-1 )*pageSize)  ,#(pageSize)
		)   AS datestrs  )
ORDER BY
	gmtCreate DESC
#end