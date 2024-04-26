package org.hoiux.newsreader.repository;

import org.hoiux.newsreader.entity.Category;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends ListCrudRepository<Category, Long> {

}
