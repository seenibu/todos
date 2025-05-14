package sn.ept.git.seminaire.cicd.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import sn.ept.git.seminaire.cicd.data.TestData;
import sn.ept.git.seminaire.cicd.data.TodoDTOTestData;
import sn.ept.git.seminaire.cicd.models.TodoDTO;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.mappers.TodoMapper;
import sn.ept.git.seminaire.cicd.entities.Todo;
import sn.ept.git.seminaire.cicd.repositories.TodoRepository;
import sn.ept.git.seminaire.cicd.services.impl.TodoServiceImpl;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TodoServiceTest   {
    @Mock
    TodoRepository todoRepository;
    @InjectMocks
    TodoServiceImpl service;

    private static TodoMapper mapper;

    TodoDTO dto ;
    private static final Map<String, Todo> fakeTodoDatabase = new HashMap<>();


    @BeforeAll
    static void beforeAll(){
        log.info(" before all");
        mapper= Mappers.getMapper(TodoMapper.class);
    }

    @BeforeEach
     void beforeEach(){
       log.info(" before each");
        ReflectionTestUtils.setField(service,"mapper",mapper);
        dto = TodoDTOTestData.defaultDTO();
        fakeTodoDatabase.clear();
    }

    private void mockSaveAndFlush() {
        Mockito.when(todoRepository.saveAndFlush(Mockito.any(Todo.class))).then(invocation->{
            Instant now = Instant.now();
            Todo todo =invocation.getArgument(0,Todo.class);
            String id =Optional.ofNullable(todo.getId()).orElse(UUID.randomUUID().toString());
            Todo toUpdate =Optional.ofNullable(fakeTodoDatabase.getOrDefault(id, null)).orElse(todo);
            toUpdate.setId(id);
            toUpdate.setTitle(todo.getTitle());
            toUpdate.setDescription(todo.getDescription());
            toUpdate.setCreatedDate(now);
            toUpdate.setLastModifiedDate(now);
            fakeTodoDatabase.put(id, toUpdate);
            return toUpdate;
        });
    }

    private void mockFindByTitle() {
        Mockito.when(todoRepository.findByTitle(Mockito.anyString())).then(invocation->{
            String title =invocation.getArgument(0,String.class);
            return fakeTodoDatabase
                    .values()
                    .stream()
                    .filter(todo->todo.getTitle().equals(title))
                    .findFirst();
        });
    }

    private void mockFindByTitleWithIdNotEquals() {
        Mockito.when(todoRepository.findByTitleWithIdNotEquals(Mockito.anyString(),Mockito.anyString())).then(invocation->{
                   String title =invocation.getArgument(0,String.class);
                   String id =invocation.getArgument(1,String.class);
                   return fakeTodoDatabase
                           .values()
                           .stream()
                           .filter(todo->todo.getTitle().equals(title))
                           .filter(todo->!todo.getId().equals(id))
                           .findFirst();
               });
    }


    private void mockFindAll() {
       Mockito.when(todoRepository.findAll())
                .thenAnswer(inv->List.copyOf(fakeTodoDatabase.values()));
    }
 private void mockFindAllPageable() {
     Mockito.when(todoRepository.findAll(Mockito.any(PageRequest.class)))
             .thenAnswer(inv->new PageImpl<>(
                     fakeTodoDatabase
                             .values()
                             .stream()
                             .toList())
             );

    }

    private void mockFindById() {
        Mockito.when(todoRepository.findById(Mockito.anyString())).thenAnswer(invocation->{
            String id =invocation.getArgument(0,String.class);
            return Optional.ofNullable(fakeTodoDatabase.getOrDefault(id,null));
        });
    }



    private void mockCount() {
        Mockito.when(todoRepository.count())
                .thenReturn(
                        (long) fakeTodoDatabase
                                .values()
                                .size()
                );
    }

    private void mockDeleteById() {

        Mockito.doAnswer( invocation->{
            String id =invocation.getArgument(0,String.class);
            fakeTodoDatabase.remove(id);
            return null;
        }).when(todoRepository).deleteById(Mockito.anyString());
    }
    @Test
    void save_shouldSaveTodo() {
        mockSaveAndFlush();
        mockFindByTitle();
        dto =service.save(dto);
        assertThat(dto)
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    void save_withSameTitle_shouldThrowException() {
        mockFindByTitle();
        mockSaveAndFlush();
        dto =service.save(dto);
        dto.setTitle(dto.getTitle());
        assertThrows(
                ItemExistsException.class,
                () -> service.save(dto)
        );
    }


    @Test
    void update_shouldSucceed() {
        mockSaveAndFlush();
        mockFindByTitle();
        mockFindByTitleWithIdNotEquals();
        mockFindById();
        dto =service.save(dto);
        dto.setTitle(TestData.Update.title);
        dto.setDescription(TestData.Update.description);
        dto =  service.update(dto.getId(), dto);
        assertThat(dto)
                .isNotNull()
                .hasFieldOrPropertyWithValue("title",dto.getTitle())
                .hasFieldOrPropertyWithValue("description",dto.getDescription());
    }


    @Test
    void update_withBadId_shouldThrowException() {
        mockFindById();
        String badId =UUID.randomUUID().toString();
        assertThrows(
                ItemNotFoundException.class,
                () ->service.update(badId, dto)
        );
    }

    @Test
    void update_withDuplicatedTitle_shouldThrowException() {
        mockSaveAndFlush();
        mockFindByTitle();
        mockFindByTitleWithIdNotEquals();
        mockFindById();
        dto =service.save(dto);
        TodoDTO dtoBis =service.save(TodoDTOTestData.updatedDTO());
        assertThrows(
                ItemExistsException.class,
                () ->service.update(dtoBis.getId(), dto)
        );
    }


    @Test
    void findAll_shouldReturnResult() {
        mockSaveAndFlush();
        mockFindAll();
         Todo todo = todoRepository.saveAndFlush(mapper.toEntity(dto));
         dto =mapper.toDTO(todo);
        final List<TodoDTO> all = service.findAll();
        assertThat(all)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(dto);
    }

    @Test
    void findAllPageable_shouldReturnResult() {
        mockSaveAndFlush();
        mockFindAllPageable();
        Todo todo = todoRepository.saveAndFlush(mapper.toEntity(dto));
        dto =mapper.toDTO(todo);
        final Page<TodoDTO> all = service.findAll(PageRequest.of(0,10));
        assertThat(all)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(dto);
    }

    @Test
    void findById_shouldReturnResult() {
        mockSaveAndFlush();
        mockFindById();
        dto =service.save(dto);
        final Optional<TodoDTO> optional = service.findById(dto.getId());
        assertThat(optional)
                .isNotNull()
                .isPresent()
                .get()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    void findById_withBadId_ShouldReturnNoResult() {
        mockFindById();
        final Optional<TodoDTO> optional = service.findById(UUID.randomUUID().toString());
        assertThat(optional)
                .isNotNull()
                .isNotPresent();
    }

    @Test
    void delete_shouldDeleteTodo() {
        mockFindByTitle();
        mockSaveAndFlush();
        mockFindById();
        mockDeleteById();
        dto = service.save(dto);
        String id = dto.getId();
        assertThat(todoRepository.findById(id)).isPresent();
        service.delete(id);
        assertThat(todoRepository.findById(id)).isEmpty();
    }




    @Test
    void delete_withBadId_ShouldThrowException() {
        mockFindById();
        String id = UUID.randomUUID().toString();
        assertThrows(
                ItemNotFoundException.class,
                () ->service.delete(id)
        );
    }

}