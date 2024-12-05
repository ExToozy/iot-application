INSERT INTO users (username, password)
VALUES ('test_user', '$2a$10$2g.on9ozD.70wxeC1BpUeOJoFvzz2fMLWmSkqhSrEEEB5wjWCM98a'),
       ('test_user1', '$2a$10$ZUsbYYxNi41l/X.cWxwhYOES3lQ9z.jvJh2aEcaYepxis4fFUzdC6');

INSERT INTO sensors (name)
VALUES ('test_sensor1'),
       ('test_sensor2');

INSERT INTO measurements (value, raining, sensor_id)
VALUES (10, true, 1),
       (20, false, 1),
       (30, true, 2);