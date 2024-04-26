package org.hoiux.newsreader.repository;

import java.util.List;

import org.hoiux.newsreader.entity.Item;    
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

    List<Item> findAllItemsByChanId(Long chanId);
}