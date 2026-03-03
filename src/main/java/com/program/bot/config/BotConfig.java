package com.program.bot.config;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "telegram.bot")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotConfig {

    final String url;
    final String token;
    final String username;

}
