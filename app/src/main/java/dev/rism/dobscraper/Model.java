package dev.rism.dobscraper;

/**
 * Created by risha on 08-10-2016.
 */
public class Model {
    String name,link;
    Model(String name,String link)
    {
        this.name=name;
        this.link=link;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
