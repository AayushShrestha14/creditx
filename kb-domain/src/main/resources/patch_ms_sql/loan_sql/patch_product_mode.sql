SET IDENTITY_INSERT product_mode ON;
TRUNCATE table product_mode;
INSERT INTO product_mode (id, product,status,description)
VALUES
('1', '0',0,'DMS'),
('2', '1',0,'MEMO'),
('3', '2',1,'ACCOUNT'),
('4', '3',0,'ELIGIBILITY'),
('5', '4',1,'LAS'),
('6', '5',1,'OFFER LETTER');

SET IDENTITY_INSERT product_mode OFF;
