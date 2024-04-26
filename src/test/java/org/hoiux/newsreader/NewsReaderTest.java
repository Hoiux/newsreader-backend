package org.hoiux.newsreader;

import org.hoiux.newsreader.entity.Channel;
import org.hoiux.newsreader.entity.Item;
import org.hoiux.newsreader.repository.ChannelRepository;
import org.hoiux.newsreader.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class NewsReaderTest {

	@Autowired
	ChannelRepository channelRepository;
	@Autowired
	ItemRepository itemsRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testWriteToDatabase_NewChannelEntry() {
		Channel channel = new Channel();
		channel.setId(1L);
		channel.setDescription("channel_description");
		channel.setLink("channel_link");
		channel.setTitle("channel_title");
		channelRepository.save(channel);

		Item item1 = new Item();
		item1.setId(1L);
		item1.setDescription("item_description1");
		item1.setTitle("item_title1");
		item1.setLink("item_link1");
		item1.setPubDate(new Date());
		item1.setIsRead(false);
		item1.setChanId(channel.getId());
		itemsRepository.save(item1);

		Item item2 = new Item();
		item2.setId(2L);
		item2.setDescription("item_description2");
		item2.setTitle("item_title2");
		item2.setLink("item_link2");
		item2.setPubDate(new Date());
		item2.setIsRead(false);
		item2.setChanId(channel.getId());
		itemsRepository.save(item2);
	}

}
