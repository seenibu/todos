package sn.ept.git.seminaire.cicd.mappers;

import java.util.List;

public interface GenericMapper<E, D> {
    E toEntity(D d);

    D toDTO(E e);

    default List<E> toEntitiesList(List<D> dList){
        return dList.stream().map(this::toEntity).toList();
    }

    default List<D> toDTOlist(List<E> eList){
        return eList.stream().map(this::toDTO).toList();
    }

}