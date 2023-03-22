package by.clevertec.factory.content;

import by.clevertec.entity.News;
import by.clevertec.entity.Content;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class NewsFactory extends ContentFactory {

    @Override
    public Content create() {
        return new News();
    }
}
