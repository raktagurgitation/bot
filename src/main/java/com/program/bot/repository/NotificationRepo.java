package com.program.bot.repository;


import com.program.bot.entity.Notification;
import com.program.bot.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface NotificationRepo extends JpaRepository<Notification,Long> {

    Notification findFirstByPersonAndNotifDateIsNullOrderByIdDesc(Person person);

    Optional <Notification> findByPersonAndName(Person person, String name);

    List<Notification> findAllByPerson(Person person);

    Optional<List<Notification>> findAllByNotifDate(LocalDateTime notifDate);
}
