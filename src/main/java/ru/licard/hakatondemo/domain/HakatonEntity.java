package ru.licard.hakatondemo.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hakaton_entity")
@AllArgsConstructor
@NoArgsConstructor
public class HakatonEntity {
    @Id
    private String name;
}
