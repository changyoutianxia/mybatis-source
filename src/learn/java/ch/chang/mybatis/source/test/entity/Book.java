package ch.chang.mybatis.source.test.entity;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 6240847185232647898L;
    private String name;

    public Book(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                '}';
    }
}
