package com.project.literalura.repository;

import com.project.literalura.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByNameIgnoreCase(String name);

    List<Author> findAllByOrderByNameAsc();

    @Query("""
           select a
           FROM Author a
           WHERE (a.birthYear IS NULL OR a.birthYear <= :year)
             AND (a.deathYear IS NULL OR a.deathYear >= :year)
           order by a.name asc
           """)
    List<Author> findAliveInYear(@Param("year") Integer year);


}
