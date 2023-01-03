package org.egov.ifix.controller;

import com.google.gson.Gson;
import org.egov.ifix.models.CoaMappingDTO;
import org.egov.ifix.repository.FiscalEventRepository;
import org.egov.ifix.repository.MdmsRepository;
import org.egov.ifix.repository.MgramsevaChallanRepository;
import org.egov.ifix.service.ChartOfAccountService;
import org.egov.ifix.service.PspclEventService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@WebMvcTest(MasterDataController.class)
class MasterDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChartOfAccountService chartOfAccountService;

    @MockBean
    private MgramsevaChallanRepository mgramSevaChallanRepository;

    @MockBean
    FiscalEventRepository fiscalEventRepository;

    @MockBean
    MdmsRepository mdmsRepository;

    @MockBean
    private PspclEventService pspclEventService;



    private CoaMappingDTO coaMappingDTO;

    private Optional<CoaMappingDTO> coaMappingDTOOptional;

    private String coaMapping;

    @BeforeAll
    void init() throws IOException {
        coaMappingDTO = new CoaMappingDTO();
        coaMappingDTO.setIFixCoaCode("2215-01-001-00-00-93");
        coaMappingDTO.setClientCode("20101");

        coaMapping = new Gson().toJson(coaMappingDTO);

        coaMappingDTOOptional = Optional.ofNullable(coaMappingDTO);
    }

    @Test
    void coaMappingSearch()  {
        doReturn(coaMappingDTOOptional).when(chartOfAccountService).getMappedCoaIdByClientCoaCode(coaMappingDTO);

        try {
            mockMvc.perform(post("/master/v1/mapping/coa/search")
                            .accept(MediaType.APPLICATION_JSON).content(coaMapping)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isAccepted());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}