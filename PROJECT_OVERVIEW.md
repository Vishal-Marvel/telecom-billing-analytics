# Telecom Billing & Analytics System

## 1. Problem Statement

Telecom operators face complex challenges in managing millions of subscribers, offering diverse tariff plans, and ensuring accurate billing while complying with regulatory and business rules. The system must be scalable, rule-driven, and capable of producing actionable analytics for business teams.

This project aims to design and implement a **Telecom Billing & Analytics Application** using **Java OOP concepts, Collections, and Stream API**, following a **three-layer architecture** (Presentation → Service → Repository).

## 2. Objectives

1.  Implement telecom-specific business rules such as **fair usage policies (FUP), rollover benefits, roaming charges, referral discounts, credit control,** and **mobile number portability (MNP) restrictions**.
2.  Demonstrate extensive use of **Java Collections and Stream API** for billing, rating, grouping, filtering, and reporting.
3.  Showcase a clean **3-layer architecture** separating concerns (UI/service/persistence).
4.  Provide **domain-driven use cases** covering both operational and analytical needs.

## 3. Use Cases / Tasks

### 3.1. Customer & Plan Management

*   Add and manage customers with attributes like name, email, and referral status.
*   Create and manage telecom plans with allowances (data, voice, SMS), rentals, and overage rates.
*   Support **family/shared plans** where multiple subscriptions share pooled resources.

### 3.2. Subscription Lifecycle

*   Customers can subscribe to plans (one or more SIMs).
*   Subscriptions have lifecycle dates (start, end) and can belong to **family groups**.
*   Support for **MNP (Mobile Number Portability)** with restrictions (e.g., no plan change during pending MNP).

### 3.3. Usage Tracking

*   Collect usage records (CDR-style): Data (GB), Voice (minutes), SMS (count).
*   Differentiate between **domestic vs. roaming** and **international usage**.
*   Apply **night-time discounts** (reduced weightage between 00:00–06:00).
*   Apply **weekend free minutes** rule for certain plans.

### 3.4. Billing & Rating

*   Generate monthly **invoices** per subscription:
    *   Base rental charges.
    *   **Overage charges** when usage exceeds allowances.
    *   **Roaming surcharges** (domestic % uplift, international flat charges).
    *   **Referral discounts** for referred customers.
    *   **Family fairness surcharge** if one member exceeds 60% of shared data pool.
    *   **GST tax calculation** on subtotal.
*   Support **data rollover** (unused data carried to next month with cap).
*   Apply **credit control**: block services if invoices unpaid > 60 days.

### 3.5. Reporting & Analytics (Stream API-heavy)

*   **Top N data users** in a billing cycle.
*   **ARPU (Average Revenue Per User)** by plan.
*   **Overage distribution** (count, total, average overage per plan).
*   **Credit risk detection**: customers with invoices unpaid > 60 days.
*   **Plan recommendation engine**: suggest higher plans if a subscriber pays frequent overages.

## 4. Expected Outcomes

*   A **working simulation** of a telecom billing cycle (seed data → usage records → invoices).
*   **Itemized invoices** with base charges, surcharges, discounts, and taxes.
*   **Analytics dashboards/reports** for management using Java Streams.
*   **Clean separation of concerns** with Presentation (CLI/Driver), Service (business logic), and Repository (in-memory persistence).

## 5. Architecture

The application follows a 3-layer architecture:

*   **Presentation:** `TelecomApp` (CLI)
*   **Service:** `BillingService`, `AnalyticsService`
*   **Repository:** `CustomerRepository`, `SubscriptionRepository`, `BillingRepository`

This separation ensures that business logic is decoupled from data access and presentation, making the system easier to maintain and test.
