package com.program.bot.service;


import com.program.bot.entity.Notification;
import com.program.bot.entity.Person;
import com.program.bot.repository.NotificationRepo;
import com.program.bot.repository.PersonRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@Service
public class NotificationService {

    private final NotificationRepo notificationRepo;

    public NotificationService(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }


    @Transactional
    public void setNotificationName(Person person, String name) {
        log.info("Начал запись названия события");

        name = name.toLowerCase().strip();

        Notification notification = Notification.builder()
                .name(name)
                .person(person)
                .build();

        notificationRepo.saveAndFlush(notification);

        log.info("Запись успешно завершена, ID: {}", notification.getId());

    }

    @Transactional
    public void setNotificationDate(Person person, String date) {
        log.info("Начал запись времени события");

        LocalDateTime localDateTime = convertToLocalDateTime(stripMessage(date));

        if (localDateTime == null) {
            log.warn("Дата не записана!");
            return;
        }

        Notification notification = notificationRepo.findFirstByPersonAndNotifDateIsNullOrderByIdDesc(person);
        notification.setNotifDate(localDateTime);
        notificationRepo.saveAndFlush(notification);

        log.info("Закончил запись времени события");
    }

    public String findAllPersonNotifications(Person person) {
        String date;
        List<Notification> notifications = notificationRepo.findAllByPerson(person);
        if(notifications.isEmpty()){
            return "Ты пока что не добавил ни одного события. Добавляй скорее ;)";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Notification notification : notifications) {
            date = notification.getNotifDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            stringBuilder
                    .append("\uD83D\uDD14 Название события: ")
                    .append(Character.toUpperCase(notification.getName().charAt(0)) + notification.getName().substring(1))
                    .append(". Сработает: ")
                    .append(date)
                    .append("\n");
        }
        return stringBuilder.toString();
    }

    public String deleteByName(Person person, String name) {
        name = name.toLowerCase().strip();
        Notification notification = notificationRepo.findByPersonAndName(person, name).orElse(null);

        if (notification != null) {
            notificationRepo.delete(notification);
            return null;
        } else {
            return "События с таким названием не найдено! Попробуйте ввести название еще раз.";
        }
    }

    public List<Notification> findAllByNotificationDate(LocalDateTime date) {
        return notificationRepo.findAllByNotifDate(date).orElse(null);
    }

    private LocalDateTime convertToLocalDateTime(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd");
            LocalDate ldt = LocalDate.parse(date, formatter);

            return ldt.atStartOfDay();
        } catch (DateTimeParseException e) {
            log.error("Неверный формат даты: " + date);

            return null;
        }
    }

    private String stripMessage(String message) {
        return message.strip();
    }


}
