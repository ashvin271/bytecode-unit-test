package com.bytecoder.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bytecoder.dto.BookDto;
import com.bytecoder.model.Book;
import com.bytecoder.repository.BookRepository;
import com.bytecoder.service.BookService;

@Service
public class BookServiceImpl implements BookService{
	
	@Autowired
	BookRepository bookRepository; 

	@Override
	public Book saveBook(Book book) {
		return bookRepository.save(book);
	}


	@Override
	public BookDto getAllBooks(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Book> books =bookRepository.findAll(pageable);
		BookDto bookDto = new BookDto();
		bookDto.setContent(books.getContent());
		bookDto.setPageNo(books.getNumber());
		bookDto.setPageSize(books.getSize());
		bookDto.setTotalElements(books.getTotalElements());
		bookDto.setTotalPages(books.getTotalPages());
		bookDto.setLast(books.isLast());
	    return bookDto;
	}


	@Override
	public Book getBook(Long id) {
	Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("book not found."));
	return book;
	}


	@Override
	public Long deleteBook(Long id) {
		Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("book not found."));
		bookRepository.delete(book);
		return book.getId();
	}


	@Override
	public Book updateBook(Book book, Long id) {
		Book bookObj = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("book not found."));
		book.setId(bookObj.getId());
		return bookRepository.save(book);
	}

}
