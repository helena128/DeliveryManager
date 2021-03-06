DROP TABLE IF EXISTS deliveries;
CREATE TABLE deliveries (
id SERIAL PRIMARY KEY,
delivery_id VARCHAR(255) UNIQUE NOT NULL,
product VARCHAR(255) NOT NULL,
supplier VARCHAR(255) NOT NULL ,
quantity INT NOT NULL,
expected_date TIMESTAMP(9) WITH TIME ZONE,
expected_warehouse VARCHAR(255),
delivery_status VARCHAR(16) DEFAULT 'PENDING',
updated_date TIMESTAMP(9) WITH TIME ZONE);