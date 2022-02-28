BEGIN
    -- Account Opening Config Start --
    DECLARE @aocId SMALLINT
    SET @aocId =
            (SELECT id FROM permission WHERE permission_name = 'Account Opening Config')

    DELETE FROM role_permission_rights WHERE permission_id = @aocId
    DELETE FROM permission_sub_navs WHERE permission_id = @aocId
    DELETE FROM sub_nav WHERE sub_nav_name in ('Account Type', 'Account Category')
    DELETE FROM permission WHERE permission_name = 'Account Opening Config'
    -- Account Opening Config End --

    -- Account Opening Start --
    DELETE FROM role_permission_rights WHERE permission_id = 19
    DELETE FROM permission_sub_navs WHERE permission_id = 19
    DELETE FROM permission WHERE id = 19
    -- Account Opening End --
END;
