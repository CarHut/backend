-- Drop tables and sequences if they exist
DROP TABLE IF EXISTS h2.public.authorities CASCADE;
DROP TABLE IF EXISTS h2.public.brand CASCADE;
DROP TABLE IF EXISTS h2.public.model CASCADE;
DROP TABLE IF EXISTS h2.public.car_images CASCADE;
DROP TABLE IF EXISTS h2.public.carhut_cars CASCADE;
DROP TABLE IF EXISTS h2.public.color CASCADE;
DROP TABLE IF EXISTS h2.public.feature CASCADE;
DROP TABLE IF EXISTS h2.public.history_of_tasks CASCADE;
DROP TABLE IF EXISTS h2.public.messages CASCADE;
DROP TABLE IF EXISTS h2.public.password_reset_token CASCADE;
DROP TABLE IF EXISTS h2.public.registration_token CASCADE;
DROP TABLE IF EXISTS h2.public.saved_cars_by_users CASCADE;
DROP TABLE IF EXISTS h2.public.saved_searches CASCADE;
DROP TABLE IF EXISTS h2.public.seller_ratings CASCADE;
DROP TABLE IF EXISTS h2.public.users CASCADE;

DROP SEQUENCE IF EXISTS h2.public.authorities_id_seq;
DROP SEQUENCE IF EXISTS h2.public.brand_sqc;
DROP SEQUENCE IF EXISTS h2.public.model_sqc;

-- Create tables
CREATE TABLE h2.public.authorities (
                             id integer NOT NULL,
                             authority character varying(255) NOT NULL,
                             PRIMARY KEY (id)
);

CREATE TABLE h2.public.users (
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
                       FOREIGN KEY (authority_id) REFERENCES h2.public.authorities(id)
);

CREATE TABLE h2.public.brand (
                       id integer NOT NULL,
                       brand character varying NOT NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE h2.public.color (
                       id character varying NOT NULL,
                       color character varying NOT NULL,
                       finish character varying,
                       secondary_color character varying,
                       color_hex character varying NOT NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE h2.public.feature (
                         feature character varying NOT NULL,
                         description character varying,
                         id integer NOT NULL,
                         PRIMARY KEY (id)
);

CREATE TABLE h2.public.messages (
                          sender_id character varying NOT NULL,
                          recipient_id character varying NOT NULL,
                          message character varying NOT NULL,
                          date timestamp with time zone NOT NULL,
                          id character varying NOT NULL,
                          delivered boolean NOT NULL,
                          PRIMARY KEY (id),
                          FOREIGN KEY (sender_id) REFERENCES h2.public.users(id),
                          FOREIGN KEY (recipient_id) REFERENCES h2.public.users(id)
);

CREATE TABLE h2.public.model (
                       id integer NOT NULL,
                       model character varying NOT NULL,
                       brand_id integer NOT NULL,
                       PRIMARY KEY (id),
                       FOREIGN KEY (brand_id) REFERENCES h2.public.brand(id)
);

CREATE TABLE h2.public.password_reset_token (
                                      token character varying NOT NULL,
                                      user_id character varying NOT NULL,
                                      expiry_date date NOT NULL,
                                      id character varying NOT NULL,
                                      PRIMARY KEY (id),
                                      FOREIGN KEY (user_id) REFERENCES h2.public.users(id)
);

CREATE TABLE h2.public.registration_token (
                                    id character varying NOT NULL,
                                    token character varying NOT NULL,
                                    expiry_date date NOT NULL,
                                    user_id character varying NOT NULL,
                                    PRIMARY KEY (id),
                                    FOREIGN KEY (user_id) REFERENCES h2.public.users(id)
);

CREATE TABLE h2.public.carhut_cars (
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
    FOREIGN KEY (seller_id) REFERENCES h2.public.users(id),
    FOREIGN KEY (brand_id) REFERENCES h2.public.brand(id),
    FOREIGN KEY (model_id) REFERENCES h2.public.model(id),
    FOREIGN KEY (interior_color_id) REFERENCES h2.public.color(id),
    FOREIGN KEY (exterior_color_id) REFERENCES h2.public.color(id)
);

CREATE TABLE h2.public.saved_cars_by_users (
    user_id character varying NOT NULL,
    car_id character varying NOT NULL,
    id character varying NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES h2.public.users(id),
    FOREIGN KEY (car_id) REFERENCES h2.public.carhut_cars(id)
);

CREATE TABLE h2.public.saved_searches (
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
    FOREIGN KEY (user_id) REFERENCES h2.public.users(id)

);

CREATE TABLE h2.public.seller_ratings (
    id character varying NOT NULL,
    rating integer NOT NULL,
    seller_id character varying NOT NULL,
    user_id character varying NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (seller_id) REFERENCES h2.public.users(id),
    FOREIGN KEY (user_id) REFERENCES h2.public.users(id)
);

CREATE TABLE h2.public.car_images (
                            id character varying NOT NULL,
                            path character varying NOT NULL,
                            user_id character varying NOT NULL,
                            car_id character varying NOT NULL,
                            is_active boolean NOT NULL,
                            PRIMARY KEY (id),
                            FOREIGN KEY (user_id) REFERENCES h2.public.users(id),
                            FOREIGN KEY (car_id) REFERENCES h2.public.carhut_cars(id)
);