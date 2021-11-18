DROP TABLE IF EXISTS delivery;
CREATE TABLE delivery (
id SERIAL PRIMARY KEY,
delivery_id VARCHAR(255) UNIQUE NOT NULL,
product VARCHAR(255) NOT NULL,
supplier VARCHAR(255) NOT NULL ,
quantity INT NOT NULL,
expected_date TIMESTAMP WITH TIME ZONE,
expected_warehouse VARCHAR(255),
delivery_status VARCHAR(16),
received_date TIMESTAMP WITH TIME ZONE);