package de.otto.asyncapidemo2.config;

import com.asyncapi.v2.model.info.Info;
import de.otto.asyncapidemo2.asyncreflector.CustomScanner;
import de.otto.asyncapidemo2.dto.AnotherPayloadDto;
import de.otto.asyncapidemo2.dto.DunningDTO;
import io.github.stavshamir.springwolf.asyncapi.types.ProducerData;
import io.github.stavshamir.springwolf.configuration.AsyncApiDocket;
import io.github.stavshamir.springwolf.configuration.EnableAsyncApi;
import io.github.stavshamir.springwolf.schemas.SchemasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAsyncApi
public class AsyncApiConfig {

    @Autowired
    SchemasService schemaService;
    @Bean
    public AsyncApiDocket asyncApiDocket(){

        Info info = Info.builder()
                .version("v1")
                .title("Customer and Partner Communication Service Messages API")
                .description(" The Customer and Partner Communication API allows product teams at Otto Payments to communicate with customers and partners.")
                .build();

        ProducerData data = ProducerData.builder()
                .payloadType(AnotherPayloadDto.class)
                .description("here could be your description")
                .channelName("${test.queue}")
                .build();

        return AsyncApiDocket.builder()
                .basePackage("de.otto.asyncapidemo2")
                .info(info)
                .servers(ServerConfiguration.configure())
                .producer(data)
                .producer(data)
                .build();


    }


}
