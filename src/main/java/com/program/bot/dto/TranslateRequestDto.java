package com.program.bot.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class TranslateRequestDto {

    String q;
    final String source = "en";
    final String target = "ru";
    final String format = "text";

}
