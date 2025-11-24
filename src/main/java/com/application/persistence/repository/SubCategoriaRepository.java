package com.application.persistence.repository;

import com.application.persistence.entity.categoria.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Long> {

    List<SubCategoria> findByCategoriaCategoriaIdAndCategoriaActivoTrue(Long categoriaId);

}
