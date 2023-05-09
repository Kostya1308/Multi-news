package extension;

import by.clevertec.dto.NewsDTO;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class ValidNewsDTOParameterResolver implements ParameterResolver {

    NewsDTO newsDTO = NewsDTO.builder()
            .id("1")
            .title("title")
            .text("text")
            .build();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == NewsDTO.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return newsDTO;
    }
}
