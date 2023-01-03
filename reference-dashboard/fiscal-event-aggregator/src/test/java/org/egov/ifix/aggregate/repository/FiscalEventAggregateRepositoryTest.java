package org.egov.ifix.aggregate.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Application;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
class FiscalEventAggregateRepositoryTest {

    @InjectMocks
    private FiscalEventAggregateRepository fiscalEventAggregateRepository;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    void testUpsert() {
        when(this.namedParameterJdbcTemplate.batchUpdate((String) any(),
                (org.springframework.jdbc.core.namedparam.SqlParameterSource[]) any())).thenReturn(new int[]{1, 1, 1, 1});
        int[] actualUpsertResult = this.fiscalEventAggregateRepository.upsert(new ArrayList<>());
        assertEquals(4, actualUpsertResult.length);
        assertEquals(1, actualUpsertResult[0]);
        assertEquals(1, actualUpsertResult[3]);
        verify(this.namedParameterJdbcTemplate).batchUpdate((String) any(),
                (org.springframework.jdbc.core.namedparam.SqlParameterSource[]) any());
    }
}

