-- ************************BRANCH*******************************************
INSERT INTO url_api (id, api_url, type)
values (1, '/v1/branch', 'ADD BRANCH');
INSERT INTO url_api (id, api_url, type)
values (2, '/v1/branch', 'EDIT BRANCH');
INSERT INTO url_api (id, api_url, type)
values (3, '/v1/branch/csv', 'DOWNLOAD CSV');
INSERT INTO url_api (id, api_url, type)
values (4, '/v1/branch/get', 'VIEW BRANCH');

INSERT INTO permission_api_list(permission_id, api_list_id)
values (1, 1);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (1, 2);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (1, 3);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (1, 4);


-- ************************BRANCH*******************************************

-- ************************APPROVAL LIMIT*******************************************
INSERT INTO url_api (id, api_url, type)
values (5, '/v1/approvallimit', 'ADD APPROVAL LIMIT');
INSERT INTO url_api (id, api_url, type)
values (6, '/v1/approvallimit/get', 'VIEW APPROVAL LIMIT');
INSERT INTO url_api (id, api_url, type)
values (34, '/v1/approvallimit/csv', 'DOWNLOAD CSV');

INSERT INTO permission_api_list(permission_id, api_list_id)
values (7, 5);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (7, 6);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (7, 34);

-- ************************APPROVAL LIMIT*******************************************


-- ************************COMPANY*******************************************
INSERT INTO url_api (id, api_url, type)
values (7, '/v1/company', 'ADD COMPANY');
INSERT INTO url_api (id, api_url, type)
values (8, '/v1/company/get', 'VIEW COMPANY');
INSERT INTO url_api (id, api_url, type)
values (9, '/v1/company/get/statusCount', 'VIEW STATUS');

INSERT INTO permission_api_list(permission_id, api_list_id)
values (11, 7);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (11, 8);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (11, 9);

-- ************************COMPANY*******************************************

-- ************************NEPSE*******************************************
INSERT INTO url_api (id, api_url, type)
values (10, '/v1/nepseCompany', 'ADD NEPSE');
INSERT INTO url_api (id, api_url, type)
values (11, '/v1/nepseCompany/get', 'VIEW NEPSE');

INSERT INTO permission_api_list(permission_id, api_list_id)
values (8, 10);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (8, 11);

-- ************************NEPSE*******************************************


-- ************************USER*******************************************
INSERT INTO url_api (id, api_url, type)
values (12, '/v1/user', 'ADD USER');
INSERT INTO url_api (id, api_url, type)
values (30, '/v1/user/csv', 'DOWNLOAD CSV');

INSERT INTO permission_api_list(permission_id, api_list_id)
values (6, 12);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (6, 30);


-- ************************USER*******************************************

-- ************************SEGMENT*******************************************
INSERT INTO url_api (id, api_url, type)
values (13, '/v1/segment', 'ADD SEGMENT');
INSERT INTO url_api (id, api_url, type)
values (14, '/v1/segment/get', 'VIEW SEGMENT');
INSERT INTO url_api (id, api_url, type)
values (15, '/v1/segment', 'EDIT SEGMENT');
INSERT INTO url_api (id, api_url, type)
values (31, '/v1/segment/csv', 'DOWNLOAD CSV');


INSERT INTO permission_api_list(permission_id, api_list_id)
values (9, 13);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (9, 14);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (9, 15);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (9, 31);

-- ************************SEGMENT*******************************************


-- ************************SUB-SEGMENT*******************************************
INSERT INTO url_api (id, api_url, type)
values (16, '/v1/subSegment', 'ADD SUB-SEGMENT');
INSERT INTO url_api (id, api_url, type)
values (17, '/v1/subSegment', 'EDIT SUB-SEGMENT');
INSERT INTO url_api (id, api_url, type)
values (18, '/v1/subSegment/get', 'VIEW SUB-SEGMENT');
INSERT INTO url_api (id, api_url, type)
values (32, '/v1/subSegment/csv', 'DOWNLOAD CSV');


INSERT INTO permission_api_list(permission_id, api_list_id)
values (10, 16);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (10, 17);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (10, 18);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (10, 32);

-- ************************SUB-SEGMENT*******************************************

-- ************************SECTOR*******************************************
INSERT INTO url_api (id, api_url, type)
values (19, '/v1/sector', 'ADD SECTOR');
INSERT INTO url_api (id, api_url, type)
values (20, '/v1/sector', 'EDIT SECTOR');
INSERT INTO url_api (id, api_url, type)
values (21, '/v1/sector/get', 'VIEW SECTOR');
INSERT INTO url_api (id, api_url, type)
values (33, '/get', 'DOWNLOAD CSV');


INSERT INTO permission_api_list(permission_id, api_list_id)
values (5, 19);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (5, 20);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (5, 21);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (5, 33);



-- ************************SECTOR*******************************************


-- ************************SUB-SECTOR*******************************************
INSERT INTO url_api (id, api_url, type)
values (22, '/v1/subSector', 'ADD SUB-SECTOR');
INSERT INTO url_api (id, api_url, type)
values (23, '/v1/subSector', 'EDIT SUB-SECTOR');
INSERT INTO url_api (id, api_url, type)
values (24, '/v1/subSector/get', 'VIEW SUB-SECTOR');
INSERT INTO url_api (id, api_url, type)
values (25, '/v1/subSector/csv', 'DOWNLOAD CSV');


INSERT INTO permission_api_list(permission_id, api_list_id)
values (12, 22);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (12, 23);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (12, 24);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (12, 25);


-- ************************SUB-SECTOR*******************************************


