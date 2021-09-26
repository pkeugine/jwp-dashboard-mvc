package nextstep.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.exception.HandlerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private static final Logger log = LoggerFactory.getLogger(HandlerMappings.class);

    private final List<HandlerMapping> mappings;

    public HandlerMappings() {
        this.mappings = new ArrayList<>();
    }

    public void init() {
        log.info("initializing handler mappings");
        addDefaultHandlerMappings();
        mappings.forEach(HandlerMapping::initialize);
    }

    private void addDefaultHandlerMappings() {
        log.info("Adding default handler mappings");
        addHandlerMapping(new AnnotationHandlerMapping("nextstep.mvc.handler"));
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        mappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return mappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new HandlerNotFoundException(request));
    }
}
