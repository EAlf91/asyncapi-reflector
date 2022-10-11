package de.otto.asyncapidemo2.Controller;


import io.github.stavshamir.springwolf.asyncapi.scanners.channels.SQSChannelsScanner;
import io.github.stavshamir.springwolf.configuration.AsyncApiDocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AsyncApiControll {

    @Autowired
    AsyncApiDocket docket;



    @GetMapping("/asyncapi")
    public String getAsyncApi(){
       System.out.println(""+docket.getConsumers().size());
       System.out.println("s"+docket.getServers());
        return "s";
    }

}
