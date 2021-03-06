package com.autu.common.generate;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.jfinal.plugin.activerecord.generator.MetaBuilder;

public class Metabuilder extends MetaBuilder {

	@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
	private List<String> tables=new ArrayList(){{
		add("survery");
		add("survery_option");
		add("survery_question");
		add("user_survery_option");
	}};
	
	public Metabuilder(DataSource dataSource) {
		super(dataSource);
		// TODO Auto-generated constructor stub
	}
	 @Override
	    protected boolean isSkipTable(String tableName) {
	        return !tables.contains(tableName);
	    }
}
