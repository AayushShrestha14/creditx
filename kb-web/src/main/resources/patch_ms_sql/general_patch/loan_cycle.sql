BEGIN
DECLARE @count smallint
SET @count = (Select count(*) from loan_cycle)

if(@count < 2)
BEGIN

SET IDENTITY_INSERT loan_cycle ON
INSERT  INTO loan_cycle (id, cycle, label, created_by_id, modified_by_id, created_at, last_modified_at, version)
VALUES ('1', 'New', 'Document required during new Loan', null, null, '2019-06-20', '2019-06-26', '0'),
('2', 'Re-New', 'Document required during re-new Loan', null, null , '2019-06-20', '2019-06-26', '0'),
('3', 'Closure', 'Document required while closing Loan', null, null , '2019-06-20', '2019-06-26', '0'),
('4', 'Eligibility', 'Document required loan eligibility', null, null , '2019-06-20', '2019-06-26', '0')
SET IDENTITY_INSERT loan_cycle OFF
END
END;

BEGIN
    DECLARE @count SMALLINT
    SET @count = (SELECT COUNT(*) FROM loan_cycle lc WHERE lc.cycle = 'Enhance')
    IF (@count = 0)
        BEGIN
            SET IDENTITY_INSERT loan_cycle ON
            INSERT INTO loan_cycle (id, cycle, label, created_by_id, modified_by_id, created_at, last_modified_at,
                                    version)
            VALUES ('5', 'Enhance', 'Document required while enhancing loan', null, null, '2019-06-20', '2019-06-26',
                    '0')
            SET IDENTITY_INSERT loan_cycle OFF
        END
END;

BEGIN
    DECLARE @count SMALLINT
    SET @count = (SELECT COUNT(*) FROM loan_cycle lc WHERE lc.cycle = 'Partial Settlement')
    IF (@count = 0)
        BEGIN
            SET IDENTITY_INSERT loan_cycle ON
            INSERT INTO loan_cycle (id, cycle, label, created_by_id, modified_by_id, created_at, last_modified_at,
                                    version)
            VALUES ('6', 'Partial Settlement', 'Document required during partial settlement of loan', null, null,
                    '2019-06-20', '2019-06-26', '0')
            SET IDENTITY_INSERT loan_cycle OFF
        END
END;

BEGIN
    DECLARE @count SMALLINT
    SET @count = (SELECT COUNT(*) FROM loan_cycle lc WHERE lc.cycle = 'Full Settlement')
    IF (@count = 0)
        BEGIN
            SET IDENTITY_INSERT loan_cycle ON
            INSERT INTO loan_cycle (id, cycle, label, created_by_id, modified_by_id, created_at, last_modified_at,
                                    version)
            VALUES ('7', 'Full Settlement', 'Document required during full settlement of loan', null, null,
                    '2019-06-20', '2019-06-26', '0')
            SET IDENTITY_INSERT loan_cycle OFF
        END
END;

BEGIN
    DECLARE @count SMALLINT
    SET @count = (SELECT COUNT(*) FROM loan_cycle lc WHERE lc.cycle = 'Account Opening')
    IF (@count = 0)
        BEGIN
            SET IDENTITY_INSERT loan_cycle ON
            INSERT INTO loan_cycle (id, cycle, label, created_by_id, modified_by_id, created_at, last_modified_at,
                                    version)
            VALUES ('8', 'Account Opening', 'Document required for account opening', null, null,
                    '2019-06-20', '2019-06-26', '0')
            SET IDENTITY_INSERT loan_cycle OFF
        END
END;

BEGIN
    DECLARE
        @count SMALLINT
    SET @count = (SELECT COUNT(*) FROM loan_cycle lc WHERE lc.cycle = 'Individual Customer')
    IF (@count = 0)
        BEGIN
            SET IDENTITY_INSERT loan_cycle ON
            INSERT INTO loan_cycle (id, cycle, label, created_by_id, modified_by_id, created_at,
                                    last_modified_at,
                                    version)
            VALUES ('9', 'Individual Customer', 'Document required for individual customer', null,
                    null,
                    '2019-06-20', '2019-06-26', '0')
            SET IDENTITY_INSERT loan_cycle OFF
        END
END;

BEGIN
    DECLARE
        @count SMALLINT
    SET @count = (SELECT COUNT(*) FROM loan_cycle lc WHERE lc.cycle = 'Company Customer')
    IF (@count = 0)
        BEGIN
            SET IDENTITY_INSERT loan_cycle ON
            INSERT INTO loan_cycle (id, cycle, label, created_by_id, modified_by_id, created_at,
                                    last_modified_at,
                                    version)
            VALUES ('10', 'Company Customer', 'Document required for company customer', null, null,
                    '2019-06-20', '2019-06-26', '0')
            SET IDENTITY_INSERT loan_cycle OFF
        END
END;

BEGIN
    DECLARE
        @count SMALLINT
    SET @count = (SELECT COUNT(*) FROM loan_cycle lc WHERE lc.cycle = 'Recovery')
    IF (@count = 0)
        BEGIN
            SET IDENTITY_INSERT loan_cycle ON
            INSERT INTO loan_cycle (id, cycle, label, created_by_id, modified_by_id, created_at,
                                    last_modified_at,
                                    version)
            VALUES ('11', 'Recovery', 'Document required for Recovery', null, null,
                    '2019-06-20', '2019-06-26', '0')
            SET IDENTITY_INSERT loan_cycle OFF
        END
END;

BEGIN
    DECLARE
        @count SMALLINT
    SET @count = (SELECT COUNT(*) FROM loan_cycle lc WHERE lc.cycle = 'Cad Document')
    IF (@count = 0)
        BEGIN
            SET IDENTITY_INSERT loan_cycle ON
            INSERT INTO loan_cycle (id, cycle, label, created_by_id, modified_by_id, created_at,
                                    last_modified_at,
                                    version)
            VALUES ('12', 'Cad Document', 'Document uploaded by CAD after loan is approved', null, null,
                    '2020-11-18', '2020-11-18', '0')
            SET IDENTITY_INSERT loan_cycle OFF
        END
END;

BEGIN
    DECLARE @count SMALLINT
    SET @count = (SELECT COUNT(*) FROM loan_cycle lc WHERE lc.cycle = 'Renew With Enhancement')
    IF (@count = 0)
BEGIN
            SET IDENTITY_INSERT loan_cycle ON
            INSERT INTO loan_cycle (id, cycle, label, created_by_id, modified_by_id, created_at, last_modified_at,
                                    version)
            VALUES ('13', 'Renew With Enhancement', 'Document required during renew with enhancement of loan', null, null,
                    '2021-03-22', '2021-03-22', '0')
            SET IDENTITY_INSERT loan_cycle OFF
END
END;
