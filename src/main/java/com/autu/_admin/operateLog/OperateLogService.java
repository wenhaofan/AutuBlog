package com.autu._admin.operateLog;

import com.autu.common.model.OperateLog;

public class OperateLogService {

	public boolean save(OperateLog log) {
		return log.save();
	}

}
