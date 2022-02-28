BEGIN
    DECLARE @assignmentOfReceivable smallint
    SET @assignmentOfReceivable= (SELECT count(*) from document where id = 1000)

    if (@assignmentOfReceivable < 1)
        BEGIN
            SET IDENTITY_INSERT document ON
            INSERT INTO document (id, name,url,status,created_at,created_by_id,modified_by_id,last_modified_at,version,display_name,check_type,contains_template)
            VALUES (1000, 'Assignment of receivable',null,1,'2021-1-7',1,1,'2021-1-7',0,'Assignment of receivable',null,1)
            SET IDENTITY_INSERT document OFF
        END
END;

BEGIN
    DECLARE @deedHypoOfMachinery smallint
    SET @deedHypoOfMachinery= (SELECT count(*) from document where id = 1001)

    if (@deedHypoOfMachinery < 1)
        BEGIN
            SET IDENTITY_INSERT document ON
            INSERT INTO document (id, name,url,status,created_at,created_by_id,modified_by_id,last_modified_at,version,display_name,check_type,contains_template)
            VALUES (1001, 'Deed Hypo of Machinery',null,1,'2021-1-7',1,1,'2021-1-7',0,'Deed Hypo of Machinery',null,1)
            SET IDENTITY_INSERT document OFF
        END
END;

BEGIN
    DECLARE @count smallint
    SET @count= (SELECT count(*) from document where id = 1002)

    if (@count < 1)
        BEGIN
            SET IDENTITY_INSERT document ON
            INSERT INTO document (id, name,url,status,created_at,created_by_id,modified_by_id,last_modified_at,version,display_name,check_type,contains_template)
            VALUES (1002, 'Hypo of Stock',null,1,'2021-1-7',1,1,'2021-1-7',0,'Hypo of Stock',null,1)
            SET IDENTITY_INSERT document OFF
        END
END;

BEGIN
    DECLARE @count smallint
    SET @count= (SELECT count(*) from document where id = 1003)

    if (@count < 1)
        BEGIN
            SET IDENTITY_INSERT document ON
            INSERT INTO document (id, name,url,status,created_at,created_by_id,modified_by_id,last_modified_at,version,display_name,check_type,contains_template)
            VALUES (1003, 'Loan Deed Company',null,1,'2021-1-7',1,1,'2021-1-7',0,'Loan Deed Company',null,1)
            SET IDENTITY_INSERT document OFF
        END
END;

BEGIN
    DECLARE @count smallint
    SET @count= (SELECT count(*) from document where id = 1004)

    if (@count < 1)
        BEGIN
            SET IDENTITY_INSERT document ON
            INSERT INTO document (id, name,url,status,created_at,created_by_id,modified_by_id,last_modified_at,version,display_name,check_type,contains_template)
            VALUES (1004, 'Manjurinama For Company',null,1,'2021-1-7',1,1,'2021-1-7',0,'Manjurinama For Company',null,1)
            SET IDENTITY_INSERT document OFF
        END
END;

BEGIN
    DECLARE @count smallint
    SET @count= (SELECT count(*) from document where id = 1005)

    if (@count < 1)
        BEGIN
            SET IDENTITY_INSERT document ON
            INSERT INTO document (id, name,url,status,created_at,created_by_id,modified_by_id,last_modified_at,version,display_name,check_type,contains_template)
            VALUES (1005, 'Personal Guarantee Company',null,1,'2021-1-7',1,1,'2021-1-7',0,'Personal Guarantee Company',null,1)
            SET IDENTITY_INSERT document OFF
        END
END;

BEGIN
    DECLARE @count smallint
    SET @count= (SELECT count(*) from document where id = 1006)

    if (@count < 1)
        BEGIN
            SET IDENTITY_INSERT document ON
            INSERT INTO document (id, name,url,status,created_at,created_by_id,modified_by_id,last_modified_at,version,display_name,check_type,contains_template)
            VALUES (1006, 'Personal Guarantee Joint Borrower',null,1,'2021-1-7',1,1,'2021-1-7',0,'Personal Guarantee Joint Borrower',null,1)
            SET IDENTITY_INSERT document OFF
        END
