package org.exaltic.app.repository;

import java.util.Optional;

import org.exaltic.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository<T> extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
