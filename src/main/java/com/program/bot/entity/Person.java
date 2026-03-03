package com.program.bot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// Тут обязательно убрать, т.к. впадаем в бесконечный цикл
@ToString(exclude = "notifications")
@Builder
public class Person {

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "state")
    private String state;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

}
