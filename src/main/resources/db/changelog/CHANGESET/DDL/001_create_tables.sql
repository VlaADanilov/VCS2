CREATE TABLE auto
(
    id          UUID NOT NULL,
    model       VARCHAR(255),
    year        INTEGER,
    price       INTEGER,
    mileage     INTEGER,
    city        VARCHAR(255),
    description VARCHAR(255),
    phone       VARCHAR(255),
    brand_id    UUID,
    user_id     UUID,
    CONSTRAINT pk_auto PRIMARY KEY (id)
);

CREATE TABLE auto_images
(
    auto_id   UUID NOT NULL,
    images_id UUID NOT NULL,
    CONSTRAINT pk_auto_images PRIMARY KEY (auto_id, images_id)
);

CREATE TABLE brand
(
    id      UUID NOT NULL,
    name    VARCHAR(255),
    country VARCHAR(255),
    CONSTRAINT pk_brand PRIMARY KEY (id)
);

CREATE TABLE currency
(
    name             VARCHAR(255) NOT NULL,
    price_to_one_rub DOUBLE PRECISION,
    CONSTRAINT pk_currency PRIMARY KEY (name)
);

CREATE TABLE employee
(
    id          UUID NOT NULL,
    name        VARCHAR(255),
    profession  VARCHAR(255),
    description VARCHAR(255),
    phone       VARCHAR(255),
    image_id    UUID,
    account_id  UUID,
    CONSTRAINT pk_employee PRIMARY KEY (id)
);

CREATE TABLE image
(
    id    UUID NOT NULL,
    image VARCHAR(255),
    CONSTRAINT pk_image PRIMARY KEY (id)
);

CREATE TABLE like_table
(
    id      UUID NOT NULL,
    user_id UUID,
    auto_id UUID,
    CONSTRAINT pk_like_table PRIMARY KEY (id)
);

CREATE TABLE report
(
    id          UUID NOT NULL,
    comment     VARCHAR(255),
    reporter_id UUID,
    auto_id     UUID,
    CONSTRAINT pk_report PRIMARY KEY (id)
);

CREATE TABLE report_viewed
(
    report_id UUID NOT NULL,
    viewed_id UUID NOT NULL,
    CONSTRAINT pk_report_viewed PRIMARY KEY (report_id, viewed_id)
);

CREATE TABLE user_table
(
    id       UUID NOT NULL,
    username VARCHAR(255),
    password VARCHAR(255),
    email    VARCHAR(255),
    role     VARCHAR(255),
    CONSTRAINT pk_user_table PRIMARY KEY (id)
);

ALTER TABLE auto_images
    ADD CONSTRAINT uc_auto_images_images UNIQUE (images_id);

ALTER TABLE auto
    ADD CONSTRAINT FK_AUTO_ON_BRAND FOREIGN KEY (brand_id) REFERENCES brand (id);

ALTER TABLE auto
    ADD CONSTRAINT FK_AUTO_ON_USER FOREIGN KEY (user_id) REFERENCES user_table (id);

ALTER TABLE employee
    ADD CONSTRAINT FK_EMPLOYEE_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES user_table (id);

ALTER TABLE employee
    ADD CONSTRAINT FK_EMPLOYEE_ON_IMAGE FOREIGN KEY (image_id) REFERENCES image (id);

ALTER TABLE like_table
    ADD CONSTRAINT FK_LIKE_TABLE_ON_AUTO FOREIGN KEY (auto_id) REFERENCES auto (id);

ALTER TABLE like_table
    ADD CONSTRAINT FK_LIKE_TABLE_ON_USER FOREIGN KEY (user_id) REFERENCES user_table (id);

ALTER TABLE report
    ADD CONSTRAINT FK_REPORT_ON_AUTO FOREIGN KEY (auto_id) REFERENCES auto (id);

ALTER TABLE report
    ADD CONSTRAINT FK_REPORT_ON_REPORTER FOREIGN KEY (reporter_id) REFERENCES user_table (id);

ALTER TABLE auto_images
    ADD CONSTRAINT fk_autima_on_auto FOREIGN KEY (auto_id) REFERENCES auto (id);

