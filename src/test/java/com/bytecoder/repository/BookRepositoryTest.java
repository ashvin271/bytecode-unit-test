package com.bytecoder.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bytecoder.model.Book;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BookRepositoryTest {

	@Autowired
	private BookRepository bookRepository;
	
	@Test
	public void saveBookTest() {
		Book book=Book.builder().name("ramayan").title("love and mistry").build();
		Book bookObj = bookRepository.save(book);
		Assertions.assertThat(bookObj).isNotNull();
		Assertions.assertThat(bookObj.getId()).isGreaterThan(0);
	}
	
	@Test
	public void getAllBookTest() {
		Book book=Book.builder().name("ramayan").title("love and mistry").build();
		Book book1=Book.builder().name("jungal book").title("jungal story").build();
		 bookRepository.save(book);
		 bookRepository.save(book1);
		List<Book> books= bookRepository.findAll();
		Assertions.assertThat(books).isNotNull();
		Assertions.assertThat(books.size()).isEqualTo(2);
	}
	
	@Test
	public void getBookTest() {
		Book book=Book.builder().name("And Then There Were None").title("love and mistry").build();
		bookRepository.save(book);
		Book bookObj= bookRepository.findById(book.getId()).get();
		Assertions.assertThat(bookObj).isNotNull();
	}
	
	@Test
	public void updateBookTest() {
		Long id=1l;
		Book book=Book.builder().name("The Hobbit").title("for good and bed Hobbit").build();
		
		bookRepository.save(book);
		Book bookObj= bookRepository.findById(id).get();
		
		bookObj.setName("the success");
		bookObj.setTitle("success not for ward");
		
		Book updateBook = bookRepository.save(bookObj);
		
		Assertions.assertThat(updateBook.getName()).isNotNull();
		Assertions.assertThat(updateBook.getTitle()).isNotNull();
	}
	
	@Test
	public void deleteBookTest() {
		Book book=Book.builder().name("ramayan").title("love and mistry").build();
		
		bookRepository.save(book);
		Book bookObj= bookRepository.findById(book.getId()).get();
		bookRepository.delete(bookObj);
		
		Optional<Book> bookReturn = bookRepository.findById(book.getId());
	    Assertions.assertThat(bookReturn).isEmpty();
	}
}
