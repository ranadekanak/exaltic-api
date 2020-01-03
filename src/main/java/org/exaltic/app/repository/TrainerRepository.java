package org.exaltic.app.repository;

import java.util.Collection;

import org.exaltic.app.domain.Trainer;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends UserRepository<Trainer> {

	Collection<Trainer> findAllByIsPremium(Boolean isPremium);
}
