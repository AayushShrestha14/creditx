BEGIN
DECLARE @count smallint
SET @count = (Select count(*) from permission where id = 15)
if(@count = 0)
BEGIN

SET IDENTITY_INSERT permission ON
INSERT  INTO permission (id, permission_name, fa_icon, front_url, orders, status) VALUES
(15,'Memo', 'hard-drive-outline', null, 62, 1)
SET IDENTITY_INSERT permission Off

SET IDENTITY_INSERT role_permission_rights ON
INSERT  INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id) VALUES
(15 , '2019-04-04 13:17:01', '2019-04-04 13:17:07', 15, 1)
SET IDENTITY_INSERT role_permission_rights Off

SET IDENTITY_INSERT sub_nav ON
INSERT  INTO sub_nav (id, sub_nav_name, front_url,fa_icon) VALUES
(1,'Under Review','/home/memo/underReview', 'inbox-outline'),
(2, 'Compose','/home/memo/compose', 'message-square-outline'),
(3,'Memo Type','/home/memo/type', 'file-text-outline')
SET IDENTITY_INSERT sub_nav Off

INSERT  INTO permission_sub_navs (permission_id, sub_navs_id) VALUES
(15, 1),
(15, 2),
(15, 3)
END
END ;