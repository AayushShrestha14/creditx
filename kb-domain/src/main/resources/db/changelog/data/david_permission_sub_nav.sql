INSERT INTO sub_nav (id, sub_nav_name, front_url, fa_icon)
VALUES (9, 'Pending Account', '/home/admin/openingAccount', 'fa fa-money-check');
INSERT INTO sub_nav (id, sub_nav_name, front_url, fa_icon)
VALUES (10, 'Approval Account', '/home/admin/approvalOpeningAccount', 'fa fa-money-check');
INSERT INTO sub_nav (id, sub_nav_name, front_url, fa_icon)
VALUES (11, 'Rejected Account', '/home/admin/rejectedOpeningAccount', 'fa fa-money-check');

INSERT INTO permission_sub_navs (permission_id, sub_navs_id)
VALUES (19, 9);
INSERT INTO permission_sub_navs (permission_id, sub_navs_id)
VALUES (19, 10);
INSERT INTO permission_sub_navs (permission_id, sub_navs_id)
VALUES (19, 11);