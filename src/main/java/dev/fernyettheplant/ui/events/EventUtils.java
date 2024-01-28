package dev.fernyettheplant.ui.events;

import static io.cloudevents.core.CloudEventUtils.mapData;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.provider.EventFormatProvider;
import io.cloudevents.jackson.JsonFormat;
import io.cloudevents.jackson.PojoCloudEventDataMapper;

public class EventUtils {
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
    }

    public static CloudEvent convertToCloudEvent(byte[] message) {
        return EventFormatProvider.getInstance()
                .resolveFormat(JsonFormat.CONTENT_TYPE)
                .deserialize(message);
    }

    public static <T> T getDataFromCloudEvent(CloudEvent cloudEvent, Class<T> dataClass) {
        var dataMapper = PojoCloudEventDataMapper.from(OBJECT_MAPPER, dataClass);
        return mapData(cloudEvent, dataMapper).getValue();
    }
}
