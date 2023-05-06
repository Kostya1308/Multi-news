package by.clevertec.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class ControllerUtil {

    protected static PageRequest getPageRequest(String page, String size, String sortBy, String sortDir) {
        Sort sort = sortDir.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), sort);
    }
}
