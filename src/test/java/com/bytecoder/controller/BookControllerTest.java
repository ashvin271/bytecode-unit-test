package com.bytecoder.controller;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bytecoder.contoller.BookController;
import com.bytecoder.dto.BookDto;
import com.bytecoder.model.Book;
import com.bytecoder.service.BookService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

	@MockBean
	private BookService bookService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Book book;
	
	 @BeforeEach
	 public void init() {
		 book=book.builder().id(1l).name("ramayan").title("love and mistry").build();
	    }
	 	 
	 @Test
	 public void saveBooksTest() throws Exception {
		 given(bookService.saveBook(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

	        ResultActions response = mockMvc.perform(post("/api/save")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(book)));
	        response.andExpect(MockMvcResultMatchers.status().isOk())
	                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(true)))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(book.getId()))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(book.getName()))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(book.getTitle()));	
	        
	        assertNotNull(response);
	        String jsonResponse = response.andReturn().getResponse().getContentAsString();
	        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
	        String dataString = jsonNode.get("data").toString();
	        Book responseBook = objectMapper.readValue(dataString, Book.class);
	        assertEquals(responseBook,book);
	 }
	 
	 @Test
	 public void saveBooksTest_Blank_Data() throws Exception {
		 given(bookService.saveBook(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

	        ResultActions response = mockMvc.perform(post("/api/save")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(new Book())));
	        response.andExpect(MockMvcResultMatchers.status().isOk())
	                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(false)))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(nullValue()));
	        
	        assertNotNull(response);
	 }
	 
	 @Test
	 public void saveBooksTest_Exception_Data() throws Exception {
		  given(bookService.saveBook(ArgumentMatchers.any())).willThrow(new RuntimeException("Simulated error"));
	        ResultActions response = mockMvc.perform(post("/api/save")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(book)));
	        response.andExpect(MockMvcResultMatchers.status().isOk())
	                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(false)))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("error occured while creating book"));	
	        
	        assertNotNull(response);
	 }
	 
	 @Test
	 public void getAllBooksTest() throws Exception {
		 BookDto bookDto=BookDto.builder().pageNo(1).last(true).pageSize(10).content(Arrays.asList(book)).build();
		 when(bookService.getAllBooks(1, 10)).thenReturn(bookDto);
		 
		 ResultActions result = mockMvc.perform(get("/api/get-books")
				 .contentType(MediaType.APPLICATION_JSON)
				 .param("pageNo", "1")
				 .param("pageSize", "10"));
		 
		 result.andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(true)))
         .andExpect(MockMvcResultMatchers.jsonPath("$.data.content.size()",CoreMatchers.is(bookDto.getContent().size())));
	 }
	 
	 @Test
	 public void getAllBooksTest_For_Blank_Data() throws Exception {
		 BookDto bookDto=BookDto.builder().pageNo(1).last(true).pageSize(10).content(Arrays.asList()).build();
		 when(bookService.getAllBooks(1, 10)).thenReturn(bookDto);
		 
		 ResultActions result = mockMvc.perform(get("/api/get-books")
				 .contentType(MediaType.APPLICATION_JSON)
				 .param("pageNo", "1")
				 .param("pageSize", "10"));
		 
		 result.andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(false)))
         .andExpect(MockMvcResultMatchers.jsonPath("$.data.content.size()",CoreMatchers.is(bookDto.getContent().size())));
	 }
	 
	 @Test
	 public void getAllBooksTest_For_Exception() throws Exception {
		 when(bookService.getAllBooks(1, 10)).thenThrow(new RuntimeException("error in getting data"));
		 
		 ResultActions result = mockMvc.perform(get("/api/get-books")
				 .contentType(MediaType.APPLICATION_JSON)
				 .param("pageNo", "1")
				 .param("pageSize", "10"));
		 
		 result.andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(false)))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("error occured while featching books"));	
	 }
	 
	 @Test
	 public void getBookTest() throws Exception {
		 Long id = 1l;
		 when(bookService.getBook(id)).thenReturn(book);
		 
		 ResultActions result = mockMvc.perform(get("/api/get-book/1")
				 .contentType(MediaType.APPLICATION_JSON));
		 
		 result.andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(true)))
         .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(book.getId()))
         .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(book.getName()))
         .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(book.getTitle()));	
		 assertNotNull(result);
	 }
	 
	 @Test
	 public void getBookTest_For_Blank_Data() throws Exception {
		 Long id = 1l;
		 when(bookService.getBook(id)).thenReturn(new Book());
		 
		 ResultActions result = mockMvc.perform(get("/api/get-book/1")
				 .contentType(MediaType.APPLICATION_JSON));
		 
		 result.andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(false)))
         .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(nullValue()));
		 assertNotNull(result);
	 }
	 
	 @Test
	 public void getBookTest_For_Exception_Data() throws Exception {
		 Long id = 1l;
		 when(bookService.getBook(id)).thenThrow(new RuntimeException("error Occured"));
		 
		 ResultActions result = mockMvc.perform(get("/api/get-book/1")
				 .contentType(MediaType.APPLICATION_JSON));
		 
		 result.andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(false)))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("error occured while getting book"))
         .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(nullValue()));
		 assertNotNull(result);
	 }
	 
	 @Test
	 public void updateBookTest() throws Exception {
		 given(bookService.updateBook(ArgumentMatchers.any(),ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));
		 ResultActions result = mockMvc.perform(put("/api/update-book/1")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(objectMapper.writeValueAsString(book)));
		 
		 result.andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(true)))
         .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(book.getId()))
         .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(book.getName()))
         .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(book.getTitle()));	
		 assertNotNull(result);
	 }
	 
	 @Test
	 public void updateBookTest_For_Blank_Data() throws Exception {
		 given(bookService.updateBook(ArgumentMatchers.any(),ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));
		 ResultActions result = mockMvc.perform(put("/api/update-book/1")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(objectMapper.writeValueAsString(new Book())));
		 
		 result.andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(false)))
         .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(nullValue()));	
		 assertNotNull(result);
	 }
	 
	 @Test
	 public void updateBookTest_Exception_Data() throws Exception {
		 given(bookService.updateBook(ArgumentMatchers.any(),ArgumentMatchers.any())).willThrow(new RuntimeException("Error occured"));
		 ResultActions result = mockMvc.perform(put("/api/update-book/1")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(objectMapper.writeValueAsString(book)));
		 
		 result.andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(false)))
         .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(nullValue()))
         .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("error occured while upted book"));	
		 assertNotNull(result);
	 }
	 
	 @Test
	 public void deletBookTest() throws Exception {
		 Long id = 1l;
		 when(bookService.deleteBook(id)).thenReturn(1l);
		 
		 ResultActions result = mockMvc.perform(delete("/api/delete-book/1")
				 .contentType(MediaType.APPLICATION_JSON));
		 
		 result.andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(true)));	
		 assertNotNull(result);
	 }
	 
	 @Test
	 public void deletBookTest_wrong_Id() throws Exception {
		 Long id = 1l;
		 when(bookService.deleteBook(id)).thenReturn(null);
		 
		 ResultActions result = mockMvc.perform(delete("/api/delete-book/1")
				 .contentType(MediaType.APPLICATION_JSON));
		 
		 result.andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(false)));	
		 assertNotNull(result);
	 }
	 
	 @Test
	 public void deletBookTest_For_Exception() throws Exception {
		 Long id = 1l;
		 when(bookService.deleteBook(id)).thenThrow(new RuntimeException("error Occured"));
		 
		 ResultActions result = mockMvc.perform(delete("/api/delete-book/1")
				 .contentType(MediaType.APPLICATION_JSON));
		 
		 result.andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(false)))
		 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("error occured while delete book"));	
		 assertNotNull(result);
	 }
}
