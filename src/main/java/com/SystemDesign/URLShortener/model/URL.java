package com.SystemDesign.URLShortener.model;


import com.SystemDesign.URLShortener.Util.ShortenURL;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class URL {

    private final String id;
    private final String url;
    private final LocalDateTime created;

    public static URL create(final String url, ShortenURL shortenURL) {
        final String id = shortenURL.shorten(url);
        return new URL(id, url, LocalDateTime.now());
    }
}