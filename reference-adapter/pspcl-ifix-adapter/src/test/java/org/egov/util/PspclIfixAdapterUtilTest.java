package org.egov.util;

import org.egov.config.PspclIfixAdapterConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PspclIfixAdapterUtilTest {

    @Mock
    private PspclIfixAdapterConfiguration pspclIfixAdapterConfiguration;

    private PspclIfixAdapterUtil pspclIfixAdapterUtil;

    @Spy
    private ResourceLoader resourceLoader;

    @BeforeEach
    private void init() throws IOException {
        MockitoAnnotations.openMocks(this);
        pspclIfixAdapterUtil = new PspclIfixAdapterUtil(resourceLoader, pspclIfixAdapterConfiguration);
    }

    @Test
    void testGetFileAsStringWithUnknownPath() {
        assertEquals("", this.pspclIfixAdapterUtil.getFileAsString("/directory/foo.txt"));
    }

    @Test
    void testGetFileAsStringWithEmptyPath() {
        assertEquals("", this.pspclIfixAdapterUtil.getFileAsString(""));
    }

    @Test
    void testFormatWithStringDate() {
        Date actualDate = this.pspclIfixAdapterUtil.format(PspclIfixAdapterConstant.DEFAULT_DATE_FORMAT, "2020-03-01");
        assertEquals("2020-03-01", (new SimpleDateFormat(PspclIfixAdapterConstant.DEFAULT_DATE_FORMAT)).format(actualDate));
    }

    @Test
    void testFormatWithDate() {
        Date dt = new Date("1/14/2021 7:46:01 PM");
        Date actualDate = this.pspclIfixAdapterUtil.format(PspclIfixAdapterConstant.TXN_DATE_FORMAT, dt);
        assertNotNull(actualDate);
    }


    @Test
    void testFormatWithStringDateAndEmptyFormat() {
        Date actualDate = this.pspclIfixAdapterUtil.format("", "2020-03-01");
        assertEquals("2020-03-01", (new SimpleDateFormat(PspclIfixAdapterConstant.DEFAULT_DATE_FORMAT)).format(actualDate));
    }

    @Test
    void testFormatWithDateAndEmptyFormat() {
        Date dt = new Date("1/14/2021 7:46:01 PM");
        assertThrows(Exception.class,()->this.pspclIfixAdapterUtil.format("", dt));
    }

    @Test
    void testFormatWithStringDateAndInvalidFormat() {
        assertNull(this.pspclIfixAdapterUtil.format("42", "2020-03-01"));
    }

    @Test
    void testFormatWithInvalidStringDate() {
        assertNull(this.pspclIfixAdapterUtil.format("Format", ""));
    }
}

