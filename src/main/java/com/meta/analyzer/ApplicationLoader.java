package com.meta.analyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.meta.analyzer.incoming.processor.RequestProcessor;

@Component
public class ApplicationLoader implements CommandLineRunner {
	
	@Autowired
	RequestProcessor requestProcessor;

    @Override
    public void run(String... strings) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String option : strings) {
            sb.append(" ").append(option);
        }
        sb = sb.length() == 0 ? sb.append("No Options Specified") : sb;
        System.out.println("Launching application loading with following options: " + sb.toString());
        
        new Thread(requestProcessor).start();
    }
}
