
DELETE FROM permission_api_list where permission_id in (2,4,8,11,13);
DELETE FROM role_permission_rights WHERE permission_id in (2,4,8,11,13);
DELETE FROM permission WHERE id in (2,4,8,11,13);

