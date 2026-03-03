package com.program.bot.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
// Тут обязательно убрать, т.к. впадаем в бесконечный цикл
@ToString(exclude = "person")
@Builder
public class Notification {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "notification_date")
    private LocalDateTime notifDate;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Person person;


}
