package com.spring.springintegrationexample.writer;

import com.spring.springintegrationexample.model.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class XmlWriter {

    private ExecutorService executorService;

    @Value("${item_xml_base_path}")
    private String itemXmlBasePath;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(5); // can be configured in properties
    }

    public void write(List<Item> item) {
        executorService.submit(new XmlWriterTask(item, itemXmlBasePath));
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }
}
