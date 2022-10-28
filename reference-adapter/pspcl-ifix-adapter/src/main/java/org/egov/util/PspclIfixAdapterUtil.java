package org.egov.util;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.config.PspclIfixAdapterConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.egov.util.PspclIfixAdapterConstant.DEFAULT_DATE_FORMAT;

@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class PspclIfixAdapterUtil {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private PspclIfixAdapterConfiguration adapterConfiguration;

    public String getFileAsString(String filePath) {
        String reqFetchPspclBill = "";
        try {
            if (StringUtils.isBlank(filePath))
                return reqFetchPspclBill;

            if (PspclIfixAdapterUtil.class.getClassLoader().getResourceAsStream(filePath) != null) {
                reqFetchPspclBill = IOUtils.toString(PspclIfixAdapterUtil.class.getClassLoader().getResourceAsStream(filePath), StandardCharsets.UTF_8);
            }
        } catch (Exception ex) {
            log.error("Exception occurred while reading the file from filePath : {}", filePath, ex);
        }
        return reqFetchPspclBill;
    }

    public Date format(String format, String date) {
        String dateFormat = DEFAULT_DATE_FORMAT;
        if (StringUtils.isNotBlank(format)) {
            dateFormat = format;
        }
        if (StringUtils.isNotBlank(date)) {
            DateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
            try {
                return (dateFormatter.parse(date));
            } catch (ParseException e) {
                log.error("Exception occurred while formatting the date : {}", date, e);
            }
        }
        return null;
    }

    public Date format(String format, Date date) {
        String dateFormat = DEFAULT_DATE_FORMAT;
        if (StringUtils.isNotBlank(format)) {
            dateFormat = format;
        }
        if (date != null) {
            DateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
            return (new Date(dateFormatter.format(date)));
        }
        return null;
    }
}
