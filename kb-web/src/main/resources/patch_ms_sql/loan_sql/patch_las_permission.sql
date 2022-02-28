BEGIN
    DECLARE
        @count smallint
    DECLARE
        @chckLoanConfig smallint
    SET @count = (Select count(*) from permission where id = 4)
    SET @chckLoanConfig = (Select count(*) from permission where id = 2)
    if (@chckLoanConfig = 0)
        BEGIN
            SET IDENTITY_INSERT permission on
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (2, 'Loan Configuration', 'smartphone-outline', '/home/admin/config', 9, 1)
            SET IDENTITY_INSERT permission off

            SET IDENTITY_INSERT role_permission_rights on
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (2, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 2, 1)

            SET IDENTITY_INSERT role_permission_rights off

        END

    if (@count = 0)
        BEGIN

            SET IDENTITY_INSERT permission on
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (4, 'Valuator', 'plus-circle-outline', '/home/admin/valuator', 10, 1)
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (7, 'Approval Limit', 'plus-circle-outline', '/home/admin/approvalLimit', 6, 1)
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (8, 'Nepse Company', 'plus-circle-outline', '/home/admin/nepse', 21, 1)
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (11, 'Company', 'map-outline', '/home/admin/company', 11, 1)
            SET IDENTITY_INSERT permission off


-- **********************MAP PERMISSION TO ADMIN ROLE DEFAULT******************************
            SET IDENTITY_INSERT role_permission_rights on
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (4, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 4, 1)
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (7, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 7, 1)
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (8, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 8, 1)
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (11, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 11, 1)
            SET IDENTITY_INSERT role_permission_rights off

            SET IDENTITY_INSERT url_api on
-- ************************APPROVAL LIMIT*******************************************
            INSERT INTO url_api (id, api_url, type)
            values (5, '/v1/approvallimit', 'ADD APPROVAL LIMIT')
            INSERT INTO url_api (id, api_url, type)
            values (6, '/v1/approvallimit/get', 'VIEW APPROVAL LIMIT')
            INSERT INTO url_api (id, api_url, type)
            values (34, '/v1/approvallimit/csv', 'DOWNLOAD CSV')

            INSERT INTO permission_api_list(permission_id, api_list_id) values (7, 5)
            INSERT INTO permission_api_list(permission_id, api_list_id) values (7, 6)
            INSERT INTO permission_api_list(permission_id, api_list_id) values (7, 34)

            -- ************************APPROVAL LIMIT*******************************************


-- ************************COMPANY*******************************************
            INSERT INTO url_api (id, api_url, type) values (7, '/v1/company', 'ADD COMPANY')
            INSERT INTO url_api (id, api_url, type) values (8, '/v1/company/get', 'VIEW COMPANY')
            INSERT INTO url_api (id, api_url, type)
            values (9, '/v1/company/get/statusCount', 'VIEW STATUS')

            INSERT INTO permission_api_list(permission_id, api_list_id) values (11, 7)
            INSERT INTO permission_api_list(permission_id, api_list_id) values (11, 8)
            INSERT INTO permission_api_list(permission_id, api_list_id) values (11, 9)

            -- ************************COMPANY*******************************************

-- ************************NEPSE*******************************************
            INSERT INTO url_api (id, api_url, type) values (10, '/v1/nepseCompany', 'ADD NEPSE')
            INSERT INTO url_api (id, api_url, type)
            values (11, '/v1/nepseCompany/get', 'VIEW NEPSE')

            INSERT INTO permission_api_list(permission_id, api_list_id) values (8, 10)
            INSERT INTO permission_api_list(permission_id, api_list_id) values (8, 11)

            -- ************************NEPSE*******************************************

-- ************************VALUATOR*******************************************
            INSERT INTO url_api (id, api_url, type) values (26, '/v1/valuator', 'ADD VALUATOR')
            INSERT INTO url_api (id, api_url, type) values (27, '/v1/valuator', 'EDIT VALUATOR')
            INSERT INTO url_api (id, api_url, type) values (28, '/v1/valuator/get', 'VIEW VALUATOR')
            INSERT INTO url_api (id, api_url, type) values (29, '/v1/valuator/csv', 'DOWNLOAD CSV')


            INSERT INTO permission_api_list(permission_id, api_list_id) values (4, 26)
            INSERT INTO permission_api_list(permission_id, api_list_id) values (4, 27)
            INSERT INTO permission_api_list(permission_id, api_list_id) values (4, 28)
            INSERT INTO permission_api_list(permission_id, api_list_id) values (4, 29)
-- ************************VALUATOR*******************************************

            SET IDENTITY_INSERT url_api off

        END
END;

