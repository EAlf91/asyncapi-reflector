package de.otto.asyncapidemo2.config;

import com.asyncapi.v2.model.server.Server;

import java.util.Map;

public class ServerConfiguration {

    public static Map<String, Server> configure(){
        String sns_queue = "https://sns.eu-central-1.amazonaws.com";

        Server dev = Server.builder()
                .protocol("sqs")
                .description("dev")
                .url(String.format("%s", sns_queue))
                .build();

        Server qa = Server.builder()
                .protocol("sqs")
                .description("live")
                .url(String.format("%s", sns_queue))
                .build();

        Server live = Server.builder()
                .protocol("sqs")
                .description("live")
                .url(String.format("%s", sns_queue))
                .build();

        return Map.of(
                "dev",dev,
                "live",live,
                "qa",qa
        );
    }
}