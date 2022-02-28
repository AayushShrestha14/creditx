BEGIN
    DECLARE
        @count smallint
    DECLARE
        @count_product_mode smallint
    DECLARE
        @count_manjurima smallint
    SET @count = (Select count(*) from offer_letter)
    SET @count_product_mode = (SELECT count(*) from product_mode where id = 6 and status = 1)
    SET @count_manjurima = (Select count(*) from offer_letter where id = 3)
    if (@count_product_mode > 0)
        BEGIN
            if (@count < 2)
                BEGIN
                    SET IDENTITY_INSERT offer_letter ON
                    INSERT INTO offer_letter (id, name, template_url)
                    VALUES (1, 'Birth Mark Letter', 'home/loan/birth-mark-letter'),
                           (2, 'Success Letter', 'home/loan/success-offer-letter')
                    SET IDENTITY_INSERT offer_letter OFF
                END

            ---------------- CAD DOCUMENT FOR MAW -----------
            if (@count_manjurima < 1)
                BEGIN
                    SET IDENTITY_INSERT offer_letter ON
                    INSERT INTO offer_letter (id, name, template_url)
                    VALUES (3, 'Dhito Likhat Manjurima',
                            'home/loan/offer-letter/dhito-likhat-manjurima'),
                           (4, 'Dhristi Bandhak', 'home/loan/offer-letter/dhristi-bandhak'),
                           (5, 'Jamani Tamsuk', 'home/loan/offer-letter/jamani-tamsuk'),
                           (6, 'Kararnama', 'home/loan/offer-letter/kararnama'),
                           (7, 'Karjatamsuk', 'home/loan/offer-letter/karjatamsuk'),
                           (8, 'Manjurinama', 'home/loan/offer-letter/manjurinama'),
                           (9, 'Pratigya Patra', 'home/loan/offer-letter/pratigya-patra')
                    SET IDENTITY_INSERT offer_letter OFF
                END
            ---------------- CAD DOCUMENT FOR MAW -----------
        END


END;
