package org.hoiux.newsreader.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(name = "items", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
public class Item {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(length = 2048)
    String title;

    @Column(length = 2048)
    String description;

    @NotNull
    @Column(length = 2048)
    String link;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, hh:mm:ss")
    @Column(name = "pub_date")
    Date pubDate;

    @NotNull
    @Column(name = "is_read")
    Boolean isRead;

    @NotNull
    @Column(name = "is_visible")
    Boolean isVisible;

    // The foreign key of the owning Channel.
    @Column(name = "chan_id")
    Long chanId;

}
