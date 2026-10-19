-- Εικονικά δεδομένα πελατών (15 εγγραφές). Τα churn_probability, risk_level, predicted_ltv
-- αφήνονται NULL και συμπληρώνονται από το Java (Main.java).
INSERT INTO customers (customer_id, tenure_months, contract_type, internet_service, tech_support, monthly_charges) VALUES
('CUST-001',  2, 'Month-to-month', 'Fiber optic', 'No',  89.90),
('CUST-002', 34, 'One year',      'DSL',         'Yes', 56.95),
('CUST-003',  5, 'Month-to-month', 'Fiber optic', 'No',  99.65),
('CUST-004', 45, 'Two year',      'DSL',         'Yes', 42.30),
('CUST-005',  8, 'Month-to-month', 'DSL',         'No',  70.70),
('CUST-006', 60, 'Two year',      'Fiber optic', 'Yes', 105.50),
('CUST-007', 11, 'Month-to-month', 'Fiber optic', 'Yes', 84.45),
('CUST-008', 24, 'One year',      'Fiber optic', 'No',  94.10),
('CUST-009', 72, 'Two year',      'No',          'No',  20.05),
('CUST-010',  1, 'Month-to-month', 'No',          'No',  25.30),
('CUST-011', 18, 'Month-to-month', 'DSL',         'Yes', 53.85),
('CUST-012', 50, 'One year',      'DSL',         'No',  61.20),
('CUST-013',  9, 'One year',      'Fiber optic', 'No',  79.75),
('CUST-014', 36, 'Two year',      'DSL',         'No',  48.60),
('CUST-015',  3, 'Month-to-month', 'Fiber optic', 'No', 110.35);