-- ************************VALUATOR*******************************************
INSERT INTO url_api (id, api_url, type)
values (26, '/v1/valuator', 'ADD VALUATOR');
INSERT INTO url_api (id, api_url, type)
values (27, '/v1/valuator', 'EDIT VALUATOR');
INSERT INTO url_api (id, api_url, type)
values (28, '/v1/valuator/get', 'VIEW VALUATOR');
INSERT INTO url_api (id, api_url, type)
values (29, '/v1/valuator/csv', 'DOWNLOAD CSV');


INSERT INTO permission_api_list(permission_id, api_list_id)
values (4, 26);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (4, 27);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (4, 28);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (4, 29);



-- ************************VALUATOR*******************************************

-- ************************DASHBOARD******************************************
INSERT INTO url_api (id, api_url, type)
values (40, '/v1/config/getAll', 'LOAN CATEGORY');
INSERT INTO url_api (id, api_url, type)
values (41, '/v1/user/get/statusCount', 'USER COUNT');
INSERT INTO url_api (id, api_url, type)
values (42, '/v1/branch/get/statusCount', 'BRANCH COUNT');
INSERT INTO url_api (id, api_url, type)
values (43, '/v1/sector/get/statusCount', 'SECTOR COUNT');
INSERT INTO url_api (id, api_url, type)
values (44, '/v1/segment/get/statusCount', 'SEGMENT COUNT');
INSERT INTO url_api (id, api_url, type)
values (45, '/v1/notification', 'NOTIFICATION');
INSERT INTO url_api(id, api_url, type)
values (46, '/v1/pending', 'PENDING');



INSERT INTO permission_api_list(permission_id, api_list_id)
values (17, 40);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (17, 41);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (17, 42);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (17, 43);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (17, 44);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (17, 45);
INSERT INTO permission_api_list(permission_id, api_list_id)
values (17, 46);

-- ************************DASHBOARD******************************************


-- ************************DEFAULT ADMIN*******************************************

-- ************************BRANCH MAP*******************************************
INSERT INTO role_permission_rights_api_rights
values (1, 1);
INSERT INTO role_permission_rights_api_rights
values (1, 2);
INSERT INTO role_permission_rights_api_rights
values (1, 3);
INSERT INTO role_permission_rights_api_rights
values (1, 4);
-- ************************BRANCH MAP*******************************************

-- ************************APPROVAL LIMIT MAP*******************************************
INSERT INTO role_permission_rights_api_rights
values (7, 5);
INSERT INTO role_permission_rights_api_rights
values (7, 6);
INSERT INTO role_permission_rights_api_rights
values (7, 34);

-- ************************APPROVAL LIMIT MAP*******************************************


-- ************************COMPANY MAP*******************************************
INSERT INTO role_permission_rights_api_rights
values (11, 7);
INSERT INTO role_permission_rights_api_rights
values (11, 8);
INSERT INTO role_permission_rights_api_rights
values (11, 9);

-- ************************COMPANY MAP*******************************************

-- ************************NEPSE MAP*******************************************
INSERT INTO role_permission_rights_api_rights
values (8, 10);
INSERT INTO role_permission_rights_api_rights
values (8, 11);

-- ************************NEPSE MAP*******************************************


-- ************************SEGMENT MAP*******************************************
INSERT INTO role_permission_rights_api_rights
values (9, 13);
INSERT INTO role_permission_rights_api_rights
values (9, 14);
INSERT INTO role_permission_rights_api_rights
values (9, 15);
INSERT INTO role_permission_rights_api_rights
values (9, 31);

-- ************************SEGMENT MAP*******************************************

-- ************************SUB-SEGMENT*******************************************
INSERT INTO role_permission_rights_api_rights
values (10, 16);
INSERT INTO role_permission_rights_api_rights
values (10, 17);
INSERT INTO role_permission_rights_api_rights
values (10, 18);
INSERT INTO role_permission_rights_api_rights
values (10, 32);

-- ************************SUB-SEGMENT*******************************************

-- ************************SECTOR*******************************************

INSERT INTO role_permission_rights_api_rights
values (5, 19);
INSERT INTO role_permission_rights_api_rights
values (5, 20);
INSERT INTO role_permission_rights_api_rights
values (5, 21);
INSERT INTO role_permission_rights_api_rights
values (5, 33);



-- ************************SECTOR*******************************************

-- ************************SUB-SECTOR*******************************************

INSERT INTO role_permission_rights_api_rights
values (12, 22);
INSERT INTO role_permission_rights_api_rights
values (12, 23);
INSERT INTO role_permission_rights_api_rights
values (12, 24);
INSERT INTO role_permission_rights_api_rights
values (12, 25);


-- ************************SUB-SECTOR*******************************************


-- ************************VALUATOR*******************************************

INSERT INTO role_permission_rights_api_rights
values (4, 26);
INSERT INTO role_permission_rights_api_rights
values (4, 27);
INSERT INTO role_permission_rights_api_rights
values (4, 28);
INSERT INTO role_permission_rights_api_rights
values (4, 29);

-- ************************VALUATOR*******************************************


-- ************************COUNTVIEW*******************************************
INSERT INTO role_permission_rights_api_rights
values (17, 41);
INSERT INTO role_permission_rights_api_rights
values (17, 42);
INSERT INTO role_permission_rights_api_rights
values (17, 43);
INSERT INTO role_permission_rights_api_rights
values (17, 44);
INSERT INTO role_permission_rights_api_rights
values (17, 45);

-- ************************COUNTVIEW*******************************************

-- ************************DEFAULT ADMIN*******************************************









