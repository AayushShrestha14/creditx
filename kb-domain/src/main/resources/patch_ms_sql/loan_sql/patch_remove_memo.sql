
DELETE FROM permission_api_list where permission_id  IN (15);
DELETE FROM permission_sub_navs WHERE permission_id =15;
DELETE FROM sub_nav WHERE id in (1,2,3);
DELETE FROM role_permission_rights WHERE permission_id =15;
DELETE FROM permission WHERE id = 15;