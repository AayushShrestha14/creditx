BEGIN
DECLARE @count smallint
SET @count = (Select count(*) from role)

if(@count < 1)
BEGIN
SET IDENTITY_INSERT role ON
INSERT  INTO role (id, created_at,  last_modified_at,  role_name, status,role_type,role_access) VALUES
(1, '2019-04-04 12:52:44', '2019-04-04 12:53:13', 'admin', 1,1,2)
SET IDENTITY_INSERT role OFF
-- password = admin1234

SET IDENTITY_INSERT users ON
INSERT  INTO users (id, created_at, last_modified_at, email, name, password, profile_picture, signature_image, status, user_name, branch_id, role_id, created_by_id, modified_by_id) VALUES
(1, '2019-04-04 12:52:44', '2019-04-04 12:53:13', 'admin@admin.com', 'SPADMIN', '$2a$10$cSqKGvZvEGEzQhRFRyDVyuCR3Lf0e7FcpIfxd/0t5IOG9U.3flG8m', NULL, NULL, 1, 'SPADMIN', NULL, 1, NULL, NULL)
SET IDENTITY_INSERT users OFF
END
END;

