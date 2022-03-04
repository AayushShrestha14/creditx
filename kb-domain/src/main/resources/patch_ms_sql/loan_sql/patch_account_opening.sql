

BEGIN
    DECLARE @count smallint
    SET @count = (Select count(*) from permission where id = 19)
    if (@count = 0)
        BEGIN
            SET IDENTITY_INSERT permission on
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (19, 'Account Opening', 'book-open-outline', '/home/admin/openingAccount', 56, 1)
            SET IDENTITY_INSERT permission off

            SET IDENTITY_INSERT role_permission_rights on
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (19, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 19, 1)
            SET IDENTITY_INSERT role_permission_rights off
        END
END;

BEGIN
    DECLARE @configCount smallint
    SET @configCount =
            (select count(*) from permission where permission_name = 'Account Opening Config')
    if (@configCount = 0)
        BEGIN
            DECLARE @acConfig smallint
            INSERT INTO permission (permission_name, fa_icon, front_url, orders, status)
            VALUES ('Account Opening Config', 'settings-outline',
                    '/home/admin/openingAccountConfig',
                    106, 1)
            SET @acConfig =
                    (SELECT id FROM permission WHERE permission_name = 'Account Opening Config')

            SET IDENTITY_INSERT sub_nav ON
            INSERT INTO sub_nav (id, sub_nav_name, front_url, fa_icon)
            VALUES (9, 'Account Type', '/home/admin/openingAccountConfig/accountType',
                    'settings-outline'),
                   (10, 'Account Category', '/home/admin/openingAccountConfig/accountCategory',
                    'settings-outline')
            SET IDENTITY_INSERT sub_nav Off

            INSERT INTO permission_sub_navs (permission_id, sub_navs_id)
            VALUES (@acConfig, 9),
                   (@acConfig, 10)


            INSERT INTO role_permission_rights (created_at, last_modified_at, permission_id, role_id)
            VALUES ('2019-04-04 13:17:01', '2019-04-04 13:17:07', @acConfig, 1)

        END
END;
