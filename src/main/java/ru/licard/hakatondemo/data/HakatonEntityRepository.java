package ru.licard.hakatondemo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.licard.hakatondemo.domain.HakatonEntity;

public interface HakatonEntityRepository extends JpaRepository<HakatonEntity, String> {

    @Modifying
    @Transactional
    @Query(value = "insert into hakaton_entity (name) values (?) ON CONFLICT DO NOTHING", nativeQuery = true)
    void createHakatonEntityIfNotExists(String name);
}