END;

BEGIN
    DECLARE @count smallint
    SET @count= (SELECT count(*) from document where id = 1007)

    if (@count < 1)
        BEGIN
            SET IDENTITY_INSERT document ON
            INSERT INTO document (id, name,url,status,created_at,created_by_id,modified_by_id,last_modified_at,version,display_name,check_type,contains_template)
            VALUES (1007, 'Personal Guarantee Person To Person',null,1,'2021-1-7',1,1,'2021-1-7',0,'Personal Guarantee Person To Person',null,1)
            SET IDENTITY_INSERT document OFF
        END
END;

BEGIN
    DECLARE @count smallint
    SET @count= (SELECT count(*) from document where id = 1008)

    if (@count < 1)
        BEGIN
            SET IDENTITY_INSERT document ON
            INSERT INTO document (id, name,url,status,created_at,created_by_id,modified_by_id,last_modified_at,version,display_name,check_type,contains_template)
            VALUES (1008, 'Promissory Note Single Borrower',null,1,'2021-1-7',1,1,'2021-1-7',0,'Promissory Note Single Borrower',null,1)
            SET IDENTITY_INSERT document OFF
        END
END;

BEGIN
    DECLARE @count smallint
    SET @count= (SELECT count(*) from document where id = 1009)

    if (@count < 1)
        BEGIN
            SET IDENTITY_INSERT document ON
            INSERT INTO document (id, name,url,status,created_at,created_by_id,modified_by_id,last_modified_at,version,display_name,check_type,contains_template)
            VALUES (1009, 'Trust Receipt Nepali Limit',null,1,'2021-1-7',1,1,'2021-1-7',0,'Trust Receipt Nepali Limit',null,1)
            SET IDENTITY_INSERT document OFF
        END
END;

BEGIN
    DECLARE @count smallint
    SET @count= (SELECT count(*) from document where id = 1010)

    if (@count < 1)
        BEGIN
            SET IDENTITY_INSERT document ON
            INSERT INTO document (id, name,url,status,created_at,created_by_id,modified_by_id,last_modified_at,version,display_name,check_type,contains_template)
            VALUES (1010, 'Loan Deed Single',null,1,'2021-1-7',1,1,'2021-1-7',0,'Loan Deed Single',null,1)
            SET IDENTITY_INSERT document OFF
        END
END;

BEGIN
    DECLARE @count smallint
    SET @count= (SELECT count(*) from document where id = 1011)

    if (@count < 1)
        BEGIN
            SET IDENTITY_INSERT document ON
            INSERT INTO document (id, name,url,status,created_at,created_by_id,modified_by_id,last_modified_at,version,display_name,check_type,contains_template)
            VALUES (1011, 'Loan Deed Multiple',null,1,'2021-1-7',1,1,'2021-1-7',0,'Loan Deed Multiple',null,1)
            SET IDENTITY_INSERT document OFF
        END
END;

BEGIN
    DECLARE @count smallint
    SET @count= (SELECT count(*) from document where id = 1012)

    if (@count < 1)
        BEGIN
            SET IDENTITY_INSERT document ON
            INSERT INTO document (id, name,url,status,created_at,created_by_id,modified_by_id,last_modified_at,version,display_name,check_type,contains_template)
            VALUES (1012, 'Promissory Note Company',null,1,'2021-1-7',1,1,'2021-1-7',0,'Promissory Note Company',null,1)
            SET IDENTITY_INSERT document OFF
        END
END;

BEGIN
    DECLARE @count smallint
    SET @count= (SELECT count(*) from document where id = 1013)

    if (@count < 1)
        BEGIN
            SET IDENTITY_INSERT document ON
            INSERT INTO document (id, name,url,status,created_at,created_by_id,modified_by_id,last_modified_at,version,display_name,check_type,contains_template)
            VALUES (1013, 'Promissory Note Joint',null,1,'2021-1-7',1,1,'2021-1-7',0,'Promissory Note Joint',null,1)
            SET IDENTITY_INSERT document OFF
        END
END;
