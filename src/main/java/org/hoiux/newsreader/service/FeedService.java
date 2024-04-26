package org.hoiux.newsreader.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.hoiux.newsreader.entity.Category;
import org.hoiux.newsreader.entity.Channel;
import org.hoiux.newsreader.entity.Filter;
import org.hoiux.newsreader.entity.Item;
import org.hoiux.newsreader.repository.CategoryRepository;
import org.hoiux.newsreader.repository.ChannelRepository;
import org.hoiux.newsreader.repository.FilterRepository;
import org.hoiux.newsreader.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndImage;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

@Service
public class FeedService {

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    ItemRepository itemsRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    FilterRepository filterRepository;

    static Map<Channel, List<Item>> EMPTY_FEED = Collections.emptyMap();

    SentenceDetectorME sentenceDetector = null;

    List<String> filterList = new ArrayList<>();

    // --------------------------------------------------------------------------------------------
    // Constructor
    public FeedService() {

        createSentenceDetector();
    }

    // --------------------------------------------------------------------------------------------
    private void createSentenceDetector() {

        InputStream inputStream = getClass().getResourceAsStream("/models/en-sent.bin");
        if (inputStream == null) {
            return;
        }

        try {
            SentenceModel sentenceModel = new SentenceModel(inputStream);
            sentenceDetector = new SentenceDetectorME(sentenceModel);
        } catch (IOException e) {
            // TODO log error message!
        }
    }

