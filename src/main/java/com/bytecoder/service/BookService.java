package com.bytecoder.service;

import java.util.List;

import com.bytecoder.dto.BookDto;
import com.bytecoder.model.Book;

public interface BookService {

	Book saveBook(Book book);

	BookDto getAllBooks(int pageNo, int pageSize);

	Book getBook(Long id);

	Long deleteBook(Long id);

	Book updateBook(Book book, Long id);

}
