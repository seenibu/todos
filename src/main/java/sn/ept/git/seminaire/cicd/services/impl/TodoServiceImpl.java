package sn.ept.git.seminaire.cicd.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.ept.git.seminaire.cicd.models.TodoDTO;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.mappers.TodoMapper;
import sn.ept.git.seminaire.cicd.entities.Todo;
import sn.ept.git.seminaire.cicd.repositories.TodoRepository;
import sn.ept.git.seminaire.cicd.services.ITodoService;
import sn.ept.git.seminaire.cicd.utils.ExceptionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sn.ept.git.seminaire.cicd.utils.LogUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class TodoServiceImpl implements ITodoService {

    private final TodoRepository repository;
    private final TodoMapper mapper;
    public static final String CLASS_NAME="TodoServiceImpl";
    @Transactional
    @Override
    public TodoDTO save(TodoDTO dto) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "save");
         Optional<Todo> optional = repository.findByTitle(dto.getTitle());
        ExceptionUtils.absentOrThrow(optional, ItemExistsException.TITLE_EXISTS, dto.getTitle());
        dto.setId(UUID.randomUUID().toString());
        dto.setCompleted(false);
        return mapper.toDTO(repository.saveAndFlush(mapper.toEntity(dto)));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(String uuid) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "delete");
        final Optional<Todo> optional = repository.findById(uuid);
        if(optional.isEmpty()){
            throw new ItemNotFoundException(
                    ItemNotFoundException.format(ItemNotFoundException.TODO_BY_ID, uuid)
            );
        }
        repository.deleteById(uuid);
    }

    @Override
    public Optional<TodoDTO> findById(String uuid) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "findById");
        return repository
                .findById(uuid)
                .map(mapper::toDTO);
    }

    @Override
    public List<TodoDTO> findAll() {
        log.info(LogUtils.LOG_START,CLASS_NAME, "findAll");
        return repository
                .findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public Page<TodoDTO> findAll(Pageable pageable) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "findAll[Pageable]");
        return repository
                .findAll(pageable)
                .map(mapper::toDTO);
    }

    @Transactional
    @Override
    public TodoDTO update(String uuid, TodoDTO dto) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "update");
        Optional<Todo>  optional = repository.findById(uuid);
        ExceptionUtils.presentOrThrow(optional, ItemNotFoundException.TODO_BY_ID, dto.getId());
        Optional<Todo>  optionalTitle = repository.findByTitleWithIdNotEquals(dto.getTitle(),uuid);
        ExceptionUtils.absentOrThrow(optionalTitle, ItemExistsException.TITLE_EXISTS, dto.getTitle());
            final Todo item = optional.get();
            item.setTitle(dto.getTitle());
            item.setDescription(dto.getDescription());
            return mapper.toDTO(repository.saveAndFlush(item));
    }

    @Transactional
    @Override
    public void deleteAll() {
        log.info(LogUtils.LOG_START,CLASS_NAME, "deleteAll");
        repository.deleteAll();
    }

    @Override
    public TodoDTO complete(String uuid) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "complete");
        final Optional<Todo> optional = repository.findById(uuid);
        if(optional.isPresent()){
            final Todo todo = optional.get();
            todo.setCompleted(true);
            return mapper.toDTO(repository.saveAndFlush(todo));
        }
        throw new ItemNotFoundException(
                ItemNotFoundException.format(ItemNotFoundException.TODO_BY_ID, uuid)
        );
    }
}
