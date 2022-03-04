
BEGIN
DECLARE @count_customerInfo smallint
SET @count_customerInfo = (Select count(*) from loan_template where id = 1)
if(@count_customerInfo = 0)
BEGIN

SET IDENTITY_INSERT loan_template on

INSERT  INTO loan_template (id, name, template_url, order_url, status, template_view, created_by_id, modified_by_id, created_at, last_modified_at, version) VALUES
(1, 'Customer Info', '#basicInfo',1, 1, NULL, NULL, NULL,'2019-04-04', '2019-04-04', 0)

SET IDENTITY_INSERT loan_template off

END
END;

BEGIN
DECLARE @count_customerInfo smallint
SET @count_customerInfo = (Select count(*) from loan_template where id = 2)
if(@count_customerInfo = 0)
BEGIN

SET IDENTITY_INSERT loan_template on

INSERT  INTO loan_template (id, name, template_url, order_url, status, template_view, created_by_id, modified_by_id, created_at, last_modified_at, version) VALUES
(2, 'Company Info', '#companyInfo', 2, 1, NULL, NULL, NULL, '2019-05-28', '2019-05-06', 0)

SET IDENTITY_INSERT loan_template off

END
END;

BEGIN
DECLARE @creditRiskCount smallint
SET @creditRiskCount = (Select count(*) from loan_template where id = 5)
if(@creditRiskCount = 0)
BEGIN

SET IDENTITY_INSERT loan_template on

INSERT  INTO loan_template (id, name, template_url, order_url, status, template_view, created_by_id, modified_by_id, created_at, last_modified_at, version) VALUES
(5, 'Credit Risk Grading', '#creditGrading', 5, 1, NULL, NULL, NULL, '2019-08-11', '2019-08-18', 0)

SET IDENTITY_INSERT loan_template off

END
END;

BEGIN
DECLARE @siteVisitCount smallint
SET @siteVisitCount = (Select count(*) from loan_template where id = 6)
if(@siteVisitCount = 0)
BEGIN

SET IDENTITY_INSERT loan_template on

INSERT  INTO loan_template (id, name, template_url, order_url, status, template_view, created_by_id, modified_by_id, created_at, last_modified_at, version) VALUES
(6, 'Site Visit', '#siteVisit', 6, 1, NULL, NULL, NULL, '2019-08-20', '2019-08-25', 0)

SET IDENTITY_INSERT loan_template off

END
END;



BEGIN
DECLARE @count_financial smallint
SET @count_financial = (Select count(*) from loan_template where id = 7)
if(@count_financial = 0)
BEGIN

SET IDENTITY_INSERT loan_template on

INSERT  INTO loan_template (id, name, template_url, order_url, status, template_view, created_by_id, modified_by_id, created_at, last_modified_at, version) VALUES
(7, 'Financial', '#financialInfo',7, 1, NULL, NULL, NULL,'2019-08-23', '2019-08-23', 0)

SET IDENTITY_INSERT loan_template off

END
END;

--patch to make proposal visible
BEGIN
DECLARE @count_proposal smallint
SET @count_proposal = (Select count(*) from loan_template where id = 8)
if(@count_proposal = 0)
BEGIN

SET IDENTITY_INSERT loan_template on

INSERT  INTO loan_template (id, name, template_url, order_url, status, template_view, created_by_id, modified_by_id, created_at, last_modified_at, version) VALUES
(8, 'Proposal', '#proposalInfo',8, 1, NULL, NULL, NULL,'2019-08-23', '2019-08-23', 0)

SET IDENTITY_INSERT loan_template off

END
END;

-- --patch to make security visible
BEGIN
DECLARE @count_security smallint
SET @count_security = (Select count(*) from loan_template where id = 9)
if(@count_security = 0)
BEGIN

SET IDENTITY_INSERT loan_template on

INSERT  INTO loan_template (id, name, template_url, order_url, status, template_view, created_by_id, modified_by_id, created_at, last_modified_at, version) VALUES
(9, 'Security', '#security',9, 1, NULL, NULL, NULL,'2019-08-23', '2019-08-23', 0)

SET IDENTITY_INSERT loan_template off

END
END;

BEGIN
DECLARE @count_document smallint
SET @count_document = (Select count(*) from loan_template where id = 10)
if(@count_document = 0)
BEGIN

SET IDENTITY_INSERT loan_template on

INSERT  INTO loan_template (id, name, template_url, order_url, status, template_view, created_by_id, modified_by_id, created_at, last_modified_at, version) VALUES
(10, 'Customer Document', '#customerDocument',10, 1, NULL, NULL, NULL,'2019-08-23', '2019-08-23', 0)

SET IDENTITY_INSERT loan_template off

END
END;


BEGIN
    DECLARE
        @count SMALLINT
    SET @count = (SELECT COUNT(*) FROM loan_template WHERE id = 15)
    if (@count = 0)
        BEGIN
            SET IDENTITY_INSERT loan_template ON

            INSERT INTO loan_template (id, name, template_url, order_url, status, template_view,
                                       created_by_id, modified_by_id, created_at, last_modified_at,
                                       version)
            VALUES (15, 'Guarantor', '#guarantor', 15, 1, NULL, NULL, NULL,
                    '2019-01-11', '2019-01-11', 0)

            SET IDENTITY_INSERT loan_template OFF
        END
END;



