package com.example.demo;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Document(indexName = "test1",type = "test2")
public class test implements Serializable {

    @Field()
    private int id;

    @Field(searchAnalyzer = "ik_smart",analyzer = "ik_smart")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "test{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
