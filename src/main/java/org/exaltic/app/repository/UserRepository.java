package org.exaltic.app.repository;

import org.exaltic.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository<T> extends JpaRepository<User, Long> {

}
