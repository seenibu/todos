package sn.ept.git.seminaire.cicd.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import sn.ept.git.seminaire.cicd.data.TodoDTOTestData;
import sn.ept.git.seminaire.cicd.models.TodoDTO;
import sn.ept.git.seminaire.cicd.mappers.TodoMapper;
import sn.ept.git.seminaire.cicd.entities.Todo;

import static org.assertj.core.api.Assertions.assertThat;

class TodoDTOMapperTest {

    TodoDTO dto;
    Todo entity;

    private final TodoMapper mapper=  Mappers.getMapper(TodoMapper.class);

    @BeforeEach
    void setUp() {
        dto = TodoDTOTestData.defaultDTO();
    }

    @Test
    void toEntityShouldReturnCorrectEntity() {
        entity = mapper.toEntity(dto);
        assertThat(entity)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("tags")
                .isEqualTo(dto);
    }

    @Test
    void toDTOShouldReturnCorrectDTO() {
        entity = mapper.toEntity(dto);
        dto = mapper.toDTO(entity);
        assertThat(dto)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .ignoringFields("tags")
                .ignoringFieldsMatchingRegexes("^_")//just to discover
                .withEqualsForFields((idOne,idTwo)-> {
                    return  idOne instanceof String && idTwo.toString().equalsIgnoreCase(idOne.toString());
                },"id") //just to discover
                .isEqualTo(entity);

    }
}
