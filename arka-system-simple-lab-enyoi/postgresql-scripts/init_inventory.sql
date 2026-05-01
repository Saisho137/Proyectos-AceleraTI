-- ═══════════════════════════════════════════════════
-- Arka · db_inventory · Initialization Script
-- Microservice: ms-inventory (Participante SAGA)
-- ═══════════════════════════════════════════════════

CREATE TABLE IF NOT EXISTS products (
    id        VARCHAR(36)    PRIMARY KEY DEFAULT gen_random_uuid()::text,
    sku       VARCHAR(50)    NOT NULL UNIQUE,
    name      VARCHAR(200)   NOT NULL,
    price     NUMERIC(15,2)  NOT NULL CHECK (price >= 0),
    stock     INTEGER        NOT NULL DEFAULT 0 CHECK (stock >= 0),
    category  VARCHAR(100)
);

-- Index for lookups by category
CREATE INDEX IF NOT EXISTS idx_products_category ON products (category);

-- ═══════════════════════════════════════════════════
-- Seed data: sample products for testing
-- ═══════════════════════════════════════════════════
INSERT INTO products (id, sku, name, price, stock, category) VALUES
    (gen_random_uuid()::text, 'KB-MECH-001', 'Teclado Mecánico RGB',        189000.00, 50, 'Periféricos'),
    (gen_random_uuid()::text, 'MS-WIRE-002', 'Mouse Inalámbrico Pro',        95000.00, 120, 'Periféricos'),
    (gen_random_uuid()::text, 'MN-UW-003',  'Monitor Ultrawide 34"',       1450000.00, 15, 'Monitores'),
    (gen_random_uuid()::text, 'GPU-RTX-004', 'GPU RTX 4070 Ti',            3200000.00, 8,  'Hardware'),
    (gen_random_uuid()::text, 'RAM-DDR5-005','Memoria RAM DDR5 32GB',       420000.00, 60, 'Hardware')
ON CONFLICT (sku) DO NOTHING;