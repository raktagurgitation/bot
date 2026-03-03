package com.program.bot.service;

import com.program.bot.entity.Notification;
import com.program.bot.entity.Person;
import com.program.bot.repository.NotificationRepo;
import com.program.bot.repository.PersonRepo;
import com.program.bot.utils.StateType;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.time.LocalDateTime;

@Slf4j
@Service
public class PersonService {

    private final PersonRepo personRepo;
    private final NotificationRepo notificationRepo;

    public PersonService(PersonRepo personRepo, NotificationRepo notificationRepo) {
        this.personRepo = personRepo;
        this.notificationRepo = notificationRepo;
    }

    public void register(Message message) {

        if (personRepo.findById(message.getFrom().getId()).isEmpty()) {
            Person person = Person.builder()
                    .chatId(message.getChatId())
                    .firstName(message.getFrom().getFirstName())
                    .registeredAt(LocalDateTime.now())
                    .state(StateType.NORMAL.name())
                    .build();
            personRepo.save(person);

            log.info("New person registered with chatId {}", person.getChatId());
        }
        else  {
            log.info("Person with chatId {} already exists", message.getChatId());
        }
    }

    public Person findById(Long chatId) {
        return personRepo.findById(chatId).orElse(null);
    }

    public void save(Person person) {
        personRepo.save(person);
    }


    public void changeState(Person person, StateType state) {
        person.setState(state.name());
        personRepo.save(person);
    }

}
