-- ============================================
-- V8 - Add status column to system_role
-- ============================================

ALTER TABLE system_role
    ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';

-- ============================================
-- Constraint to enforce valid status values
-- ============================================

ALTER TABLE system_role
    ADD CONSTRAINT chk_system_role_status
    CHECK (status IN ('ACTIVE', 'INACTIVE'));

-- ============================================
-- Index for filtering by status
-- ============================================

CREATE INDEX idx_system_role_status
    ON system_role (status);
