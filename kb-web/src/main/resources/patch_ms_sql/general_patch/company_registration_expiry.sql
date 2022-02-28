BEGIN
    DECLARE @count SMALLINT
    SET @count = (SELECT COUNT(*)
                  FROM notification_master nm
                  WHERE nm.notification_key = 'COMPANY_REGISTRATION_EXPIRY_BEFORE')
    IF (@count = 0)
        BEGIN
            SET IDENTITY_INSERT notification_master ON
            INSERT INTO notification_master (id, notification_key, value, status, created_by_id,
                                             modified_by_id, created_at, last_modified_at,
                                             version)
            VALUES (2, 'COMPANY_REGISTRATION_EXPIRY_BEFORE', 30, 0, null, null, '2020-04-29', '2020-04-29', 0)
            SET IDENTITY_INSERT notification_master OFF
        END
END;
