package com.baeldung.camel.file;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class DeadLetterChannelFileRouter extends RouteBuilder {
    private static final String SOURCE_FOLDER = "src/test/source-folder";

    @Override
    public void configure() throws Exception {
        errorHandler(deadLetterChannel("log:dead?level=ERROR").maximumRedeliveries(3).redeliveryDelay(1000).retryAttemptedLogLevel(LoggingLevel.ERROR));

        from("file://" + SOURCE_FOLDER + "?delete=true").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                throw new IllegalArgumentException("Exception thrown!");
            }
        });
    }
}