package com.portifolio.wealthinker.user.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portifolio.wealthinker.user.models.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long>{
    Optional<Role> findByName(String roleName);
}
