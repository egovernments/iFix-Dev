package org.egov.service;

import org.egov.config.IfixDepartmentEntityConfig;
import org.egov.config.TestDataFormatter;
import org.egov.repository.DepartmentEntityRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.DepartmentEntityAncestryUtil;
import org.egov.validator.DepartmentEntityValidator;
import org.egov.web.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class DepartmentEntityServiceTest {

    @Mock
    private DepartmentEntityAncestryUtil departmentEntityAncestryUtil;

    @Mock
    private DepartmentEntityEnrichmentService departmentEntityEnrichmentService;

    @Mock
    private DepartmentEntityRepository departmentEntityRepository;

    @InjectMocks
    private DepartmentEntityService departmentEntityService;

    @Mock
    private DepartmentEntityValidator departmentEntityValidator;

    @Mock
    private IfixDepartmentEntityConfig ifixDepartmentEntityConfig;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private DepartmentEntityRequest departmentEntityRequest;
    private DepartmentEntity departmentEntity;
    private DepartmentEntitySearchRequest departmentEntitySearchRequest;
    private DepartmentEntityResponse searchDepartmentEntityResponse;
    private DepartmentEntityRequest departmentEntityUpdateRequest;
    private DepartmentEntity updateDepartmentEntity;

    @BeforeAll
    void init() throws IOException {
        departmentEntityRequest = testDataFormatter.getDeptEntityCreateRequestData();
        departmentEntity = departmentEntityRequest.getDepartmentEntity();
        departmentEntitySearchRequest = testDataFormatter.getDeptEntitySearchRequestData();
        searchDepartmentEntityResponse = testDataFormatter.getDeptEntitySearchResponseData();

        departmentEntityUpdateRequest = testDataFormatter.getDeptEntityUpdateRequestData();
        updateDepartmentEntity = departmentEntityUpdateRequest.getDepartmentEntity();
    }

    @Test
    void testCreateDepartmentEntityWithDefaultDeptEntityRequest() {
        doNothing().when(this.departmentEntityValidator).validateDepartmentEntityRequest((DepartmentEntityRequest) any());
        doNothing().when(this.departmentEntityRepository).save((org.egov.web.models.DepartmentEntity) any());
        doNothing().when(this.departmentEntityEnrichmentService)
                .enrichDepartmentEntityData((DepartmentEntityRequest) any());
        DepartmentEntityRequest departmentEntityRequest = new DepartmentEntityRequest();
        assertSame(departmentEntityRequest, this.departmentEntityService.createDepartmentEntity(departmentEntityRequest));
        verify(this.departmentEntityValidator).validateDepartmentEntityRequest((DepartmentEntityRequest) any());
        verify(this.departmentEntityRepository).save((org.egov.web.models.DepartmentEntity) any());
        verify(this.departmentEntityEnrichmentService).enrichDepartmentEntityData((DepartmentEntityRequest) any());
    }

    @Test
    void testCreateDepartmentEntityWithDeptEntityRequest() {
        doNothing().when(this.departmentEntityValidator).validateDepartmentEntityRequest((DepartmentEntityRequest) any());
        doNothing().when(this.departmentEntityRepository).save((org.egov.web.models.DepartmentEntity) any());
        doNothing().when(this.departmentEntityEnrichmentService)
                .enrichDepartmentEntityData((DepartmentEntityRequest) any());
        assertSame(departmentEntityRequest, this.departmentEntityService.createDepartmentEntity(departmentEntityRequest));
        verify(this.departmentEntityValidator).validateDepartmentEntityRequest((DepartmentEntityRequest) any());
        verify(this.departmentEntityRepository).save((org.egov.web.models.DepartmentEntity) any());
        verify(this.departmentEntityEnrichmentService).enrichDepartmentEntityData((DepartmentEntityRequest) any());
    }

    @Test
    void testFindAllByCriteriaWithEmptySearchCriteria() {
        doNothing().when(this.departmentEntityValidator)
                .validateSearchDepartmentEntity((DepartmentEntitySearchRequest) any());
        ArrayList<DepartmentEntity> departmentEntityList = new ArrayList<DepartmentEntity>();
        when(this.departmentEntityRepository.searchEntity((DepartmentEntitySearchRequest) any()))
                .thenReturn(departmentEntityList);

        DepartmentEntitySearchRequest departmentEntitySearchRequest = new DepartmentEntitySearchRequest();
        departmentEntitySearchRequest.setCriteria(new DepartmentEntitySearchCriteria());
        List<? extends DepartmentEntityAbstract> actualFindAllByCriteriaResult = this.departmentEntityService
                .findAllByCriteria(departmentEntitySearchRequest);
        assertSame(departmentEntityList, actualFindAllByCriteriaResult);
        assertTrue(actualFindAllByCriteriaResult.isEmpty());
        verify(this.departmentEntityValidator).validateSearchDepartmentEntity((DepartmentEntitySearchRequest) any());
        verify(this.departmentEntityRepository).searchEntity((DepartmentEntitySearchRequest) any());
    }

    @Test
    void testFindAllByCriteriaWithEmptySearchResult() {
        doNothing().when(this.departmentEntityValidator)
                .validateSearchDepartmentEntity((DepartmentEntitySearchRequest) any());
        when(this.departmentEntityRepository.searchEntity((DepartmentEntitySearchRequest) any()))
                .thenReturn(new ArrayList<DepartmentEntity>());

        assertTrue(this.departmentEntityService.findAllByCriteria(departmentEntitySearchRequest).isEmpty());
        verify(this.departmentEntityValidator).validateSearchDepartmentEntity((DepartmentEntitySearchRequest) any());
        verify(this.departmentEntityRepository).searchEntity((DepartmentEntitySearchRequest) any());
    }

    @Test
    void testFindAllByCriteriaWithError() {
        when(this.ifixDepartmentEntityConfig.getMaximumSupportedDepartmentHierarchy()).thenReturn(3);
        doNothing().when(this.departmentEntityValidator)
                .validateSearchDepartmentEntity((DepartmentEntitySearchRequest) any());

        ArrayList<DepartmentEntity> departmentEntityList = new ArrayList<DepartmentEntity>();
        departmentEntityList.add(new DepartmentEntity());
        when(this.departmentEntityRepository.getParent((String) any())).thenReturn(new DepartmentEntity());
        when(this.departmentEntityRepository.searchEntity((DepartmentEntitySearchRequest) any()))
                .thenReturn(departmentEntityList);
        when(this.departmentEntityAncestryUtil.createDepartmentEntityAncestryFromDepartmentEntity((DepartmentEntity) any()))
                .thenReturn(new DepartmentEntityAncestry());
        departmentEntitySearchRequest.getCriteria().setGetAncestry(true);
        assertThrows(CustomException.class,
                () -> this.departmentEntityService.findAllByCriteria(departmentEntitySearchRequest));
        verify(this.ifixDepartmentEntityConfig).getMaximumSupportedDepartmentHierarchy();
        verify(this.departmentEntityValidator).validateSearchDepartmentEntity((DepartmentEntitySearchRequest) any());
        verify(this.departmentEntityRepository, atLeast(1)).getParent((String) any());
        verify(this.departmentEntityRepository).searchEntity((DepartmentEntitySearchRequest) any());
        verify(this.departmentEntityAncestryUtil, atLeast(1))
                .createDepartmentEntityAncestryFromDepartmentEntity((DepartmentEntity) any());
    }

    @Test
    void testFindAllByCriteriaWithSearchResult() {
        doNothing().when(this.departmentEntityValidator)
                .validateSearchDepartmentEntity((DepartmentEntitySearchRequest) any());

        ArrayList<DepartmentEntity> departmentEntityList = new ArrayList<DepartmentEntity>();
        departmentEntityList.add(new DepartmentEntity());
        when(this.departmentEntityRepository.getParent((String) any())).thenReturn(null);
        when(this.departmentEntityRepository.searchEntity((DepartmentEntitySearchRequest) any()))
                .thenReturn(departmentEntityList);
        when(this.departmentEntityAncestryUtil.createDepartmentEntityAncestryFromDepartmentEntity((DepartmentEntity) any()))
                .thenReturn(new DepartmentEntityAncestry());

        assertEquals(1, this.departmentEntityService.findAllByCriteria(departmentEntitySearchRequest).size());
        verify(this.departmentEntityValidator).validateSearchDepartmentEntity((DepartmentEntitySearchRequest) any());
        verify(this.departmentEntityRepository).searchEntity((DepartmentEntitySearchRequest) any());
    }

    @Test
    void testFindAllByCriteriaWithParentAndError() {
        when(this.ifixDepartmentEntityConfig.getMaximumSupportedDepartmentHierarchy()).thenReturn(3);
        doNothing().when(this.departmentEntityValidator)
                .validateSearchDepartmentEntity((DepartmentEntitySearchRequest) any());

        ArrayList<DepartmentEntity> departmentEntityList = new ArrayList<DepartmentEntity>();
        departmentEntityList.add(new DepartmentEntity());
        when(this.departmentEntityRepository.getParent((String) any())).thenReturn(new DepartmentEntity());
        when(this.departmentEntityRepository.searchEntity((DepartmentEntitySearchRequest) any()))
                .thenReturn(departmentEntityList);
        DepartmentEntityAncestry departmentEntityAncestry = mock(DepartmentEntityAncestry.class);
        when(departmentEntityAncestry.getChildren()).thenReturn(new ArrayList<DepartmentEntityAncestry>());
        when(this.departmentEntityAncestryUtil.createDepartmentEntityAncestryFromDepartmentEntity((DepartmentEntity) any()))
                .thenReturn(departmentEntityAncestry);
        departmentEntitySearchRequest.getCriteria().setGetAncestry(true);
        assertThrows(CustomException.class,
                () -> this.departmentEntityService.findAllByCriteria(departmentEntitySearchRequest));
        verify(this.ifixDepartmentEntityConfig).getMaximumSupportedDepartmentHierarchy();
        verify(this.departmentEntityValidator).validateSearchDepartmentEntity((DepartmentEntitySearchRequest) any());
        verify(this.departmentEntityRepository, atLeast(1)).getParent((String) any());
        verify(this.departmentEntityRepository).searchEntity((DepartmentEntitySearchRequest) any());
        verify(this.departmentEntityAncestryUtil, atLeast(1))
                .createDepartmentEntityAncestryFromDepartmentEntity((DepartmentEntity) any());
        verify(departmentEntityAncestry, atLeast(1)).getChildren();
    }

    @Test
    void testCreateAncestryForWithParentNotNull() {
        when(this.ifixDepartmentEntityConfig.getMaximumSupportedDepartmentHierarchy()).thenReturn(3);
        when(this.departmentEntityRepository.getParent((String) any())).thenReturn(new DepartmentEntity());
        when(this.departmentEntityAncestryUtil.createDepartmentEntityAncestryFromDepartmentEntity((DepartmentEntity) any()))
                .thenReturn(new DepartmentEntityAncestry());
        assertThrows(CustomException.class, () -> this.departmentEntityService.createAncestryFor(new DepartmentEntity()));
        verify(this.ifixDepartmentEntityConfig).getMaximumSupportedDepartmentHierarchy();
        verify(this.departmentEntityRepository, atLeast(1)).getParent((String) any());
        verify(this.departmentEntityAncestryUtil, atLeast(1))
                .createDepartmentEntityAncestryFromDepartmentEntity((DepartmentEntity) any());
    }

    @Test
    void testCreateAncestryForWithNullParent() {
        when(this.ifixDepartmentEntityConfig.getMaximumSupportedDepartmentHierarchy()).thenReturn(3);
        when(this.departmentEntityRepository.getParent((String) any())).thenReturn(null);
        DepartmentEntityAncestry departmentEntityAncestry = new DepartmentEntityAncestry();
        when(this.departmentEntityAncestryUtil.createDepartmentEntityAncestryFromDepartmentEntity((DepartmentEntity) any()))
                .thenReturn(departmentEntityAncestry);
        assertSame(departmentEntityAncestry, this.departmentEntityService.createAncestryFor(departmentEntity));
        verify(this.ifixDepartmentEntityConfig).getMaximumSupportedDepartmentHierarchy();
        verify(this.departmentEntityRepository).getParent((String) any());
        verify(this.departmentEntityAncestryUtil)
                .createDepartmentEntityAncestryFromDepartmentEntity((DepartmentEntity) any());
    }

    @Test
    void testCreateAncestryForWithChildrenList() {
        when(this.ifixDepartmentEntityConfig.getMaximumSupportedDepartmentHierarchy()).thenReturn(3);
        when(this.departmentEntityRepository.getParent((String) any())).thenReturn(new DepartmentEntity());
        DepartmentEntityAncestry departmentEntityAncestry = mock(DepartmentEntityAncestry.class);
        when(departmentEntityAncestry.getChildren()).thenReturn(new ArrayList<DepartmentEntityAncestry>());
        when(this.departmentEntityAncestryUtil.createDepartmentEntityAncestryFromDepartmentEntity((DepartmentEntity) any()))
                .thenReturn(departmentEntityAncestry);
        assertThrows(CustomException.class, () -> this.departmentEntityService.createAncestryFor(new DepartmentEntity()));
        verify(this.ifixDepartmentEntityConfig).getMaximumSupportedDepartmentHierarchy();
        verify(this.departmentEntityRepository, atLeast(1)).getParent((String) any());
        verify(this.departmentEntityAncestryUtil, atLeast(1))
                .createDepartmentEntityAncestryFromDepartmentEntity((DepartmentEntity) any());
        verify(departmentEntityAncestry, atLeast(1)).getChildren();
    }


    @Test
    void testUpdateDepartmentEntityWithDeptEntityRequest() {
        doNothing().when(this.departmentEntityValidator)
                .validateUpdateDepartmentEntityRequest((DepartmentEntityRequest) any());

        Optional<DepartmentEntity> departmentEntityOptional = Optional.ofNullable(updateDepartmentEntity);

        doReturn(departmentEntityOptional).when(departmentEntityRepository).findById(anyString());

        doNothing().when(departmentEntityRepository).save(departmentEntity);

        assertSame(departmentEntityUpdateRequest, this.departmentEntityService.updateDepartmentEntity(departmentEntityUpdateRequest));
    }
}

