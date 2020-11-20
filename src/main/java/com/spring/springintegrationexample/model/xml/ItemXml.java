package com.spring.springintegrationexample.model.xml;

import com.spring.springintegrationexample.model.ItemKey;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "item")
public class ItemXml {
    public String link;
    public String title;
    public String description;
    public String pubDate;
    public String category;
    public Guid guid;
}
