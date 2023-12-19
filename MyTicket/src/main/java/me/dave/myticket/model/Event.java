package me.dave.myticket.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "event")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NonNull
    private String name;

    @Column(name = "from_date")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    @NonNull
    private Date from;

    @Column(name = "to_date")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    @NonNull
    private Date to;

    @Column(name = "description")
    @NonNull
    private String description;

    @OneToMany(mappedBy = "event")
    private List<Category> ticketCategories;
}
