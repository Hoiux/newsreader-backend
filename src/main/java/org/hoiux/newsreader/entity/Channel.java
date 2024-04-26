package org.hoiux.newsreader.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "channels", uniqueConstraints = { @UniqueConstraint(columnNames = { "source" }) })
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Long id;

    // The URL of the original RSS feed.
    @NotNull
    @Column(length = 2048)
    String source;

    @NotNull
    @Column(length = 2048)
    String link;

    @Column(length = 2048)
    String title;

    @Column(length = 2048)
    String description;

    // The foreign key of the owning Category.
    @Column(name = "cat_id")
    Long catId;

    @Column(length = 2048)
    String image;

    @Column(length = 2048)
    String icon;
}
