CREATE TABLE IF NOT EXISTS "operation"
(
    "code"
    INTEGER,
    "name"
    VARCHAR
(
    15
),
    CONSTRAINT "operation_PK" PRIMARY KEY
(
    "code"
),
    CONSTRAINT "operation_UQ" UNIQUE
(
    "code"
)
    );

CREATE TABLE IF NOT EXISTS "operand"
(
    "id" UUID DEFAULT RANDOM_UUID
(
),
    "first" INTEGER,
    "second" INTEGER,
    "calculation_id" UUID,
    CONSTRAINT "operand_PK" PRIMARY KEY
(
    "id"
)
    );

CREATE TABLE IF NOT EXISTS "result"
(
    "id" UUID DEFAULT RANDOM_UUID
(
),
    "value" FLOAT,
    "calculation_id" UUID,
    CONSTRAINT "result_PK" PRIMARY KEY
(
    "id"
)
    );

CREATE TABLE IF NOT EXISTS "calculation"
(
    "id" UUID DEFAULT RANDOM_UUID
(
),
    "done" BOOLEAN DEFAULT FALSE,
    "operand_id" UUID,
    "operation_code" INTEGER,
    "result_id" UUID,
    CONSTRAINT "calculation_PK" PRIMARY KEY
(
    "id"
),
    CONSTRAINT "calculation_operation_FK" FOREIGN KEY
(
    "operation_code"
) REFERENCES "operation",
    CONSTRAINT "calculation_operand_FK" FOREIGN KEY
(
    "operand_id"
) REFERENCES "operand",
    CONSTRAINT "calculation_result_FK" FOREIGN KEY
(
    "result_id"
) REFERENCES "result"
    );

ALTER TABLE IF EXISTS "operand"
    ADD CONSTRAINT "operand_calculation_FK"
    FOREIGN KEY ("calculation_id") REFERENCES "calculation";

ALTER TABLE IF EXISTS "result"
    ADD CONSTRAINT "result_calculation_FK"
    FOREIGN KEY ("calculation_id") REFERENCES "calculation";
