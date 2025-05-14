package sn.ept.git.seminaire.cicd.mappers;

import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.entities.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public   interface TagMapper extends GenericMapper<Tag, TagDTO> {

}