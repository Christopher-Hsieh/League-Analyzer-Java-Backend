package com.meta.analyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.meta.analyzer.service.RequestProcessor;

@Component
public class ApplicationLoader implements CommandLineRunner {
	
	@Autowired
	RequestProcessor requestProcessor;

    @Override
    public void run(String... strings) throws Exception {
        Thread reqProcessorThread = new Thread(requestProcessor);
        reqProcessorThread.setName("ReqProcessorThread");
        reqProcessorThread.start();
    }
}
