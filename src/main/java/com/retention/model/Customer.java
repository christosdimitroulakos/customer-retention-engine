package com.retention.model;

public class Customer {

    private String customerId;
    private int tenureMonths;
    private String contractType;
    private String internetService;
    private String techSupport;
    private double monthlyCharges;
    private double churnProbability;
    private String riskLevel;
    private double predictedLtv;

    public Customer(String customerId, int tenureMonths, String contractType,
                    String internetService, String techSupport, double monthlyCharges) {
        this.customerId = customerId;
        this.tenureMonths = tenureMonths;
        this.contractType = contractType;
        this.internetService = internetService;
        this.techSupport = techSupport;
        this.monthlyCharges = monthlyCharges;
    }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public int getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }

    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }

    public String getInternetService() { return internetService; }
    public void setInternetService(String internetService) { this.internetService = internetService; }

    public String getTechSupport() { return techSupport; }
    public void setTechSupport(String techSupport) { this.techSupport = techSupport; }

    public double getMonthlyCharges() { return monthlyCharges; }
    public void setMonthlyCharges(double monthlyCharges) { this.monthlyCharges = monthlyCharges; }

    public double getChurnProbability() { return churnProbability; }
    public void setChurnProbability(double churnProbability) { this.churnProbability = churnProbability; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public double getPredictedLtv() { return predictedLtv; }
    public void setPredictedLtv(double predictedLtv) { this.predictedLtv = predictedLtv; }

    @Override
    public String toString() {
        return String.format("%s | %s | %.0f%% | %s | LTV %.2f EUR",
                customerId, contractType, churnProbability * 100, riskLevel, predictedLtv);
    }
}
