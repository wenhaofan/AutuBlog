package com.autu.common.log;

import com.autu.common.model.entity.AccessLog;

public class AccessLogService {

	public void add(AccessLog log) {
		log.save();
	}
}
