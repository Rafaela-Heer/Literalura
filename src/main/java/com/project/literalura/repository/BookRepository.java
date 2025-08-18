package com.project.literalura.repository;

import com.project.literalura.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByGutenId(Integer gutenId);

    @Query("""
       select b
       from Book b
       where upper(b.title) like upper(concat('%', :term, '%'))
       order by b.id desc
       """)
    List<Book> findRecentByTitleLike(@Param("term") String term);

    @Query("""
           select distinct b
           from Book b
           join b.languages l
           where upper(l) = upper(:language)
           """)
    List<Book> findByLanguage(@Param("language") String language);

    @Query("""
           select distinct b
           from Book b
           join b.languages l
           where upper(l) like upper(concat('%', :fragment, '%'))
           """)
    List<Book> searchByLanguageFragment(@Param("fragment") String fragment);

    @Query("""
           select count(distinct b)
           from Book b
           join b.languages l
           where upper(l) = upper(:language)
           """)
    long countByLanguage(@Param("language") String language);

    List<Book> findTop10ByOrderByDownloadCountDesc();

    @Query("""
           select distinct b
           from Book b
           join b.authors a
           where upper(a.name) like upper(concat('%', :name, '%'))
           order by b.title asc
           """)
    List<Book> findByAuthorName(@Param("name") String name);
}
