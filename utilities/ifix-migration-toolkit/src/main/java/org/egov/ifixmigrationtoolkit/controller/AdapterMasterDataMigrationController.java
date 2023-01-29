package org.egov.ifixmigrationtoolkit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.ifixmigrationtoolkit.models.MigrationRequest;
import org.egov.ifixmigrationtoolkit.service.AdapterMasterDataMigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class AdapterMasterDataMigrationController {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AdapterMasterDataMigrationService adapterMasterDataMigrationService;

    @RequestMapping(value="/department/v1/_migrate", method = RequestMethod.POST)
    public ResponseEntity<?> migrateDepartmentToPostgres(@RequestBody @Valid MigrationRequest request) throws JsonProcessingException {
        adapterMasterDataMigrationService.migrateDepartment(request);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("SUCCESS", "Department data migration job created successfully");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @RequestMapping(value="/expenditure/v1/_migrate", method = RequestMethod.POST)
    public ResponseEntity<?> migrateExpenditureToPostgres(@RequestBody @Valid MigrationRequest request) throws JsonProcessingException {
//        adapterMasterDataMigrationService.migrateExpenditure(request);
//        Map<String, Object> responseMap = new HashMap<>();
//        responseMap.put("SUCCESS", "Expenditure data migration job created successfully");
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value="/project/v1/_migrate", method = RequestMethod.POST)
    public ResponseEntity<?> migrateProjectToPostgres(@RequestBody @Valid MigrationRequest request) throws JsonProcessingException {
//        adapterMasterDataMigrationService.migrateProject(request);
//        Map<String, Object> responseMap = new HashMap<>();
//        responseMap.put("SUCCESS", "Project data migration job created successfully");
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
