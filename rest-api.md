# News Reader REST API

## <p style="color:blue">Feeds</p>
### Add a new feed
`POST /feed`

Body - String URL of the RSS source link.

    { "rss_source": "https://news.com/rss.xml" }

Response - 200 and the channel + items as json.

    {
        "feeds": [
            {
                "channel": {
                    "source": "https://rss.nytimes.com/services/xml/rss/nyt/World.xml",
                    "link": "https://www.nytimes.com/section/world",
                    "title": "NYT > World News",
                    "description": "NYT > World News",
                    "catId": null,
                    "id": 1
                },
                "items": [
                    {
                        "id": 1,
                        "title": "In Gaza, Witnesses Describe Fear and Deprivation at Besieged Al-Shifa Hospital",
                        "description": "Several patients have died as a result of the Israeli military assault, the Gazan authorities say. Israel says the operation is targeting Hamas leaders.",
                        "link": "https://www.nytimes.com/2024/03/24/world/middleeast/al-shifa-hospital-gaza-israel.html",
                        "pubDate": "2024-03-25, 03:01:44",
                        "isRead": false,
                        "isVisible": true,
                        "chanId": 1
                    },
                    {
                        "id": 2,
                        "title": "Sugar in India, Fueled by Child Marriage and Hysterectomies",
                        "description": "An investigation into the sugar-cane industry in the Indian state of Maharashtra found workers ensnared by debt and pushed into child marriages and unnecessary hysterectomies.",
                        "link": "https://www.nytimes.com/2024/03/24/world/europe/india-sugar-cane-fields-child-labor-hysterectomies.html",
                        "pubDate": "2024-03-24, 06:30:30",
                        "isRead": false,
                        "isVisible": true,
                        "chanId": 1
                    }
                ]
            }
        ]
    }

---

### Get an existing feed
`GET /feed/{id}`

- `id` - the id a previously saved Channel.

Response - 200 and the channel + items as json.

---

### Refresh an existing feed
`PUT /feed/`

Gets the latest Channel and Items and replaces the old ones in the database.

Body - String URL of the RSS source link.

    { "rss_source": "https://news.com/rss.xml" }


Response - 200 and the channel + items as json.

--- 

### Delete an existing feed
`DELETE /feed/{id}`

- `id` - the id of a previously returned Channel.

Response - 200 and message:

    {
        "chan_id": 102,
        "Message": "Feed deleted."
    }

---

### Apply a Category to a feed
`PATCH /feed/{id}`

- `id` - the id a previously saved Channel.

Body - the id of the category to be applied.

    { "id": 52 }

Response - 200.

Response - 400 and error message

    {
        "Error": "Category does not exist."
    }

    {
        "Error": "Channel does not exist."
    }

---

### Remove the Category from a feed
`PATCH /feed/{id}`

- `id` - the id a previously saved Channel.

Body - set `null` as the category id:

    { "id": null }

Response - 200.

Response - 400 and error message

    {
        "Error": "Channel does not exist."
    }

---

### Get All Saved Feeds
`GET /feed`

Response - 200 and all channels + items as json.

    {
        "feeds": [
            {
                "channel": {
                    "source": "https://rss.nytimes.com/services/xml/rss/nyt/World.xml",
                    "link": "https://www.nytimes.com/section/world",
                    "title": "NYT > World News",
                    "description": "NYT > World News",
                    "catId": null,
                    "id": 1
                },
                "items": [
                    {
                        "id": 1,
                        "title": "In Gaza, Witnesses Describe Fear and Deprivation at Besieged Al-Shifa Hospital",
                        "description": "Several patients have died as a result of the Israeli military assault, the Gazan authorities say. Israel says the operation is targeting Hamas leaders.",
                        "link": "https://www.nytimes.com/2024/03/24/world/middleeast/al-shifa-hospital-gaza-israel.html",
                        "pubDate": "2024-03-25, 03:01:44",
                        "isRead": false,
                        "isVisible": true,
                        "chanId": 1
                    },
                    {
                        "id": 2,
                        "title": "Sugar in India, Fueled by Child Marriage and Hysterectomies",
                        "description": "An investigation into the sugar-cane industry in the Indian state of Maharashtra found workers ensnared by debt and pushed into child marriages and unnecessary hysterectomies.",
                        "link": "https://www.nytimes.com/2024/03/24/world/europe/india-sugar-cane-fields-child-labor-hysterectomies.html",
                        "pubDate": "2024-03-24, 06:30:30",
                        "isRead": false,
                        "isVisible": true,
                        "chanId": 1
                    }
                ]
            },
            {
                "channel": {
                    "source": "https://feeds.bbci.co.uk/news/world/rss.xml",
                    "link": "https://www.bbc.co.uk/news/world",
                    "title": "BBC News",
                    "description": "BBC News - World",
                    "catId": null,
                    "id": 2
                },
                "items": [
                    {
                        "id": 102,
                        "title": "UN Security Council resolution calls for Gaza ceasefire",
                        "description": "It was passed after the US refrained from vetoing the measure in a shift from its previous position.",
                        "link": "https://www.bbc.co.uk/news/world-middle-east-68658415",
                        "pubDate": "2024-03-25, 03:26:02",
                        "isRead": false,
                        "isVisible": true,
                        "chanId": 2
                    }
                ]
            }
        ]
    }





