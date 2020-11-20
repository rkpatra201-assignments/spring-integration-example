package com.spring.springintegrationexample.integration.transformer;

import com.spring.springintegrationexample.exception.ResourceReadErrorException;
import com.spring.springintegrationexample.model.Item;
import com.spring.springintegrationexample.reader.ItemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.util.Collection;

@Component
public class ItemMessageTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemMessageTransformer.class);
    @Autowired
    private ItemReader itemReader;

    public Collection<Item> prepareItemList(InputStream inputStream) {
        try {
            return itemReader.readItems(inputStream);
        } catch (SAXException e) {
            LOGGER.error("error while reading data from stream: {}", e.toString(), e);
            throw new ResourceReadErrorException("error while reading data from stream");
        }
    }
}
