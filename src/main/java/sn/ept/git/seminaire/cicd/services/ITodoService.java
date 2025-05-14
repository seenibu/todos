package sn.ept.git.seminaire.cicd.services;

import sn.ept.git.seminaire.cicd.models.TodoDTO;
import sn.ept.git.seminaire.cicd.services.generic.GenericService;


public interface ITodoService extends GenericService<TodoDTO, String> {

    TodoDTO complete(String id);
}
