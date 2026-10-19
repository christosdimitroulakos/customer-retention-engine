package com.retention.service;

import com.retention.model.Customer;

/**
 * Business logic: churn probability, risk level, predicted LTV.
 * Οι ποσοστιαίες μονάδες υπολογίζονται με ακέραιους ώστε να αποφεύγονται
 * σφάλματα κινητής υποδιαστολής στα όρια (π.χ. 35%, 60%).
 */
public class ChurnService {

    private static final int BASE_RISK_PCT = 10;
    private static final int MONTH_TO_MONTH_PCT = 35;
    private static final int FIBER_OPTIC_PCT = 20;
    private static final int NO_TECH_SUPPORT_PCT = 15;
    private static final int SHORT_TENURE_PCT = 15;
    private static final int MAX_CAP_PCT = 95;

    private static final int HIGH_RISK_THRESHOLD_PCT = 60;
    private static final int MEDIUM_RISK_THRESHOLD_PCT = 35;

    private static final int LTV_HORIZON_MONTHS = 24;

    /** Επιστρέφει πιθανότητα αποχώρησης στο [0.0, 0.95]. */
    public double calculateChurnProbability(Customer c) {
        return calculateChurnPercent(c) / 100.0;
    }

    private int calculateChurnPercent(Customer c) {
        int pct = BASE_RISK_PCT;
        if ("Month-to-month".equalsIgnoreCase(c.getContractType())) pct += MONTH_TO_MONTH_PCT;
        if ("Fiber optic".equalsIgnoreCase(c.getInternetService())) pct += FIBER_OPTIC_PCT;
        if ("No".equalsIgnoreCase(c.getTechSupport())) pct += NO_TECH_SUPPORT_PCT;
        if (c.getTenureMonths() < 12) pct += SHORT_TENURE_PCT;
        return Math.min(pct, MAX_CAP_PCT);
    }

    public String determineRiskLevel(double churnProbability) {
        int pct = (int) Math.round(churnProbability * 100);
        if (pct >= HIGH_RISK_THRESHOLD_PCT) return "High Risk";
        if (pct >= MEDIUM_RISK_THRESHOLD_PCT) return "Medium Risk";
        return "Low Risk";
    }

    /** monthly_charges * (1 - churn_probability) * 24 */
    public double calculatePredictedLtv(double monthlyCharges, double churnProbability) {
        return monthlyCharges * (1 - churnProbability) * LTV_HORIZON_MONTHS;
    }

    /** Εφαρμόζει όλους τους υπολογισμούς στον πελάτη. */
    public void analyze(Customer c) {
        double prob = calculateChurnProbability(c);
        c.setChurnProbability(prob);
        c.setRiskLevel(determineRiskLevel(prob));
        c.setPredictedLtv(Math.round(calculatePredictedLtv(c.getMonthlyCharges(), prob) * 100.0) / 100.0);
    }
}
