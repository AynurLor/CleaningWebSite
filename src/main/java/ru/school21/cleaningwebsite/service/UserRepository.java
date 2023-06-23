package ru.school21.cleaningwebsite.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.school21.cleaningwebsite.models.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.idTelegram = :idTelegram")
    User findByIdTelegram(@Param("idTelegram")Integer idTelegram);

}


