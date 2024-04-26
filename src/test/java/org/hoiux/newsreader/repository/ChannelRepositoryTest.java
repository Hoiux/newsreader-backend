package org.hoiux.newsreader.repository;

import org.hoiux.newsreader.entity.Category;
import org.hoiux.newsreader.entity.Channel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ChannelRepositoryTest {

    @Autowired
    ChannelRepository channelRepository;
    private Channel channel;

    @BeforeEach
    public void setUp() {
        channel = new Channel(
                Mockito.anyLong(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyLong(),
                Mockito.anyString(),
                Mockito.anyString());
    }

    @Test
    public void testCreateChannel() {
        Channel saveChannel = channelRepository.save(channel);
        Assertions.assertNotNull(saveChannel);
    }

}