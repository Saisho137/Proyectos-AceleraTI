-- ═══════════════════════════════════════════════════
-- Arka · db_orders · Initialization Script
-- Microservice: ms-orders (Orquestador / Iniciador SAGA)
-- ═══════════════════════════════════════════════════

CREATE TABLE IF NOT EXISTS orders (
    id            VARCHAR(36)    PRIMARY KEY DEFAULT gen_random_uuid()::text,
    customer_id   VARCHAR(100)   NOT NULL,
    sku           VARCHAR(50)    NOT NULL,
    quantity      INTEGER        NOT NULL CHECK (quantity > 0),
    total_amount  NUMERIC(15,2)  NOT NULL CHECK (total_amount >= 0),
    status        VARCHAR(30)    NOT NULL DEFAULT 'PENDING',
    created_at    TIMESTAMP      NOT NULL DEFAULT now()
);

-- Index for frequent queries by customer and status
CREATE INDEX IF NOT EXISTS idx_orders_customer_id ON orders (customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_status      ON orders (status);
CREATE INDEX IF NOT EXISTS idx_orders_sku         ON orders (sku);