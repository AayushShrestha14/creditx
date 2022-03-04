BEGIN
    DECLARE
        @count smallint
    DECLARE
        @role smallint
    SET @count = (Select count(*) from role where role_name = 'CAD')


    BEGIN
        if (@count < 1)
            INSERT INTO role (created_at, last_modified_at, role_name, status, role_type,
                              role_access)
            VALUES ('2019-04-04 12:52:44', '2019-04-04 12:53:13', 'CAD', 1, 1, 2)
        set @role = (select id from role where role_name = 'CAD')

        DELETE FROM role_permission_rights WHERE role_id= @role

        INSERT INTO role_permission_rights (created_at, last_modified_at, permission_id, role_id)
        VALUES ('2019-04-04 13:17:01', '2019-04-04 13:17:07', 17, @role)

        INSERT INTO role_permission_rights (created_at, last_modified_at, permission_id, role_id)
        VALUES ('2019-04-04 13:17:01', '2019-04-04 13:17:07', 102, @role)


    END



END;
