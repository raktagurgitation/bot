package com.program.bot.service;

import com.program.bot.dto.JokeDto;
import com.program.bot.dto.JokeResponseDto;
import com.program.bot.dto.TranslateRequestDto;
import com.program.bot.dto.TranslateResponseDto;
import com.program.bot.keyboard.InlineKeyboard;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class JokeService {

    final RestTemplate restTemplate;


    public JokeDto getJoke() {
        Long start = System.currentTimeMillis();
        JokeDto jokeDto = new JokeDto();
        String jokeUrl = "https://official-joke-api.appspot.com/random_joke";

        JokeResponseDto joke = restTemplate.getForObject((jokeUrl), JokeResponseDto.class);

        System.out.println(joke);
        log.info("Well-received joke");

        if (joke != null) {
            jokeDto.setSetup(translateJoke(joke.getSetup()));
            jokeDto.setPunchline(translateJoke(joke.getPunchline()));
        }

        Long end = System.currentTimeMillis();
        log.warn("Time taken to get joke: " + (end - start));
        return jokeDto;
    }


    private String translateJoke(String text) {
        Long start = System.currentTimeMillis();
        String translateUrl = "http://localhost:5000/translate";

        var translateRequestDto = new TranslateRequestDto();
        translateRequestDto.setQ(text);
        System.out.println(translateRequestDto + " ЗАПРОС УЛЕТЕЛ");
        TranslateResponseDto translate = restTemplate.postForObject(translateUrl, translateRequestDto, TranslateResponseDto.class);
        System.out.println(translate);
        Long end = System.currentTimeMillis();
        log.warn("Time taken to get translation: " + (end - start));

        if (translate != null) {
            return translate.getTranslatedText();
        }
        else {
            return null;
        }
    }


}
