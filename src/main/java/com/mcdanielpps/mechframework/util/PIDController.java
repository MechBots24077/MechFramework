package com.mcdanielpps.mechframework.util;

public class PIDController {
    // Controller Gains
    public double Kp;
    public double Ki;
    public double Kd;

    // Derivative low pass filter time constant
    public double Tau;

    // Output limits
    public double LimMin;
    public double LimMax;

    // Sample time in seconds
    public double T;

    private double m_Integrator = 0.0;
    private double m_PrevError = 0.0;
    private double m_Differentiator = 0.0;
    private double m_PrevMeasurement = 0.0;

    private double m_Out = 0.0;

    public PIDController(double kp, double ki, double kd, double tau, double lMin, double lMax, double t) {
        Kp = kp;
        Ki = ki;
        Kd = kd;

        Tau = tau;
        LimMin = lMin;
        LimMax = lMax;
        T = t;
    }

    public double Update(double setpoint, double measurement) {
        // Error signal
        double error = setpoint - measurement;

        // Proportional
        double proportional = Kp * error;

        // Integral
        m_Integrator = m_Integrator + 0.5 * Ki * T * (error + m_PrevError);

        // Clamp the integral for integrator anti-windup
        double limMinInt = 0.0, limMaxInt = 0.0;
        if (LimMax > proportional) {
            limMaxInt = LimMax - proportional;
        }

        if (LimMin < proportional) {
            limMinInt = LimMin - proportional;
        }

        m_Integrator = Math.max(Math.min(m_Integrator, limMaxInt), limMinInt);

        // Derivative (band limited differentiator)
        m_Differentiator = (2.0 * Kd * (measurement - m_PrevMeasurement)
                + (2.0 * Tau - T) * m_Differentiator)
                / (2.0 * Tau + T);

        // Compute output
        m_Out = proportional + m_Integrator + m_Differentiator;
        m_Out = Math.max(Math.min(m_Out, LimMax), LimMin);

        // Store terms for later use
        m_PrevError = error;
        m_PrevMeasurement = measurement;

        // Return output
        return m_Out;
    }
}
