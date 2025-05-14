package sn.ept.git.seminaire.cicd.mappers;

import sn.ept.git.seminaire.cicd.models.TodoDTO;
import sn.ept.git.seminaire.cicd.entities.Todo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoMapper extends GenericMapper<Todo, TodoDTO> {

}