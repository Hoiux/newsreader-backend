-- /*
-- Feed categories, e.g. "News", "Fashion", "Lifestyle", etc.
-- */
-- CREATE TABLE category(

--     -- unique id
--     id bigint not null,
    
--     -- category name
--     title varchar(255)
-- )

-- /*
-- Feed Channel
-- */
-- CREATE TABLE channel(

--     -- unique id
--     id bigint not null,

--     -- foreign key: category id
--     cat_id bigint,

--     -- source link of original RSS
--     source varchar(2048)

--     -- channel title
--     title varchar(255),

--     -- channel web site link
--     link varchar(2048),

--     -- channel description
--     description varchar(4096)
-- )

-- /*
-- Feed Item
-- */
-- CREATE TABLE item(

--     -- unique id
--     id bigint not null,

--     -- foreign key: channel id
--     chan_id bigint not null,

--     -- item title
--     title varchar(255),

--     -- item description
--     description varchar(4096),

--     -- the link to the item
--     link varchar(2048),

--     -- item publication date
--     pub_date datetime,

--     -- has the item been marked as read?
--     is_read bit,

--     -- is the item hidden from display?
--     is_visible bit
-- )

-- /*
-- Keywords or phrases used for filtering Items.
-- */
-- CREATE TABLE filter(

--     -- unique id
--     id bigint not null,

--     -- keyword or phrase for use in filtering
--     content varchar(255)
-- )