package org.hoiux.newsreader.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) })
public class User {

    @Id
    @NotNull
    @Column(length = 64)
    String username;

    @NotNull
    @Column(length = 64)
    String password;

    @NotNull
    @Column(length = 255)
    String firstName;

    @NotNull
    @Column(length = 255)
    String surname;

    @Column(length = 254)
    String email;
}