package org.egov.ifix.service;

import com.google.gson.JsonObject;

public interface ProjectService {
    String getResolvedProjectId(String projectCode, JsonObject jsonObjectData);
}
