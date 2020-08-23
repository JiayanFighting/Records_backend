package com.demo.records.utils;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;


@Data
public class Query extends LinkedHashMap<String, Object> {
	final int DEFAULT_OFFSET = 0;
	final int DEFAULT_LIMIT = 100;
	private int offset;
	private int limit;

	public Query(Map<String, Object> params) {
		this.putAll(params);
		this.offset = (params.get("offset") == null || params.get("offset") =="") ? DEFAULT_OFFSET:Integer.parseInt(params.get("offset").toString());
		this.limit = (params.get("limit") == null || params.get("limit") =="") ? DEFAULT_LIMIT:Integer.parseInt(params.get("limit").toString());
		this.put("offset", offset);
		this.put("page", offset / limit + 1);
		this.put("limit", limit);
	}
}
