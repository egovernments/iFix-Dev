package org.egov.kafka;

import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;

public class HashMapDeserializer extends JsonDeserializer<HashMap> {

    public HashMapDeserializer() {
        super(HashMap.class);
    }


}