-- ************************DASHBOARD******************************************
BEGIN
    DECLARE
        @chkLoanurlapi smallint
    DECLARE
        @chkNotificationUrlapi smallint
    DECLARE
        @chkPendingurlapi smallint
    set @chkLoanurlapi = (select count(*) from url_api where id = 40)
    set @chkNotificationUrlapi = (select count(*) from url_api where id = 45)
    set @chkPendingurlapi = (select count(*) from url_api where id = 46)

    if (@chkLoanurlapi = 0)
        BEGIN
            SET IDENTITY_INSERT url_api on
            INSERT INTO url_api (id, api_url, type)
            values (40, '/v1/config/getAll', 'LOAN CATEGORY')
            INSERT INTO permission_api_list(permission_id, api_list_id)
            values (17, 40)
            SET IDENTITY_INSERT url_api off


        END
    DELETE FROM permission_api_list where permission_id = 17


END;


-- ************************DASHBOARD******************************************


-- ************************DEFAULT ADMIN*******************************************

BEGIN
    DECLARE
        @ChkAPiMap smallint
    SET @ChkAPiMap = (select count(*)
                      from role_permission_rights_api_rights
                      where role_permission_rights_id = 7)

    if (@ChkAPiMap = 0)
        BEGIN
            -- ************************APPROVAL LIMIT MAP*******************************************
            INSERT INTO role_permission_rights_api_rights values (7, 5)
            INSERT INTO role_permission_rights_api_rights values (7, 6)
            INSERT INTO role_permission_rights_api_rights values (7, 34)

            -- ************************APPROVAL LIMIT MAP*******************************************


            -- ************************COMPANY MAP*******************************************
            INSERT INTO role_permission_rights_api_rights values (11, 7)
            INSERT INTO role_permission_rights_api_rights values (11, 8)
            INSERT INTO role_permission_rights_api_rights values (11, 9)

            -- ************************COMPANY MAP*******************************************

            -- ************************NEPSE MAP*******************************************
            INSERT INTO role_permission_rights_api_rights values (8, 10)
            INSERT INTO role_permission_rights_api_rights values (8, 11)

            -- ************************NEPSE MAP*******************************************

            -- ************************VALUATOR*******************************************

            INSERT INTO role_permission_rights_api_rights values (4, 26)
            INSERT INTO role_permission_rights_api_rights values (4, 27)
            INSERT INTO role_permission_rights_api_rights values (4, 28)
            INSERT INTO role_permission_rights_api_rights values (4, 29)

            -- ************************VALUATOR*******************************************

-- ************************DEFAULT ADMIN*******************************************
-- *************************DELETE EMAIL CONFIG AND NEPSE FROM NAV LINK ********************
            DELETE FROM role_permission_rights_api_rights WHERE role_permission_rights_id in (8)
            DELETE FROM role_permission_rights WHERE permission_id in (8, 105)
            DELETE FROM permission_api_list WHERE permission_id = 8
            DELETE FROM permission where id = 8
-- *************************DELETE EMAIL CONFIG AND NEPSE FROM NAV LINK ********************
        END

END;

-- ************************Reporting Info*******************************************
BEGIN
    DECLARE @reportName VARCHAR(100)
    DECLARE @reportId BIGINT
    SET @reportName = 'Reporting Info'
    IF ((SELECT COUNT(*) FROM permission WHERE permission_name = @reportName) = 0)
        BEGIN
            INSERT INTO permission (permission_name, fa_icon, front_url, orders, status)
            VALUES (@reportName, 'pie-chart-outline', '/home/report/dashboard', 121, 1)
            SET @reportId = (SELECT id FROM permission WHERE permission_name = @reportName)
        END
    IF ((SELECT COUNT(*) FROM role_permission_rights WHERE permission_id = @reportId) = 0)
        BEGIN
            INSERT INTO role_permission_rights (created_at, last_modified_at, permission_id, role_id)
            VALUES ('2020-03-27 09:31:01', '2020-03-27 09:31:01', @reportId, 1)
        END
END;
-- ************************Reporting Info*******************************************

-- ************************Credit Risk Grading (Gamma)*******************************************
BEGIN
    DECLARE @configCount smallint
    SET @configCount =
            (select count(*) from permission where permission_name = 'Credit Risk Grading (Gamma)')
    if (@configCount = 0)
        BEGIN
            DECLARE @acConfig smallint
            INSERT INTO permission (permission_name, fa_icon, front_url, orders, status)
            VALUES ('Credit Risk Grading (Gamma)', 'settings-outline',
                    '/home/crg',
                    107, 1)
            SET @acConfig =
                    (SELECT id FROM permission WHERE permission_name = 'Credit Risk Grading (Gamma)')

            SET IDENTITY_INSERT sub_nav ON
            INSERT INTO sub_nav (id, sub_nav_name, front_url, fa_icon)
            VALUES (11, 'Question setup', '/home/crg/setup',
                    'settings-outline'),
                    (12, 'Group', '/home/crg/group',
                    'settings-outline')
            SET IDENTITY_INSERT sub_nav Off

            INSERT INTO permission_sub_navs (permission_id, sub_navs_id)
            VALUES (@acConfig, 11),
                    (@acConfig, 12)


            INSERT INTO role_permission_rights (created_at, last_modified_at, permission_id, role_id)
            VALUES ('2019-04-04 13:17:01', '2019-04-04 13:17:07', @acConfig, 1)

        END
END;
