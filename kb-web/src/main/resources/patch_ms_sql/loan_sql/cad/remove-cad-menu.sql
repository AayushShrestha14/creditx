BEGIN
    DECLARE @perm_id smallint
    SET @perm_id = (Select id from permission where permission_name = 'Credit Administration')
    IF (@perm_id IS NOT NULL)
        BEGIN
            DELETE FROM role_permission_rights WHERE permission_id = @perm_id
            DELETE FROM permission_sub_navs WHERE permission_id =@perm_id
            DELETE FROM sub_nav WHERE id in (13,14,15,16,17,18,19,20)
            DELETE FROM permission WHERE id = @perm_id
        END


END;
