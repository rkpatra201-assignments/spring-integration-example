package com.spring.springintegrationexample.utils;

import com.spring.springintegrationexample.exception.InvalidDateInputException;
import com.spring.springintegrationexample.model.Item;
import com.spring.springintegrationexample.model.ItemKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MarkerIgnoringBase;

import java.text.ParseException;

public class ItemUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemUtils.class);

    public static ItemKey getItemKey(Item item) {
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
}
