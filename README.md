# ⚡ Gatling Performance Framework

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square)
![Gatling](https://img.shields.io/badge/Gatling-3.9-orange?style=flat-square)
![Pipeline](https://github.com/francisco9403/GatlingPerformanceFramework/actions/workflows/gatling.yml/badge.svg)

## 📊 Live Report
[**View Latest Load Test Results**](https://francisco9403.github.io/GatlingPerformanceFramework/) 👈 _Click here!_

## 📋 Overview

Advanced **Performance & Load Testing Framework** following the "Performance as Code" philosophy.
It simulates realistic user behavior using **CSV Data Feeders**, **Correlation** mechanisms, and strictly enforced **SLA Assertions**.

## 🚀 Key Features

* **Real User Simulation:** Implements "Think Time" (pauses) and dynamic data injection to mimic real traffic.
* **SLAs / Assertions:** Pipeline fails automatically if response time > 500ms or success rate < 100%.
* **Correlation:** Captures dynamic data (Post ID/Title) from responses to use in subsequent requests (Chained API calls).
* **CI/CD Integration:** Automated execution via GitHub Actions with auto-published HTML reports.

## 🛠️ Tech Stack

* **Core:** Gatling (Java DSL)
* **Build:** Maven
* **Data:** CSV Feeder
* **CI/CD:** GitHub Actions + Pages

## 🧪 Scenario: "Advanced API Load"

1.  **Dynamic Read:** Users pick a random `postId` from `posts.csv`.
2.  **Correlation:** System captures the `postTitle` from the response.
3.  **Dependent Write:** User creates a new Post using the captured `postTitle`.

**Load Profile:** Ramp-up to 20 concurrent users over 10 seconds.

## 📦 How to Run

```bash
git clone [https://github.com/francisco9403/GatlingPerformanceFramework.git](https://github.com/francisco9403/GatlingPerformanceFramework.git)
mvn gatling:test
