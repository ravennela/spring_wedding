package com.example.online.location.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.online.event.dto.PagedResponse;
import com.example.online.location.dto.CityResponse;
import com.example.online.location.service.CityService;

@RestController
@RequestMapping("/api/admin/cities")
public class CityController {

        @Autowired
        private CityService cityService;

    @GetMapping
    public PagedResponse<?> getCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CityResponse> cityPage =
                cityService.getCities(search, pageable);

        return new PagedResponse<>(
                cityPage.getContent(),
                 cityPage.isLast(),
                cityPage.getNumber(),
                cityPage.getSize(),
                cityPage.getTotalElements(),
                cityPage.getTotalPages()
               
        );
    }
}

