package sn.ept.git.seminaire.cicd.services.generic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenericService<D, I> {
    D save(D dto);

    void delete(I id);

    Optional<D> findById(I id);

    List<D> findAll();

    Page<D> findAll(Pageable pageable);

    D update(I id, D dto);

    void deleteAll();
}