package org.egov.ifix.mapper;

import java.util.List;

import org.egov.ifix.models.FiscalEvent;

import com.google.gson.JsonObject;

public interface EventMapper {
	
	public String getEventType();
	
	public List<FiscalEvent> transformData(JsonObject data);

}
