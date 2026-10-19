# Power BI Dashboard

Το αρχείο `Retention_Dashboard.pbix` δημιουργείται στο **Power BI Desktop** (δεν μπορεί να παραχθεί από κώδικα).
Ακολούθησε τα παρακάτω βήματα και αποθήκευσε το αρχείο σε αυτόν τον φάκελο.

## 1. Φόρτωση δεδομένων

Μετά την εκτέλεση του `Main` δημιουργούνται σε αυτόν τον φάκελο τα:

| Αρχείο | Περιεχόμενο |
|---|---|
| `customers.csv` | Πίνακας `customers` με τα υπολογισμένα πεδία |
| `vw_revenue_at_risk.csv` | Έσοδα σε κίνδυνο ανά contract type |
| `vw_retention_priority.csv` | Κατάταξη πελατών υψηλού κινδύνου |

**Get Data → Text/CSV** για καθένα (ή ODBC προς SQLite, αν έχεις εγκαταστήσει SQLite ODBC driver).
Ονόμασε τον πίνακα `customers` ώστε να ταιριάζουν τα measures.

## 2. DAX Measures (πίνακας `customers`)

```dax
Monthly Revenue at Risk =
CALCULATE ( SUM ( customers[monthly_charges] ), customers[risk_level] = "High Risk" )

Avg Churn Risk =
AVERAGE ( customers[churn_probability] )

Total High Risk Customers =
CALCULATE ( COUNTROWS ( customers ), customers[risk_level] = "High Risk" )

Avg LTV =
AVERAGE ( customers[predicted_ltv] )
```

Όρισε το `Avg Churn Risk` σε μορφή ποσοστού (Measure tools → Format → Percentage).

## 3. Σχεδιασμός Dashboard

| Visual | Πεδία |
|---|---|
| Card | `Total High Risk Customers` |
| Card | `Monthly Revenue at Risk` (μορφή €) |
| Card | `Avg LTV` (μορφή €) |
| Clustered bar chart | Axis: `contract_type`, Values: `Monthly Revenue at Risk` |
| Table | Από `vw_retention_priority`: `priority_rank`, `customer_id`, `contract_type`, `monthly_charges`, `churn_probability`, `expected_monthly_loss`, `predicted_ltv`, ταξινόμηση κατά `priority_rank` |

## 4. Screenshots

Αποθήκευσε ένα screenshot του τελικού dashboard ως `powerbi/dashboard.png` ώστε να εμφανίζεται στο κεντρικό README.
