package by.clevertec.factory;

import by.clevertec.entity.Content;
import by.clevertec.entity.News;

public class NewsFactory extends ContentFactory{
    @Override
    public Content create() {
        return new News();
    }
}
