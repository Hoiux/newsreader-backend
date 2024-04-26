package org.hoiux.newsreader.repository;

import org.hoiux.newsreader.entity.Channel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChannelRepository extends ListCrudRepository<Channel, Long> {

    Channel findChannelById(Long id);

    Channel findChannelBySource(String source);

    List<Channel> findByCatId(Long catId);

}