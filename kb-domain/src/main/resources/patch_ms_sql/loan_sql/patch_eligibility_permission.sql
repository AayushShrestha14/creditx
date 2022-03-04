BEGIN
DECLARE @count smallint
DECLARE @countLoanConfig smallint
SET @count = (Select count(*) from permission where id = 18)
SET @countLoanConfig = (Select count(*) from permission where id = 2)

if(@countLoanConfig = 0)
BEGIN
SET IDENTITY_INSERT permission ON
INSERT  INTO permission (id,  permission_name, fa_icon, front_url, orders, status) VALUES
(2,  'Loan Configuration', 'settings-2-outline', '/home/admin/config', 9, 1)
SET IDENTITY_INSERT permission OFF
SET IDENTITY_INSERT role_permission_rights ON
INSERT  INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
VALUES (2, '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  2, 1)

SET IDENTITY_INSERT role_permission_rights OFF
END

if(@count = 0)
BEGIN

SET IDENTITY_INSERT permission ON
INSERT  INTO permission (id,  permission_name, fa_icon, front_url, orders, status)
VALUES (18,  'Eligibility', 'checkmark-circle-outline', '/home/admin/eligibility', 62, 1)
SET IDENTITY_INSERT permission Off

SET IDENTITY_INSERT sub_nav ON
INSERT  INTO sub_nav (id, sub_nav_name, front_url,fa_icon) VALUES
(4, 'Questions','/home/admin/eligibility/question', 'fa fa-question-circle'),
(5, 'General Questions','/home/admin/eligibility/general-question', 'fa fa-exclamation-circle'),
(6, 'New Requests','/home/admin/eligibility/new-requests', 'checkmark-square-outline'),
(7, 'Eligible ','/home/admin/eligibility/eligible', 'checkmark-circle-2-outline'),
(8, 'Non Eligible','/home/admin/eligibility/non-eligible', 'close-circle-outline')
SET IDENTITY_INSERT sub_nav Off

INSERT  INTO permission_sub_navs (permission_id, sub_navs_id) VALUES
(18, 4),
(18, 5),
(18, 6),
(18, 7),
(18, 8)

SET IDENTITY_INSERT role_permission_rights ON
INSERT  INTO role_permission_rights (id, created_at,  last_modified_at,  permission_id, role_id)
VALUES (18 , '2019-04-04 13:17:01',  '2019-04-04 13:17:07',  18, 1)
SET IDENTITY_INSERT role_permission_rights Off

END
END;
