package ru.licard.hakatondemo.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "hackathon_entity")
@AllArgsConstructor
@NoArgsConstructor
public class HackathonEntity {


    @Id
    private String name;

}
