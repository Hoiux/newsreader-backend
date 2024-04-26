package org.hoiux.newsreader.repository;

import org.hoiux.newsreader.entity.Filter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface FilterRepository extends CrudRepository<Filter, Long> {

    List<Filter> findByContent(String content);

}