package org.openmrs.module.messages.api.mappers;

import com.google.gson.Gson;
import org.apache.commons.lang.NotImplementedException;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.model.ScheduledServiceParameter;
import org.openmrs.module.messages.api.util.MapperUtil;
import org.springframework.beans.BeanUtils;
import java.util.ArrayList;
import java.util.List;

public class ScheduledParamsMapper implements ListMapper<ServiceResult, ScheduledServiceParameter> {

    private Gson gson = MapperUtil.getGson();

    @Override
    public ServiceResult toDto(List<ScheduledServiceParameter> daos) {
        throw new NotImplementedException("mapping from List<ScheduledServiceParameter> to ServiceResult " +
                "is not implemented yet");
    }

    @Override
    public List<ScheduledServiceParameter> fromDto(ServiceResult dao) {
        List<ScheduledServiceParameter> result = new ArrayList<>();

        for (String key : dao.getAdditionalParams().keySet()) {
            Object value = dao.getAdditionalParams().get(key);
            if (!BeanUtils.isSimpleValueType(value.getClass())) { // only complex value should be serialized
                value = gson.toJson(value);
            }
            result.add(new ScheduledServiceParameter(key, value.toString()));
        }

        return result;
    }
}
