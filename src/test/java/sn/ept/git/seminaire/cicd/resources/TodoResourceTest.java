package sn.ept.git.seminaire.cicd.resources;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sn.ept.git.seminaire.cicd.data.TestData;
import sn.ept.git.seminaire.cicd.data.TodoDTOTestData;
import sn.ept.git.seminaire.cicd.models.TodoDTO;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.services.impl.TodoServiceImpl;
import sn.ept.git.seminaire.cicd.utils.TestUtil;
import sn.ept.git.seminaire.cicd.utils.UrlMapping;

import java.time.Instant;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(TodoResource.class)
class TodoResourceTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private TodoServiceImpl service;

    @Autowired
    private TodoResource todoResource;
    private TodoDTO dto;
     private TodoDTO vm;
    private static final  Instant now = Instant.now();
    private static final Map<String, TodoDTO> fakeTodoDatabase = new HashMap<>();


    @BeforeAll
    static void beforeAll() {
        log.info(" before all ");
    }

    @BeforeEach
    void beforeEach() {
        log.info(" before each ");
        service.deleteAll();
        vm = TodoDTOTestData.defaultDTO();
        fakeTodoDatabase.clear();
    }


    private void mockServiceUpdate() {
        Mockito.when(service.update(Mockito.anyString(),Mockito.any(TodoDTO.class)))
                .thenAnswer(invocation -> {
                    String id =invocation.getArgument(0,String.class);
                    TodoDTO todo =invocation.getArgument(1,TodoDTO.class);
                    TodoDTO toUpdate =fakeTodoDatabase.getOrDefault(id, null);
                    if(null == toUpdate){
                        throw new ItemNotFoundException();
                    }
                    toUpdate.setId(id);
                    toUpdate.setTitle(todo.getTitle());
                    toUpdate.setDescription(todo.getDescription());
                    toUpdate.setCreatedDate(now);
                    toUpdate.setLastModifiedDate(now);
                    fakeTodoDatabase.put(id, toUpdate);
                    return toUpdate;
                });
    }

    private void mockServiceSave() {
        Mockito.when(service.save(Mockito.any(TodoDTO.class)))
                .thenAnswer(invocation -> {
                    TodoDTO todo =invocation.getArgument(0,TodoDTO.class);
                    String id =UUID.randomUUID().toString();
                    todo.setId(id);
                    todo.setTitle(todo.getTitle());
                    todo.setDescription(todo.getDescription());
                    todo.setCreatedDate(now);
                    todo.setLastModifiedDate(now);
                    fakeTodoDatabase.put(id, todo);
                    return todo;
                });
    }

    private void mockServiceFindALL() {
        Mockito.when(service.findAll(Mockito.any(Pageable.class)))
                .thenAnswer(invocation -> new PageImpl<>(List.copyOf(fakeTodoDatabase.values())));
    }

    private void mockServiceFindById() {
        Mockito.when(service.findById(Mockito.any(String.class)))
                .thenAnswer(invocation ->{
                    String id =invocation.getArgument(0,String.class);
                    return Optional.ofNullable(fakeTodoDatabase.getOrDefault(id,null));
                });
    }

    private void mockServiceDelete() {
        Mockito.doAnswer(invocation ->{
                    String id =invocation.getArgument(0,String.class);
                    TodoDTO toDelete= fakeTodoDatabase.get(id);
                    if(null == toDelete){
                        throw new ItemNotFoundException();
                    }
                    fakeTodoDatabase.remove(id);
                    return null;
                }).when(service)
                .delete(Mockito.any(String.class));
    }


    @Test
    void findAll_shouldReturnTodos() throws Exception {
        mockServiceSave();
        mockServiceFindALL();
        dto = service.save(vm);
        mockMvc.perform(get(UrlMapping.Todo.ALL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content.[0].id").exists())
                .andExpect(jsonPath("$.content.[0].version").exists())
                .andExpect(jsonPath("$.content.[0].title", is(dto.getTitle())))
                .andExpect(jsonPath("$.content.[0].description").value(dto.getDescription()))
        ;

    }
    @Test
    void findById_shouldReturnTodo() throws Exception {
        mockServiceSave();
        mockServiceFindById();
        dto = service.save(vm);
        mockMvc.perform(get(UrlMapping.Todo.FIND_BY_ID, dto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.title", is(dto.getTitle())))
                .andExpect(jsonPath("$.description").value(dto.getDescription()))
        ;
    }

    @Test
    void findById_withBadId_shouldReturnNotFound() throws Exception {
        mockServiceFindById();
        mockMvc.perform(get(UrlMapping.Todo.FIND_BY_ID, UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void add_shouldCreateTodo() throws Exception {
        mockServiceSave();
        mockMvc.perform(
                        post(UrlMapping.Todo.ADD)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(vm))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.title").value(vm.getTitle()))
                .andExpect(jsonPath("$.description").value(vm.getDescription()))
        ;
    }


    @Test
    void update_shouldUpdateTodo() throws Exception {
        mockServiceSave();
        mockServiceUpdate();
        dto = service.save(vm);
        vm.setTitle(TestData.Update.title);
        vm.setDescription(TestData.Update.description);
        mockMvc.perform(
                        put(UrlMapping.Todo.UPDATE, dto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(vm))
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.title").value(vm.getTitle()))
                .andExpect(jsonPath("$.description").value(vm.getDescription()))
        ;
    }

    @Test
    void delete_shouldDeleteTodo() throws Exception {
        mockServiceSave();
        mockServiceDelete();
        dto = service.save(vm);
        mockMvc.perform(
                delete(UrlMapping.Todo.DELETE, dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }

    @Test
    void delete_withBadId_shouldReturnNotFound() throws Exception {
        mockServiceSave();
        mockServiceDelete();
        dto = service.save(vm);
        mockMvc.perform(
                delete(UrlMapping.Todo.DELETE, UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }
}