package de.otto.asyncapidemo2.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import de.otto.asyncapidemo2.asyncreflector.CustomApiSerializerService;
import de.otto.asyncapidemo2.asyncreflector.Publisher;
import de.otto.asyncapidemo2.dto.AnotherPayloadDto;
import de.otto.asyncapidemo2.dto.DunningDTO;
import io.github.stavshamir.springwolf.asyncapi.AsyncApiSerializerService;
import io.github.stavshamir.springwolf.asyncapi.AsyncApiService;
import io.github.stavshamir.springwolf.configuration.AsyncApiDocket;
import io.github.stavshamir.springwolf.schemas.SchemasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AsyncApiControll {

    @Autowired
    AsyncApiDocket docket;

    @Autowired
    AsyncApiService apiService;

    @Autowired
    AsyncApiSerializerService service;



    @GetMapping("/asyncapi")
    public String getAsyncApi() throws JsonProcessingException {
       System.out.println(""+docket.getConsumers().size());
       System.out.println("s"+docket.getComponentsScanner().scanForComponents());
       return service.toJsonString(apiService.getAsyncAPI());

    }

    @Publisher("${test.queue}")
    public void test(AnotherPayloadDto dunningDTO){
        System.out.println(dunningDTO);
    }

}
