INSERT INTO role (id, created_at, last_modified_at, role_name, status, role_type, role_access)
VALUES (1, '2019-04-04 12:52:44', '2019-04-04 12:53:13', 'admin', 1, 1, 2);

-- password = admin1234
INSERT INTO users (id, created_at, last_modified_at, account_no, email, name, password,
                   profile_picture, signature_image, status, user_name, branch_id, role_id,
                   created_by_id, modified_by_id)
VALUES (1, '2019-04-04 12:52:44', '2019-04-04 12:53:13', NULL, 'admin@admin.com', 'SPADMIN',
        '$2a$10$cSqKGvZvEGEzQhRFRyDVyuCR3Lf0e7FcpIfxd/0t5IOG9U.3flG8m', NULL, NULL, 1, 'SPADMIN',
        NULL, 1, NULL, NULL);


-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (1,  'Branch', 'fa fa-bank', '/home/admin/branch', 10, 1);
-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (2,  'Loan Configuration', 'fa fa-gear', '/home/admin/config', 30, 1);
-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (3,  'Role and Permission', 'fa fa-exchange', '/home/admin/role', 19, 1);
-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (4, 'Valuator', 'fas fa-money-check', '/home/admin/valuator', 25, 1);
-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (5,  'Sector', 'fas fa-hands-helping', '/home/admin/sector', 50, 1);
-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (6, 'Users', 'fa fa-user', '/home/admin/user', 1, 1);
-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (7, 'Approval Limit', 'fas fa-indent', '/home/admin/approvalLimit', 2, 1);
-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (8, 'Nepse Company', 'fas fa-money-bill-wave', '/home/admin/nepse', 21, 1);
-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (9,  'Segment', 'fa fa-user', '/home/admin/segment', 22, 1);
-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (10,  'Sub Segment', 'fa fa-user', '/home/admin/sub-segment', 23, 1);
-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (11, 'Company', 'fas fa-desktop', '/home/admin/company', 24, 1);
-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (12,  'SubSector', 'fa fa-user', '/home/admin/subSector', 51, 1);
-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (13,  'Document', 'fa fa-file', '/home/admin/document', 55, 1);
-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (100,  'Role Hierarchy', 'fas fa-sitemap', '/home/admin/roleHierarchy', 20, 1);
-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (17,  'Dashboard', 'fa fa-dashboard', '/home/admin/dashboard', 1, 1);
-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (18,  'Eligibility', 'fa fa-check-square', '/home/admin/eligibility', 62, 1);
-- INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES (19,  'Account Opening', 'fa fa-money-check', null, 56, 1);
--
--
--
-- -- **********************MAP PERMISSION TO ADMIN ROLE DEFAULT******************************
-- INSERT INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
--  VALUES (1, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  1, 1);
--
--  INSERT INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
--  VALUES (2, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  2, 1);
--
--  INSERT INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
--  VALUES (3, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  3, 1);
--
--  INSERT INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
--  VALUES (4, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  4, 1);
--
--  INSERT INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
--  VALUES (5, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  5, 1);
--
--  INSERT INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
--  VALUES (6, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  6, 1);
--
--   INSERT INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
--  VALUES (7, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  7, 1);
--
--   INSERT INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
--  VALUES (8, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  8, 1);
--
--   INSERT INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
--  VALUES (9, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  9, 1);
--
--   INSERT INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
--  VALUES (10, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  10, 1);
--
--   INSERT INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
--  VALUES (11, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  11, 1);
--
--   INSERT INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
--  VALUES (12, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  12, 1);
--
--   INSERT INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
--  VALUES (13, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  13, 1);
--
-- INSERT INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
--  VALUES (100, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  100, 1);
--
--
--  INSERT INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
--  VALUES (17, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  17, 1);
--
--  INSERT INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
--  VALUES (18 , '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  18, 1);
--
--
--
--
--
--
