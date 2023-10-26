CREATE TABLE IF NOT EXISTS remind.user_data
(
    username          VARCHAR(255) PRIMARY KEY,
    password          VARCHAR(255) NOT NULL,
    created_at        TIMESTAMP DEFAULT NOW(),
    updated_at        TIMESTAMP DEFAULT NOW()
);

CREATE OR REPLACE FUNCTION remind.method_get_updated_at() RETURNS TRIGGER
    LANGUAGE plpgsql
AS '
    BEGIN
        NEW.updated_at =now();
        RETURN NEW;
    END;
';

CREATE TRIGGER trigger_updated_at BEFORE UPDATE ON remind.user_data
    FOR EACH ROW EXECUTE PROCEDURE remind.method_get_updated_at();

CREATE TABLE IF NOT EXISTS remind.reminders
(
    id                      uuid PRIMARY KEY,
    active                  boolean,
    evening_reminder        boolean,
    morning_reminder        boolean,
    activity_time           TIMESTAMP,
    reminder_time           TIMESTAMP,
    subject                 VARCHAR(255) NOT NULL,
    user_fk                 VARCHAR(255) NOT NULL,
    created_at              TIMESTAMP DEFAULT NOW(),
    updated_at              TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_user_data123 FOREIGN KEY (user_fk) REFERENCES remind.user_data (username)
);

CREATE TRIGGER trigger_updated_at BEFORE UPDATE ON remind.reminders
    FOR EACH ROW EXECUTE PROCEDURE remind.method_get_updated_at();