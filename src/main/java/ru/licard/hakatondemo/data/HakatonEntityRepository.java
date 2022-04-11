package ru.licard.hakatondemo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.licard.hakatondemo.domain.HakatonEntity;

import java.util.Optional;

public interface HakatonEntityRepository extends JpaRepository<HakatonEntity, String> {
    Boolean existsHakatonEntityByName(String name);

    @Query(value = "insert into hakaton_entity (name) values (?) ON CONFLICT DO NOTHING", nativeQuery = true)
    @Transactional
    @Modifying
    void insertNewEntity(String name);
}
