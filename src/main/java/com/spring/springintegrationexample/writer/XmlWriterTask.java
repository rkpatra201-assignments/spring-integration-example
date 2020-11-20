package com.spring.springintegrationexample.writer;

import com.spring.springintegrationexample.model.Item;
import com.spring.springintegrationexample.model.xml.Guid;
import com.spring.springintegrationexample.model.xml.ItemXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class XmlWriterTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlWriterTask.class);

    private List<Item> items;
    private String basePath;

    public XmlWriterTask(List<Item> items, String basePath) {
        this.items = items;
        this.basePath = basePath;
    }

    @Override
    public void run() {

        for (Item item : items) {
            ItemXml itemXml = convertToItemXml(item);
            String dateWiseCategoryPath = getDateWiseCategoryPath(item);

            try {
                File file = new File(dateWiseCategoryPath);
                LOGGER.info("looking for directory: {}", file.getAbsolutePath());
                if (!file.exists())
                    file.mkdirs();
                String xmlFilePath = file.getAbsolutePath() + "/" + UUID.randomUUID().toString() + ".xml";
                LOGGER.info("writing to path: {} for key: {}", xmlFilePath, item.getKey().getHash());
                writeItemXml(itemXml, xmlFilePath);
            } catch (JAXBException | IOException e) {
                LOGGER.error("error while writing to xml file: {}", e.toString(), e);
            }
        }
    }

    public void writeItemXml(ItemXml itemXml, String path) throws JAXBException, IOException {
        JAXBContext contextObj = JAXBContext.newInstance(ItemXml.class);
        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshallerObj.marshal(itemXml, new FileOutputStream(path));
    }

    private ItemXml convertToItemXml(Item item) {
        ItemXml itemXml = new ItemXml();
        itemXml.title = item.getTitle();
        itemXml.description = item.getDescription();
        itemXml.category = item.getCategory();
        itemXml.pubDate = item.getPubDate();
        itemXml.link = item.getLink();
        itemXml.guid = new Guid();
        itemXml.guid.isPermaLink = item.isPermaLink();
        itemXml.guid.value = item.getGuid();
        return itemXml;
    }

    private String getDateWiseCategoryPath(Item item) {
        String datePath = item.getKey().getFormattedDate() == null ? "empty-date" : item.getKey().getFormattedDate();
        String categoryPath = item.getKey().getCategory() == null ? "empty-category" : item.getKey().getCategory().length() == 0 ? "empty-category" : item.getKey().getCategory();
        String dateWisecategoryPath = null;

        if (basePath == null || basePath.length() == 0) {
            dateWisecategoryPath = datePath + "/" + categoryPath;
        } else {
            dateWisecategoryPath = basePath + datePath + "/" + categoryPath;
        }
        return dateWisecategoryPath;
    }
}
