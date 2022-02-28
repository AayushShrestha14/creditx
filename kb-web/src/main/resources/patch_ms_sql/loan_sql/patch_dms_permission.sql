
BEGIN
DECLARE @countPermission smallint
SET @countPermission = (Select count(*) from permission where id = 2)
DELETE FROM permission_api_list where permission_id in (4,8,11)
DELETE FROM role_permission_rights WHERE permission_id in (4,8,11)
DELETE FROM permission WHERE id in (4,8,11)
  if(@countPermission < 1)

BEGIN

SET IDENTITY_INSERT permission ON
INSERT INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES
(2,  'Loan Configuration', 'settings-2-outline', '/home/admin/config', 9, 1)
SET IDENTITY_INSERT permission OFF

-- **********************MAP PERMISSION TO ADMIN ROLE DEFAULT****************************** --

-- Loan Configuration --
SET IDENTITY_INSERT role_permission_rights ON
INSERT  INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
VALUES (2, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  2, 1)

SET IDENTITY_INSERT role_permission_rights OFF
END
END;























