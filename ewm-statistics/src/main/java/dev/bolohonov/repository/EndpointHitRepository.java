package dev.bolohonov.repository;

import dev.bolohonov.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long>, EndpointHitRepositoryCustom {
}
