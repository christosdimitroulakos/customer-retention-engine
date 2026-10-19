-- Customer Retention Engine - Schema (SQLite)
DROP VIEW IF EXISTS vw_retention_priority;
DROP VIEW IF EXISTS vw_revenue_at_risk;
DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
    customer_id        VARCHAR(20) PRIMARY KEY,
    tenure_months      INT         NOT NULL,
    contract_type      VARCHAR(20) NOT NULL,   -- Month-to-month, One year, Two year
    internet_service   VARCHAR(20) NOT NULL,   -- Fiber optic, DSL, No
    tech_support       VARCHAR(5)  NOT NULL,   -- Yes, No
    monthly_charges    REAL        NOT NULL,
    churn_probability  REAL,                   -- 0.0 - 0.95, υπολογίζεται από Java
    risk_level         VARCHAR(15),            -- High Risk, Medium Risk, Low Risk
    predicted_ltv      REAL                    -- σε €, υπολογίζεται από Java
);
