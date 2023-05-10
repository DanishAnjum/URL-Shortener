package com.example.URLShortener.URLShortener.service;

import org.springframework.stereotype.Service;

import com.example.URLShortener.URLShortener.model.Url;
import com.example.URLShortener.URLShortener.model.UrlDto;

@Service
public interface UrlService {

	public Url generateShortLink(UrlDto urlDto);
	public Url persistShortLink(Url url);
	public Url getEncodedUrl(String url);
	public void deleteShortLink(Url url);
}
