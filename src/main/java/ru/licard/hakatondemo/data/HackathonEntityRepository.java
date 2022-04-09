package ru.licard.hakatondemo.data;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.licard.hakatondemo.domain.HackathonEntity;

public interface HackathonEntityRepository extends JpaRepository<HackathonEntity, String> {
    Boolean existsHackathonEntityByName(String name);
}
