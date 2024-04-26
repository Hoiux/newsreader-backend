package org.hoiux.newsreader.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.hoiux.newsreader.entity.Category;
import org.hoiux.newsreader.entity.Channel;
import org.hoiux.newsreader.entity.Item;
import org.hoiux.newsreader.entity.Filter;
import org.hoiux.newsreader.service.FeedService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
@RequestMapping("/api/v1/newsreader")
public class FeedController {

    @Autowired
    FeedService feedService;

    // --------------------------------------------------------------------------------------------
    // Converts objects to json strings.
    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    // Add a new RSS feed to the collection.
    @PostMapping(value = "/feed", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFeed(@RequestBody String rssUrlJson) {

        // Get the RSS source URL from the post body.
        String rssSource = new JSONObject(rssUrlJson).getString("rss_source");

        URL url;
        try {
            url = new URL(rssSource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().body(buildErrorJson(e.getMessage()));
        }

        Map<Channel, List<Item>> channelAndItems = feedService.addNewFeed(url, null);

        return ResponseEntity.ok(toJson(channelAndItems));
    }

    // --------------------------------------------------------------------------------------------
    // Delete a Channel and all its Items based on the given channel id.
    @DeleteMapping(value = "/feed/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteFeed(@PathVariable("id") Long id) {

        feedService.deleteFeed(id);

        return ResponseEntity.ok("{ \"id\": " + id + ",\n\"Message\": \"Feed deleted.\" }");
    }

    @PutMapping(value = "/feed", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshFeed(@RequestBody String rssUrlJson) {

        // Get the RSS source URL from the post body.
        String rssSource = new JSONObject(rssUrlJson).getString("rss_source");

        URL url;
        try {
            url = new URL(rssSource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().body(buildErrorJson(e.getMessage()));
        }

        Map<Channel, List<Item>> channelAndItems = feedService.refreshFeed(url);

        return ResponseEntity.ok(toJson(channelAndItems));
    }

    // --------------------------------------------------------------------------------------------
    // Return all saved Channels and their Items.
    @GetMapping(value = "/feed", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllFeeds() {

        Map<Channel, List<Item>> channelAndItems = feedService.getAllChannelsAndItems();

        return ResponseEntity.ok(toJson(channelAndItems));
    }

    // --------------------------------------------------------------------------------------------
    // Return a previously saved feed. The path variable is the feed id.
    @GetMapping(value = "/feed/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getExistingFeed(@PathVariable("id") Long id) {

        Map<Channel, List<Item>> channelAndItems = feedService.getExistingFeed(id);
        return ResponseEntity.ok(toJson(channelAndItems));
    }

    // --------------------------------------------------------------------------------------------
    @PatchMapping(value = "/feed/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> setCategoryForChannel(@RequestBody Category category, @PathVariable("id") Long chanId) {

        Pair<Boolean, Boolean> result = feedService.setCategoryForChannel(category.getId(), chanId);

        if (result.getLeft() == Boolean.FALSE) {
            return ResponseEntity.badRequest().body(buildErrorJson("Category does not exist."));
        }

        if (result.getRight() == Boolean.FALSE) {
            return ResponseEntity.badRequest().body(buildErrorJson("Channel does not exist."));
        }

        return ResponseEntity.ok(null);
    }

    // --------------------------------------------------------------------------------------------
    @PostMapping(value = "/category", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewCategory(@RequestBody Category category) {

        Category savedCategory = feedService.addNewCategory(category);

        return ResponseEntity.ok(savedCategory);
    }

    // --------------------------------------------------------------------------------------------
    @DeleteMapping(value = "/category/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {

        Long currId = feedService.deleteCategory(id);
        if (currId == null) {
            return ResponseEntity.badRequest().body(buildErrorJson("Category does not exist."));
        }

        return ResponseEntity.ok(null);
    }

    // --------------------------------------------------------------------------------------------
    @PutMapping(value = "/category", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> renameCategory(@RequestBody Category category) {

        Long currId = feedService.renameCategory(category);
        if (currId == null) {
            return ResponseEntity.badRequest().body(buildErrorJson("Category does not exist."));
        }

        return ResponseEntity.ok(null);
    }

    // --------------------------------------------------------------------------------------------
    @GetMapping(value = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllCategories() {

        List<Category> categories = feedService.getAllCategories();

        return ResponseEntity.ok(categories);
    }

    // --------------------------------------------------------------------------------------------
    @PatchMapping(value = "/item", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateItems(@RequestBody List<Item> items) {

        List<Item> updatedItems = feedService.updateItems(items);

        return ResponseEntity.ok(updatedItems);
    }

    // --------------------------------------------------------------------------------------------
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFilters() {

        List<Filter> filters = feedService.getAllFilters();

        return ResponseEntity.ok(filters);
    }

    // --------------------------------------------------------------------------------------------
    @PostMapping(value = "/filter", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFilter(@RequestBody Filter filter) {

        filter = feedService.addNewFilter(filter);

        return ResponseEntity.ok(filter);
    }

    // --------------------------------------------------------------------------------------------
    @DeleteMapping("/filter/{id}")
    public ResponseEntity<?> removeFilter(@PathVariable("id") Long id) {

        boolean isDeleted = feedService.removeFilter(id);

        return ResponseEntity.ok(isDeleted);
    }

    // --------------------------------------------------------------------------------------------
    // Convert the whole feed to json.
    private String toJson(Map<Channel, List<Item>> channelAndItems) {

        // Have to do this manually because Jackson ObjectWriter is
        // too dumb to parse Map<Channel, List<Item>> by itself.

        try {
            String channelsAndItemsJson = "{ \"feeds\": [";

            long i = 1L;
            for (Map.Entry<Channel, List<Item>> entry : channelAndItems.entrySet()) {

                Channel ch = entry.getKey();
                String channelJson = String.format("\"id\": %d,%n\"source\": \"%s\",%n\"link\": \"%s\",%n\"description\": \"%s\",%n\"title\": \"%s\",%n\"catId\": %d,%n\"image\": \"%s\",%n\"icon\": \"%s\"",
                    ch.getId(),
                    ch.getSource(),
                    ch.getLink(),
                    ch.getDescription(),
                    ch.getTitle(),
                    // use -1 as an invalid category id because JSON does not support deserialzing null.
                    ch.getCatId() == null ? -1L : ch.getCatId(),
                    ch.getImage(),
                    ch.getIcon()); 

                String itemsJson = objectWriter.writeValueAsString(entry.getValue());

                channelsAndItemsJson += String.format("{ %s,%n\"items\": %s%n}", channelJson, itemsJson);

                if (i < channelAndItems.size()) {
                    channelsAndItemsJson += ",";
                }

                i++;
            }

            channelsAndItemsJson += "]}";

            return channelsAndItemsJson;

        } catch (JsonProcessingException e) {
            return buildErrorJson(e.getOriginalMessage());
        }
    }

    // --------------------------------------------------------------------------------------------
    // Wrap the error message in a json body.
    private String buildErrorJson(String errorMessage) {

        return String.format("{ \"error\": \"%s\" }", errorMessage);
    }

}