package dev.bolohonov.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * класс с описанием пользователя - User
 */
@Entity
@Table(name = "users")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
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
