---------------- CAD DOCUMENT FOR MEGA -----------
BEGIN

    DECLARE
        @count smallint
    SET @count = (Select count(*) from offer_letter)
    DECLARE @count_retail smallint
    SET @count_retail = (SELECT count(*) from offer_letter where id = 10)

    if (@count_retail < 1)
        BEGIN
            SET IDENTITY_INSERT offer_letter ON
            INSERT INTO offer_letter (id, name, template_url,post_approval_doc_type)
            VALUES (10, 'Retail Housing', 'home/loan/offer-letter/retail-housing-loan',0)
            SET IDENTITY_INSERT offer_letter OFF
        END
END

BEGIN
    DECLARE @retailEducational smallint
    SET @retailEducational = (SELECT count(*) from offer_letter where id = 11)
    if (@retailEducational < 1)
        BEGIN
            SET IDENTITY_INSERT offer_letter ON
            INSERT INTO offer_letter (id, name, template_url,post_approval_doc_type)
            VALUES (11, 'Retail Educational', 'home/loan/offer-letter/retail-educational',0)
            SET IDENTITY_INSERT offer_letter OFF
        END
END

BEGIN
    DECLARE @retail_mortgage smallint
    SET @retail_mortgage = (SELECT count(*) from offer_letter where id = 12)
    if (@retail_mortgage < 1)
        BEGIN
            SET IDENTITY_INSERT offer_letter ON
            INSERT INTO offer_letter (id, name, template_url,post_approval_doc_type)
            VALUES (12, 'Retail Mortgage Overdraft', 'home/loan/offer-letter/retail-mortgage',0)
            SET IDENTITY_INSERT offer_letter OFF
        END
END;

BEGIN
    DECLARE @hayerPurchase smallint
    SET @hayerPurchase = (SELECT count(*) from offer_letter where id = 13)
    if (@hayerPurchase < 1)
        BEGIN
            SET IDENTITY_INSERT offer_letter ON
            INSERT INTO offer_letter (id, name, template_url,post_approval_doc_type)
            VALUES (13, 'Hayer Purchase', 'home/loan/offer-letter/hayer-purchase',0)
            SET IDENTITY_INSERT offer_letter OFF
        END
END;

BEGIN
    DECLARE @retailProfessional smallint
    SET @retailProfessional = (SELECT count(*) from offer_letter where id = 14)
    if (@retailProfessional < 1)
        BEGIN
            SET IDENTITY_INSERT offer_letter ON
            INSERT INTO offer_letter (id, name, template_url,post_approval_doc_type)
            VALUES (14, 'Retail Professional Loan', 'home/loan/offer-letter/retail-professional-loan',0)
            SET IDENTITY_INSERT offer_letter OFF
        END
END;

BEGIN
    DECLARE @sme smallint
    SET @sme = (SELECT count(*) from offer_letter where id = 15)
    if (@sme < 1)
        BEGIN
            SET IDENTITY_INSERT offer_letter ON
            INSERT INTO offer_letter (id, name, template_url,post_approval_doc_type)
            VALUES (15, 'SME', 'home/loan/offer-letter/sme',0)
            SET IDENTITY_INSERT offer_letter OFF
        END
END;
BEGIN
    DECLARE @retail_mortgage_loan smallint
    SET @retail_mortgage_loan = (SELECT count(*) from offer_letter where id = 16)
    if (@retail_mortgage_loan < 1)
        BEGIN
            SET IDENTITY_INSERT offer_letter ON
            INSERT INTO offer_letter (id, name, template_url,post_approval_doc_type)
            VALUES (16, 'Retail Mortgage Loan', 'home/loan/offer-letter/retail-mortgage-loan',0)
            SET IDENTITY_INSERT offer_letter OFF
        END
END;

BEGIN
    DECLARE @retail_educational_loan_english smallint
    SET @retail_educational_loan_english  = (SELECT count(*) from offer_letter where id = 17)
    if (@retail_educational_loan_english  < 1)
        BEGIN
            SET IDENTITY_INSERT offer_letter ON
            INSERT INTO offer_letter (id, name, template_url,post_approval_doc_type)
            VALUES (17, 'Retail Educational Loan English', 'home/loan/offer-letter/retail-educational-loan-english',0)
            SET IDENTITY_INSERT offer_letter OFF
        END
END;
---------------- CAD DOCUMENT FOR MEGA -----------
