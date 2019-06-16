
package com.autu.common.model;

import com.autu.common._config.BlogConfig;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;

import javax.sql.DataSource;

/**
 * Model、BaseModel、_MappingKit 生成器
 */
public class _Generator {

	/**
	 * 部分功能使用 Db + Record 模式实现，无需生成 model 的 table 在此配置
	 */
	private static String[] excludedTable = {
			"news_feed_reply",  // 暂不实现该功能
			"project_page_view", "share_page_view", "feedback_page_view",
			"login_log",
			"sensitive_words",
			"upload_counter",
			"task_run_log",
			"message_tip",
			"friend",
            "project_like", "share_like", "feedback_like",
            "share_reply_like", "feedback_reply_like",
            "like_message_log"
	};

	/**
	 * 重用 JFinalClubConfig 中的数据源配置，避免冗余配置
	 */
	public static DataSource getDataSource() {
		DruidPlugin druidPlugin = BlogConfig.getDruidPlugin();
		druidPlugin.start();
		return druidPlugin.getDataSource();
	}

	public static void main(String[] args) {
		// base model 所使用的包名
		String baseModelPackageName = "tpl.com.wenhaofan.common.model.base";
		// base model 文件保存路径
		String baseModelOutputDir = PathKit.getWebRootPath()
				+ "/src/main/java/tpl/com/wenhaofan/common/model/base";

		System.out.println("输出路径："+ baseModelOutputDir);

		// model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = "tpl.com.wenhaofan.common.model";
		// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = baseModelOutputDir + "/..";

		DataSource dataSource=getDataSource();
		// 创建生成器
		Generator gen = new Generator(dataSource, baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
		// 设置数据库方言
		gen.setDialect(new MysqlDialect());
		gen.setGenerateChainSetter(true);
		// 添加不需要生成的表名
		for (String table : excludedTable) {
			gen.addExcludedTable(table);
		}
		// 设置是否在 Model 中生成 dao 对象
		gen.setGenerateDaoInModel(false);
		// 设置是否生成字典文件
		gen.setGenerateDataDictionary(false);
		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
		// gernerator.setRemovedTableNamePrefixes("t_");
		// 生成
		gen.setMetaBuilder(new Metabuilder(dataSource));
		gen.generate();
	}
}
