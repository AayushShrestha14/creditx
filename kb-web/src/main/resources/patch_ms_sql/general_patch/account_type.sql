BEGIN
    DECLARE @count smallint
    SET @count = (select count(*) from account_type)

    IF (@count = 0)
        BEGIN
            SET IDENTITY_INSERT account_type ON
            INSERT INTO account_type (id, name) VALUES (1, 'Current'), (2, 'Saving')
            SET IDENTITY_INSERT account_type OFF
        END
END;
