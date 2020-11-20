# spring-integration-example

![image](https://user-images.githubusercontent.com/17001948/99814750-6f15dc80-2b6f-11eb-80e3-ff05c30f2a99.png)

## Description

Below is out pojo model for Item. 
```java
public class Item {

    private String link;
    private String title;
    private String description;
    private String pubDate;
    private String category;
    private ItemKey key;
    private String guid;
    private boolean isPermaLink;
}    

public class ItemKey implements Serializable {

    private String formattedDate;
    private String category;
    private int hash;
}
    
```
We have added ItemKey into the Item so that we can aggregate the messages based upon `date` and `category`.

## setup version:

* JDK 1.8
* Spring Boot: 2.4.0

## Screenshots

* https://github.com/rkpatra201-assignments/spring-integration-example/issues/2

## update base path directory (must have write permission) where you want to store the xml:

* item_xml_base_path in application.properties
* application.properties here (https://github.com/rkpatra201-assignments/spring-integration-example/blob/master/src/main/resources/application.properties)
* you can update based on your own machine

## run the application
We can use one of the mentioned way below
* use `mvn spring-boot:run`
* build the project using `mvn clean install -DskipTests`. Then java -jar path_to_your_bundle
