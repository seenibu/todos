package sn.ept.git.seminaire.cicd.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import sn.ept.git.seminaire.cicd.data.TagDTOTestData;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.mappers.TagMapper;
import sn.ept.git.seminaire.cicd.entities.Tag;

import static org.assertj.core.api.Assertions.assertThat;

class TagDTOMapperTest {

    TagDTO dto;
    Tag entity;

    private final TagMapper mapper =  Mappers.getMapper(TagMapper.class);


    @BeforeEach
    void setUp() {
        dto = TagDTOTestData.defaultDTO();
    }

    @Test
    void toEntityShouldReturnCorrectEntity() {
        entity = mapper.toEntity(dto);
        assertThat(entity)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("todos")
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
                .ignoringFields("todos")
                .ignoringFieldsMatchingRegexes("^_")//just to discover
                .isEqualTo(entity);

    }
}