    // --------------------------------------------------------------------------------------------
    public Map<String, List<URL>> initialiseChannels() {

        Map<String, List<URL>> rssChannels = new HashMap<>();

        List<URL> worldNewsUrls = new ArrayList<>();
        // List<URL> sportUrls = new ArrayList<>();
        // List<URL> politicsUrls = new ArrayList<>();
        // List<URL> entertainmentUrls = new ArrayList<>();
        // List<URL> businessUrls = new ArrayList<>();
        // List<URL> healthUrls = new ArrayList<>();

        try {
            worldNewsUrls.add(new URL("https://rss.nytimes.com/services/xml/rss/nyt/World.xml"));
            // worldNewsUrls.add("https://feeds.bbci.co.uk/news/world/rss.xml");
            // worldNewsUrls.add("https://feeds.skynews.com/feeds/rss/world.xml");
            // worldNewsUrls.add("http://rss.cnn.com/rss/edition_world.rss");
            // worldNewsUrls.add("https://feeds.skynews.com/feeds/rss/world.xml");
            // worldNewsUrls.add("https://www.gbnews.com/feeds/news.rss ");

            // sportUrls.add("https://www.gbnews.com/feeds/news.rss ");
            // sportUrls.add("https://www.standard.co.uk/sport/rss");
            // sportUrls.add("https://www.independent.co.uk/sport/rss");
            // sportUrls.add("https://www.gbnews.com/feeds/sport.rss");
            // sportUrls.add("https://deadspin.com/rss");
            // sportUrls.add("https://news.sportslogos.net/feed/");

            // politicsUrls.add("https://www.huffingtonpost.co.uk/feeds/index.xml");
            // politicsUrls.add("https://feeds.feedburner.com/guidofawkes");
            // politicsUrls.add("https://www.politico.eu/uk/rss");
            // politicsUrls.add("https://www.politicshome.com/foreign-affairs/rss");
            // politicsUrls.add("https://www.realclearpolitics.com/index.xml");
            // politicsUrls.add("https://dailykos.com/blogs/main.rss");

            // entertainmentUrls.add("https://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml");
            // entertainmentUrls.add("http://rss.cnn.com/rss/money_media.rss");
            // entertainmentUrls.add("https://www.tmz.com/rss.xml");
            // entertainmentUrls.add("https://www.etonline.com/news/rss");
            // entertainmentUrls.add("https://www.thehollywoodgossip.com/rss.xml");
            // entertainmentUrls.add("https://www.usmagazine.com/feed");

            // businessUrls.add("https://feeds.bbci.co.uk/news/business/rss.xml");
            // businessUrls.add("http://rss.cnn.com/rss/money_news_companies.rss");
            // businessUrls.add("https://www.theguardian.com/uk/business/rss");
            // businessUrls.add("https://www.cnbc.com/id/19746125/device/rss/rss.xml");
            // businessUrls.add("https://www.ft.com/rss/home/uk");
            // businessUrls.add("https://seekingalpha.com/market_currents.xml");

            // healthUrls.add("http://rssfeeds.webmd.com/rss/rss.aspx?RSSSource=RSS_PUBLIC");
            // healthUrls.add("https://feeds.bbci.co.uk/news/health/rss.xml");
            // healthUrls.add("https://www.wellnessmama.com/feed");
            // healthUrls.add("https://rss.nytimes.com/services/xml/rss/nyt/Health.xml");
            // healthUrls.add("https://chaski.huffpost.com/us/auto/vertical/healthy-living");
            // healthUrls.add("https://www.mobihealthnews.com/feed");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        rssChannels.put("World News", worldNewsUrls);
        // rssChannels.put("Sport", sportUrls);
        // rssChannels.put("Politics", politicsUrls);
        // rssChannels.put("Media & Entertainment", entertainmentUrls);
        // rssChannels.put("Business", businessUrls);
        // rssChannels.put("Health", healthUrls);

        return rssChannels;
    }

    // --------------------------------------------------------------------------------------------
    private Channel buildChannel(SyndFeed syndFeed) {

        Channel channel = new Channel();

        channel.setTitle(syndFeed.getTitle());
        channel.setLink(syndFeed.getLink());

        // If no description then use title as description instead.
        if (syndFeed.getDescription().isEmpty()) {
            channel.setDescription(syndFeed.getTitle());
        } else {
            channel.setDescription(syndFeed.getDescription());
        }

        SyndImage img = syndFeed.getImage();
        if (img != null && img.getLink() != null)  {
            channel.setImage(img.getUrl());
        }

        SyndImage ico = syndFeed.getIcon();
        if (ico != null && ico.getLink() != null)  {
            channel.setIcon(ico.getUrl());
        }

        return channel;
    }

    // --------------------------------------------------------------------------------------------
    private List<Item> buildItemList(SyndFeed syndFeed, Channel channel) {

        // Populate an Item and add it to the
        // collection of Items.
        List<Item> items = new ArrayList<>();

        for (SyndEntry e : syndFeed.getEntries()) {

            Item item = new Item();
            item.setChanId(channel.getId()); // set foreign key
            item.setTitle(e.getTitle());
            item.setLink(e.getLink());

            // If no description then use title as description instead.
            if (e.getDescription().getValue().isEmpty()) {
                item.setDescription(e.getTitle());
            } else {
                item.setDescription(e.getDescription().getValue());
            }

            item.setPubDate(e.getPublishedDate());
            item.setIsRead(false);
            item.setIsVisible(true);
            items.add(item);
        }
        return items;
    }

    // --------------------------------------------------------------------------------------------
    public Map<Channel, List<Item>> getAllChannelsAndItems() {

        Map<Channel, List<Item>> allChannelsAndItems = new HashMap<>();

        List<Channel> allChannels = channelRepository.findAll();

        for (Channel channel : allChannels) {

            List<Item> items = itemsRepository.findAllItemsByChanId(channel.getId());
            allChannelsAndItems.put(channel, items);
        }

        return allChannelsAndItems;

    }

    // --------------------------------------------------------------------------------------------
    // Return the existing Channel and all its Items given a Channel id.
    public Map<Channel, List<Item>> getExistingFeed(Long id) {

        // Get existing Channel, if any.
        Channel channel = channelRepository.findChannelById(id);
        if (channel == null) {
            return EMPTY_FEED;
        }

        // Get the channel's items.
        List<Item> items = itemsRepository.findAllItemsByChanId(channel.getId());

        // Return the Channel and its Items.
        Map<Channel, List<Item>> channelAndItems = new HashMap<>();

        channelAndItems.put(channel, items);

        return channelAndItems;
    }

    // --------------------------------------------------------------------------------------------
    // Return the existing Channel and all its Items given the original RSS url.
    public Map<Channel, List<Item>> getExistingFeed(URL url) {

        // Get existing Channel, if any.
        Channel channel = channelRepository.findChannelBySource(url.toString());
        if (channel == null) {
            return EMPTY_FEED;
        }

        // Get the channel's items.
        List<Item> items = itemsRepository.findAllItemsByChanId(channel.getId());

        // Return the Channel and its Items.
        Map<Channel, List<Item>> channelAndItems = new HashMap<>();

        channelAndItems.put(channel, items);

        return channelAndItems;
    }

    // --------------------------------------------------------------------------------------------
    public Map<Channel, List<Item>> refreshFeed(URL url) {

        // Delete existing feed.
        Channel channel = channelRepository.findChannelBySource(url.toString());
        if (channel == null) {
            return EMPTY_FEED;
        }

        // Keep category id to restore it into the new Channel in addNewFeed().
        Long catId = channel.getCatId();

        this.deleteFeed(channel.getId());

        // Add feed again as a new feed.
        return this.addNewFeed(url, catId);
    }

    // --------------------------------------------------------------------------------------------
    // Delete a whole feed based on a Channel id.
    @SuppressWarnings("null")
    public void deleteFeed(Long id) {

        // Delete all the Items then the Channel.

        List<Item> items = itemsRepository.findAllItemsByChanId(id);
        itemsRepository.deleteAll(items);

        channelRepository.deleteById(id);
    }

    // --------------------------------------------------------------------------------------------
    // Adds a new feed to the database and returns the Channel and all its Items.
    public Map<Channel, List<Item>> addNewFeed(URL url, Long catId) {

        // Return any existing feed.
        Map<Channel, List<Item>> channelAndItems = this.getExistingFeed(url);
        if (channelAndItems.size() > 0) {
            return channelAndItems;
        }

        // Feed doesn't exist yet. Make one and add it to the database.

        channelAndItems = new HashMap<Channel, List<Item>>();

        try {
            // This async HTTP code was suggested as the correct way to stream
            // an RSS feed into the Rome RSS parser. From this Rome GitHub page:
            // https://github.com/rometools/rome/issues/276#issuecomment-557935844

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(url.toURI()).build();

            CompletableFuture<HttpResponse<InputStream>> fut = client.sendAsync(request,
                    BodyHandlers.ofInputStream());

            HttpResponse<InputStream> response = fut.get();

            if (response.statusCode() == HttpURLConnection.HTTP_OK) {

                SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(response.body()));

                // Populate a channel and save it to the database.
                Channel channel = this.buildChannel(syndFeed);
                channel.setSource(url.toString());
                if (catId != null) {
                    channel.setCatId(catId);
                }
                channel = channelRepository.save(channel);

                // Now populate the collection of items (if any)
                // and save them to the database.
                List<SyndEntry> entries = syndFeed.getEntries();
                if (!entries.isEmpty()) {

                    List<Item> items = this.buildItemList(syndFeed, channel);

                    for (Item item : items) {

                        // Apply keyword filtering and save to database.
                        item = applyFilters(item);
                        itemsRepository.save(item);
                    }

                    channelAndItems.put(channel, items);
                } else {

                    channelAndItems.put(channel, new ArrayList<Item>());
                }
            }
        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return channelAndItems;
    }

    // --------------------------------------------------------------------------------------------
    @SuppressWarnings("null")
    public Category addNewCategory(Category category) {

        // Don't add an additional Category with the same name.
        if (category.getId() != null && categoryRepository.existsById(category.getId())) {
            return category;
        }

        return categoryRepository.save(category);
    }

    // --------------------------------------------------------------------------------------------
    @SuppressWarnings("null")
    public Long deleteCategory(Long id) {

        if (!categoryRepository.existsById(id)) {
            return null;
        }

        categoryRepository.deleteById(id);

        // Remove this category from any channels.
        List<Channel> channels = channelRepository.findByCatId(id);
        for (Channel channel : channels) {
            channel.setCatId(null);
            channelRepository.save(channel);
        }

        return id;
    }

    // --------------------------------------------------------------------------------------------
    @SuppressWarnings("null")
    public Long renameCategory(Category category) {

        Optional<Category> opt = categoryRepository.findById(category.getId());
        if (!opt.isPresent()) {
            return null;
        }

        Category origCategory = opt.get();
        if (!origCategory.getId().equals(category.getId())) {
            return null;
        }

        Category savedCategory = categoryRepository.save(category);

        return savedCategory.getId();
    }

    // --------------------------------------------------------------------------------------------
    public List<Category> getAllCategories() {

        return categoryRepository.findAll();
    }

    // --------------------------------------------------------------------------------------------
    @SuppressWarnings("null")
    // Sets the category for the channel. Returns <bool, bool> as the result.
    // The left hand bool is true if the category exists.
    // The right hand bool is true if the channel exists.
    // <true, true> is the only valid success result.
    public Pair<Boolean, Boolean> setCategoryForChannel(Long idCategory, Long idChannel) {

        boolean isCategory = true;

        if (idCategory != null) {
            // Does Category exist?
            isCategory = categoryRepository.existsById(idCategory);
            if (!isCategory) {
                return new ImmutablePair<>(isCategory, false);
            }
        }

        // Does Channel exist?
        Optional<Channel> opt = channelRepository.findById(idChannel);
        boolean isChannel = opt.isPresent();
        if (!isChannel) {
            return new ImmutablePair<>(isCategory, isChannel);
        }

        // Set category into channel.
        Channel channel = opt.get();
        channel.setCatId(idCategory);
        channelRepository.save(channel);

        return new ImmutablePair<>(isCategory, isChannel);
    }

    // --------------------------------------------------------------------------------------------
    @SuppressWarnings("null")
    public List<Item> updateItems(List<Item> items) {

        // Find a better way to do this. Processing each item one-by-one is really
        // inefficient.

        List<Item> modifiedItems = new ArrayList<>();

        for (Item item : items) {

            Optional<Item> opt = itemsRepository.findById(item.getId());

            // Only update existing Items.
            if (opt.isPresent()) {

                Item dbItem = opt.get();

                // Only the isRead and isVisible properties are alterable.
                if (item.getIsRead() != null) {
                    dbItem.setIsRead(item.getIsRead());
                }

                if (item.getIsVisible() != null) {
                    dbItem.setIsVisible(item.getIsVisible());
                }

                itemsRepository.save(dbItem);

                modifiedItems.add(dbItem);

            }
        }

        return modifiedItems;
    }

    // --------------------------------------------------------------------------------------------
    public Filter addNewFilter(Filter filter) {

        // Don't add an existing filter string.
        List<Filter> allFilters = this.filterRepository.findByContent(filter.getContent());

        for (Filter dbFilter : allFilters) {
            if (dbFilter.getContent().equals(filter.getContent())) {
                return dbFilter;
            }
        }

        // Filter is new. Add it.
        filter = this.filterRepository.save(filter);

        filterList.add(filter.getContent());

        updateFiltersForAllItems();

        return filter;
    }

    // --------------------------------------------------------------------------------------------
    public List<Filter> getAllFilters() {
        List<Filter> result = new ArrayList<>();
        this.filterRepository.findAll().forEach(result::add);
        return result;
    }

    // --------------------------------------------------------------------------------------------
    public boolean removeFilter(Long id) {

        boolean isDeleted = false;

        Optional<Filter> opt = this.filterRepository.findById(id);

        if (opt.isPresent()) {

            this.filterRepository.deleteById(id);

            isDeleted = true;

            Filter filter = opt.get();

            filterList.remove(filter.getContent());
        }

        updateFiltersForAllItems();

        return isDeleted;
    }

    // --------------------------------------------------------------------------------------------
    // private Item applyFilters(List<Item> items, Item item) throws IOException {

    // if (sentenceDetector == null) {
    // return item;
    // }

    // // Iterate through each item individually.
    // // If the item contains a filter keyword,
    // // mark it as invisible to the UI.

    // String[] sentences = sentenceDetector.sentDetect(items.toString());
    // if (Arrays.stream(sentences).anyMatch(keyword ->
    // filterList.contains(keyword))) {
    // item.setIsVisible(false);
    // }

    // return item;
    // }

    // ------------------------------------------------------------------------------------------
    Item applyFilters(Item item) {

        // Make item visible by default.
        item.setIsVisible(true);

        // Apply all filters. Filters are not case-sensitive.
        // "Greta Thunberg" is the same as "grETA thUNberg".

        for (String filter : filterList) {

            if (item.getTitle().toLowerCase().contains(filter.toLowerCase())) {

                item.setIsVisible(false);
                return item;
            }
        }

        return item;
    }

    // --------------------------------------------------------------------------------------------
    void updateFiltersForAllItems() {

        Map<Channel, List<Item>> allChannelsAndItems = this.getAllChannelsAndItems();

        for (Map.Entry<Channel, List<Item>> entry : allChannelsAndItems.entrySet()) {

            List<Item> items = entry.getValue();

            // Update visibilty of all Items and save back to the database.
            for (Item item : items) {
                applyFilters(item);
            }

            this.itemsRepository.saveAll(items);
        }
    }
}