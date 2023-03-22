package by.clevertec.factory.reaction;

import by.clevertec.entity.Reaction;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public abstract class ReactionFactory {
    protected abstract Reaction create();
}
