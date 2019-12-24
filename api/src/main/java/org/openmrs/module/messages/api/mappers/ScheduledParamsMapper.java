package org.openmrs.module.messages.api.mappers;

import com.google.gson.Gson;
import org.apache.commons.lang.NotImplementedException;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.model.ScheduledServiceParameter;
import org.openmrs.module.messages.api.util.MapperUtil;

import java.util.ArrayList;
import java.util.List;

public class ScheduledParamsMapper implements ListMapper<ServiceResult, ScheduledServiceParameter> {

    @Override
    public ServiceResult toDto(List<ScheduledServiceParameter> daos) {
        throw new NotImplementedException("mapping from List<ScheduledServiceParameter> to ServiceResult " +
                "is not implemented yet");
    }

    @Override
    public List<ScheduledServiceParameter> fromDto(ServiceResult dao) {
        List<ScheduledServiceParameter> result = new ArrayList<>();
        Gson gson = MapperUtil.getGson();

        for (String key : dao.getAdditionalParams().keySet()) {
            result.add(new ScheduledServiceParameter(key, gson.toJson(dao.getAdditionalParams().get(key))));
        }

        return result;
    }
}
