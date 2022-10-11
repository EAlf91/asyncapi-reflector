package de.otto.asyncapidemo2.asyncreflector;

import com.asyncapi.v2.binding.kafka.KafkaChannelBinding;
import com.asyncapi.v2.binding.kafka.KafkaOperationBinding;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.stavshamir.springwolf.asyncapi.AsyncApiSerializerService;
import io.github.stavshamir.springwolf.asyncapi.DefaultAsyncApiService;
import io.github.stavshamir.springwolf.asyncapi.serializers.KafkaChannelBindingSerializer;
import io.github.stavshamir.springwolf.asyncapi.serializers.KafkaOperationBindingSerializer;
import io.github.stavshamir.springwolf.asyncapi.types.AsyncAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
@Primary
public class CustomApiSerializerService implements AsyncApiSerializerService {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @PostConstruct
    void postConstruct() {
        jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        registerKafkaOperationBindingSerializer();
    }

    private void registerKafkaOperationBindingSerializer() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(KafkaChannelBinding.class, new KafkaChannelBindingSerializer());
        module.addSerializer(KafkaOperationBinding.class, new KafkaOperationBindingSerializer());
        jsonMapper.registerModule(module);
    }

    @Override
    public String toJsonString(AsyncAPI asyncAPI) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(asTitleToDocMap(asyncAPI));
    }

    private Map<String, AsyncAPI> asTitleToDocMap(AsyncAPI asyncAPI) {
        String title = asyncAPI.getInfo().getTitle();
        return Map.of(title, asyncAPI);
    }


}