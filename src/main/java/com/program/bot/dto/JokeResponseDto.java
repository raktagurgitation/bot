package com.program.bot.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JokeResponseDto {
    String type;
    String setup;
    String punchline;
    int id;
}