---

### Mark Item read or unread
### Mark Item visible or invisible
`PATCH /item`

Sets the `isRead` and/or `isVisible` flags in the Item and updates the database.

Body - an array of Items and the new values:

    [{ "id": 1, "isRead": true }]

or

    [
     { "id": 1, "isRead": true },
     { "id": 5, "isVisible": false}
    ]

Response - 200 and an array of modified Items

    [
        {
            "id": 1,
            "title": "Title blah blah...",
            "description": "Description...",
            "link": "https://news.com",
            "pubDate": "2024-03-26, 09:29:52",
            "isRead": true,
            "isVisible": false,
            "chanId": 1
        },
        {
            "id": 5,
            "title": "Title blah blah...",
            "description": "Description...",
            "link": "https://news.com",
            "pubDate": "2024-03-26, 09:29:52",
            "isRead": false,
            "isVisible": false,
            "chanId": 1
        }
    ]

---

## <p style="color:blue">Filters
### Add a Filter 
`POST /filter`

Body - string containing a keyword or phrase

Response - 200

    { "id": 152, "content": "Greta Thunberg" }

--- 

### Delete a Filter
`DELETE /filter/{id}`

- `id` - the id of a previously returned Filter.
  
Response - 200, `true` if filter was deleted, otherwise `false`.

---

## <p style="color:blue">Categories
### Add a Category
`POST /category`

Body - the name of the category:

    { "name": "CATEGORY NAME" }

Response - 200 and the category

    { "id": 123, "name": "CATEGORY NAME" }

---

### Delete a Category
`DELETE /category/{id}`

- `id` - the id of a previously returned Category.

Response - 200

Response - 400 and error message

    {
        "Error": "Category does not exist."
    }

---

### Rename a Category
`PUT /category`
- `id` - the id of a previously returned Category.

Body - the id of an existing category and its new name:

    { "id": 123, "name": "NEW NAME" }

Response - 200 and the category

    { "id": 123, "name": "NEW NAME"" }

Response - 400 and error message

    {
        "Error": "Category does not exist."
    }

---


### Get all Categories
`GET /category`

Response - 200 and all categories

    [
    {
        "id": 2,
        "name": "LIFESTYLE"
    },
    {
        "id": 3,
        "name": "NEWS"
    },
    {
        "id": 4,
        "name": "HOLLYWOOD"
    }
    ]

---

## <p style="color:blue">Users
### Add a User
`POST /user`

Body - User details

    {
        "username": "billynomates",
        "password": "2222",
        "firstName": "Billy",
        "surname": "Nomates",
        "email": "billynomates@hotmail.com"
    }

Response - 200 and the User returned as json.

Response - 400 and error message

    {
        "Error": "User already exists."
    }

---

### Delete a User
`DELETE /user/{username}`

- `username` - the username of a previously added User.

Response - 200.

Response - 400 and error message

    {
        "Error": "User does not exist."
    }

---

### Get a User
`GET /user/{username}`

Body - User details

    {
        "username": "billynomates"
    }

Response - 200 and the User returned as json.

Response - 400 and error message

    {
        "Error": "User does not exist."
    }

---