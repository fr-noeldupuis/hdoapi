package fr.noeldupuis.hdoapi.pilgrimage.repository;

import fr.noeldupuis.hdoapi.pilgrimage.entity.Pilgrimage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PilgrimageRepository extends JpaRepository<Pilgrimage, Long> {
} 