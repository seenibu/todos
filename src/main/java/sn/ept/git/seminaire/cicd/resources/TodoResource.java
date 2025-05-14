package sn.ept.git.seminaire.cicd.resources;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import sn.ept.git.seminaire.cicd.models.TodoDTO;
import sn.ept.git.seminaire.cicd.services.ITodoService;
import sn.ept.git.seminaire.cicd.utils.LogUtils;
import sn.ept.git.seminaire.cicd.utils.ResponseUtil;
import sn.ept.git.seminaire.cicd.utils.UrlMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
public class TodoResource {

    public static final String CLASS_NAME="TodoResource";
    private final ITodoService service;

    @GetMapping(UrlMapping.Todo.ALL)
    public ResponseEntity<Page<TodoDTO>> findAll(
            @PageableDefault Pageable page
    ) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "findAll");
        Page<TodoDTO> result = service.findAll(page);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(UrlMapping.Todo.FIND_BY_ID)
    public ResponseEntity<TodoDTO> findById(@PathVariable ("id") String id) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "findById");
        return ResponseUtil.wrapOrNotFound(service.findById(id),HttpStatus.OK);
    }

    @PostMapping(UrlMapping.Todo.ADD)
    public ResponseEntity<TodoDTO> create(@Valid @RequestBody TodoDTO dto) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "create");
        TodoDTO created = service.save(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping(UrlMapping.Todo.DELETE)
    public ResponseEntity<TodoDTO> delete(@PathVariable("id") String id) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "delete");
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(UrlMapping.Todo.UPDATE)
    public ResponseEntity<TodoDTO> update(
            @PathVariable("id") String id,
            @RequestBody @Valid TodoDTO dto) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "update");
        final TodoDTO updateDTO = service.update(id, dto);
        Optional<TodoDTO> optional = Optional.ofNullable(updateDTO);
        return ResponseUtil.wrapOrNotFound(optional,HttpStatus.ACCEPTED);
    }

    @GetMapping(UrlMapping.Todo.COMPLETE)
    public ResponseEntity<TodoDTO> complete(@PathVariable("id") String id) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "complete");
        final TodoDTO dto = service.complete(id);
        Optional<TodoDTO> optional = Optional.ofNullable(dto);
        return ResponseUtil.wrapOrNotFound(optional,HttpStatus.ACCEPTED);
    }
}
