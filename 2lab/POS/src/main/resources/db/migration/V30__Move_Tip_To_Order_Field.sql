ALTER TABLE "order"
    ADD tip DECIMAL(19, 2);

ALTER TABLE "order"
    ALTER COLUMN tip SET NOT NULL;