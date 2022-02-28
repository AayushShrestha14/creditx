BEGIN
DECLARE @status smallint
SET @status = (Select status from product_mode where id = 4)
if(@status = 0)
BEGIN
DELETE FROM permission_api_list where permission_id  IN (2,4,5,7,8,9,10,11,12)
DELETE FROM role_permission_rights WHERE permission_id  IN (2,4,5,7,8,9,10,11,12)
DELETE FROM permission WHERE id  IN (2,4,5,7,8,9,10,11,12)
DELETE FROM loan_config_template_list WHERE template_list_id  IN (1,2,3)
DELETE FROM loan_template WHERE id  IN (1,2,3)

DELETE FROM role_permission_rights_api_rights WHERE api_rights_id = 40
DELETE FROM permission_api_list WHERE api_list_id =  40

DELETE FROM url_api WHERE id = 40
END
ELSE
BEGIN
DELETE FROM permission_api_list where permission_id  IN (4,5,7,8,9,10,11,12)
DELETE FROM role_permission_rights WHERE permission_id  IN (4,5,7,8,9,10,11,12)
DELETE FROM permission WHERE id  IN (4,5,7,8,9,10,11,12)
DELETE FROM loan_config_template_list WHERE template_list_id  IN (1,2,3)
DELETE FROM loan_template WHERE id  IN (1,2,3)

DELETE FROM role_permission_rights_api_rights WHERE api_rights_id = 40
DELETE FROM permission_api_list WHERE api_list_id =  40

DELETE FROM url_api WHERE id = 40
END

END;





