-- Analytics Views

-- 1) Συνολικά μηνιαία έσοδα σε κίνδυνο ανά τύπο συμβολαίου
CREATE VIEW vw_revenue_at_risk AS
SELECT
    contract_type,
    COUNT(*)                                   AS high_risk_customers,
    ROUND(SUM(monthly_charges), 2)             AS monthly_revenue_at_risk,
    ROUND(SUM(monthly_charges * churn_probability), 2) AS expected_monthly_loss,
    ROUND(AVG(churn_probability), 4)           AS avg_churn_probability
FROM customers
WHERE risk_level = 'High Risk'
GROUP BY contract_type;

-- 2) Κατάταξη πελατών υψηλού κινδύνου (προτεραιότητα retention)
CREATE VIEW vw_retention_priority AS
SELECT
    DENSE_RANK() OVER (ORDER BY monthly_charges * churn_probability DESC) AS priority_rank,
    customer_id,
    contract_type,
    internet_service,
    tech_support,
    tenure_months,
    monthly_charges,
    churn_probability,
    ROUND(monthly_charges * churn_probability, 2) AS expected_monthly_loss,
    predicted_ltv
FROM customers
WHERE risk_level = 'High Risk';
