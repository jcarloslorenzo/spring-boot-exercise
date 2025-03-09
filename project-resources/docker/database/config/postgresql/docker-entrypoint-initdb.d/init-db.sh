#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE ROLE "user" WITH LOGIN NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;
	ALTER USER "user" WITH PASSWORD 'userPass';

	GRANT ALL ON SCHEMA public TO "user";

	CREATE TABLE IF NOT EXISTS T_RATES (
	    ID SERIAL PRIMARY KEY NOT NULL,
	    BRAND_ID INT NOT NULL,
	    PRODUCT_ID INT NOT NULL,
	    START_DATE DATE NOT NULL,
	    END_DATE DATE NOT NULL,
	    PRICE INT NOT NULL,
	    CURRENCY_CODE VARCHAR(3) NOT NULL
	);

	CREATE INDEX rates_brand_id_index ON T_RATES USING btree (brand_id);
	CREATE INDEX rates_product_id_index ON T_RATES USING btree (product_id);
	CREATE INDEX rates_currency_code_index ON T_RATES USING btree (currency_code);

	INSERT INTO public.t_rates
	(brand_id, product_id, start_date, end_date, price, currency_code)
	VALUES( 1, 1, '2022-01-01', '2022-05-31', 1550, 'EUR');
	INSERT INTO public.t_rates
	(brand_id, product_id, start_date, end_date, price, currency_code)
	VALUES( 1, 1, '2022-06-01', '2022-12-31', 1850, 'USD');
	INSERT INTO public.t_rates
	(brand_id, product_id, start_date, end_date, price, currency_code)
	VALUES( 2, 1, '2022-01-01', '2022-05-31', 2050, 'EUR');
	INSERT INTO public.t_rates
	(brand_id, product_id, start_date, end_date, price, currency_code)
	VALUES( 2, 1, '2022-06-01', '2022-12-31', 2250, 'USD');
	INSERT INTO public.t_rates
	(brand_id, product_id, start_date, end_date, price, currency_code)
	VALUES( 1, 2, '2022-01-01', '2022-05-31', 2550, 'EUR');
	INSERT INTO public.t_rates
	(brand_id, product_id, start_date, end_date, price, currency_code)
	VALUES( 1, 2, '2022-06-01', '2022-12-31', 2850, 'USD');
	INSERT INTO public.t_rates
	(brand_id, product_id, start_date, end_date, price, currency_code)
	VALUES( 2, 2, '2022-01-01', '2022-05-31', 3050, 'EUR');
	INSERT INTO public.t_rates
	(brand_id, product_id, start_date, end_date, price, currency_code)
	VALUES( 2, 2, '2022-06-01', '2022-12-31', 3250, 'USD');
EOSQL
