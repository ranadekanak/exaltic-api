package org.exaltic.app.repository;

import org.exaltic.app.domain.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

}
