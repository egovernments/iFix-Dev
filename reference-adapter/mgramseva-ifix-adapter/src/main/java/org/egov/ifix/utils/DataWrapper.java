package org.egov.ifix.utils;

import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.exception.GenericCustomException;
import org.egov.ifix.models.ErrorDataModel;
import org.egov.ifix.models.FiscalEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.egov.ifix.utils.EventConstants.*;

@Slf4j
@Component
public class DataWrapper {

    @Autowired
    ApplicationConfiguration applicationConfiguration;

    /**
     * @param data
     * @param dataName
     * @param attributeName
     * @param attributeValue
     * @param errorType
     * @param status
     * @param message
     * @return
     */
    public Optional<ErrorDataModel> getErrorDataModel(String data, String dataName, String attributeName,
                                                      String attributeValue, String errorType, String status,
                                                      String message) {
        if (!StringUtils.isEmpty(data) && !StringUtils.isEmpty(dataName) && !StringUtils.isEmpty(attributeName)
                && !StringUtils.isEmpty(attributeValue) && !StringUtils.isEmpty(status)) {

            ErrorDataModel errorDataModel = new ErrorDataModel();
            errorDataModel.setData(data);
            errorDataModel.setDataName(dataName);
            errorDataModel.setAttributeName(attributeName);
            errorDataModel.setAttributeValue(attributeValue);
            errorDataModel.setErrorType(errorType);
            errorDataModel.setStatus(status);

            if (!StringUtils.isEmpty(message)) {
                errorDataModel.setMessage(message);
            } else {
                errorDataModel.setMessage(NA);
            }

            errorDataModel.setOrigin(APPLICATION_NAME);
            errorDataModel.setDestination(DESTINATION_APP_NAME);
            errorDataModel.setCreatedBy(APPLICATION_NAME);
            errorDataModel.setCreatedTime(System.currentTimeMillis());

            return Optional.ofNullable(errorDataModel);
        }

        return Optional.empty();
    }

    /**
     * @param fiscalEventList
     * @return
     */
    public String getFiscalEventDetailsInformation(List<FiscalEvent> fiscalEventList) {
        StringBuilder fiscalMessage = new StringBuilder();

        if (fiscalEventList != null && !fiscalEventList.isEmpty()) {
            AtomicInteger index = new AtomicInteger(0);

            fiscalEventList.stream()
                    .forEach(fiscalEvent -> {
                        fiscalMessage.append("[ index " + index);
                        fiscalMessage.append(" Tenant: " + fiscalEvent.getTenantId());
                        fiscalMessage.append(" Project Id: " + fiscalEvent.getProjectId());
                        fiscalMessage.append(" Event Type: " + fiscalEvent.getEventType());
                        fiscalMessage.append(" Amount Details: " + fiscalEvent.getAmountDetails());
                        fiscalMessage.append(" ] ");
                    });
        }

        return fiscalMessage.toString();
    }

    /**
     * It calculates time from cron job expression <strong>samplejob.frequency</strong>.
     * It also check expression length - which is strict 6.
     * It does not throw exception in case of length is greater 6.
     *
     * @return it returns in millisecond with additional overlap minutes.
     */
    public @NotNull Long translateCronExpressionIntoMilliSecond() {
        String plainTime = applicationConfiguration.getIfixFiscalEventSearchTimeOverlapMinutes();

        if (!org.apache.commons.lang3.StringUtils.isNumeric(plainTime)) {
            log.error(">>>>> Ifix fiscal event search overlap time value is invalid");
            throw new GenericCustomException(TIME, "Ifix fiscal event search overlap time value is invalid");
        }else {
            Long overlapTime = Long.parseLong(plainTime) * 60 * 1000;

            String cronExpression = getValidatedCronExpression(applicationConfiguration.getSampleJobFrequency());

            CronSequenceGenerator generator = new CronSequenceGenerator(cronExpression);
            Date nextRunTime = generator.next(new Date());

            Date nextToNextExecution = generator.next(nextRunTime);
            Duration durationBetweenExecutions = Duration.between(nextRunTime.toInstant(),
                    nextToNextExecution.toInstant());

            return durationBetweenExecutions.toMillis() + overlapTime;
        }
    }


    /**
     * @param expression It can't be null, before breaking this condition cron job will crash.
     * @return
     */
    private String getValidatedCronExpression(@NonNull String expression) {
        StringBuilder cronExpBuilder = new StringBuilder();
        String[] fields = StringUtils.tokenizeToStringArray(expression, " ");

        if (fields.length > 6) {
            log.warn(">>>>> Cron expression elements are more than six and we are considering only six element other " +
                    "elements will be ignored");

            for (int expIndex = 0; expIndex < fields.length - 1; expIndex++) {
                if (expIndex < 5) {
                    cronExpBuilder = cronExpBuilder.append(fields[expIndex] + " ");
                }else {
                    cronExpBuilder = cronExpBuilder.append(fields[expIndex]);
                }
            }
        }else {
            cronExpBuilder = cronExpBuilder.append(expression);
        }

        return cronExpBuilder.toString();
    }

}
