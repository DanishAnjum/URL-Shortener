package com.example.URLShortener.URLShortener.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.URLShortener.URLShortener.model.Url;
import com.example.URLShortener.URLShortener.model.UrlDto;
import com.example.URLShortener.URLShortener.repository.UrlRepository;
import com.google.common.hash.Hashing;

@Component
public class UrlServiceImpl implements UrlService{
	
	@Autowired
	private UrlRepository urlRepository;    //creating repository to persist data

	@Override
	public Url generateShortLink(UrlDto urlDto) {
		// TODO Auto-generated method stub
		if(StringUtils.isNotEmpty(urlDto.getUrl())) {
			String encodedUrl = encodeUrl(urlDto.getUrl());
			Url urlToPersist = new Url();
			urlToPersist.setCreationDate(LocalDateTime.now());
			urlToPersist.setOriginalUrl(urlDto.getUrl());
			urlToPersist.setShortLink(encodedUrl);
			urlToPersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(),urlToPersist.getCreationDate()));
			Url urlToRet = persistShortLink(urlToPersist);
			
			if(urlToRet != null)
				return urlToRet;
			
			return null;
		}
		return null;
	}

	private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate) {
		// TODO Auto-generated method stub
		if(StringUtils.isBlank(expirationDate)) {
			return creationDate.plusSeconds(60);
		}
		LocalDateTime expirationDateToret = LocalDateTime.parse(expirationDate);
		return expirationDateToret;
	}

	private String encodeUrl(String url) {
		// TODO Auto-generated method stub
		//using guava library to generate short link
		String encodedUrl = "";
		LocalDateTime time = LocalDateTime.now();       //appending it to original url and hashing it
		encodedUrl = Hashing.murmur3_32()
				     .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
				     .toString();
		return encodedUrl;
	}

	@Override
	public Url persistShortLink(Url url) {
		// TODO Auto-generated method stub
		//persist -> saving into database
		Url urlToRet = urlRepository.save(url);
		return urlToRet;
	}

	@Override
	public Url getEncodedUrl(String url) {
		// TODO Auto-generated method stub
		Url urlToRet = urlRepository.findByShortLink(url);
		return urlToRet;
	}

	@Override
	public void deleteShortLink(Url url) {
		// TODO Auto-generated method stub
		urlRepository.delete(url);
	}

}
