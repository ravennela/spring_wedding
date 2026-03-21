package com.example.online.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.online.home.dto.HomeResponse;
import com.example.online.home.services.HomeService;

@RestController
@RequestMapping("/api/admin/home")
public class PublicHomeController {
    @Autowired
    private HomeService homeService;

    @GetMapping
    public HomeResponse getHomeData(Boolean active) {
        return homeService.getHomeData(active);
    }
}

