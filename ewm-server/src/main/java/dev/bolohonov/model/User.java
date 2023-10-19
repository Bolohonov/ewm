package dev.bolohonov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * класс с описанием пользователя - User //
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * уникальный идентификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * имя или логин пользователя, должно быть уникальным
     */
    private String name;
    /**
     * имя пользователя
     */
    private String firstname;
    /**
     * фамилия пользователя
     */
    private String lastname;
    /**
     * адрес электронной почты
     */
    private String email;
    /**
     * хэш-код пароля
     */
    private String password_hash;
}