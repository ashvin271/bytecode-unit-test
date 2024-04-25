package com.bytecoder.contoller;


import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bytecoder.dto.BookDto;
import com.bytecoder.model.Book;
import com.bytecoder.service.BookService;
import com.bytecoder.util.ResponseMessage;

@RestController
@RequestMapping("/api")
public class BookController {
	
	@Autowired
	private BookService bookService;

	
	@PostMapping("/save")
	public ResponseMessage<Book>  authenticateUser(@Validated @RequestBody Book book){
		try {
			Book bookObj = bookService.saveBook(book);
			if(Objects.isNull(bookObj.getId())) {
				return new ResponseMessage<>(false, "Book not created", null);
			}
			return new ResponseMessage<>(true, "Successfully created", bookObj);
		}catch(Exception e) {
			return new ResponseMessage<>(false, "error occured while creating book", null);
		}
	}
	
	@GetMapping("/get-books")
	public ResponseMessage<BookDto> getAllBooks( @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
		try {
			BookDto bookList = bookService.getAllBooks(pageNo,pageSize);
			if(bookList.getContent().isEmpty()) {
				return new ResponseMessage<BookDto>(false, "Books not found", bookList);
			}
			return new ResponseMessage<BookDto>(true, "Successfully featched", bookList);
		}catch(Exception e) {
			return new ResponseMessage<BookDto>(false, "error occured while featching books", new ArrayList<>());
		}
	}
	
	   @GetMapping("get-book/{id}")
	    public ResponseMessage<Book> getBook(@PathVariable Long id) {
		   try {
			   Book book = bookService.getBook(id);
				if(Objects.isNull(book.getId())) {
					return new ResponseMessage<>(false, "Book not found", null);
				}
				return new ResponseMessage<>(true, "Successfully featched", book);
			}catch(Exception e) {
				return new ResponseMessage<>(false, "error occured while getting book", null);
			}

	    }
	   
	   @DeleteMapping("delete-book/{id}")
	    public ResponseMessage<Book> deleteBook(@PathVariable Long id) {
		   try {
			   Long value = bookService.deleteBook(id);
				if(Objects.isNull(value)) {
					return new ResponseMessage<>(false, "Book not found",null);
				}
				return new ResponseMessage<>(true, "Successfully deleted", null);
			}catch(Exception e) {
				return new ResponseMessage<>(false, "error occured while delete book", null);
			}
	    }
	   
	   @PutMapping("update-book/{id}")
	   public ResponseMessage<Book>  updateBook(@Validated @RequestBody Book book,@PathVariable("id") Long id){
			try {
				Book bookObj = bookService.updateBook(book,id);
				if(Objects.isNull(bookObj.getId())) {
					return new ResponseMessage<>(false, "Book not update", null);
				}
				return new ResponseMessage<>(true, "Successfully update", bookObj);
			}catch(Exception e) {
				return new ResponseMessage<>(false, "error occured while upted book", null);
			}
		}
}
