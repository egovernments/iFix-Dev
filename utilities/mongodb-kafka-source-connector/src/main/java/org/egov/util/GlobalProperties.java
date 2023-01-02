package org.egov.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class GlobalProperties {

    @Value("${mongodb.collection.name}")
    private String mongoDBCollectionName;

    @Value("${kafka.topic}")
    private String kafkaTopic;
}
