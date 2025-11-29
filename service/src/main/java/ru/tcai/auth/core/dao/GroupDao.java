package ru.tcai.auth.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tcai.auth.core.entity.Group;

import java.util.List;

@Repository
public interface GroupDao extends JpaRepository<Group, Long> {

    List<Group> findAllByMembers_IdOrOwnerId(Long memberId, Long ownerId);

}