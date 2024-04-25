package com.bytecoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bytecoder.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

}
