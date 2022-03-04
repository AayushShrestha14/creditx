

DELETE FROM role_permission_rights WHERE permission_id = 18;
DELETE FROM permission_sub_navs WHERE permission_id =18;
DELETE FROM sub_nav WHERE id in (4,5,6,7,8);
DELETE FROM permission WHERE id = 18;