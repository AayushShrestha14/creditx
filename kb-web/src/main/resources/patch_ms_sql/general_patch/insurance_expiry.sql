BEGIN
    DECLARE @count SMALLINT
    SET @count = (SELECT COUNT(*)
                  FROM notification_master nm
                  WHERE nm.notification_key = 'INSURANCE_EXPIRY_NOTIFY')
    IF (@count = 0)
        BEGIN
            SET IDENTITY_INSERT notification_master ON
            INSERT INTO notification_master (id, notification_key, value, status, created_by_id,
                                             modified_by_id, created_at, last_modified_at,
                                             version)
            VALUES (1, 'INSURANCE_EXPIRY_NOTIFY', 30, 0, null, null, '2019-06-20', '2019-06-26', 0)
            SET IDENTITY_INSERT notification_master OFF
        END
END;
