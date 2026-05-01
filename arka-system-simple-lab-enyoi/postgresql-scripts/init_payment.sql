-- ═══════════════════════════════════════════════════
-- Arka · db_payment · Initialization Script
-- Microservice: ms-payment (Simulador de Pagos)
-- ═══════════════════════════════════════════════════

CREATE TABLE IF NOT EXISTS payments (
    id              VARCHAR(36)    PRIMARY KEY DEFAULT gen_random_uuid()::text,
    order_id        VARCHAR(36)    NOT NULL,
    amount          NUMERIC(15,2)  NOT NULL CHECK (amount >= 0),
    status          VARCHAR(30)    NOT NULL DEFAULT 'PENDING',
    payment_method  VARCHAR(50),
    processed_at    TIMESTAMP      NOT NULL DEFAULT now()
);

-- Index for lookups by order
CREATE INDEX IF NOT EXISTS idx_payments_order_id ON payments (order_id);
CREATE INDEX IF NOT EXISTS idx_payments_status   ON payments (status);