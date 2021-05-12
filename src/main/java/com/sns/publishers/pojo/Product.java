package com.sns.publishers.pojo;

import lombok.Data;

@Data
public class Product {

    String productName;
    String productId;
    Double price;
    String buyerName;
    String shippingRegion;
    String topicArn;
    Category category;
    public enum Category {PHONE, LAPTOP, IPAD};

}
