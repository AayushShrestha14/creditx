
BEGIN
DECLARE @count smallint
SET @count = (Select count(*) from province)

if(@count < 7)
BEGIN

SET IDENTITY_INSERT province ON
INSERT  INTO province (id, name) VALUES
(1, 'Province 1'),
(2, 'Province 2'),
(3, 'Bagmati'),
(4, 'Gandaki'),
(5, 'Lumbini'),
(6, 'Karnali'),
(7, 'Sudur Paschim')
SET IDENTITY_INSERT province OFF
END
END ;
