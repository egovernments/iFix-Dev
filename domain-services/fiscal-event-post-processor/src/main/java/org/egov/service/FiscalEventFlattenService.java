package org.egov.service;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.egov.web.models.FiscalEventLineItemUnbundled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class FiscalEventFlattenService {

    /**
     * @param eventLineItemUnbundledList
     * @return
     */
    public List<String> getFlattenData(List<FiscalEventLineItemUnbundled> eventLineItemUnbundledList) {
        if (eventLineItemUnbundledList != null && !eventLineItemUnbundledList.isEmpty()) {
            List<String> flattenDataList = new ArrayList<>();

            for (FiscalEventLineItemUnbundled lineItemUnbundled : eventLineItemUnbundledList) {
                flattenDataList.add(JsonFlattener.flatten(new Gson().toJson(lineItemUnbundled)));
            }
            return flattenDataList;
        }

        return Collections.emptyList();
    }
}
