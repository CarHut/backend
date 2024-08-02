-- Drop tables and sequences if they exist
DROP TABLE IF EXISTS authorities CASCADE;
DROP TABLE IF EXISTS brand CASCADE;
DROP TABLE IF EXISTS model CASCADE;
DROP TABLE IF EXISTS car_images CASCADE;
DROP TABLE IF EXISTS carhut_cars CASCADE;
DROP TABLE IF EXISTS color CASCADE;
DROP TABLE IF EXISTS feature CASCADE;
DROP TABLE IF EXISTS history_of_tasks CASCADE;
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS password_reset_token CASCADE;
DROP TABLE IF EXISTS registration_token CASCADE;
DROP TABLE IF EXISTS saved_cars_by_users CASCADE;
DROP TABLE IF EXISTS saved_searches CASCADE;
DROP TABLE IF EXISTS seller_ratings CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DROP SEQUENCE IF EXISTS authorities_id_seq;
DROP SEQUENCE IF EXISTS brand_sqc;
DROP SEQUENCE IF EXISTS model_sqc;

-- Create tables
CREATE TABLE authorities (
                             id integer NOT NULL,
                             authority character varying(255) NOT NULL,
                             PRIMARY KEY (id)
);

CREATE TABLE users (
                       id character varying NOT NULL,
                       username character varying NOT NULL,
                       password character varying NOT NULL,
                       email character varying NOT NULL,
                       is_active boolean NOT NULL,
                       authority_id integer NOT NULL,
                       first_name character varying NOT NULL,
                       surname character varying NOT NULL,
                       date_registered date NOT NULL,
                       num_of_offered_cars integer NOT NULL,
                       registration_type character varying(255),
                       PRIMARY KEY (id),
                       FOREIGN KEY (authority_id) REFERENCES authorities(id)
);

CREATE TABLE brand (
                       id integer NOT NULL,
                       brand character varying NOT NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE color (
                       id character varying NOT NULL,
                       color character varying NOT NULL,
                       finish character varying,
                       secondary_color character varying,
                       color_hex character varying NOT NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE feature (
                         feature character varying NOT NULL,
                         description character varying,
                         id integer NOT NULL,
                         PRIMARY KEY (id)
);

CREATE TABLE messages (
                          sender_id character varying NOT NULL,
                          recipient_id character varying NOT NULL,
                          message character varying NOT NULL,
                          date timestamp with time zone NOT NULL,
                          id character varying NOT NULL,
                          delivered boolean NOT NULL,
                          PRIMARY KEY (id),
                          FOREIGN KEY (sender_id) REFERENCES users(id),
                          FOREIGN KEY (recipient_id) REFERENCES users(id)
);

CREATE TABLE model (
                       id integer NOT NULL,
                       model character varying NOT NULL,
                       brand_id integer NOT NULL,
                       PRIMARY KEY (id),
                       FOREIGN KEY (brand_id) REFERENCES brand(id)
);

CREATE TABLE password_reset_token (
                                      token character varying NOT NULL,
                                      user_id character varying NOT NULL,
                                      expiry_date date NOT NULL,
                                      id character varying NOT NULL,
                                      PRIMARY KEY (id),
                                      FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE registration_token (
                                    id character varying NOT NULL,
                                    token character varying NOT NULL,
                                    expiry_date date NOT NULL,
                                    user_id character varying NOT NULL,
                                    PRIMARY KEY (id),
                                    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE carhut_cars (
    id VARCHAR NOT NULL,
    seller_id VARCHAR NOT NULL,
    seller_address VARCHAR NOT NULL,
    brand_id INTEGER NOT NULL,
    model_id INTEGER NOT NULL,
    header VARCHAR NOT NULL,
    price VARCHAR NOT NULL,
    mileage VARCHAR,
    registration VARCHAR,
    engine_power VARCHAR,
    engine_displacement VARCHAR,
    fuel VARCHAR,
    fuel_consumption_avg VARCHAR,
    fuel_consumption_city VARCHAR,
    fuel_consumption_highway VARCHAR,
    gearbox VARCHAR,
    gearbox_gears VARCHAR,
    body_type VARCHAR,
    powertrain VARCHAR,
    description VARCHAR,
    base_img_path VARCHAR,
    previous_owners VARCHAR,
    energy_eff_class VARCHAR,
    seats VARCHAR,
    doors VARCHAR,
    emission_class VARCHAR,
    exterior_color_id VARCHAR NOT NULL,
    interior_color_id VARCHAR NOT NULL,
    damage_status VARCHAR,
    parking_sensors BOOLEAN,
    parking_cameras BOOLEAN,
    country_of_origin VARCHAR,
    technical_inspection_date DATE,
    emission_inspection_date DATE,
    features VARCHAR,
    date_added DATE,
    is_active BOOLEAN,
    PRIMARY KEY (id),
    FOREIGN KEY (seller_id) REFERENCES users(id),
    FOREIGN KEY (brand_id) REFERENCES brand(id),
    FOREIGN KEY (model_id) REFERENCES model(id),
    FOREIGN KEY (interior_color_id) REFERENCES color(id),
    FOREIGN KEY (exterior_color_id) REFERENCES color(id)
);

CREATE TABLE saved_cars_by_users (
    user_id character varying NOT NULL,
    car_id character varying NOT NULL,
    id character varying NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (car_id) REFERENCES carhut_cars(id)
);

CREATE TABLE saved_searches (
    id character varying NOT NULL,
    user_id character varying NOT NULL,
    sort_by character(1),
    offers_per_page integer,
    price_from character varying,
    price_to character varying,
    mileage_from character varying,
    mileage_to character varying,
    fuel_type character varying,
    gearbox_type character varying,
    powertrain_type character varying,
    power_from character varying,
    power_to character varying,
    brands_and_models character varying,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id)

);

CREATE TABLE seller_ratings (
    id character varying NOT NULL,
    rating integer NOT NULL,
    seller_id character varying NOT NULL,
    user_id character varying NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (seller_id) REFERENCES users(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE car_images (
                            id character varying NOT NULL,
                            path character varying NOT NULL,
                            user_id character varying NOT NULL,
                            car_id character varying NOT NULL,
                            is_active boolean NOT NULL,
                            PRIMARY KEY (id),
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (car_id) REFERENCES carhut_cars(id)
);