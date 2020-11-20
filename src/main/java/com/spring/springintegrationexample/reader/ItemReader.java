package com.spring.springintegrationexample.reader;

import com.spring.springintegrationexample.constants.AppConstants;
import com.spring.springintegrationexample.exception.InvalidDateInputException;
import com.spring.springintegrationexample.exception.ResourceReadErrorException;
import com.spring.springintegrationexample.model.Item;
import com.spring.springintegrationexample.model.ItemKey;
import com.spring.springintegrationexample.utils.DateConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemReader.class);

    public List<Item> readItems(InputStream inputStream) throws org.xml.sax.SAXException {

        List<Item> items = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList nodeList = (NodeList) xPath.compile(AppConstants.expression).evaluate(
                    doc, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node itemNode = nodeList.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE && "item".equalsIgnoreCase(itemNode.getNodeName())) {
                    Item item = getItem(itemNode);
                    ItemKey itemKey = getItemKey(item);
                    item.setKey(itemKey);
                    items.add(item);
                }
            }
        } catch (ParserConfigurationException | IOException | XPathExpressionException e) {
            LOGGER.error("error while reading resource: {}", e.toString(), e);
            throw new ResourceReadErrorException("error while reading resource");
        }
        return items;
    }

    private ItemKey getItemKey(Item item) {
        ItemKey itemKey = new ItemKey();
        try {
            itemKey.setFormattedDate(DateConverterUtils.getFormattedDate(item.getPubDate()));
        } catch (ParseException e) {
            LOGGER.error("error while parsing the date: {}, {}", item.getPubDate(), e.toString(), e);
            throw new InvalidDateInputException("input date may be not valid");
        }
        itemKey.setCategory(item.getCategory());
        return itemKey;
    }

    private Item getItem(Node nNode) {
        Item item = new Item();
        for (String tagName : AppConstants.ITEM_TAGS) {
            Element eElement = (Element) nNode;
            Node tagNode = eElement
                    .getElementsByTagName(tagName)
                    .item(0);
            String textValue = tagNode.getTextContent();

            boolean isPermaLink = false;
            if (AppConstants.GUID.equalsIgnoreCase(tagNode.getNodeName())) {
                Element element = (Element) tagNode;
                if (element.hasAttribute(AppConstants.IS_PERMALINK)) {
                    isPermaLink = Boolean.valueOf(element.getAttribute(AppConstants.IS_PERMALINK));
                }
            }

            switch (tagName) {
                case AppConstants.LINK:
                    item.setLink(textValue);
                    break;
                case AppConstants.TITLE:
                    item.setTitle(textValue);
                    break;
                case AppConstants.DESCRIPTION:
                    item.setDescription(textValue);
                    break;
                case AppConstants.PUB_DATE:
                    item.setPubDate(textValue);
                    break;
                case AppConstants.GUID:
                    item.setGuid(textValue);
                    item.setPermaLink(isPermaLink);
                    break;
                case AppConstants.CATEGORY:
                    item.setCategory(textValue);
                    break;
            }

        }
        return item;
    }
}