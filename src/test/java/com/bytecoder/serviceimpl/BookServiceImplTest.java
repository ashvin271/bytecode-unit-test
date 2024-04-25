package com.bytecoder.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bytecoder.dto.BookDto;
import com.bytecoder.model.Book;
import com.bytecoder.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

	@Mock
	private BookRepository bookRepository; 
	
	@InjectMocks
	private BookServiceImpl bookServiceImpl;
	
	private Book book;
	
	 @BeforeEach
	 public void init() {
		 book=book.builder().id(1l).name("ramayan").title("love and mistry").build();
	    }
	
	@Test
	public void saveBookTest() {
		when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);
		Book bookObj=bookServiceImpl.saveBook(book);
		Assertions.assertThat(bookObj).isNotNull();
	}
	
	@Test
	public void getALLBookTest() {
		Page books = Mockito.mock(Page.class);
		when(bookRepository.findAll(Mockito.any(Pageable.class))).thenReturn(books);
		BookDto bookDto=bookServiceImpl.getAllBooks(1, 10);
		Assertions.assertThat(bookDto).isNotNull();
	}
	
	@Test
	public void getBookTest() {
		Long id=1l;
		when(bookRepository.findById(id)).thenReturn(Optional.ofNullable(book));
	    //when(bookRepository.findById(id).orElseThrow(() -> new RuntimeException("book not found."))).thenReturn(book);
		Book bookObj=bookServiceImpl.getBook(id);
		Assertions.assertThat(bookObj).isNotNull();
	}
	
	@Test
	public void getBookTest_Exception() {
		Long id=1l;
		 when(bookRepository.findById(id)).thenReturn(Optional.empty());
		 assertThrows(RuntimeException.class, () -> {
			 bookServiceImpl.getBook(id);
		 });
	}
	
	@Test
	public void updateBookTest() {
		Long id=1l;
		when(bookRepository.findById(id)).thenReturn(Optional.ofNullable(book));
		when(bookRepository.save(book)).thenReturn(book);
		Book bookObj =bookServiceImpl.updateBook(book,id);
		Assertions.assertThat(bookObj).isNotNull();
	}
	
	@Test
	public void updateBookTest_Exception() {
		Long id=1l;
		 when(bookRepository.findById(id)).thenReturn(Optional.empty());
		 assertThrows(RuntimeException.class, () -> {
			 bookServiceImpl.updateBook(book,id);
		 });
	}
	
	@Test
	public void deleteBookTest() {
		Long id=1l;
		when(bookRepository.findById(id)).thenReturn(Optional.ofNullable(book));
		 doNothing().when(bookRepository).delete(book);
		Long value =bookServiceImpl.deleteBook(id);
		Assertions.assertThat(value).isGreaterThan(0);
	}
	
	@Test
	public void deleteBook_ThrowsException_WhenBookNotFound() {
	    Long id = 1L;
	    when(bookRepository.findById(id)).thenReturn(Optional.empty());
	    assertThrows(RuntimeException.class, () -> {
	        // Call the deleteBook method with the mocked id
	    	bookServiceImpl.deleteBook(id);
	    });
	}
}
