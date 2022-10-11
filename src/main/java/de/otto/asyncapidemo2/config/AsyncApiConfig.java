package de.otto.asyncapidemo2.config;

import com.asyncapi.v2.model.info.Info;
import io.github.stavshamir.springwolf.configuration.AsyncApiDocket;
import io.github.stavshamir.springwolf.configuration.EnableAsyncApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAsyncApi
public class AsyncApiConfig {

    @Bean
    public AsyncApiDocket asyncApiDocket(){

        Info info = Info.builder()
                .version("v1")
                .title("Customer and Partner Communication Service Messages API")
                .description(" The Customer and Partner Communication API allows product teams at Otto Payments to communicate with customers and partners.")
                .build();

        return AsyncApiDocket.builder()
                .basePackage("de.otto.asyncapidemo2")
                .info(info)
                .servers(ServerConfiguration.configure())
                .build();
    }


}
