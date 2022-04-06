package ru.licard.hakatondemo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.licard.hakatondemo.domain.HakatonEntity;

import java.util.Optional;

public interface HakatonEntityRepository extends JpaRepository<HakatonEntity, String> {
    Boolean existsHakatonEntityByName(String name);
}
