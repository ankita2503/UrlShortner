package com.SystemDesign.URLShortener.controller;


import com.SystemDesign.URLShortener.Exceptions.TinyUrlError;
import com.SystemDesign.URLShortener.Util.ShortenURL;
import com.SystemDesign.URLShortener.model.URL;
;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping(value = "/tinyurl")
public class TinyUrlController {

    @Autowired
    private RedisTemplate<String, URL> redisTemplate;

    @Autowired
    ShortenURL shortenURL;

    @Value("${redis.ttl}")
    private long ttl;

    @Value("${error.url}")
    private String errorUrl;

    @PostMapping
    public ResponseEntity create(@RequestBody final String url) {
        final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
        if (!urlValidator.isValid(url)) {
            return ResponseEntity.badRequest().body(new TinyUrlError("Invalid URL."));
        }
        final URL urlDto = URL.create(url,shortenURL);
        log.info("URL id generated = {}", urlDto.getId());
        redisTemplate.opsForValue().set(urlDto.getId(), urlDto, ttl, TimeUnit.SECONDS);
        // Return the generated id as a response header with status code 201 Created.
        HttpHeaders headers = new HttpHeaders();
        headers.add("id", urlDto.getId());
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();
    }


    @GetMapping(value = "/{id}")
    public RedirectView getUrl(@PathVariable final String id) {
        RedirectView redirectView = new RedirectView();
        final URL urlDto = redisTemplate.opsForValue().get(id);
        if (Objects.isNull(urlDto)) {
            redirectView.setUrl(errorUrl);
            return redirectView;
        }
        log.info("URL retrieved = {}", urlDto.getUrl());
        redirectView.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
        redirectView.setUrl(urlDto.getUrl());
        return redirectView;
    }

    //Need to handle the error scenario better
    @GetMapping(value = "/error")
    public String getErrorUrl() {
        return "Requested URL not found.";
    }
}
