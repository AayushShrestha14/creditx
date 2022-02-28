BEGIN
    DECLARE @count smallint
    DECLARE @perm_id smallint
     DECLARE @perm_del_id smallint
     SET @perm_del_id = (Select id from permission where permission_name = 'Post Approval')
    SET @count = (Select count(*) from permission where permission_name = 'Credit Administration')
    if (@count = 0)
        BEGIN

            INSERT INTO permission (permission_name, fa_icon, front_url, orders, status)
            VALUES ('Credit Administration', 'book-open-outline', '/home/credit', 19, 1)

            SET @perm_id = (Select id from permission where permission_name = 'Credit Administration')

            SET IDENTITY_INSERT sub_nav ON
            INSERT  INTO sub_nav (id, sub_nav_name, front_url,fa_icon) VALUES
            (13, 'Unassigned Approved Loan','/home/credit', 'file-text-outline'),
            (14, 'Offer Letter Pending','/home/credit/offer-pending', 'edit-2-outline'),
            (15, 'Offer Letter Approved','/home/credit/offer-approved', 'checkmark-square-outline'),
            (16, 'Legal Review Pending','/home/credit/legal-pending', 'edit-2-outline'),
            (18, 'Disbursement Pending','/home/credit/disbursement-pending', 'edit-2-outline'),
            (19, 'Disbursement Approved','/home/credit/disbursement-approved', 'checkmark-circle-outline'),
            (20, 'All CAD Files','/home/credit/cad-documents', 'file-text-outline')
            SET IDENTITY_INSERT sub_nav Off


            INSERT  INTO permission_sub_navs (permission_id, sub_navs_id) VALUES
            (@perm_id, 13),
            (@perm_id, 14),
            (@perm_id, 15),
            (@perm_id, 16),
            (@perm_id, 18),
            (@perm_id, 19),
            (@perm_id, 20)

        END
         IF (@perm_del_id IS NOT NULL)
        BEGIN
            DELETE FROM role_permission_rights WHERE permission_id = @perm_del_id
            DELETE FROM permission_sub_navs WHERE permission_id =@perm_del_id
           DELETE FROM permission WHERE id = @perm_del_id
        END
END;
