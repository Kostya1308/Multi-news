package extension;

import by.clevertec.dto.NewsDTO;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class InvalidNewsDTOParameterResolver implements ParameterResolver {

    NewsDTO invalidNewsDTOWithoutText = NewsDTO.builder()
            .id("1")
            .title("title")
            .build();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == NewsDTO.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return invalidNewsDTOWithoutText;
    }
}
