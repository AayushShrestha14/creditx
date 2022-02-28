INSERT IGNORE INTO `loan_template` (`id`, `name`, `template_url`, `order_url`, `status`,
                                    `template_view`, `created_by_id`, `modified_by_id`,
                                    `created_at`, `last_modified_at`, `version`)
VALUES (1, 'Customer Info', '#basicInfo', 1, 1, NULL, NULL, NULL, '2019-04-04', '2019-04-04', 0),
       (2, 'Company Info', '#companyInfo', 2, 1, NULL, NULL, NULL, '2019-05-28', '2019-05-06', 0),
       (3, 'General', '#loanType', 3, 1, NULL, NULL, NULL, '2019-05-28', '2019-05-06', 0),
       (4, 'Proposal', '#proposalInfo', 4, 1, NULL, NULL, NULL, '2019-05-28', '2019-05-06', 0),
       (5, 'Site Visit', '#siteVisit', 5, 1, NULL, NULL, NULL, '2019-05-28', '2019-05-30', 0);
