package com.program.bot.service;

import com.program.bot.dto.JokeDto;
import com.program.bot.dto.JokeResponseDto;
import com.program.bot.dto.TranslateResponseDto;
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


//    public JokeDto getJoke() {
//        Long start = System.currentTimeMillis();
//        JokeDto jokeDto = new JokeDto();
//        String jokeUrl = "https://official-joke-api.appspot.com/random_joke";
//        JokeResponseDto joke = restTemplate.getForObject((jokeUrl), JokeResponseDto.class);
//        log.info("Well-received joke");
//
//        String beforeTranslate = joke.getSetup() + joke.getPunchline();
//        System.out.println("before translate: " + beforeTranslate);
//        String afterTranslate = translateJoke(beforeTranslate);
//        System.out.println("after translate: " + afterTranslate);
//        String[] text =  afterTranslate.split("(?i)ррр");
//        System.out.println(Arrays.toString(text));
//        jokeDto.setSetup(text[0]);
//        jokeDto.setPunchline(text[1]);
//
//
//
//
//
//
//        Long end = System.currentTimeMillis();
//        log.warn("Time taken to get joke: " + (end - start));
//
//        return jokeDto;
//    }

    public JokeDto getJoke() {
        Long start = System.currentTimeMillis();
        JokeDto jokeDto = new JokeDto();
        String jokeUrl = "https://official-joke-api.appspot.com/random_joke";
        JokeResponseDto joke = restTemplate.getForObject((jokeUrl), JokeResponseDto.class);
        log.info("Well-received joke");
        if (joke != null) {
            jokeDto.setSetup(getSetup(joke.getSetup()));
            jokeDto.setPunchline(getPunchline(joke.getPunchline()));
        }
        Long end = System.currentTimeMillis();
        log.warn("Time taken to get joke: " + (end - start));

        return jokeDto;
    }

    private String getSetup(String setup) {
        return translateJoke(setup);
    }

    private String getPunchline(String punchline) {
        return translateJoke(punchline);
    }

    private String translateJoke(String text) {
        Long start = System.currentTimeMillis();

        String translateUrl = "https://lingva.ml/api/v1/en/ru/";
        TranslateResponseDto translate = restTemplate.getForObject(translateUrl + text, TranslateResponseDto.class);
        Long end = System.currentTimeMillis();
        log.warn("Time taken to get translation: " + (end - start));
        if (translate != null) {
            return translate.getTranslation();
        }
        else {
            return null;
        }
    }


}
