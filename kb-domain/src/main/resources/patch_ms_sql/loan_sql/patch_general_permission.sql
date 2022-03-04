BEGIN

    DECLARE
        @count smallint
    DECLARE
        @countapproval smallint
    DECLARE
        @counttransfer smallint
    DECLARE
        @countPull smallint
    DECLARE
        @count_preference smallint
    DECLARE
        @count_customer smallint
    SET @count = (Select count(*) from permission)
    SET @countPull = (Select count(*) from permission where id = 125)
    SET @count_preference = (SELECT count(*) from permission where id = 142)
    SET @count_customer = (SELECT count(*) from permission where id = 143)
    SET @countapproval = (Select count(*) from permission where id = 150)
    SET @counttransfer = (Select count(*) from permission where id = 160)
    if (@count < 8)
        BEGIN
            SET IDENTITY_INSERT permission ON
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (1, 'Branch', 'camera-outline', '/home/admin/branch', 4, 1)
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (3, 'Role and Permission', 'lock-outline', '/home/admin/role', 7, 1)
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (6, 'Users', 'person-add-outline', '/home/admin/user', 5, 1)
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (13, 'Document', 'book-outline', '/home/admin/document', 13, 1)
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (100, 'Role Hierarchy', 'shield-outline', '/home/admin/roleHierarchy', 8, 1)
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (17, 'Dashboard', 'home-outline', '/home/admin/dashboard', 1, 1)
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (102, 'Catalogue', 'layers-outline', '/home/admin/catalogue', 14, 1)
            SET IDENTITY_INSERT permission OFF


            -- **********************MAP PERMISSION TO ADMIN ROLE DEFAULT****************************** --
-- BRANCH --
            SET IDENTITY_INSERT role_permission_rights ON
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (1, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 1, 1)


-- Role And Permission --
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (3, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 3, 1)


-- Users --
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (6, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 6, 1)

-- Document --
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (13, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 13, 1)

-- Role Hierarchy --
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (100, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 100, 1)


-- Dashboard --
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (17, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 17, 1)

-- Email Config --
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (102, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 102, 1)


-- Email Config --
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (55, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 105, 1)


            SET IDENTITY_INSERT role_permission_rights OFF
            -- **********************MAP PERMISSION TO ADMIN ROLE DEFAULT****************************** --

-- ************************BRANCH*******************************************
            SET IDENTITY_INSERT url_api ON
            INSERT INTO url_api (id, api_url, type) values (1, '/v1/branch', 'ADD BRANCH')
            INSERT INTO url_api (id, api_url, type) values (2, '/v1/branch', 'EDIT BRANCH')
            INSERT INTO url_api (id, api_url, type) values (3, '/v1/branch/csv', 'DOWNLOAD CSV')
            INSERT INTO url_api (id, api_url, type) values (4, '/v1/branch/get', 'VIEW BRANCH')
            SET IDENTITY_INSERT url_api OFF

            INSERT INTO permission_api_list(permission_id, api_list_id) values (1, 1)
            INSERT INTO permission_api_list(permission_id, api_list_id) values (1, 2)
            INSERT INTO permission_api_list(permission_id, api_list_id) values (1, 3)
            INSERT INTO permission_api_list(permission_id, api_list_id) values (1, 4)


            -- ************************BRANCH*******************************************


-- ************************USER*******************************************
            SET IDENTITY_INSERT url_api ON
            INSERT INTO url_api (id, api_url, type) values (12, '/v1/user', 'ADD USER')
            INSERT INTO url_api (id, api_url, type) values (30, '/v1/user/csv', 'DOWNLOAD CSV')
            SET IDENTITY_INSERT url_api OFF
            INSERT INTO permission_api_list(permission_id, api_list_id) values (6, 12)
            INSERT INTO permission_api_list(permission_id, api_list_id) values (6, 30)


            -- ************************USER*******************************************


-- ************************DASHBOARD******************************************
            SET IDENTITY_INSERT url_api ON
            -- INSERT  INTO url_api (id, api_url, type)
-- values (41, '/v1/user/get/statusCount', 'USER COUNT')
-- INSERT  INTO url_api (id, api_url, type)
-- values (42, '/v1/branch/get/statusCount', 'BRANCH COUNT')
--
-- INSERT  INTO url_api (id, api_url, type)
-- values (45,'/v1/notification', 'NOTIFICATION')
            SET IDENTITY_INSERT url_api OFF


            -- INSERT  INTO permission_api_list(permission_id, api_list_id)
-- values (17, 41)
-- INSERT  INTO permission_api_list(permission_id, api_list_id)
-- values (17, 42)
-- INSERT  INTO permission_api_list(permission_id, api_list_id)
-- values (17, 45)


-- ************************DASHBOARD******************************************


-- ************************DEFAULT ADMIN*******************************************

-- ************************BRANCH MAP*******************************************

            INSERT INTO role_permission_rights_api_rights values (1, 1)
            INSERT INTO role_permission_rights_api_rights values (1, 2)
            INSERT INTO role_permission_rights_api_rights values (1, 3)
            INSERT INTO role_permission_rights_api_rights values (1, 4)

            -- ************************BRANCH MAP*******************************************

-- ************************COUNTVIEW*******************************************
-- INSERT  INTO role_permission_rights_api_rights values (17, 41)
-- INSERT  INTO role_permission_rights_api_rights values (17, 42)

-- INSERT  INTO role_permission_rights_api_rights values (17, 45)

-- ************************COUNTVIEW*******************************************

-- ************************DEFAULT ADMIN*******************************************
        END


    --     if (@countPull < 1)
--         BEGIN
--             SET IDENTITY_INSERT permission ON
--             INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
--             VALUES (125, 'Pull', 'fas fa-angle-double-down', '/home/loan/pull', 100, 1)
--             SET IDENTITY_INSERT permission OFF
--         END

    if (@count_preference < 1)
        BEGIN
            DELETE FROM role_permission_rights where permission_id = 105
            DELETE FROM permission where id = 105
            SET identity_insert permission ON
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (142, 'Preference Master', 'settings-2-outline', '/home/admin/preference-master',
                    0, 1)
            UPDATE permission set front_url = '/home/dashboard' where id = 17
            SET IDENTITY_INSERT permission OFF


            SET IDENTITY_INSERT role_permission_rights ON
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (57, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 142, 1)
            SET IDENTITY_INSERT role_permission_rights Off
        end

    if (@count_customer < 1)
        BEGIN

            SET IDENTITY_INSERT permission ON
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (143, 'Customer', 'people-outline', '/home/customer', 3, 1)

            SET IDENTITY_INSERT permission OFF


            SET IDENTITY_INSERT role_permission_rights ON
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (60, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 143, 1)
            SET IDENTITY_INSERT role_permission_rights Off
        end

        if(@countapproval < 1)
        BEGIN
        SET IDENTITY_INSERT permission ON
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (150, 'Post Approval', 'checkmark-circle-outline', '/home/loan/post-approval-form', 110, 1)
        end
        if(@counttransfer < 1)
        BEGIN
        SET IDENTITY_INSERT permission ON
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (160, 'Transfer Document', 'book-outline', '/home/loan/transfer-doc', 119, 1)
        end
END;















